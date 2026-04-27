package com.baserbac.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.BusinessOpportunityCreateDTO;
import com.baserbac.dto.BusinessOpportunityQueryDTO;
import com.baserbac.dto.BusinessOpportunityStageMoveDTO;
import com.baserbac.dto.BusinessOpportunityUpdateDTO;
import com.baserbac.entity.CrmBusinessOpportunity;
import com.baserbac.entity.CrmSalesStage;
import com.baserbac.entity.SysUser;
import com.baserbac.mapper.BusinessOpportunityMapper;
import com.baserbac.mapper.SalesStageMapper;
import com.baserbac.mapper.UserMapper;
import com.baserbac.vo.BusinessOpportunityVO;
import com.baserbac.vo.SalesFunnelStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessOpportunityService {

    private final BusinessOpportunityMapper businessOpportunityMapper;
    private final SalesStageMapper salesStageMapper;
    private final UserMapper userMapper;

    public static final String[] STATUS_NAMES = {"进行中", "已赢单", "已输单"};
    public static final int STATUS_IN_PROGRESS = 0;
    public static final int STATUS_WON = 1;
    public static final int STATUS_LOST = 2;

    public PageResult<BusinessOpportunityVO> pageOpportunities(BusinessOpportunityQueryDTO queryDTO) {
        Page<CrmBusinessOpportunity> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmBusinessOpportunity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getOpportunityName()), CrmBusinessOpportunity::getOpportunityName, queryDTO.getOpportunityName())
                .like(StrUtil.isNotBlank(queryDTO.getCustomerName()), CrmBusinessOpportunity::getCustomerName, queryDTO.getCustomerName())
                .eq(queryDTO.getStageId() != null, CrmBusinessOpportunity::getStageId, queryDTO.getStageId())
                .eq(StrUtil.isNotBlank(queryDTO.getStageCode()), CrmBusinessOpportunity::getStageCode, queryDTO.getStageCode())
                .eq(queryDTO.getStatus() != null, CrmBusinessOpportunity::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getAssignUserId() != null, CrmBusinessOpportunity::getAssignUserId, queryDTO.getAssignUserId())
                .eq(StrUtil.isNotBlank(queryDTO.getIndustry()), CrmBusinessOpportunity::getIndustry, queryDTO.getIndustry())
                .ge(queryDTO.getMinExpectedAmount() != null, CrmBusinessOpportunity::getExpectedAmount, queryDTO.getMinExpectedAmount())
                .le(queryDTO.getMaxExpectedAmount() != null, CrmBusinessOpportunity::getExpectedAmount, queryDTO.getMaxExpectedAmount())
                .ge(queryDTO.getCreateTimeStart() != null, CrmBusinessOpportunity::getCreateTime, queryDTO.getCreateTimeStart())
                .le(queryDTO.getCreateTimeEnd() != null, CrmBusinessOpportunity::getCreateTime, queryDTO.getCreateTimeEnd())
                .like(StrUtil.isNotBlank(queryDTO.getTags()), CrmBusinessOpportunity::getTags, queryDTO.getTags())
                .orderByDesc(CrmBusinessOpportunity::getCreateTime);

        Page<CrmBusinessOpportunity> result = businessOpportunityMapper.selectPage(page, wrapper);

        List<BusinessOpportunityVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    public BusinessOpportunityVO getOpportunityById(Long id) {
        CrmBusinessOpportunity opportunity = businessOpportunityMapper.selectById(id);
        if (opportunity == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(opportunity);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createOpportunity(BusinessOpportunityCreateDTO createDTO, Long userId, String userName) {
        CrmSalesStage stage = salesStageMapper.selectById(createDTO.getStageId());
        if (stage == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }

        CrmBusinessOpportunity opportunity = new CrmBusinessOpportunity();
        opportunity.setOpportunityNo(generateOpportunityNo());
        opportunity.setOpportunityName(createDTO.getOpportunityName());
        opportunity.setCustomerId(createDTO.getCustomerId());
        opportunity.setCustomerName(createDTO.getCustomerName());
        opportunity.setLeadId(createDTO.getLeadId());
        opportunity.setStageId(stage.getId());
        opportunity.setStageCode(stage.getStageCode());
        opportunity.setStageName(stage.getStageName());
        opportunity.setWinProbability(stage.getWinProbability());
        opportunity.setExpectedAmount(createDTO.getExpectedAmount() != null ? createDTO.getExpectedAmount() : BigDecimal.ZERO);
        opportunity.setForecastedAmount(calculateForecastedAmount(opportunity.getExpectedAmount(), stage.getWinProbability()));
        opportunity.setExpectedDealDate(createDTO.getExpectedDealDate());
        opportunity.setStatus(createDTO.getStatus() != null ? createDTO.getStatus() : STATUS_IN_PROGRESS);
        opportunity.setSourceName(createDTO.getSourceName());
        opportunity.setIndustry(createDTO.getIndustry());
        opportunity.setDescription(createDTO.getDescription());
        opportunity.setRemark(createDTO.getRemark());
        opportunity.setTags(createDTO.getTags());
        opportunity.setNextFollowTime(createDTO.getNextFollowTime());
        opportunity.setFollowCount(0L);
        opportunity.setIsDeleted(0);

        if (createDTO.getAssignUserId() != null) {
            SysUser assignUser = userMapper.selectById(createDTO.getAssignUserId());
            if (assignUser != null) {
                opportunity.setAssignUserId(assignUser.getId());
                opportunity.setAssignUserName(assignUser.getRealName() != null ? assignUser.getRealName() : assignUser.getUsername());
                opportunity.setAssignTime(LocalDateTime.now());
            }
        }

        businessOpportunityMapper.insert(opportunity);
        return opportunity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOpportunity(BusinessOpportunityUpdateDTO updateDTO, Long userId, String userName) {
        CrmBusinessOpportunity opportunity = businessOpportunityMapper.selectById(updateDTO.getId());
        if (opportunity == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        boolean stageChanged = false;
        CrmSalesStage newStage = null;

        if (updateDTO.getStageId() != null && !updateDTO.getStageId().equals(opportunity.getStageId())) {
            newStage = salesStageMapper.selectById(updateDTO.getStageId());
            if (newStage == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST);
            }
            stageChanged = true;
        }

        if (updateDTO.getOpportunityName() != null) {
            opportunity.setOpportunityName(updateDTO.getOpportunityName());
        }
        if (updateDTO.getCustomerId() != null) {
            opportunity.setCustomerId(updateDTO.getCustomerId());
        }
        if (updateDTO.getCustomerName() != null) {
            opportunity.setCustomerName(updateDTO.getCustomerName());
        }
        if (stageChanged && newStage != null) {
            opportunity.setStageId(newStage.getId());
            opportunity.setStageCode(newStage.getStageCode());
            opportunity.setStageName(newStage.getStageName());
            opportunity.setWinProbability(newStage.getWinProbability());

            if (newStage.getIsClosed() != null && newStage.getIsClosed() == 1) {
                if (newStage.getCloseType() != null && newStage.getCloseType() == 1) {
                    opportunity.setStatus(STATUS_WON);
                } else if (newStage.getCloseType() != null && newStage.getCloseType() == 2) {
                    opportunity.setStatus(STATUS_LOST);
                }
            }
        }
        if (updateDTO.getExpectedAmount() != null) {
            opportunity.setExpectedAmount(updateDTO.getExpectedAmount());
        }
        if (stageChanged || updateDTO.getExpectedAmount() != null) {
            opportunity.setForecastedAmount(calculateForecastedAmount(
                    opportunity.getExpectedAmount(),
                    opportunity.getWinProbability()
            ));
        }
        if (updateDTO.getExpectedDealDate() != null) {
            opportunity.setExpectedDealDate(updateDTO.getExpectedDealDate());
        }
        if (updateDTO.getStatus() != null) {
            opportunity.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getSourceName() != null) {
            opportunity.setSourceName(updateDTO.getSourceName());
        }
        if (updateDTO.getIndustry() != null) {
            opportunity.setIndustry(updateDTO.getIndustry());
        }
        if (updateDTO.getDescription() != null) {
            opportunity.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getRemark() != null) {
            opportunity.setRemark(updateDTO.getRemark());
        }
        if (updateDTO.getTags() != null) {
            opportunity.setTags(updateDTO.getTags());
        }
        if (updateDTO.getNextFollowTime() != null) {
            opportunity.setNextFollowTime(updateDTO.getNextFollowTime());
        }

        if (updateDTO.getAssignUserId() != null && !updateDTO.getAssignUserId().equals(opportunity.getAssignUserId())) {
            SysUser assignUser = userMapper.selectById(updateDTO.getAssignUserId());
            if (assignUser != null) {
                opportunity.setAssignUserId(assignUser.getId());
                opportunity.setAssignUserName(assignUser.getRealName() != null ? assignUser.getRealName() : assignUser.getUsername());
                opportunity.setAssignTime(LocalDateTime.now());
            }
        }

        businessOpportunityMapper.updateById(opportunity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void moveOpportunityStage(Long opportunityId, BusinessOpportunityStageMoveDTO moveDTO, Long userId, String userName) {
        CrmBusinessOpportunity opportunity = businessOpportunityMapper.selectById(opportunityId);
        if (opportunity == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        CrmSalesStage targetStage = salesStageMapper.selectById(moveDTO.getTargetStageId());
        if (targetStage == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }

        opportunity.setStageId(targetStage.getId());
        opportunity.setStageCode(targetStage.getStageCode());
        opportunity.setStageName(targetStage.getStageName());
        opportunity.setWinProbability(targetStage.getWinProbability());
        opportunity.setForecastedAmount(calculateForecastedAmount(
                opportunity.getExpectedAmount(),
                targetStage.getWinProbability()
        ));

        if (targetStage.getIsClosed() != null && targetStage.getIsClosed() == 1) {
            if (targetStage.getCloseType() != null && targetStage.getCloseType() == 1) {
                opportunity.setStatus(STATUS_WON);
            } else if (targetStage.getCloseType() != null && targetStage.getCloseType() == 2) {
                opportunity.setStatus(STATUS_LOST);
            }
        }

        businessOpportunityMapper.updateById(opportunity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOpportunity(Long id, Long userId, String userName) {
        CrmBusinessOpportunity opportunity = businessOpportunityMapper.selectById(id);
        if (opportunity == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        businessOpportunityMapper.deleteById(id);
    }

    public SalesFunnelStatisticsVO getSalesFunnelStatistics(BusinessOpportunityQueryDTO queryDTO) {
        List<CrmSalesStage> allStages = salesStageMapper.selectList(
                new LambdaQueryWrapper<CrmSalesStage>()
                        .eq(CrmSalesStage::getIsEnabled, 1)
                        .orderByAsc(CrmSalesStage::getSortOrder)
        );

        LambdaQueryWrapper<CrmBusinessOpportunity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getAssignUserId() != null, CrmBusinessOpportunity::getAssignUserId, queryDTO.getAssignUserId())
                .eq(StrUtil.isNotBlank(queryDTO.getIndustry()), CrmBusinessOpportunity::getIndustry, queryDTO.getIndustry())
                .ge(queryDTO.getCreateTimeStart() != null, CrmBusinessOpportunity::getCreateTime, queryDTO.getCreateTimeStart())
                .le(queryDTO.getCreateTimeEnd() != null, CrmBusinessOpportunity::getCreateTime, queryDTO.getCreateTimeEnd());

        List<CrmBusinessOpportunity> allOpportunities = businessOpportunityMapper.selectList(wrapper);

        Long totalCount = (long) allOpportunities.size();
        BigDecimal totalExpectedAmount = allOpportunities.stream()
                .map(CrmBusinessOpportunity::getExpectedAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalForecastedAmount = allOpportunities.stream()
                .map(CrmBusinessOpportunity::getForecastedAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long wonCount = allOpportunities.stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == STATUS_WON)
                .count();

        BigDecimal wonAmount = allOpportunities.stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == STATUS_WON)
                .map(CrmBusinessOpportunity::getExpectedAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long lostCount = allOpportunities.stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == STATUS_LOST)
                .count();

        Long inProgressCount = allOpportunities.stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == STATUS_IN_PROGRESS)
                .count();

        Map<Long, List<CrmBusinessOpportunity>> stageOpportunities = allOpportunities.stream()
                .filter(o -> o.getStageId() != null)
                .collect(Collectors.groupingBy(CrmBusinessOpportunity::getStageId));

        List<SalesFunnelStatisticsVO.StageStatisticsVO> stageStatisticsList = new ArrayList<>();

        for (CrmSalesStage stage : allStages) {
            List<CrmBusinessOpportunity> stageOps = stageOpportunities.getOrDefault(stage.getId(), new ArrayList<>());

            Long stageCount = (long) stageOps.size();
            BigDecimal stageExpectedAmount = stageOps.stream()
                    .map(CrmBusinessOpportunity::getExpectedAmount)
                    .filter(amount -> amount != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal stageForecastedAmount = stageOps.stream()
                    .map(CrmBusinessOpportunity::getForecastedAmount)
                    .filter(amount -> amount != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal countPercentage = totalCount > 0
                    ? BigDecimal.valueOf(stageCount).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal amountPercentage = totalExpectedAmount.compareTo(BigDecimal.ZERO) > 0
                    ? stageExpectedAmount.multiply(BigDecimal.valueOf(100)).divide(totalExpectedAmount, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            SalesFunnelStatisticsVO.StageStatisticsVO stageVO = SalesFunnelStatisticsVO.StageStatisticsVO.builder()
                    .stageId(stage.getId())
                    .stageCode(stage.getStageCode())
                    .stageName(stage.getStageName())
                    .sortOrder(stage.getSortOrder())
                    .winProbability(stage.getWinProbability())
                    .opportunityCount(stageCount)
                    .expectedAmount(stageExpectedAmount)
                    .forecastedAmount(stageForecastedAmount)
                    .countPercentage(countPercentage)
                    .amountPercentage(amountPercentage)
                    .build();

            stageStatisticsList.add(stageVO);
        }

        return SalesFunnelStatisticsVO.builder()
                .totalOpportunityCount(totalCount)
                .totalExpectedAmount(totalExpectedAmount)
                .totalForecastedAmount(totalForecastedAmount)
                .wonCount(wonCount)
                .wonAmount(wonAmount)
                .lostCount(lostCount)
                .inProgressCount(inProgressCount)
                .stageStatistics(stageStatisticsList)
                .build();
    }

    private BigDecimal calculateForecastedAmount(BigDecimal expectedAmount, BigDecimal winProbability) {
        if (expectedAmount == null || winProbability == null) {
            return BigDecimal.ZERO;
        }
        return expectedAmount.multiply(winProbability).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private String generateOpportunityNo() {
        return "OP" + System.currentTimeMillis() + IdUtil.nanoId(4);
    }

    private BusinessOpportunityVO convertToVO(CrmBusinessOpportunity opportunity) {
        BusinessOpportunityVO.BusinessOpportunityVOBuilder builder = BusinessOpportunityVO.builder()
                .id(opportunity.getId())
                .opportunityNo(opportunity.getOpportunityNo())
                .opportunityName(opportunity.getOpportunityName())
                .customerId(opportunity.getCustomerId())
                .customerName(opportunity.getCustomerName())
                .leadId(opportunity.getLeadId())
                .stageId(opportunity.getStageId())
                .stageCode(opportunity.getStageCode())
                .stageName(opportunity.getStageName())
                .winProbability(opportunity.getWinProbability())
                .expectedAmount(opportunity.getExpectedAmount())
                .forecastedAmount(opportunity.getForecastedAmount())
                .expectedDealDate(opportunity.getExpectedDealDate())
                .assignUserId(opportunity.getAssignUserId())
                .assignUserName(opportunity.getAssignUserName())
                .assignTime(opportunity.getAssignTime())
                .status(opportunity.getStatus())
                .statusName(opportunity.getStatus() != null && opportunity.getStatus() >= 0 && opportunity.getStatus() <= 2
                        ? STATUS_NAMES[opportunity.getStatus()] : null)
                .sourceName(opportunity.getSourceName())
                .industry(opportunity.getIndustry())
                .description(opportunity.getDescription())
                .remark(opportunity.getRemark())
                .tags(opportunity.getTags())
                .nextFollowTime(opportunity.getNextFollowTime())
                .followCount(opportunity.getFollowCount())
                .lastFollowTime(opportunity.getLastFollowTime())
                .createBy(opportunity.getCreateBy())
                .createTime(opportunity.getCreateTime())
                .updateBy(opportunity.getUpdateBy())
                .updateTime(opportunity.getUpdateTime());

        if (StrUtil.isNotBlank(opportunity.getTags())) {
            builder.tagList(Arrays.asList(opportunity.getTags().split(",")));
        }

        return builder.build();
    }
}
