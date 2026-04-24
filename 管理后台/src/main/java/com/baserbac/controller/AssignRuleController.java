package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.AssignRuleCreateDTO;
import com.baserbac.dto.AssignRuleQueryDTO;
import com.baserbac.dto.AssignRuleUpdateDTO;
import com.baserbac.service.AssignRuleService;
import com.baserbac.vo.AssignRuleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "分配规则管理")
@RestController
@RequestMapping("/api/crm/assign-rules")
@RequiredArgsConstructor
public class AssignRuleController {

    private final AssignRuleService assignRuleService;

    @Operation(summary = "分页查询分配规则列表")
    @GetMapping
    public R<PageResult<AssignRuleVO>> pageRules(AssignRuleQueryDTO queryDTO) {
        return R.success(assignRuleService.pageRules(queryDTO));
    }

    @Operation(summary = "根据ID查询分配规则详情")
    @GetMapping("/{id}")
    public R<AssignRuleVO> getRule(@PathVariable Long id) {
        return R.success(assignRuleService.getRuleById(id));
    }

    @OperationLog(module = "分配规则", value = "新增分配规则")
    @Operation(summary = "创建分配规则")
    @PostMapping
    public R<Long> createRule(@Valid @RequestBody AssignRuleCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = assignRuleService.createRule(createDTO, userId, userName);
        return R.success(id);
    }

    @OperationLog(module = "分配规则", value = "编辑分配规则")
    @Operation(summary = "更新分配规则")
    @PutMapping("/{id}")
    public R<Void> updateRule(@PathVariable Long id, @Valid @RequestBody AssignRuleUpdateDTO updateDTO) {
        updateDTO.setId(id);
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        assignRuleService.updateRule(updateDTO, userId, userName);
        return R.success();
    }

    @OperationLog(module = "分配规则", value = "删除分配规则")
    @Operation(summary = "删除分配规则")
    @DeleteMapping("/{id}")
    public R<Void> deleteRule(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        assignRuleService.deleteRule(id, userId, userName);
        return R.success();
    }
}
