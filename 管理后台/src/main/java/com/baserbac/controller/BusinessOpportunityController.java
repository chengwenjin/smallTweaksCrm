package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.annotation.RequirePermission;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.BusinessOpportunityCreateDTO;
import com.baserbac.dto.BusinessOpportunityQueryDTO;
import com.baserbac.dto.BusinessOpportunityStageMoveDTO;
import com.baserbac.dto.BusinessOpportunityUpdateDTO;
import com.baserbac.service.BusinessOpportunityService;
import com.baserbac.vo.BusinessOpportunityVO;
import com.baserbac.vo.SalesFunnelStatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商机管理")
@RestController
@RequestMapping("/api/crm/opportunities")
@RequiredArgsConstructor
public class BusinessOpportunityController {

    private final BusinessOpportunityService businessOpportunityService;

    @Operation(summary = "分页查询商机列表")
    @GetMapping
    public R<PageResult<BusinessOpportunityVO>> pageOpportunities(BusinessOpportunityQueryDTO queryDTO) {
        return R.success(businessOpportunityService.pageOpportunities(queryDTO));
    }

    @Operation(summary = "根据ID查询商机详情")
    @GetMapping("/{id}")
    public R<BusinessOpportunityVO> getOpportunity(@PathVariable Long id) {
        return R.success(businessOpportunityService.getOpportunityById(id));
    }

    @OperationLog(module = "商机管理", value = "新增商机")
    @RequirePermission("crm:opportunity:add")
    @Operation(summary = "创建商机")
    @PostMapping
    public R<Long> createOpportunity(@Valid @RequestBody BusinessOpportunityCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        return R.success(businessOpportunityService.createOpportunity(createDTO, userId, userName));
    }

    @OperationLog(module = "商机管理", value = "编辑商机")
    @RequirePermission("crm:opportunity:edit")
    @Operation(summary = "更新商机")
    @PutMapping("/{id}")
    public R<Void> updateOpportunity(@PathVariable Long id, @Valid @RequestBody BusinessOpportunityUpdateDTO updateDTO) {
        updateDTO.setId(id);
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        businessOpportunityService.updateOpportunity(updateDTO, userId, userName);
        return R.success();
    }

    @OperationLog(module = "商机管理", value = "删除商机")
    @RequirePermission("crm:opportunity:delete")
    @Operation(summary = "删除商机")
    @DeleteMapping("/{id}")
    public R<Void> deleteOpportunity(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        businessOpportunityService.deleteOpportunity(id, userId, userName);
        return R.success();
    }

    @OperationLog(module = "商机管理", value = "转移商机阶段")
    @RequirePermission("crm:opportunity:move")
    @Operation(summary = "转移商机到指定阶段")
    @PutMapping("/{id}/move-stage")
    public R<Void> moveOpportunityStage(
            @PathVariable Long id,
            @Valid @RequestBody BusinessOpportunityStageMoveDTO moveDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        businessOpportunityService.moveOpportunityStage(id, moveDTO, userId, userName);
        return R.success();
    }

    @Operation(summary = "获取销售漏斗统计数据")
    @GetMapping("/funnel-statistics")
    public R<SalesFunnelStatisticsVO> getSalesFunnelStatistics(BusinessOpportunityQueryDTO queryDTO) {
        return R.success(businessOpportunityService.getSalesFunnelStatistics(queryDTO));
    }
}
