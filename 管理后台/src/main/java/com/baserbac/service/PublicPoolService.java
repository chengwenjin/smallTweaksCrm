package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.PublicPoolClaimDTO;
import com.baserbac.dto.PublicPoolQueryDTO;
import com.baserbac.entity.*;
import com.baserbac.mapper.*;
import com.baserbac.vo.PublicPoolVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicPoolService {

    private final LeadMapper leadMapper;
    private final LeadLogMapper leadLogMapper;
    private final AssignRecordMapper assignRecordMapper;
    private final ObjectMapper objectMapper;

    public static final int RECYCLE_TYPE_LONG_TIME_NO_FOLLOW = 1;
    public static final int RECYCLE_TYPE_INVALID = 2;
    public static final int RECYCLE_TYPE_MANUAL = 3;

    public static final String[] RECYCLE_TYPE_NAMES = {"", "长期未跟进", "跟进无效", "手动回收"};

    public static final int ASSIGN_TYPE_AUTO = 1;
    public static final int ASSIGN_TYPE_MANUAL = 2;
    public static final int ASSIGN_TYPE_RECYCLE = 3;

    public static final String[] ASSIGN_TYPE_NAMES = {"", "自动分配", "手动分配", "回收"};

    public PageResult<PublicPoolVO> pagePublicPool(PublicPoolQueryDTO queryDTO) {
        Page<CrmLead> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmLead> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmLead::getIsPublic, 1)
                .like(StrUtil.isNotBlank(queryDTO.getLeadName()), CrmLead::getLeadName, queryDTO.getLeadName())
                .like(StrUtil.isNotBlank(queryDTO.getContactMobile()), CrmLead::getContactMobile, queryDTO.getContactMobile())
                .like(StrUtil.isNotBlank(queryDTO.getIndustry()), CrmLead::getIndustry, queryDTO.getIndustry())
                .eq(queryDTO.getSourceId() != null, CrmLead::getSourceId, queryDTO.getSourceId())
                .eq(queryDTO.getLevel() != null, CrmLead::getLevel, queryDTO.getLevel())
                .eq(queryDTO.getStatus() != null, CrmLead::getStatus, queryDTO.getStatus())
                .orderByDesc(CrmLead::getPublicTime);

        Page<CrmLead> result = leadMapper.selectPage(page, wrapper);

        List<PublicPoolVO> voList = new ArrayList<>();
        for (CrmLead lead : result.getRecords()) {
            voList.add(convertToVO(lead));
        }

        return new PageResult<>(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public void claimLead(PublicPoolClaimDTO claimDTO, Long userId, String userName) {
        log.info("开始认领线索: leadId={}, userId={}, userName={}", 
                 claimDTO.getLeadId(), userId, userName);
        
        if (userId == null) {
            log.error("认领失败: userId 为 null");
            throw new BusinessException("用户未登录，请先登录");
        }
        
        CrmLead lead = leadMapper.selectById(claimDTO.getLeadId());
        if (lead == null) {
            log.error("认领失败: 线索不存在, leadId={}", claimDTO.getLeadId());
            throw new BusinessException("线索不存在");
        }
        
        log.info("找到线索: leadId={}, leadNo={}, leadName={}, isPublic={}",
                 lead.getId(), lead.getLeadNo(), lead.getLeadName(), lead.getIsPublic());

        if (lead.getIsPublic() == null || lead.getIsPublic() != 1) {
            log.error("认领失败: 线索不在公海, isPublic={}", lead.getIsPublic());
            throw new BusinessException("该线索不在公海池中");
        }

        CrmLead beforeUpdate = null;
        try {
            beforeUpdate = lead.clone();
        } catch (Exception e) {
            log.warn("clone线索失败: {}", e.getMessage());
        }

        lead.setIsPublic(0);
        lead.setPublicReason(null);
        lead.setPublicTime(null);
        lead.setAssignUserId(userId);
        lead.setAssignUserName(userName != null ? userName : "未知用户");
        lead.setAssignTime(LocalDateTime.now());
        if (lead.getSourceUserId() == null) {
            lead.setSourceUserId(userId);
        }
        if (lead.getSourceUserName() == null) {
            lead.setSourceUserName(userName != null ? userName : "未知用户");
        }

        log.info("更新线索状态: assignUserId={}, assignUserName={}", 
                 lead.getAssignUserId(), lead.getAssignUserName());
        
        int updateCount = leadMapper.updateById(lead);
        log.info("更新线索结果: updateCount={}", updateCount);

        String logRemark = claimDTO.getRemark() != null ? claimDTO.getRemark() : "认领线索";
        saveLeadLog(lead, LeadService.OPERATE_TYPE_ASSIGN, "认领线索", 
                     beforeUpdate, userId, userName, logRemark);
        log.info("线索操作日志已保存");

        saveAssignRecord(lead, null, null, userId, userName, 
                         ASSIGN_TYPE_MANUAL, null, "从公海认领");
        log.info("分配记录已保存");
    }

    @Transactional(rollbackFor = Exception.class)
    public void recycleLead(Long leadId, Integer recycleType, String reason, Long operatorId, String operatorName) {
        CrmLead lead = leadMapper.selectById(leadId);
        if (lead == null) {
            throw new BusinessException("线索不存在");
        }

        if (lead.getIsPublic() != null && lead.getIsPublic() == 1) {
            throw new BusinessException("该线索已在公海池中");
        }

        CrmLead beforeUpdate = null;
        try {
            beforeUpdate = lead.clone();
        } catch (Exception e) {
            log.warn("clone线索失败: {}", e.getMessage());
        }

        Long fromUserId = lead.getAssignUserId();
        String fromUserName = lead.getAssignUserName();

        lead.setIsPublic(1);
        lead.setPublicReason(reason);
        lead.setPublicTime(LocalDateTime.now());
        lead.setAssignUserId(null);
        lead.setAssignUserName(null);
        lead.setAssignTime(null);

        leadMapper.updateById(lead);

        String logReason = RECYCLE_TYPE_NAMES[recycleType] + ": " + (reason != null ? reason : "");
        saveLeadLog(lead, LeadService.OPERATE_TYPE_RECYCLE, "回收线索", 
                     beforeUpdate, operatorId, operatorName, logReason);

        saveAssignRecord(lead, fromUserId, fromUserName, null, null, 
                         ASSIGN_TYPE_RECYCLE, null, logReason);
    }

    private void saveLeadLog(CrmLead lead, int operateType, String operateName, CrmLead beforeUpdate,
                             Long userId, String userName, String remark) {
        log.info("保存线索操作日志: leadId={}, leadNo={}, operateType={}, operateName={}",
                 lead.getId(), lead.getLeadNo(), operateType, operateName);
        
        if (lead.getId() == null) {
            log.error("保存日志失败: lead.id 为 null");
            return;
        }
        if (lead.getLeadNo() == null) {
            log.warn("leadNo 为 null, 使用 ID 代替");
        }
        
        CrmLeadLog leadLog = new CrmLeadLog();
        leadLog.setLeadId(lead.getId());
        leadLog.setLeadNo(lead.getLeadNo() != null ? lead.getLeadNo() : "LD-" + lead.getId());
        leadLog.setOperateType(operateType);
        leadLog.setOperateName(operateName);
        leadLog.setOperateUserId(userId);
        leadLog.setOperateUserName(userName != null ? userName : "未知用户");
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

        try {
            int insertCount = leadLogMapper.insert(leadLog);
            log.info("线索操作日志插入结果: insertCount={}, leadLogId={}", 
                     insertCount, leadLog.getId());
        } catch (Exception e) {
            log.error("插入线索操作日志失败", e);
        }
    }

    private void saveAssignRecord(CrmLead lead, Long fromUserId, String fromUserName,
                                   Long toUserId, String toUserName, Integer assignType,
                                   Long ruleId, String reason) {
        log.info("保存分配记录: leadId={}, leadNo={}, fromUserId={}, toUserId={}, assignType={}",
                 lead.getId(), lead.getLeadNo(), fromUserId, toUserId, assignType);
        
        if (lead.getId() == null) {
            log.error("保存分配记录失败: lead.id 为 null");
            return;
        }
        
        CrmAssignRecord record = new CrmAssignRecord();
        record.setLeadId(lead.getId());
        record.setLeadNo(lead.getLeadNo() != null ? lead.getLeadNo() : "LD-" + lead.getId());
        record.setFromUserId(fromUserId);
        record.setFromUserName(fromUserName);
        record.setToUserId(toUserId);
        record.setToUserName(toUserName);
        record.setAssignType(assignType);
        record.setRuleId(ruleId);
        record.setReason(reason);
        
        try {
            int insertCount = assignRecordMapper.insert(record);
            log.info("分配记录插入结果: insertCount={}, recordId={}", 
                     insertCount, record.getId());
        } catch (Exception e) {
            log.error("插入分配记录失败", e);
        }
    }

    private PublicPoolVO convertToVO(CrmLead lead) {
        return PublicPoolVO.builder()
                .id(lead.getId())
                .leadId(lead.getId())
                .leadNo(lead.getLeadNo())
                .leadName(lead.getLeadName())
                .contactName(lead.getContactName())
                .contactMobile(lead.getContactMobile())
                .province(lead.getProvince())
                .city(lead.getCity())
                .industry(lead.getIndustry())
                .sourceId(lead.getSourceId())
                .sourceName(lead.getSourceName())
                .level(lead.getLevel())
                .levelName(lead.getLevel() != null && lead.getLevel() > 0 && lead.getLevel() <= 3
                        ? LeadService.LEVEL_NAMES[lead.getLevel()] : null)
                .status(lead.getStatus())
                .statusName(lead.getStatus() != null && lead.getStatus() >= 0 && lead.getStatus() <= 4
                        ? LeadService.STATUS_NAMES[lead.getStatus()] : null)
                .expectedAmount(lead.getExpectedAmount())
                .fromUserId(lead.getAssignUserId())
                .fromUserName(lead.getAssignUserName())
                .publicTime(lead.getPublicTime())
                .createTime(lead.getCreateTime())
                .build();
    }
}
