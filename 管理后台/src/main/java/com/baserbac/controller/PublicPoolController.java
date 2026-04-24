package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.PublicPoolClaimDTO;
import com.baserbac.dto.PublicPoolQueryDTO;
import com.baserbac.service.PublicPoolService;
import com.baserbac.vo.PublicPoolVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.baserbac.service.PublicPoolService.*;

@Tag(name = "公海池管理")
@RestController
@RequestMapping("/api/crm/public-pool")
@RequiredArgsConstructor
public class PublicPoolController {

    private final PublicPoolService publicPoolService;

    @Operation(summary = "分页查询公海池线索列表")
    @GetMapping
    public R<PageResult<PublicPoolVO>> pagePublicPool(PublicPoolQueryDTO queryDTO) {
        return R.success(publicPoolService.pagePublicPool(queryDTO));
    }

    @OperationLog(module = "公海池", value = "认领线索")
    @Operation(summary = "认领公海线索")
    @PostMapping("/claim")
    public R<Void> claimLead(@Valid @RequestBody PublicPoolClaimDTO claimDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        publicPoolService.claimLead(claimDTO, userId, userName);
        return R.success();
    }

    @OperationLog(module = "公海池", value = "手动回收线索")
    @Operation(summary = "手动回收线索到公海")
    @PostMapping("/recycle/{leadId}")
    public R<Void> recycleLead(@PathVariable Long leadId,
                                @RequestParam(required = false) String reason) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        publicPoolService.recycleLead(leadId, RECYCLE_TYPE_MANUAL, reason, userId, userName);
        return R.success();
    }
}
