package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.AssignRuleCreateDTO;
import com.baserbac.dto.AssignRuleQueryDTO;
import com.baserbac.dto.AssignRuleUpdateDTO;
import com.baserbac.entity.CrmAssignRule;
import com.baserbac.entity.CrmLead;
import com.baserbac.entity.SysUser;
import com.baserbac.mapper.AssignRuleMapper;
import com.baserbac.mapper.LeadMapper;
import com.baserbac.mapper.UserMapper;
import com.baserbac.vo.AssignRuleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignRuleService {

    private final AssignRuleMapper assignRuleMapper;
    private final UserMapper userMapper;
    private final LeadMapper leadMapper;

    public static final String[] RULE_TYPE_NAMES = {"", "地域规则", "行业规则", "规模规则"};

    public PageResult<AssignRuleVO> pageRules(AssignRuleQueryDTO queryDTO) {
        Page<CrmAssignRule> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmAssignRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getRuleName()), CrmAssignRule::getRuleName, queryDTO.getRuleName())
                .eq(queryDTO.getRuleType() != null, CrmAssignRule::getRuleType, queryDTO.getRuleType())
                .eq(queryDTO.getIsEnabled() != null, CrmAssignRule::getIsEnabled, queryDTO.getIsEnabled())
                .eq(queryDTO.getAssignUserId() != null, CrmAssignRule::getAssignUserId, queryDTO.getAssignUserId())
                .orderByAsc(CrmAssignRule::getPriority)
                .orderByDesc(CrmAssignRule::getCreateTime);

        Page<CrmAssignRule> result = assignRuleMapper.selectPage(page, wrapper);

        List<AssignRuleVO> voList = new ArrayList<>();
        for (CrmAssignRule rule : result.getRecords()) {
            voList.add(convertToVO(rule));
        }

        return new PageResult<>(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    public AssignRuleVO getRuleById(Long id) {
        CrmAssignRule rule = assignRuleMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }
        return convertToVO(rule);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createRule(AssignRuleCreateDTO createDTO, Long userId, String userName) {
        CrmAssignRule rule = new CrmAssignRule();
        rule.setRuleName(createDTO.getRuleName());
        rule.setRuleType(createDTO.getRuleType());
        rule.setProvince(createDTO.getProvince());
        rule.setCity(createDTO.getCity());
        rule.setDistrict(createDTO.getDistrict());
        rule.setIndustry(createDTO.getIndustry());
        rule.setMinEmployeeCount(createDTO.getMinEmployeeCount());
        rule.setMaxEmployeeCount(createDTO.getMaxEmployeeCount());
        rule.setMinAnnualRevenue(createDTO.getMinAnnualRevenue());
        rule.setMaxAnnualRevenue(createDTO.getMaxAnnualRevenue());
        rule.setAssignUserId(createDTO.getAssignUserId());
        rule.setPriority(createDTO.getPriority() != null ? createDTO.getPriority() : 100);
        rule.setIsEnabled(createDTO.getIsEnabled() != null ? createDTO.getIsEnabled() : 1);
        rule.setDescription(createDTO.getDescription());
        rule.setIsDeleted(0);

        if (createDTO.getAssignUserId() != null) {
            SysUser user = userMapper.selectById(createDTO.getAssignUserId());
            if (user != null) {
                rule.setAssignUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }

        assignRuleMapper.insert(rule);
        return rule.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRule(AssignRuleUpdateDTO updateDTO, Long userId, String userName) {
        CrmAssignRule rule = assignRuleMapper.selectById(updateDTO.getId());
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }

        if (StrUtil.isNotBlank(updateDTO.getRuleName())) {
            rule.setRuleName(updateDTO.getRuleName());
        }
        if (updateDTO.getRuleType() != null) {
            rule.setRuleType(updateDTO.getRuleType());
        }
        if (updateDTO.getProvince() != null) {
            rule.setProvince(updateDTO.getProvince());
        }
        if (updateDTO.getCity() != null) {
            rule.setCity(updateDTO.getCity());
        }
        if (updateDTO.getDistrict() != null) {
            rule.setDistrict(updateDTO.getDistrict());
        }
        if (updateDTO.getIndustry() != null) {
            rule.setIndustry(updateDTO.getIndustry());
        }
        if (updateDTO.getMinEmployeeCount() != null) {
            rule.setMinEmployeeCount(updateDTO.getMinEmployeeCount());
        }
        if (updateDTO.getMaxEmployeeCount() != null) {
            rule.setMaxEmployeeCount(updateDTO.getMaxEmployeeCount());
        }
        if (updateDTO.getMinAnnualRevenue() != null) {
            rule.setMinAnnualRevenue(updateDTO.getMinAnnualRevenue());
        }
        if (updateDTO.getMaxAnnualRevenue() != null) {
            rule.setMaxAnnualRevenue(updateDTO.getMaxAnnualRevenue());
        }
        if (updateDTO.getAssignUserId() != null) {
            rule.setAssignUserId(updateDTO.getAssignUserId());
            SysUser user = userMapper.selectById(updateDTO.getAssignUserId());
            if (user != null) {
                rule.setAssignUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }
        if (updateDTO.getPriority() != null) {
            rule.setPriority(updateDTO.getPriority());
        }
        if (updateDTO.getIsEnabled() != null) {
            rule.setIsEnabled(updateDTO.getIsEnabled());
        }
        if (updateDTO.getDescription() != null) {
            rule.setDescription(updateDTO.getDescription());
        }

        assignRuleMapper.updateById(rule);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(Long id, Long userId, String userName) {
        CrmAssignRule rule = assignRuleMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }
        assignRuleMapper.deleteById(id);
    }

    public CrmAssignRule findMatchingRule(CrmLead lead) {
        List<CrmAssignRule> enabledRules = assignRuleMapper.selectList(
                new LambdaQueryWrapper<CrmAssignRule>()
                        .eq(CrmAssignRule::getIsEnabled, 1)
                        .orderByAsc(CrmAssignRule::getPriority)
        );

        for (CrmAssignRule rule : enabledRules) {
            if (matchesRule(rule, lead)) {
                return rule;
            }
        }

        return null;
    }

    private boolean matchesRule(CrmAssignRule rule, CrmLead lead) {
        boolean matched = true;

        if (rule.getRuleType() == null || rule.getRuleType() == 1) {
            if (StrUtil.isNotBlank(rule.getProvince())) {
                if (!rule.getProvince().equals(lead.getProvince())) {
                    matched = false;
                }
            }
            if (matched && StrUtil.isNotBlank(rule.getCity())) {
                if (!rule.getCity().equals(lead.getCity())) {
                    matched = false;
                }
            }
            if (matched && StrUtil.isNotBlank(rule.getDistrict())) {
                if (!rule.getDistrict().equals(lead.getDistrict())) {
                    matched = false;
                }
            }
        }

        if (matched && (rule.getRuleType() == null || rule.getRuleType() == 2)) {
            if (StrUtil.isNotBlank(rule.getIndustry())) {
                if (!rule.getIndustry().equals(lead.getIndustry())) {
                    matched = false;
                }
            }
        }

        if (matched && (rule.getRuleType() == null || rule.getRuleType() == 3)) {
            if (rule.getMinEmployeeCount() != null && lead.getEmployeeCount() != null) {
                if (lead.getEmployeeCount() < rule.getMinEmployeeCount()) {
                    matched = false;
                }
            }
            if (matched && rule.getMaxEmployeeCount() != null && lead.getEmployeeCount() != null) {
                if (lead.getEmployeeCount() > rule.getMaxEmployeeCount()) {
                    matched = false;
                }
            }
        }

        return matched;
    }

    private AssignRuleVO convertToVO(CrmAssignRule rule) {
        return AssignRuleVO.builder()
                .id(rule.getId())
                .ruleName(rule.getRuleName())
                .ruleType(rule.getRuleType())
                .ruleTypeName(rule.getRuleType() != null && rule.getRuleType() > 0 ? RULE_TYPE_NAMES[rule.getRuleType()] : null)
                .province(rule.getProvince())
                .city(rule.getCity())
                .district(rule.getDistrict())
                .industry(rule.getIndustry())
                .minEmployeeCount(rule.getMinEmployeeCount())
                .maxEmployeeCount(rule.getMaxEmployeeCount())
                .minAnnualRevenue(rule.getMinAnnualRevenue())
                .maxAnnualRevenue(rule.getMaxAnnualRevenue())
                .assignUserId(rule.getAssignUserId())
                .assignUserName(rule.getAssignUserName())
                .priority(rule.getPriority())
                .isEnabled(rule.getIsEnabled())
                .description(rule.getDescription())
                .createBy(rule.getCreateBy())
                .createTime(rule.getCreateTime())
                .updateBy(rule.getUpdateBy())
                .updateTime(rule.getUpdateTime())
                .build();
    }
}
