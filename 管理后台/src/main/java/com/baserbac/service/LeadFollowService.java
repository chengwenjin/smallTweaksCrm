package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.LeadFollowCreateDTO;
import com.baserbac.entity.CrmLead;
import com.baserbac.entity.CrmLeadFollow;
import com.baserbac.entity.CrmLeadLog;
import com.baserbac.mapper.LeadFollowMapper;
import com.baserbac.mapper.LeadLogMapper;
import com.baserbac.mapper.LeadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadFollowService {

    private final LeadFollowMapper leadFollowMapper;
    private final LeadMapper leadMapper;
    private final LeadLogMapper leadLogMapper;

    public static final int FOLLOW_TYPE_PHONE = 1;
    public static final int FOLLOW_TYPE_WECHAT = 2;
    public static final int FOLLOW_TYPE_EMAIL = 3;
    public static final int FOLLOW_TYPE_VISIT = 4;
    public static final int FOLLOW_TYPE_OTHER = 5;

    public static final String[] FOLLOW_TYPE_NAMES = {"", "电话", "微信", "邮件", "拜访", "其他"};

    public PageResult<CrmLeadFollow> pageFollows(Long leadId, Integer pageNum, Integer pageSize) {
        Page<CrmLeadFollow> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<CrmLeadFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmLeadFollow::getLeadId, leadId)
                .orderByDesc(CrmLeadFollow::getCreateTime);

        Page<CrmLeadFollow> result = leadFollowMapper.selectPage(page, wrapper);

        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createFollow(LeadFollowCreateDTO createDTO, Long userId, String userName) {
        CrmLead lead = leadMapper.selectById(createDTO.getLeadId());
        if (lead == null) {
            throw new BusinessException("线索不存在");
        }

        leadFollowMapper.update(
                new LambdaUpdateWrapper<CrmLeadFollow>()
                        .eq(CrmLeadFollow::getLeadId, createDTO.getLeadId())
                        .eq(CrmLeadFollow::getIsLast, 1)
                        .set(CrmLeadFollow::getIsLast, 0)
        );

        CrmLeadFollow follow = new CrmLeadFollow();
        follow.setLeadId(createDTO.getLeadId());
        follow.setLeadNo(lead.getLeadNo());
        follow.setFollowUserId(userId);
        follow.setFollowUserName(userName);
        follow.setFollowType(createDTO.getFollowType());
        follow.setFollowContent(createDTO.getFollowContent());
        follow.setNextFollowRemark(createDTO.getNextFollowRemark());
        follow.setIsLast(1);
        follow.setIsDeleted(0);

        if (StrUtil.isNotBlank(createDTO.getNextFollowTime())) {
            try {
                follow.setNextFollowTime(LocalDateTime.parse(createDTO.getNextFollowTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (Exception e) {
                log.warn("解析下次跟进时间失败: {}", createDTO.getNextFollowTime());
            }
        }

        leadFollowMapper.insert(follow);

        CrmLead beforeUpdate = null;
        try {
            beforeUpdate = (CrmLead) lead.clone();
        } catch (CloneNotSupportedException ignored) {}

        lead.setLastFollowTime(LocalDateTime.now());
        if (lead.getFollowCount() == null) {
            lead.setFollowCount(1L);
        } else {
            lead.setFollowCount(lead.getFollowCount() + 1);
        }
        if (follow.getNextFollowTime() != null) {
            lead.setNextFollowTime(follow.getNextFollowTime());
        }

        leadMapper.updateById(lead);

        saveLeadLog(lead, LeadService.OPERATE_TYPE_FOLLOW, "跟进线索", beforeUpdate, userId, userName, createDTO.getFollowContent());

        return follow.getId();
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
}
