package com.baserbac.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.FollowRecordCreateDTO;
import com.baserbac.dto.FollowRecordQueryDTO;
import com.baserbac.service.FollowRecordService;
import com.baserbac.vo.FollowRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "跟进记录管理")
@RestController
@RequestMapping("/api/crm/follow-records")
@RequiredArgsConstructor
public class FollowRecordController {

    private final FollowRecordService followRecordService;

    @Operation(summary = "分页查询跟进记录")
    @GetMapping
    public R<Page<FollowRecordVO>> getFollowRecordPage(FollowRecordQueryDTO queryDTO) {
        return R.success(followRecordService.getFollowRecordPage(queryDTO));
    }

    @Operation(summary = "根据业务查询跟进记录列表")
    @GetMapping("/business/{businessType}/{businessId}")
    public R<List<FollowRecordVO>> getFollowRecordsByBusiness(
            @PathVariable String businessType,
            @PathVariable Long businessId) {
        return R.success(followRecordService.getFollowRecordsByBusiness(businessType, businessId));
    }

    @Operation(summary = "获取跟进记录详情")
    @GetMapping("/{id}")
    public R<FollowRecordVO> getFollowRecordById(@PathVariable Long id) {
        return R.success(followRecordService.getFollowRecordById(id));
    }

    @OperationLog(module = "跟进记录管理", value = "创建跟进记录")
    @Operation(summary = "创建跟进记录")
    @PostMapping
    public R<Long> createFollowRecord(@Valid @RequestBody FollowRecordCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = followRecordService.createFollowRecord(createDTO, userId, userName);
        return R.success(id);
    }

    @OperationLog(module = "跟进记录管理", value = "删除跟进记录")
    @Operation(summary = "删除跟进记录")
    @DeleteMapping("/{id}")
    public R<Void> deleteFollowRecord(@PathVariable Long id) {
        followRecordService.deleteFollowRecord(id);
        return R.success();
    }
}
