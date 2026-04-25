package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.TransferCreateDTO;
import com.baserbac.dto.TransferRecordQueryDTO;
import com.baserbac.entity.CrmCustomer;
import com.baserbac.entity.CrmLead;
import com.baserbac.service.TransferService;
import com.baserbac.vo.TransferRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "资源转移管理")
@RestController
@RequestMapping("/api/crm/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @Operation(summary = "分页查询转移记录列表")
    @GetMapping
    public R<PageResult<TransferRecordVO>> pageRecords(TransferRecordQueryDTO queryDTO) {
        return R.success(transferService.pageRecords(queryDTO));
    }

    @Operation(summary = "获取转移记录详情")
    @GetMapping("/{id}")
    public R<TransferRecordVO> getRecordById(@PathVariable Long id) {
        return R.success(transferService.getRecordById(id));
    }

    @Operation(summary = "获取用户名下的客户列表")
    @GetMapping("/user/customers/{userId}")
    public R<List<CrmCustomer>> getUserCustomers(@PathVariable Long userId) {
        return R.success(transferService.getUserCustomers(userId));
    }

    @Operation(summary = "获取用户名下的线索列表")
    @GetMapping("/user/leads/{userId}")
    public R<List<CrmLead>> getUserLeads(@PathVariable Long userId) {
        return R.success(transferService.getUserLeads(userId));
    }

    @OperationLog(module = "资源转移", value = "创建转移记录")
    @Operation(summary = "创建资源转移记录")
    @PostMapping
    public R<TransferRecordVO> createTransfer(@Valid @RequestBody TransferCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();

        if (userId == null) {
            log.warn("创建转移记录时无法获取用户ID，请登录后重试");
            return R.error(401, "请登录后重试");
        }

        log.info("用户 [{}] 创建资源转移记录: userId={}, fromUserId={}, toUserId={}",
                userName, userId, createDTO.getFromUserId(), createDTO.getToUserId());

        return R.success(transferService.createTransfer(createDTO, userId, userName));
    }

    @OperationLog(module = "资源转移", value = "离职一键回收")
    @Operation(summary = "离职一键回收：转移用户名下所有资源")
    @PostMapping("/resign")
    public R<TransferRecordVO> resignTransfer(
            @RequestParam Long fromUserId,
            @RequestParam(required = false) Long toUserId,
            @RequestParam(required = false) String reason) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();

        if (userId == null) {
            log.warn("离职一键回收时无法获取用户ID，请登录后重试");
            return R.error(401, "请登录后重试");
        }

        log.info("用户 [{}] 执行离职一键回收: operatorId={}, fromUserId={}, toUserId={}",
                userName, userId, fromUserId, toUserId);

        return R.success(transferService.resignTransfer(fromUserId, toUserId, reason, userId, userName));
    }
}
