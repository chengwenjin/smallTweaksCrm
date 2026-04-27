package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.SalesStageCreateDTO;
import com.baserbac.dto.SalesStageQueryDTO;
import com.baserbac.dto.SalesStageUpdateDTO;
import com.baserbac.entity.CrmSalesStage;
import com.baserbac.mapper.SalesStageMapper;
import com.baserbac.vo.SalesStageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesStageService {

    private final SalesStageMapper salesStageMapper;

    public static final String[] CLOSE_TYPE_NAMES = {"", "赢单", "输单"};

    public PageResult<SalesStageVO> pageStages(SalesStageQueryDTO queryDTO) {
        Page<CrmSalesStage> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmSalesStage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getStageName()), CrmSalesStage::getStageName, queryDTO.getStageName())
                .eq(StrUtil.isNotBlank(queryDTO.getStageCode()), CrmSalesStage::getStageCode, queryDTO.getStageCode())
                .eq(queryDTO.getIsEnabled() != null, CrmSalesStage::getIsEnabled, queryDTO.getIsEnabled())
                .eq(queryDTO.getIsSystem() != null, CrmSalesStage::getIsSystem, queryDTO.getIsSystem())
                .orderByAsc(CrmSalesStage::getSortOrder);

        Page<CrmSalesStage> result = salesStageMapper.selectPage(page, wrapper);

        List<SalesStageVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    public List<SalesStageVO> getAllEnabledStages() {
        List<CrmSalesStage> stages = salesStageMapper.selectList(
                new LambdaQueryWrapper<CrmSalesStage>()
                        .eq(CrmSalesStage::getIsEnabled, 1)
                        .orderByAsc(CrmSalesStage::getSortOrder)
        );

        return stages.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public SalesStageVO getStageById(Long id) {
        CrmSalesStage stage = salesStageMapper.selectById(id);
        if (stage == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(stage);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createStage(SalesStageCreateDTO createDTO) {
        LambdaQueryWrapper<CrmSalesStage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmSalesStage::getStageCode, createDTO.getStageCode());
        Long count = salesStageMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }

        CrmSalesStage stage = new CrmSalesStage();
        stage.setStageCode(createDTO.getStageCode());
        stage.setStageName(createDTO.getStageName());
        stage.setSortOrder(createDTO.getSortOrder() != null ? createDTO.getSortOrder() : 0);
        stage.setWinProbability(createDTO.getWinProbability() != null ? createDTO.getWinProbability() : BigDecimal.ZERO);
        stage.setDescription(createDTO.getDescription());
        stage.setIsSystem(0);
        stage.setIsEnabled(createDTO.getIsEnabled() != null ? createDTO.getIsEnabled() : 1);
        stage.setIsClosed(createDTO.getIsClosed() != null ? createDTO.getIsClosed() : 0);
        stage.setCloseType(createDTO.getCloseType() != null ? createDTO.getCloseType() : 0);
        stage.setIsDeleted(0);

        salesStageMapper.insert(stage);
        return stage.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStage(SalesStageUpdateDTO updateDTO) {
        CrmSalesStage stage = salesStageMapper.selectById(updateDTO.getId());
        if (stage == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (StrUtil.isNotBlank(updateDTO.getStageCode()) && !updateDTO.getStageCode().equals(stage.getStageCode())) {
            LambdaQueryWrapper<CrmSalesStage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CrmSalesStage::getStageCode, updateDTO.getStageCode())
                    .ne(CrmSalesStage::getId, updateDTO.getId());
            Long count = salesStageMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST);
            }
            stage.setStageCode(updateDTO.getStageCode());
        }

        if (updateDTO.getStageName() != null) {
            stage.setStageName(updateDTO.getStageName());
        }
        if (updateDTO.getSortOrder() != null) {
            stage.setSortOrder(updateDTO.getSortOrder());
        }
        if (updateDTO.getWinProbability() != null) {
            stage.setWinProbability(updateDTO.getWinProbability());
        }
        if (updateDTO.getDescription() != null) {
            stage.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getIsEnabled() != null) {
            stage.setIsEnabled(updateDTO.getIsEnabled());
        }
        if (updateDTO.getIsClosed() != null) {
            stage.setIsClosed(updateDTO.getIsClosed());
        }
        if (updateDTO.getCloseType() != null) {
            stage.setCloseType(updateDTO.getCloseType());
        }

        salesStageMapper.updateById(stage);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStage(Long id) {
        CrmSalesStage stage = salesStageMapper.selectById(id);
        if (stage == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (stage.getIsSystem() != null && stage.getIsSystem() == 1) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }

        salesStageMapper.deleteById(id);
    }

    private SalesStageVO convertToVO(CrmSalesStage stage) {
        String closeTypeName = null;
        if (stage.getCloseType() != null && stage.getCloseType() >= 1 && stage.getCloseType() <= 2) {
            closeTypeName = CLOSE_TYPE_NAMES[stage.getCloseType()];
        }

        return SalesStageVO.builder()
                .id(stage.getId())
                .stageCode(stage.getStageCode())
                .stageName(stage.getStageName())
                .sortOrder(stage.getSortOrder())
                .winProbability(stage.getWinProbability())
                .description(stage.getDescription())
                .isSystem(stage.getIsSystem())
                .isEnabled(stage.getIsEnabled())
                .isClosed(stage.getIsClosed())
                .closeType(stage.getCloseType())
                .closeTypeName(closeTypeName)
                .createTime(stage.getCreateTime())
                .updateTime(stage.getUpdateTime())
                .build();
    }
}
