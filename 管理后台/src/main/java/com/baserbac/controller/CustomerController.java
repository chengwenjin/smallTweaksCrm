package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.CustomerCreateDTO;
import com.baserbac.dto.CustomerQueryDTO;
import com.baserbac.dto.CustomerUpdateDTO;
import com.baserbac.service.CustomerService;
import com.baserbac.vo.CustomerLevelVO;
import com.baserbac.vo.CustomerTagVO;
import com.baserbac.vo.CustomerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户管理")
@RestController
@RequestMapping("/api/crm/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "分页查询客户列表")
    @GetMapping
    public R<PageResult<CustomerVO>> pageCustomers(CustomerQueryDTO queryDTO) {
        return R.success(customerService.pageCustomers(queryDTO));
    }

    @Operation(summary = "根据ID查询客户详情（全景视图）")
    @GetMapping("/{id}")
    public R<CustomerVO> getCustomer(@PathVariable Long id) {
        return R.success(customerService.getCustomerById(id));
    }

    @OperationLog(module = "客户管理", value = "新增客户")
    @Operation(summary = "创建客户")
    @PostMapping
    public R<Long> createCustomer(@Valid @RequestBody CustomerCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = customerService.createCustomer(createDTO, userId, userName);
        return R.success(id);
    }

    @OperationLog(module = "客户管理", value = "编辑客户")
    @Operation(summary = "更新客户")
    @PutMapping("/{id}")
    public R<Void> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerUpdateDTO updateDTO) {
        updateDTO.setId(id);
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        customerService.updateCustomer(updateDTO, userId, userName);
        return R.success();
    }

    @OperationLog(module = "客户管理", value = "删除客户")
    @Operation(summary = "删除客户")
    @DeleteMapping("/{id}")
    public R<Void> deleteCustomer(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        customerService.deleteCustomer(id, userId, userName);
        return R.success();
    }

    @Operation(summary = "获取所有客户等级")
    @GetMapping("/levels")
    public R<List<CustomerLevelVO>> getAllLevels() {
        return R.success(customerService.getAllCustomerLevels());
    }

    @Operation(summary = "获取所有客户标签")
    @GetMapping("/tags")
    public R<List<CustomerTagVO>> getAllTags() {
        return R.success(customerService.getAllCustomerTags());
    }
}
