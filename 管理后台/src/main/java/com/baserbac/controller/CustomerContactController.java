package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.dto.CustomerContactCreateDTO;
import com.baserbac.dto.CustomerContactUpdateDTO;
import com.baserbac.service.CustomerContactService;
import com.baserbac.vo.CustomerContactVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户联系人管理")
@RestController
@RequestMapping("/api/crm/customers/{customerId}/contacts")
@RequiredArgsConstructor
public class CustomerContactController {

    private final CustomerContactService customerContactService;

    @Operation(summary = "获取客户的联系人列表")
    @GetMapping
    public R<List<CustomerContactVO>> getContacts(@PathVariable Long customerId) {
        return R.success(customerContactService.getContactsByCustomerId(customerId));
    }

    @Operation(summary = "根据ID获取联系人详情")
    @GetMapping("/{id}")
    public R<CustomerContactVO> getContact(@PathVariable Long customerId, @PathVariable Long id) {
        return R.success(customerContactService.getContactById(id));
    }

    @OperationLog(module = "客户管理", value = "新增联系人")
    @Operation(summary = "创建联系人")
    @PostMapping
    public R<Long> createContact(@PathVariable Long customerId, @Valid @RequestBody CustomerContactCreateDTO createDTO) {
        createDTO.setCustomerId(customerId);
        Long id = customerContactService.createContact(createDTO);
        return R.success(id);
    }

    @OperationLog(module = "客户管理", value = "编辑联系人")
    @Operation(summary = "更新联系人")
    @PutMapping("/{id}")
    public R<Void> updateContact(@PathVariable Long customerId, @PathVariable Long id, @Valid @RequestBody CustomerContactUpdateDTO updateDTO) {
        updateDTO.setId(id);
        customerContactService.updateContact(updateDTO);
        return R.success();
    }

    @OperationLog(module = "客户管理", value = "删除联系人")
    @Operation(summary = "删除联系人")
    @DeleteMapping("/{id}")
    public R<Void> deleteContact(@PathVariable Long customerId, @PathVariable Long id) {
        customerContactService.deleteContact(id);
        return R.success();
    }
}
