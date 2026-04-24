package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.AssignRecordQueryDTO;
import com.baserbac.dto.LeadFollowCreateDTO;
import com.baserbac.entity.CrmLeadFollow;
import com.baserbac.service.AssignRecordService;
import com.baserbac.service.LeadFollowService;
import com.baserbac.vo.AssignRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "线索跟进管理")
@RestController
@RequestMapping("/api/crm/lead-follows")
@RequiredArgsConstructor
public class LeadFollowController {

    private final LeadFollowService leadFollowService;
    private final AssignRecordService assignRecordService;

    @Operation(summary = "分页查询线索跟进记录")
    @GetMapping
    public R<PageResult<CrmLeadFollow>> pageFollows(
            @RequestParam Long leadId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.success(leadFollowService.pageFollows(leadId, pageNum, pageSize));
    }

    @OperationLog(module = "线索跟进", value = "新增跟进记录")
    @Operation(summary = "创建线索跟进记录")
    @PostMapping
    public R<Long> createFollow(@Valid @RequestBody LeadFollowCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = leadFollowService.createFollow(createDTO, userId, userName);
        return R.success(id);
    }

    @Operation(summary = "分页查询分配记录")
    @GetMapping("/assign-records")
    public R<PageResult<AssignRecordVO>> pageAssignRecords(AssignRecordQueryDTO queryDTO) {
        return R.success(assignRecordService.pageRecords(queryDTO));
    }
}
