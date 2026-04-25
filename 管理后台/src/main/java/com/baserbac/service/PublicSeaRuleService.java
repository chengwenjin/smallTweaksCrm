package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.PublicSeaRuleCreateDTO;
import com.baserbac.dto.PublicSeaRuleQueryDTO;
import com.baserbac.dto.PublicSeaRuleUpdateDTO;
import com.baserbac.entity.*;
import com.baserbac.mapper.*;
import com.baserbac.vo.PublicSeaRuleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicSeaRuleService {

    private final PublicSeaRuleMapper publicSeaRuleMapper;
    private final LeadMapper leadMapper;

    public static final String[] RULE_TYPE_NAMES = {"", "先抢先得", "定期轮换"};
    public static final String[] STATUS_NAMES = {"禁用", "启用"};

    public PageResult<PublicSeaRuleVO> pageRules(PublicSeaRuleQueryDTO queryDTO) {
        Page<CrmPublicSeaRule> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmPublicSeaRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getRuleType() != null, CrmPublicSeaRule::getRuleType, queryDTO.getRuleType())
                .eq(queryDTO.getIsEnabled() != null, CrmPublicSeaRule::getIsEnabled, queryDTO.getIsEnabled())
                .orderByAsc(CrmPublicSeaRule::getSortOrder)
                .orderByDesc(CrmPublicSeaRule::getCreateTime);

        Page<CrmPublicSeaRule> result = publicSeaRuleMapper.selectPage(page, wrapper);

        List<PublicSeaRuleVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    public PublicSeaRuleVO getRuleById(Long id) {
        CrmPublicSeaRule rule = publicSeaRuleMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(rule);
    }

    public CrmPublicSeaRule getActiveRule() {
        CrmPublicSeaRule rule = publicSeaRuleMapper.selectOne(
                new LambdaQueryWrapper<CrmPublicSeaRule>()
                        .eq(CrmPublicSeaRule::getIsEnabled, 1)
                        .orderByAsc(CrmPublicSeaRule::getSortOrder)
                        .last("LIMIT 1")
        );

        if (rule == null) {
            rule = new CrmPublicSeaRule();
            rule.setRuleName("默认规则");
            rule.setRuleType(CrmPublicSeaRule.RULE_TYPE_FIRST_COME);
            rule.setClaimLimitPerDay(10);
            rule.setClaimLimitPerWeek(50);
            rule.setAutoRecycleEnabled(1);
            rule.setAutoRecycleDays(30);
        }

        return rule;
    }

    public int getTodayClaimCount(Long userId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Long count = leadMapper.selectCount(
                new LambdaQueryWrapper<CrmLead>()
                        .eq(CrmLead::getAssignUserId, userId)
                        .eq(CrmLead::getIsDeleted, 0)
                        .apply("DATE(assign_time) = {0}", today)
        );
        return count != null ? count.intValue() : 0;
    }

    public int getWeekClaimCount(Long userId) {
        LocalDateTime weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        Long count = leadMapper.selectCount(
                new LambdaQueryWrapper<CrmLead>()
                        .eq(CrmLead::getAssignUserId, userId)
                        .eq(CrmLead::getIsDeleted, 0)
                        .ge(CrmLead::getAssignTime, weekStart)
        );
        return count != null ? count.intValue() : 0;
    }

    public boolean canClaimToday(Long userId) {
        CrmPublicSeaRule rule = getActiveRule();
        int todayCount = getTodayClaimCount(userId);
        return todayCount < rule.getClaimLimitPerDay();
    }

    public boolean canClaimThisWeek(Long userId) {
        CrmPublicSeaRule rule = getActiveRule();
        int weekCount = getWeekClaimCount(userId);
        return weekCount < rule.getClaimLimitPerWeek();
    }

    public boolean canClaim(Long userId) {
        return canClaimToday(userId) && canClaimThisWeek(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createRule(PublicSeaRuleCreateDTO createDTO) {
        CrmPublicSeaRule rule = new CrmPublicSeaRule();
        rule.setRuleName(createDTO.getRuleName());
        rule.setRuleType(createDTO.getRuleType() != null ? createDTO.getRuleType() : CrmPublicSeaRule.RULE_TYPE_FIRST_COME);
        rule.setRotateDays(createDTO.getRotateDays() != null ? createDTO.getRotateDays() : 7);
        rule.setClaimLimitPerDay(createDTO.getClaimLimitPerDay() != null ? createDTO.getClaimLimitPerDay() : 10);
        rule.setClaimLimitPerWeek(createDTO.getClaimLimitPerWeek() != null ? createDTO.getClaimLimitPerWeek() : 50);
        rule.setAutoRecycleEnabled(createDTO.getAutoRecycleEnabled() != null ? createDTO.getAutoRecycleEnabled() : 1);
        rule.setAutoRecycleDays(createDTO.getAutoRecycleDays() != null ? createDTO.getAutoRecycleDays() : 30);
        rule.setIsEnabled(createDTO.getIsEnabled() != null ? createDTO.getIsEnabled() : 1);
        rule.setDescription(createDTO.getDescription());
        rule.setSortOrder(createDTO.getSortOrder() != null ? createDTO.getSortOrder() : 0);

        if (rule.getIsEnabled() != null && rule.getIsEnabled() == 1) {
            publicSeaRuleMapper.update(
                    null,
                    new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<CrmPublicSeaRule>()
                            .set(CrmPublicSeaRule::getIsEnabled, 0)
            );
        }

        publicSeaRuleMapper.insert(rule);
        return rule.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRule(PublicSeaRuleUpdateDTO updateDTO) {
        CrmPublicSeaRule rule = publicSeaRuleMapper.selectById(updateDTO.getId());
        if (rule == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (updateDTO.getRuleName() != null) {
            rule.setRuleName(updateDTO.getRuleName());
        }
        if (updateDTO.getRuleType() != null) {
            rule.setRuleType(updateDTO.getRuleType());
        }
        if (updateDTO.getRotateDays() != null) {
            rule.setRotateDays(updateDTO.getRotateDays());
        }
        if (updateDTO.getClaimLimitPerDay() != null) {
            rule.setClaimLimitPerDay(updateDTO.getClaimLimitPerDay());
        }
        if (updateDTO.getClaimLimitPerWeek() != null) {
            rule.setClaimLimitPerWeek(updateDTO.getClaimLimitPerWeek());
        }
        if (updateDTO.getAutoRecycleEnabled() != null) {
            rule.setAutoRecycleEnabled(updateDTO.getAutoRecycleEnabled());
        }
        if (updateDTO.getAutoRecycleDays() != null) {
            rule.setAutoRecycleDays(updateDTO.getAutoRecycleDays());
        }
        if (updateDTO.getDescription() != null) {
            rule.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getSortOrder() != null) {
            rule.setSortOrder(updateDTO.getSortOrder());
        }

        if (updateDTO.getIsEnabled() != null && updateDTO.getIsEnabled() == 1) {
            publicSeaRuleMapper.update(
                    null,
                    new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<CrmPublicSeaRule>()
                            .ne(CrmPublicSeaRule::getId, rule.getId())
                            .set(CrmPublicSeaRule::getIsEnabled, 0)
            );
            rule.setIsEnabled(1);
        } else if (updateDTO.getIsEnabled() != null) {
            rule.setIsEnabled(updateDTO.getIsEnabled());
        }

        publicSeaRuleMapper.updateById(rule);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(Long id) {
        CrmPublicSeaRule rule = publicSeaRuleMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        publicSeaRuleMapper.deleteById(id);
    }

    private PublicSeaRuleVO convertToVO(CrmPublicSeaRule rule) {
        return PublicSeaRuleVO.builder()
                .id(rule.getId())
                .ruleName(rule.getRuleName())
                .ruleType(rule.getRuleType())
                .ruleTypeName(rule.getRuleType() != null && rule.getRuleType() >= 1 && rule.getRuleType() <= 2
                        ? RULE_TYPE_NAMES[rule.getRuleType()] : null)
                .rotateDays(rule.getRotateDays())
                .claimLimitPerDay(rule.getClaimLimitPerDay())
                .claimLimitPerWeek(rule.getClaimLimitPerWeek())
                .autoRecycleEnabled(rule.getAutoRecycleEnabled())
                .autoRecycleEnabledName(rule.getAutoRecycleEnabled() != null ? STATUS_NAMES[rule.getAutoRecycleEnabled()] : null)
                .autoRecycleDays(rule.getAutoRecycleDays())
                .isEnabled(rule.getIsEnabled())
                .isEnabledName(rule.getIsEnabled() != null ? STATUS_NAMES[rule.getIsEnabled()] : null)
                .description(rule.getDescription())
                .sortOrder(rule.getSortOrder())
                .createTime(rule.getCreateTime())
                .updateTime(rule.getUpdateTime())
                .build();
    }
}
