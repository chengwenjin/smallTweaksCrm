package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.annotation.RequirePermission;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.SalesStageCreateDTO;
import com.baserbac.dto.SalesStageQueryDTO;
import com.baserbac.dto.SalesStageUpdateDTO;
import com.baserbac.service.SalesStageService;
import com.baserbac.vo.SalesStageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "销售阶段管理")
@RestController
@RequestMapping("/api/crm/sales-stages")
@RequiredArgsConstructor
public class SalesStageController {

    private final SalesStageService salesStageService;

    @Operation(summary = "分页查询销售阶段列表")
    @GetMapping
    public R<PageResult<SalesStageVO>> pageStages(SalesStageQueryDTO queryDTO) {
        return R.success(salesStageService.pageStages(queryDTO));
    }

    @Operation(summary = "获取所有启用的销售阶段（用于下拉选择）")
    @GetMapping("/enabled")
    public R<List<SalesStageVO>> getAllEnabledStages() {
        return R.success(salesStageService.getAllEnabledStages());
    }

    @Operation(summary = "根据ID查询销售阶段详情")
    @GetMapping("/{id}")
    public R<SalesStageVO> getStage(@PathVariable Long id) {
        return R.success(salesStageService.getStageById(id));
    }

    @OperationLog(module = "销售阶段管理", value = "新增销售阶段")
    @RequirePermission("crm:stage:add")
    @Operation(summary = "创建销售阶段")
    @PostMapping
    public R<Long> createStage(@Valid @RequestBody SalesStageCreateDTO createDTO) {
        return R.success(salesStageService.createStage(createDTO));
    }

    @OperationLog(module = "销售阶段管理", value = "编辑销售阶段")
    @RequirePermission("crm:stage:edit")
    @Operation(summary = "更新销售阶段")
    @PutMapping
    public R<Void> updateStage(@Valid @RequestBody SalesStageUpdateDTO updateDTO) {
        salesStageService.updateStage(updateDTO);
        return R.success();
    }

    @OperationLog(module = "销售阶段管理", value = "删除销售阶段")
    @RequirePermission("crm:stage:delete")
    @Operation(summary = "删除销售阶段")
    @DeleteMapping("/{id}")
    public R<Void> deleteStage(@PathVariable Long id) {
        salesStageService.deleteStage(id);
        return R.success();
    }
}
