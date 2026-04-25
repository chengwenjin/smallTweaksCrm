package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.PublicSeaRuleCreateDTO;
import com.baserbac.dto.PublicSeaRuleQueryDTO;
import com.baserbac.dto.PublicSeaRuleUpdateDTO;
import com.baserbac.entity.CrmPublicSeaRule;
import com.baserbac.service.PublicSeaRuleService;
import com.baserbac.vo.PublicSeaRuleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "公海规则管理")
@RestController
@RequestMapping("/api/crm/public-sea-rule")
@RequiredArgsConstructor
public class PublicSeaRuleController {

    private final PublicSeaRuleService publicSeaRuleService;

    @Operation(summary = "分页查询公海规则列表")
    @GetMapping
    public R<PageResult<PublicSeaRuleVO>> pageRules(PublicSeaRuleQueryDTO queryDTO) {
        return R.success(publicSeaRuleService.pageRules(queryDTO));
    }

    @Operation(summary = "获取公海规则详情")
    @GetMapping("/{id}")
    public R<PublicSeaRuleVO> getRuleById(@PathVariable Long id) {
        return R.success(publicSeaRuleService.getRuleById(id));
    }

    @Operation(summary = "获取当前启用的公海规则")
    @GetMapping("/active")
    public R<PublicSeaRuleVO> getActiveRule() {
        CrmPublicSeaRule rule = publicSeaRuleService.getActiveRule();
        return R.success(convertToVO(rule));
    }

    @Operation(summary = "检查当前用户今日是否还能认领")
    @GetMapping("/can-claim")
    public R<Boolean> canClaim() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return R.error(401, "请登录后重试");
        }
        return R.success(publicSeaRuleService.canClaim(userId));
    }

    @Operation(summary = "获取当前用户今日认领数量")
    @GetMapping("/claim-count/today")
    public R<Integer> getTodayClaimCount() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return R.error(401, "请登录后重试");
        }
        return R.success(publicSeaRuleService.getTodayClaimCount(userId));
    }

    @Operation(summary = "获取当前用户本周认领数量")
    @GetMapping("/claim-count/week")
    public R<Integer> getWeekClaimCount() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return R.error(401, "请登录后重试");
        }
        return R.success(publicSeaRuleService.getWeekClaimCount(userId));
    }

    @OperationLog(module = "公海规则", value = "新增公海规则")
    @Operation(summary = "新增公海规则")
    @PostMapping
    public R<Long> createRule(@Valid @RequestBody PublicSeaRuleCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        log.info("用户 [{}] 新增公海规则: userId={}", userName, userId);
        return R.success(publicSeaRuleService.createRule(createDTO));
    }

    @OperationLog(module = "公海规则", value = "更新公海规则")
    @Operation(summary = "更新公海规则")
    @PutMapping
    public R<Void> updateRule(@Valid @RequestBody PublicSeaRuleUpdateDTO updateDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        log.info("用户 [{}] 更新公海规则: userId={}, ruleId={}", userName, userId, updateDTO.getId());
        publicSeaRuleService.updateRule(updateDTO);
        return R.success();
    }

    @OperationLog(module = "公海规则", value = "删除公海规则")
    @Operation(summary = "删除公海规则")
    @DeleteMapping("/{id}")
    public R<Void> deleteRule(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        log.info("用户 [{}] 删除公海规则: userId={}, ruleId={}", userName, userId, id);
        publicSeaRuleService.deleteRule(id);
        return R.success();
    }

    private PublicSeaRuleVO convertToVO(CrmPublicSeaRule rule) {
        if (rule == null) {
            return null;
        }
        return PublicSeaRuleVO.builder()
                .id(rule.getId())
                .ruleName(rule.getRuleName())
                .ruleType(rule.getRuleType())
                .ruleTypeName(rule.getRuleType() != null && rule.getRuleType() >= 1 && rule.getRuleType() <= 2
                        ? PublicSeaRuleService.RULE_TYPE_NAMES[rule.getRuleType()] : null)
                .rotateDays(rule.getRotateDays())
                .claimLimitPerDay(rule.getClaimLimitPerDay())
                .claimLimitPerWeek(rule.getClaimLimitPerWeek())
                .autoRecycleEnabled(rule.getAutoRecycleEnabled())
                .autoRecycleEnabledName(rule.getAutoRecycleEnabled() != null ? PublicSeaRuleService.STATUS_NAMES[rule.getAutoRecycleEnabled()] : null)
                .autoRecycleDays(rule.getAutoRecycleDays())
                .isEnabled(rule.getIsEnabled())
                .isEnabledName(rule.getIsEnabled() != null ? PublicSeaRuleService.STATUS_NAMES[rule.getIsEnabled()] : null)
                .description(rule.getDescription())
                .sortOrder(rule.getSortOrder())
                .createTime(rule.getCreateTime())
                .updateTime(rule.getUpdateTime())
                .build();
    }
}
