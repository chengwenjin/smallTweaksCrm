package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.CustomerFollowCreateDTO;
import com.baserbac.service.CustomerFollowService;
import com.baserbac.vo.CustomerFollowVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户跟进管理")
@RestController
@RequestMapping("/api/crm/customers/{customerId}/follows")
@RequiredArgsConstructor
public class CustomerFollowController {

    private final CustomerFollowService customerFollowService;

    @Operation(summary = "获取客户的跟进记录列表")
    @GetMapping
    public R<List<CustomerFollowVO>> getFollows(@PathVariable Long customerId) {
        return R.success(customerFollowService.getFollowsByCustomerId(customerId));
    }

    @OperationLog(module = "客户管理", value = "新增跟进记录")
    @Operation(summary = "创建跟进记录")
    @PostMapping
    public R<Long> createFollow(@PathVariable Long customerId, @Valid @RequestBody CustomerFollowCreateDTO createDTO) {
        createDTO.setCustomerId(customerId);
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = customerFollowService.createFollow(createDTO, userId, userName);
        return R.success(id);
    }

    @OperationLog(module = "客户管理", value = "删除跟进记录")
    @Operation(summary = "删除跟进记录")
    @DeleteMapping("/{id}")
    public R<Void> deleteFollow(@PathVariable Long customerId, @PathVariable Long id) {
        customerFollowService.deleteFollow(id);
        return R.success();
    }
}
