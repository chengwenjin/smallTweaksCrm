package com.baserbac.controller;

import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.PageResult;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.PrivateSeaConfigCreateDTO;
import com.baserbac.dto.PrivateSeaConfigQueryDTO;
import com.baserbac.dto.PrivateSeaConfigUpdateDTO;
import com.baserbac.service.PrivateSeaConfigService;
import com.baserbac.vo.PrivateSeaConfigVO;
import com.baserbac.vo.PrivateSeaUsageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "私海配置管理")
@RestController
@RequestMapping("/api/crm/private-sea")
@RequiredArgsConstructor
public class PrivateSeaConfigController {

    private final PrivateSeaConfigService privateSeaConfigService;

    @Operation(summary = "分页查询私海配置列表")
    @GetMapping
    public R<PageResult<PrivateSeaConfigVO>> pageConfigs(PrivateSeaConfigQueryDTO queryDTO) {
        return R.success(privateSeaConfigService.pageConfigs(queryDTO));
    }

    @Operation(summary = "获取私海配置详情")
    @GetMapping("/{id}")
    public R<PrivateSeaConfigVO> getConfigById(@PathVariable Long id) {
        return R.success(privateSeaConfigService.getConfigById(id));
    }

    @Operation(summary = "获取当前用户私海使用情况")
    @GetMapping("/usage")
    public R<PrivateSeaUsageVO> getPrivateSeaUsage() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return R.error(401, "请登录后重试");
        }
        return R.success(privateSeaConfigService.getPrivateSeaUsage(userId));
    }

    @Operation(summary = "获取指定用户私海使用情况")
    @GetMapping("/usage/{userId}")
    public R<PrivateSeaUsageVO> getPrivateSeaUsageByUser(@PathVariable Long userId) {
        return R.success(privateSeaConfigService.getPrivateSeaUsage(userId));
    }

    @OperationLog(module = "私海配置", value = "新增私海配置")
    @Operation(summary = "新增私海配置")
    @PostMapping
    public R<Long> createConfig(@Valid @RequestBody PrivateSeaConfigCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        log.info("用户 [{}] 新增私海配置: userId={}", userName, userId);
        return R.success(privateSeaConfigService.createConfig(createDTO));
    }

    @OperationLog(module = "私海配置", value = "更新私海配置")
    @Operation(summary = "更新私海配置")
    @PutMapping
    public R<Void> updateConfig(@Valid @RequestBody PrivateSeaConfigUpdateDTO updateDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        log.info("用户 [{}] 更新私海配置: userId={}, configId={}", userName, userId, updateDTO.getId());
        privateSeaConfigService.updateConfig(updateDTO);
        return R.success();
    }

    @OperationLog(module = "私海配置", value = "删除私海配置")
    @Operation(summary = "删除私海配置")
    @DeleteMapping("/{id}")
    public R<Void> deleteConfig(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        log.info("用户 [{}] 删除私海配置: userId={}, configId={}", userName, userId, id);
        privateSeaConfigService.deleteConfig(id);
        return R.success();
    }
}
