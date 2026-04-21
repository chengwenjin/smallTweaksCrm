package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.LeadAssignDTO;
import com.baserbac.dto.LeadCreateDTO;
import com.baserbac.dto.LeadImportResultDTO;
import com.baserbac.dto.LeadQueryDTO;
import com.baserbac.dto.LeadStatusUpdateDTO;
import com.baserbac.dto.LeadUpdateDTO;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.service.LeadService;
import com.baserbac.vo.LeadSourceVO;
import com.baserbac.vo.LeadVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 线索管理控制器
 */
@Tag(name = "线索管理")
@RestController
@RequestMapping("/api/crm/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @Operation(summary = "分页查询线索列表")
    @GetMapping
    public R<PageResult<LeadVO>> pageLeads(LeadQueryDTO queryDTO) {
        return R.success(leadService.pageLeads(queryDTO));
    }

    @Operation(summary = "根据ID查询线索详情")
    @GetMapping("/{id}")
    public R<LeadVO> getLead(@PathVariable Long id) {
        return R.success(leadService.getLeadById(id));
    }

    @OperationLog(module = "线索管理", value = "新增线索")
    @Operation(summary = "手工录入线索")
    @PostMapping
    public R<Long> createLead(@Valid @RequestBody LeadCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUserName();
        Long id = leadService.createLead(createDTO, userId, userName);
        return R.success(id);
    }

    @OperationLog(module = "线索管理", value = "编辑线索")
    @Operation(summary = "更新线索")
    @PutMapping("/{id}")
    public R<Void> updateLead(@PathVariable Long id, @Valid @RequestBody LeadUpdateDTO updateDTO) {
        updateDTO.setId(id);
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUserName();
        leadService.updateLead(updateDTO, userId, userName);
        return R.success();
    }

    @OperationLog(module = "线索管理", value = "删除线索")
    @Operation(summary = "删除线索")
    @DeleteMapping("/{id}")
    public R<Void> deleteLead(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUserName();
        leadService.deleteLead(id, userId, userName);
        return R.success();
    }

    @OperationLog(module = "线索管理", value = "更新线索状态")
    @Operation(summary = "更新线索状态")
    @PutMapping("/{id}/status")
    public R<Void> updateLeadStatus(@PathVariable Long id, @Valid @RequestBody LeadStatusUpdateDTO statusDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUserName();
        leadService.updateLeadStatus(id, statusDTO, userId, userName);
        return R.success();
    }

    @OperationLog(module = "线索管理", value = "分配线索")
    @Operation(summary = "分配线索")
    @PutMapping("/{id}/assign")
    public R<Void> assignLead(@PathVariable Long id, @Valid @RequestBody LeadAssignDTO assignDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUserName();
        leadService.assignLead(id, assignDTO, userId, userName);
        return R.success();
    }

    @Operation(summary = "获取所有线索来源")
    @GetMapping("/sources")
    public R<List<LeadSourceVO>> getAllSources() {
        return R.success(leadService.getAllSources());
    }

    @OperationLog(module = "线索管理", value = "Excel导入线索")
    @Operation(summary = "Excel批量导入线索")
    @PostMapping("/import")
    public R<LeadImportResultDTO> importLeads(@RequestParam("file") MultipartFile file) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUserName();
        LeadImportResultDTO result = leadService.importLeads(file, userId, userName);
        return R.success(result);
    }

    @Operation(summary = "下载Excel导入模板")
    @GetMapping("/import/template")
    public void downloadTemplate() {
        throw new UnsupportedOperationException("模板下载功能请使用前端提供的静态模板");
    }
}
