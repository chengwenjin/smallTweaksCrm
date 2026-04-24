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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicPoolService {

    private final LeadMapper leadMapper;
    private final LeadLogMapper leadLogMapper;
    private final AssignRecordMapper assignRecordMapper;
    private final UserMapper userMapper;
    private final AssignRuleService assignRuleService;

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
        CrmLead lead = leadMapper.selectById(claimDTO.getLeadId());
        if (lead == null) {
            throw new BusinessException("线索不存在");
        }

        if (lead.getIsPublic() == null || lead.getIsPublic() != 1) {
            throw new BusinessException("该线索不在公海池中");
        }

        CrmLead beforeUpdate = null;
        try {
            beforeUpdate = (CrmLead) lead.clone();
        } catch (CloneNotSupportedException ignored) {}

        lead.setIsPublic(0);
        lead.setPublicReason(null);
        lead.setPublicTime(null);
        lead.setAssignUserId(userId);
        lead.setAssignUserName(userName);
        lead.setAssignTime(LocalDateTime.now());
        lead.setSourceUserId(userId);
        lead.setSourceUserName(userName);

        leadMapper.updateById(lead);

        saveLeadLog(lead, LeadService.OPERATE_TYPE_ASSIGN, "认领线索", beforeUpdate, userId, userName, claimDTO.getRemark());

        saveAssignRecord(lead, null, null, userId, userName, ASSIGN_TYPE_MANUAL, null, "从公海认领");
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
            beforeUpdate = (CrmLead) lead.clone();
        } catch (CloneNotSupportedException ignored) {}

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
        saveLeadLog(lead, LeadService.OPERATE_TYPE_RECYCLE, "回收线索", beforeUpdate, operatorId, operatorName, logReason);

        saveAssignRecord(lead, fromUserId, fromUserName, null, null, ASSIGN_TYPE_RECYCLE, null, logReason);
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoAssignLead(CrmLead lead) {
        CrmAssignRule matchingRule = assignRuleService.findMatchingRule(lead);

        Long toUserId = null;
        String toUserName = null;
        Long ruleId = null;

        if (matchingRule != null && matchingRule.getAssignUserId() != null) {
            toUserId = matchingRule.getAssignUserId();
            toUserName = matchingRule.getAssignUserName();
            ruleId = matchingRule.getId();
        }

        if (toUserId == null) {
            return;
        }

        CrmLead beforeUpdate = null;
        try {
            beforeUpdate = (CrmLead) lead.clone();
        } catch (CloneNotSupportedException ignored) {}

        lead.setAssignUserId(toUserId);
        lead.setAssignUserName(toUserName);
        lead.setAssignTime(LocalDateTime.now());
        lead.setIsPublic(0);

        leadMapper.updateById(lead);

        String reason = "匹配规则: " + (matchingRule != null ? matchingRule.getRuleName() : "默认规则");
        saveLeadLog(lead, LeadService.OPERATE_TYPE_ASSIGN, "自动分配线索", beforeUpdate, toUserId, toUserName, reason);

        saveAssignRecord(lead, lead.getSourceUserId(), lead.getSourceUserName(), toUserId, toUserName, ASSIGN_TYPE_AUTO, ruleId, reason);
    }

    private void saveLeadLog(CrmLead lead, int operateType, String operateName, CrmLead beforeUpdate,
                             Long userId, String userName, String remark) {
        CrmLeadLog log = new CrmLeadLog();
        log.setLeadId(lead.getId());
        log.setLeadNo(lead.getLeadNo());
        log.setOperateType(operateType);
        log.setOperateName(operateName);
        log.setOperateUserId(userId);
        log.setOperateUserName(userName);
        log.setRemark(remark);
        leadLogMapper.insert(log);
    }

    private void saveAssignRecord(CrmLead lead, Long fromUserId, String fromUserName,
                                   Long toUserId, String toUserName, Integer assignType,
                                   Long ruleId, String reason) {
        CrmAssignRecord record = new CrmAssignRecord();
        record.setLeadId(lead.getId());
        record.setLeadNo(lead.getLeadNo());
        record.setFromUserId(fromUserId);
        record.setFromUserName(fromUserName);
        record.setToUserId(toUserId);
        record.setToUserName(toUserName);
        record.setAssignType(assignType);
        record.setRuleId(ruleId);
        record.setReason(reason);
        assignRecordMapper.insert(record);
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
