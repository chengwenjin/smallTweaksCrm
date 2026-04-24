package com.baserbac.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.LeadApiCreateDTO;
import com.baserbac.dto.LeadAssignDTO;
import com.baserbac.dto.LeadCreateDTO;
import com.baserbac.dto.LeadDuplicateCheckDTO;
import com.baserbac.dto.LeadImportResultDTO;
import com.baserbac.dto.LeadQueryDTO;
import com.baserbac.dto.LeadStatusUpdateDTO;
import com.baserbac.dto.LeadUpdateDTO;
import com.baserbac.entity.CrmLead;
import com.baserbac.entity.CrmLeadLog;
import com.baserbac.entity.CrmLeadSource;
import com.baserbac.entity.SysUser;
import com.baserbac.mapper.LeadLogMapper;
import com.baserbac.mapper.LeadMapper;
import com.baserbac.mapper.LeadSourceMapper;
import com.baserbac.mapper.UserMapper;
import com.baserbac.vo.LeadDuplicateCheckVO;
import com.baserbac.vo.LeadSourceVO;
import com.baserbac.vo.LeadVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 线索服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadMapper leadMapper;
    private final LeadSourceMapper leadSourceMapper;
    private final LeadLogMapper leadLogMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public static final String[] LEVEL_NAMES = {"", "高", "中", "低"};
    public static final String[] STATUS_NAMES = {"新线索", "跟进中", "已转化", "已无效", "已回收"};
    public static final String[] SOURCE_TYPE_NAMES = {"", "手动录入", "批量导入", "API对接"};

    public static final int OPERATE_TYPE_CREATE = 1;
    public static final int OPERATE_TYPE_UPDATE = 2;
    public static final int OPERATE_TYPE_ASSIGN = 3;
    public static final int OPERATE_TYPE_FOLLOW = 4;
    public static final int OPERATE_TYPE_CONVERT = 5;
    public static final int OPERATE_TYPE_INVALID = 6;
    public static final int OPERATE_TYPE_RECYCLE = 7;
    public static final int OPERATE_TYPE_DELETE = 8;

    public static final String[] OPERATE_NAMES = {
            "", "创建线索", "编辑线索", "分配线索", "跟进线索", "转化线索", "无效线索", "回收线索", "删除线索"
    };

    /**
     * 分页查询线索列表
     */
    public PageResult<LeadVO> pageLeads(LeadQueryDTO queryDTO) {
        Page<CrmLead> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmLead> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getLeadName()), CrmLead::getLeadName, queryDTO.getLeadName())
                .like(StrUtil.isNotBlank(queryDTO.getContactName()), CrmLead::getContactName, queryDTO.getContactName())
                .like(StrUtil.isNotBlank(queryDTO.getContactMobile()), CrmLead::getContactMobile, queryDTO.getContactMobile())
                .eq(queryDTO.getSourceId() != null, CrmLead::getSourceId, queryDTO.getSourceId())
                .eq(StrUtil.isNotBlank(queryDTO.getSourceCode()), CrmLead::getSourceCode, queryDTO.getSourceCode())
                .eq(queryDTO.getLevel() != null, CrmLead::getLevel, queryDTO.getLevel())
                .eq(queryDTO.getStatus() != null, CrmLead::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getAssignUserId() != null, CrmLead::getAssignUserId, queryDTO.getAssignUserId())
                .ge(queryDTO.getCreateTimeStart() != null, CrmLead::getCreateTime, queryDTO.getCreateTimeStart())
                .le(queryDTO.getCreateTimeEnd() != null, CrmLead::getCreateTime, queryDTO.getCreateTimeEnd())
                .like(StrUtil.isNotBlank(queryDTO.getTags()), CrmLead::getTags, queryDTO.getTags())
                .orderByDesc(CrmLead::getCreateTime);

        Page<CrmLead> result = leadMapper.selectPage(page, wrapper);

        List<LeadVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    /**
     * 根据ID查询线索
     */
    public LeadVO getLeadById(Long id) {
        CrmLead lead = leadMapper.selectById(id);
        if (lead == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(lead);
    }

    /**
     * 创建线索（手工录入）
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createLead(LeadCreateDTO createDTO, Long userId, String userName) {
        CrmLeadSource source = validateSource(createDTO.getSourceId(), null);

        CrmLead lead = buildLeadFromCreateDTO(createDTO, source, userId, userName);
        leadMapper.insert(lead);

        saveLeadLog(lead, OPERATE_TYPE_CREATE, "创建线索", null, userId, userName, null);

        return lead.getId();
    }

    /**
     * API 对接创建线索（用于官网表单、市场活动）
     * 无需登录认证，仅验证来源编码
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createLeadFromApi(LeadApiCreateDTO createDTO) {
        CrmLeadSource source = validateSource(null, createDTO.getSourceCode());

        CrmLead lead = new CrmLead();
        lead.setLeadNo(generateLeadNo());
        lead.setLeadName(createDTO.getLeadName());
        lead.setContactName(createDTO.getContactName());
        lead.setContactPhone(createDTO.getContactPhone());
        lead.setContactMobile(createDTO.getContactMobile());
        lead.setContactEmail(createDTO.getContactEmail());
        lead.setContactQq(createDTO.getContactQq());
        lead.setContactWechat(createDTO.getContactWechat());
        lead.setProvince(createDTO.getProvince());
        lead.setCity(createDTO.getCity());
        lead.setDistrict(createDTO.getDistrict());
        lead.setAddress(createDTO.getAddress());
        lead.setIndustry(createDTO.getIndustry());
        lead.setSourceId(source.getId());
        lead.setSourceCode(source.getSourceCode());
        lead.setSourceName(StrUtil.isNotBlank(createDTO.getSourceName()) ? createDTO.getSourceName() : source.getSourceName());
        lead.setSourceRemark(createDTO.getSourceRemark());
        lead.setLevel(3);
        lead.setStatus(0);
        lead.setDescription(createDTO.getDescription());
        lead.setRemark(createDTO.getRemark());
        lead.setTags(createDTO.getTags());
        lead.setIsDeleted(0);

        leadMapper.insert(lead);

        saveLeadLog(lead, OPERATE_TYPE_CREATE, "API对接创建线索", null, null, "API对接", createDTO.getSourceRemark());

        return lead.getId();
    }

    /**
     * 更新线索
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLead(LeadUpdateDTO updateDTO, Long userId, String userName) {
        CrmLead lead = leadMapper.selectById(updateDTO.getId());
        if (lead == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        CrmLead beforeUpdate = deepCopyLead(lead);

        boolean changed = false;

        if (updateDTO.getLeadName() != null) {
            lead.setLeadName(updateDTO.getLeadName());
            changed = true;
        }
        if (updateDTO.getContactName() != null) {
            lead.setContactName(updateDTO.getContactName());
            changed = true;
        }
        if (updateDTO.getContactPhone() != null) {
            lead.setContactPhone(updateDTO.getContactPhone());
            changed = true;
        }
        if (updateDTO.getContactMobile() != null) {
            lead.setContactMobile(updateDTO.getContactMobile());
            changed = true;
        }
        if (updateDTO.getContactEmail() != null) {
            lead.setContactEmail(updateDTO.getContactEmail());
            changed = true;
        }
        if (updateDTO.getContactQq() != null) {
            lead.setContactQq(updateDTO.getContactQq());
            changed = true;
        }
        if (updateDTO.getContactWechat() != null) {
            lead.setContactWechat(updateDTO.getContactWechat());
            changed = true;
        }
        if (updateDTO.getProvince() != null) {
            lead.setProvince(updateDTO.getProvince());
            changed = true;
        }
        if (updateDTO.getCity() != null) {
            lead.setCity(updateDTO.getCity());
            changed = true;
        }
        if (updateDTO.getDistrict() != null) {
            lead.setDistrict(updateDTO.getDistrict());
            changed = true;
        }
        if (updateDTO.getAddress() != null) {
            lead.setAddress(updateDTO.getAddress());
            changed = true;
        }
        if (updateDTO.getIndustry() != null) {
            lead.setIndustry(updateDTO.getIndustry());
            changed = true;
        }
        if (updateDTO.getSourceId() != null) {
            CrmLeadSource source = validateSource(updateDTO.getSourceId(), null);
            lead.setSourceId(source.getId());
            lead.setSourceCode(source.getSourceCode());
            lead.setSourceName(source.getSourceName());
            changed = true;
        }
        if (updateDTO.getSourceRemark() != null) {
            lead.setSourceRemark(updateDTO.getSourceRemark());
            changed = true;
        }
        if (updateDTO.getLevel() != null) {
            lead.setLevel(updateDTO.getLevel());
            changed = true;
        }
        if (updateDTO.getStatus() != null) {
            lead.setStatus(updateDTO.getStatus());
            changed = true;
        }
        if (updateDTO.getProbability() != null) {
            lead.setProbability(updateDTO.getProbability());
            changed = true;
        }
        if (updateDTO.getExpectedAmount() != null) {
            lead.setExpectedAmount(updateDTO.getExpectedAmount());
            changed = true;
        }
        if (updateDTO.getExpectedDealDate() != null) {
            lead.setExpectedDealDate(updateDTO.getExpectedDealDate());
            changed = true;
        }
        if (updateDTO.getNextFollowTime() != null) {
            lead.setNextFollowTime(updateDTO.getNextFollowTime());
            changed = true;
        }
        if (updateDTO.getDescription() != null) {
            lead.setDescription(updateDTO.getDescription());
            changed = true;
        }
        if (updateDTO.getRemark() != null) {
            lead.setRemark(updateDTO.getRemark());
            changed = true;
        }
        if (updateDTO.getTags() != null) {
            lead.setTags(updateDTO.getTags());
            changed = true;
        }

        if (changed) {
            leadMapper.updateById(lead);
            saveLeadLog(lead, OPERATE_TYPE_UPDATE, "编辑线索", beforeUpdate, userId, userName, updateDTO.getRemark());
        }
    }

    /**
     * 删除线索（软删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteLead(Long id, Long userId, String userName) {
        CrmLead lead = leadMapper.selectById(id);
        if (lead == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        leadMapper.deleteById(id);
        saveLeadLog(lead, OPERATE_TYPE_DELETE, "删除线索", null, userId, userName, null);
    }

    /**
     * 更新线索状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLeadStatus(Long id, LeadStatusUpdateDTO statusDTO, Long userId, String userName) {
        CrmLead lead = leadMapper.selectById(id);
        if (lead == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        CrmLead beforeUpdate = deepCopyLead(lead);
        lead.setStatus(statusDTO.getStatus());
        leadMapper.updateById(lead);

        String operateName = "更新状态";
        int operateType = OPERATE_TYPE_UPDATE;
        if (statusDTO.getStatus() == 2) {
            operateType = OPERATE_TYPE_CONVERT;
            operateName = "转化线索";
        } else if (statusDTO.getStatus() == 3) {
            operateType = OPERATE_TYPE_INVALID;
            operateName = "无效线索";
        } else if (statusDTO.getStatus() == 4) {
            operateType = OPERATE_TYPE_RECYCLE;
            operateName = "回收线索";
        }

        saveLeadLog(lead, operateType, operateName, beforeUpdate, userId, userName, statusDTO.getRemark());
    }

    /**
     * 分配线索
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignLead(Long id, LeadAssignDTO assignDTO, Long userId, String userName) {
        CrmLead lead = leadMapper.selectById(id);
        if (lead == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        SysUser assignUser = userMapper.selectById(assignDTO.getAssignUserId());
        if (assignUser == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        CrmLead beforeUpdate = deepCopyLead(lead);
        lead.setAssignUserId(assignUser.getId());
        lead.setAssignUserName(assignUser.getRealName() != null ? assignUser.getRealName() : assignUser.getUsername());
        lead.setAssignTime(LocalDateTime.now());
        lead.setStatus(1);
        leadMapper.updateById(lead);

        saveLeadLog(lead, OPERATE_TYPE_ASSIGN, "分配线索给：" + lead.getAssignUserName(),
                beforeUpdate, userId, userName, assignDTO.getRemark());
    }

    /**
     * 获取所有线索来源
     */
    public List<LeadSourceVO> getAllSources() {
        List<CrmLeadSource> sources = leadSourceMapper.selectList(
                new LambdaQueryWrapper<CrmLeadSource>()
                        .eq(CrmLeadSource::getIsEnabled, 1)
                        .orderByAsc(CrmLeadSource::getSortOrder)
        );

        return sources.stream()
                .map(this::convertSourceToVO)
                .collect(Collectors.toList());
    }

    /**
     * Excel 批量导入线索
     */
    @Transactional(rollbackFor = Exception.class)
    public LeadImportResultDTO importLeads(MultipartFile file, Long userId, String userName) {
        List<LeadImportRow> importRows = new ArrayList<>();
        List<CrmLeadSource> allSources = leadSourceMapper.selectList(
                new LambdaQueryWrapper<CrmLeadSource>().eq(CrmLeadSource::getIsEnabled, 1)
        );
        Map<String, CrmLeadSource> sourceMap = allSources.stream()
                .collect(Collectors.toMap(CrmLeadSource::getSourceName, s -> s, (s1, s2) -> s1));

        try {
            EasyExcel.read(file.getInputStream(), LeadImportRow.class, new ReadListener<LeadImportRow>() {
                @Override
                public void invoke(LeadImportRow row, AnalysisContext context) {
                    importRows.add(row);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(1).doRead();
        } catch (IOException e) {
            log.error("读取Excel文件失败", e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }

        int totalCount = importRows.size();
        int successCount = 0;
        List<Long> successIds = new ArrayList<>();
        List<LeadImportResultDTO.FailDetail> failDetails = new ArrayList<>();

        for (int i = 0; i < importRows.size(); i++) {
            int rowNum = i + 2;
            LeadImportRow row = importRows.get(i);
            try {
                validateImportRow(row);

                CrmLeadSource source = null;
                if (StrUtil.isNotBlank(row.getSourceName())) {
                    source = sourceMap.get(row.getSourceName());
                }
                if (source == null) {
                    source = sourceMap.get("手工录入");
                    if (source == null && !allSources.isEmpty()) {
                        source = allSources.get(0);
                    }
                }
                if (source == null) {
                    throw new BusinessException(ResultCode.BAD_REQUEST);
                }

                CrmLead lead = new CrmLead();
                lead.setLeadNo(generateLeadNo());
                lead.setLeadName(row.getLeadName());
                lead.setContactName(row.getContactName());
                lead.setContactPhone(row.getContactPhone());
                lead.setContactMobile(row.getContactMobile());
                lead.setContactEmail(row.getContactEmail());
                lead.setContactQq(row.getContactQq());
                lead.setContactWechat(row.getContactWechat());
                lead.setProvince(row.getProvince());
                lead.setCity(row.getCity());
                lead.setDistrict(row.getDistrict());
                lead.setAddress(row.getAddress());
                lead.setIndustry(row.getIndustry());
                lead.setSourceId(source.getId());
                lead.setSourceCode(source.getSourceCode());
                lead.setSourceName(source.getSourceName());
                lead.setSourceRemark(row.getSourceRemark());
                lead.setLevel(parseLevel(row.getLevel()));
                lead.setStatus(0);
                lead.setDescription(row.getDescription());
                lead.setRemark(row.getRemark());
                lead.setTags(row.getTags());
                lead.setIsDeleted(0);

                leadMapper.insert(lead);
                saveLeadLog(lead, OPERATE_TYPE_CREATE, "Excel批量导入", null, userId, userName,
                        "第" + rowNum + "行导入");

                successCount++;
                successIds.add(lead.getId());
            } catch (Exception e) {
                log.error("导入第 {} 行失败: {}", rowNum, e.getMessage());
                LeadImportResultDTO.FailDetail detail = new LeadImportResultDTO.FailDetail();
                detail.setRowNum(rowNum);
                detail.setData(row.toString());
                detail.setReason(e.getMessage());
                failDetails.add(detail);
            }
        }

        LeadImportResultDTO result = new LeadImportResultDTO();
        result.setTotalCount(totalCount);
        result.setSuccessCount(successCount);
        result.setFailCount(totalCount - successCount);
        result.setSuccessIds(successIds);
        result.setFailDetails(failDetails);

        return result;
    }

    private void validateImportRow(LeadImportRow row) {
        if (StrUtil.isBlank(row.getLeadName())) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }
    }

    private int parseLevel(String levelName) {
        if (StrUtil.isBlank(levelName)) {
            return 3;
        }
        if ("高".equals(levelName)) {
            return 1;
        }
        if ("中".equals(levelName)) {
            return 2;
        }
        if ("低".equals(levelName)) {
            return 3;
        }
        try {
            int level = Integer.parseInt(levelName);
            if (level >= 1 && level <= 3) {
                return level;
            }
        } catch (NumberFormatException ignored) {
        }
        return 3;
    }

    private CrmLeadSource validateSource(Long sourceId, String sourceCode) {
        CrmLeadSource source = null;

        if (sourceId != null) {
            source = leadSourceMapper.selectById(sourceId);
        } else if (StrUtil.isNotBlank(sourceCode)) {
            source = leadSourceMapper.selectOne(
                    new LambdaQueryWrapper<CrmLeadSource>()
                            .eq(CrmLeadSource::getSourceCode, sourceCode)
                            .eq(CrmLeadSource::getIsEnabled, 1)
            );
        }

        if (source == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }

        if (source.getIsEnabled() == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }

        return source;
    }

    private CrmLead buildLeadFromCreateDTO(LeadCreateDTO createDTO, CrmLeadSource source, Long userId, String userName) {
        CrmLead lead = new CrmLead();
        lead.setLeadNo(generateLeadNo());
        lead.setLeadName(createDTO.getLeadName());
        lead.setContactName(createDTO.getContactName());
        lead.setContactPhone(createDTO.getContactPhone());
        lead.setContactMobile(createDTO.getContactMobile());
        lead.setContactEmail(createDTO.getContactEmail());
        lead.setContactQq(createDTO.getContactQq());
        lead.setContactWechat(createDTO.getContactWechat());
        lead.setProvince(createDTO.getProvince());
        lead.setCity(createDTO.getCity());
        lead.setDistrict(createDTO.getDistrict());
        lead.setAddress(createDTO.getAddress());
        lead.setIndustry(createDTO.getIndustry());
        lead.setSourceId(source.getId());
        lead.setSourceCode(source.getSourceCode());
        lead.setSourceName(source.getSourceName());
        lead.setSourceRemark(createDTO.getSourceRemark());
        lead.setLevel(createDTO.getLevel() != null ? createDTO.getLevel() : 3);
        lead.setStatus(0);
        lead.setProbability(createDTO.getProbability());
        lead.setExpectedAmount(createDTO.getExpectedAmount());
        lead.setExpectedDealDate(createDTO.getExpectedDealDate());
        lead.setNextFollowTime(createDTO.getNextFollowTime());
        lead.setDescription(createDTO.getDescription());
        lead.setRemark(createDTO.getRemark());
        lead.setTags(createDTO.getTags());
        lead.setIsDeleted(0);

        if (createDTO.getAssignUserId() != null) {
            SysUser assignUser = userMapper.selectById(createDTO.getAssignUserId());
            if (assignUser != null) {
                lead.setAssignUserId(assignUser.getId());
                lead.setAssignUserName(assignUser.getRealName() != null ? assignUser.getRealName() : assignUser.getUsername());
                lead.setAssignTime(LocalDateTime.now());
            }
        }

        return lead;
    }

    private String generateLeadNo() {
        return "LD" + System.currentTimeMillis() + IdUtil.nanoId(4);
    }

    private CrmLead deepCopyLead(CrmLead lead) {
        CrmLead copy = new CrmLead();
        copy.setId(lead.getId());
        copy.setLeadNo(lead.getLeadNo());
        copy.setLeadName(lead.getLeadName());
        copy.setContactName(lead.getContactName());
        copy.setContactPhone(lead.getContactPhone());
        copy.setContactMobile(lead.getContactMobile());
        copy.setContactEmail(lead.getContactEmail());
        copy.setContactQq(lead.getContactQq());
        copy.setContactWechat(lead.getContactWechat());
        copy.setProvince(lead.getProvince());
        copy.setCity(lead.getCity());
        copy.setDistrict(lead.getDistrict());
        copy.setAddress(lead.getAddress());
        copy.setIndustry(lead.getIndustry());
        copy.setSourceId(lead.getSourceId());
        copy.setSourceCode(lead.getSourceCode());
        copy.setSourceName(lead.getSourceName());
        copy.setSourceRemark(lead.getSourceRemark());
        copy.setLevel(lead.getLevel());
        copy.setStatus(lead.getStatus());
        copy.setProbability(lead.getProbability());
        copy.setExpectedAmount(lead.getExpectedAmount());
        copy.setExpectedDealDate(lead.getExpectedDealDate());
        copy.setNextFollowTime(lead.getNextFollowTime());
        copy.setAssignUserId(lead.getAssignUserId());
        copy.setAssignUserName(lead.getAssignUserName());
        copy.setAssignTime(lead.getAssignTime());
        copy.setDescription(lead.getDescription());
        copy.setRemark(lead.getRemark());
        copy.setTags(lead.getTags());
        return copy;
    }

    private void saveLeadLog(CrmLead lead, int operateType, String operateName, CrmLead beforeUpdate,
                             Long userId, String userName, String remark) {
        CrmLeadLog leadLog = new CrmLeadLog();
        leadLog.setLeadId(lead.getId());
        leadLog.setLeadNo(lead.getLeadNo());
        leadLog.setOperateType(operateType);
        leadLog.setOperateName(operateName);
        leadLog.setOperateUserId(userId);
        leadLog.setOperateUserName(userName);
        leadLog.setRemark(remark);

        if (beforeUpdate != null) {
            try {
                Map<String, Object> content = new HashMap<>();
                content.put("before", beforeUpdate);
                content.put("after", lead);
                leadLog.setOperateContent(objectMapper.writeValueAsString(content));
            } catch (JsonProcessingException e) {
                log.error("序列化操作日志失败", e);
            }
        }

        leadLogMapper.insert(leadLog);
    }

    private LeadVO convertToVO(CrmLead lead) {
        LeadVO.LeadVOBuilder builder = LeadVO.builder()
                .id(lead.getId())
                .leadNo(lead.getLeadNo())
                .leadName(lead.getLeadName())
                .contactName(lead.getContactName())
                .contactPhone(lead.getContactPhone())
                .contactMobile(lead.getContactMobile())
                .contactEmail(lead.getContactEmail())
                .contactQq(lead.getContactQq())
                .contactWechat(lead.getContactWechat())
                .province(lead.getProvince())
                .city(lead.getCity())
                .district(lead.getDistrict())
                .address(lead.getAddress())
                .industry(lead.getIndustry())
                .sourceId(lead.getSourceId())
                .sourceCode(lead.getSourceCode())
                .sourceName(lead.getSourceName())
                .sourceRemark(lead.getSourceRemark())
                .level(lead.getLevel())
                .levelName(lead.getLevel() != null && lead.getLevel() > 0 && lead.getLevel() <= 3
                        ? LEVEL_NAMES[lead.getLevel()] : null)
                .status(lead.getStatus())
                .statusName(lead.getStatus() != null && lead.getStatus() >= 0 && lead.getStatus() <= 4
                        ? STATUS_NAMES[lead.getStatus()] : null)
                .probability(lead.getProbability())
                .expectedAmount(lead.getExpectedAmount())
                .expectedDealDate(lead.getExpectedDealDate())
                .nextFollowTime(lead.getNextFollowTime())
                .assignUserId(lead.getAssignUserId())
                .assignUserName(lead.getAssignUserName())
                .assignTime(lead.getAssignTime())
                .description(lead.getDescription())
                .remark(lead.getRemark())
                .tags(lead.getTags())
                .createBy(lead.getCreateBy())
                .createTime(lead.getCreateTime())
                .updateBy(lead.getUpdateBy())
                .updateTime(lead.getUpdateTime());

        if (StrUtil.isNotBlank(lead.getTags())) {
            builder.tagList(Arrays.asList(lead.getTags().split(",")));
        }

        return builder.build();
    }

    private LeadSourceVO convertSourceToVO(CrmLeadSource source) {
        return LeadSourceVO.builder()
                .id(source.getId())
                .sourceCode(source.getSourceCode())
                .sourceName(source.getSourceName())
                .sourceType(source.getSourceType())
                .sourceTypeName(source.getSourceType() != null && source.getSourceType() >= 1 && source.getSourceType() <= 3
                        ? SOURCE_TYPE_NAMES[source.getSourceType()] : null)
                .isSystem(source.getIsSystem())
                .isEnabled(source.getIsEnabled())
                .sortOrder(source.getSortOrder())
                .description(source.getDescription())
                .createTime(source.getCreateTime())
                .updateTime(source.getUpdateTime())
                .build();
    }

    @Data
    public static class LeadImportRow implements Serializable {
        private static final long serialVersionUID = 1L;

        private String leadName;
        private String contactName;
        private String contactPhone;
        private String contactMobile;
        private String contactEmail;
        private String contactQq;
        private String contactWechat;
        private String province;
        private String city;
        private String district;
        private String address;
        private String industry;
        private String sourceName;
        private String sourceRemark;
        private String level;
        private String description;
        private String remark;
        private String tags;

        @Override
        public String toString() {
            return "leadName=" + leadName +
                    ", contactName=" + contactName +
                    ", contactMobile=" + contactMobile;
        }
    }

    public LeadDuplicateCheckVO checkDuplicate(LeadDuplicateCheckDTO checkDTO) {
        Long excludeId = checkDTO.getLeadId();
        Integer checkType = checkDTO.getCheckType();

        if (checkType == null) {
            checkType = 3;
        }

        if (checkType == 1 || checkType == 3) {
            if (StrUtil.isNotBlank(checkDTO.getContactMobile())) {
                LambdaQueryWrapper<CrmLead> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CrmLead::getContactMobile, checkDTO.getContactMobile())
                        .ne(excludeId != null, CrmLead::getId, excludeId);
                CrmLead duplicate = leadMapper.selectOne(wrapper);
                if (duplicate != null) {
                    return LeadDuplicateCheckVO.builder()
                            .hasDuplicate(true)
                            .duplicateType("mobile")
                            .duplicateMessage("手机号已存在，线索名称：" + duplicate.getLeadName())
                            .duplicateLeadId(duplicate.getId())
                            .duplicateLeadName(duplicate.getLeadName())
                            .duplicateContactMobile(duplicate.getContactMobile())
                            .build();
                }
            }
        }

        if (checkType == 2 || checkType == 3) {
            if (StrUtil.isNotBlank(checkDTO.getLeadName())) {
                LambdaQueryWrapper<CrmLead> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CrmLead::getLeadName, checkDTO.getLeadName())
                        .ne(excludeId != null, CrmLead::getId, excludeId);
                CrmLead duplicate = leadMapper.selectOne(wrapper);
                if (duplicate != null) {
                    return LeadDuplicateCheckVO.builder()
                            .hasDuplicate(true)
                            .duplicateType("name")
                            .duplicateMessage("企业名称已存在，手机号：" + duplicate.getContactMobile())
                            .duplicateLeadId(duplicate.getId())
                            .duplicateLeadName(duplicate.getLeadName())
                            .duplicateContactMobile(duplicate.getContactMobile())
                            .build();
                }
            }
        }

        return LeadDuplicateCheckVO.builder()
                .hasDuplicate(false)
                .duplicateType(null)
                .duplicateMessage("未发现重复数据")
                .build();
    }
}
