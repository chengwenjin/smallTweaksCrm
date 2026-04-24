package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.dto.CustomerTagCreateDTO;
import com.baserbac.service.CustomerTagService;
import com.baserbac.vo.CustomerTagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户标签管理")
@RestController
@RequestMapping("/api/crm/customer-tags")
@RequiredArgsConstructor
public class CustomerTagController {

    private final CustomerTagService customerTagService;

    @Operation(summary = "获取所有客户标签")
    @GetMapping
    public R<List<CustomerTagVO>> getAllTags() {
        return R.success(customerTagService.getAllTags());
    }

    @Operation(summary = "按分类获取客户标签")
    @GetMapping("/category/{category}")
    public R<List<CustomerTagVO>> getTagsByCategory(@PathVariable String category) {
        return R.success(customerTagService.getTagsByCategory(category));
    }

    @OperationLog(module = "客户管理", value = "新增标签")
    @Operation(summary = "创建客户标签")
    @PostMapping
    public R<Long> createTag(@Valid @RequestBody CustomerTagCreateDTO createDTO) {
        Long id = customerTagService.createTag(createDTO);
        return R.success(id);
    }

    @OperationLog(module = "客户管理", value = "删除标签")
    @Operation(summary = "删除客户标签")
    @DeleteMapping("/{id}")
    public R<Void> deleteTag(@PathVariable Long id) {
        customerTagService.deleteTag(id);
        return R.success();
    }
}
