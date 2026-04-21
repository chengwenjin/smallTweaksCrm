package com.baserbac.controller;

import com.baserbac.common.result.R;
import com.baserbac.dto.LeadApiCreateDTO;
import com.baserbac.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线索 API 对接控制器
 * 用于官网表单、市场活动等外部系统对接
 * 无需登录认证
 */
@Tag(name = "线索API对接")
@RestController
@RequestMapping("/api/crm/public/leads")
@RequiredArgsConstructor
public class LeadApiController {

    private final LeadService leadService;

    @Operation(summary = "API对接创建线索（官网表单、市场活动等）")
    @PostMapping
    public R<Long> createLeadFromApi(@Valid @RequestBody LeadApiCreateDTO createDTO) {
        Long id = leadService.createLeadFromApi(createDTO);
        return R.success(id);
    }
}
