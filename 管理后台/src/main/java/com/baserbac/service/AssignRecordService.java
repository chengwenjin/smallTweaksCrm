package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.AssignRecordQueryDTO;
import com.baserbac.entity.CrmAssignRecord;
import com.baserbac.entity.CrmAssignRule;
import com.baserbac.entity.CrmLead;
import com.baserbac.mapper.AssignRecordMapper;
import com.baserbac.mapper.AssignRuleMapper;
import com.baserbac.mapper.LeadMapper;
import com.baserbac.vo.AssignRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baserbac.service.PublicPoolService.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignRecordService {

    private final AssignRecordMapper assignRecordMapper;
    private final LeadMapper leadMapper;
    private final AssignRuleMapper assignRuleMapper;

    public PageResult<AssignRecordVO> pageRecords(AssignRecordQueryDTO queryDTO) {
        Page<CrmAssignRecord> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmAssignRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getAssignType() != null, CrmAssignRecord::getAssignType, queryDTO.getAssignType())
                .eq(queryDTO.getFromUserId() != null, CrmAssignRecord::getFromUserId, queryDTO.getFromUserId())
                .eq(queryDTO.getToUserId() != null, CrmAssignRecord::getToUserId, queryDTO.getToUserId())
                .orderByDesc(CrmAssignRecord::getCreateTime);

        Page<CrmAssignRecord> result = assignRecordMapper.selectPage(page, wrapper);

        List<Long> leadIds = result.getRecords().stream()
                .map(CrmAssignRecord::getLeadId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> leadNameMap = new java.util.HashMap<>();
        if (!leadIds.isEmpty()) {
            List<CrmLead> leads = leadMapper.selectList(
                    new LambdaQueryWrapper<CrmLead>()
                            .in(CrmLead::getId, leadIds)
                            .select(CrmLead::getId, CrmLead::getLeadName)
            );
            leadNameMap = leads.stream()
                    .collect(Collectors.toMap(CrmLead::getId, CrmLead::getLeadName));
        }

        List<Long> ruleIds = result.getRecords().stream()
                .map(CrmAssignRecord::getRuleId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> ruleNameMap = new java.util.HashMap<>();
        if (!ruleIds.isEmpty()) {
            List<CrmAssignRule> rules = assignRuleMapper.selectList(
                    new LambdaQueryWrapper<CrmAssignRule>()
                            .in(CrmAssignRule::getId, ruleIds)
                            .select(CrmAssignRule::getId, CrmAssignRule::getRuleName)
            );
            ruleNameMap = rules.stream()
                    .collect(Collectors.toMap(CrmAssignRule::getId, CrmAssignRule::getRuleName));
        }

        List<AssignRecordVO> voList = new ArrayList<>();
        for (CrmAssignRecord record : result.getRecords()) {
            voList.add(convertToVO(record, leadNameMap, ruleNameMap));
        }

        return new PageResult<>(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    private AssignRecordVO convertToVO(CrmAssignRecord record, Map<Long, String> leadNameMap, Map<Long, String> ruleNameMap) {
        return AssignRecordVO.builder()
                .id(record.getId())
                .leadId(record.getLeadId())
                .leadNo(record.getLeadNo())
                .leadName(leadNameMap.get(record.getLeadId()))
                .fromUserId(record.getFromUserId())
                .fromUserName(record.getFromUserName())
                .toUserId(record.getToUserId())
                .toUserName(record.getToUserName())
                .assignType(record.getAssignType())
                .assignTypeName(record.getAssignType() != null && record.getAssignType() > 0
                        ? ASSIGN_TYPE_NAMES[record.getAssignType()] : null)
                .ruleId(record.getRuleId())
                .ruleName(record.getRuleId() != null ? ruleNameMap.get(record.getRuleId()) : null)
                .reason(record.getReason())
                .createTime(record.getCreateTime())
                .build();
    }
}
