package com.baserbac.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.FieldCheckinCreateDTO;
import com.baserbac.dto.FieldCheckinQueryDTO;
import com.baserbac.dto.FieldTrackQueryDTO;
import com.baserbac.service.FieldCheckinService;
import com.baserbac.service.FieldTrackService;
import com.baserbac.vo.FieldCheckinVO;
import com.baserbac.vo.FieldTrackVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "外勤签到管理")
@RestController
@RequestMapping("/api/crm/field-checkins")
@RequiredArgsConstructor
public class FieldCheckinController {

    private final FieldCheckinService fieldCheckinService;
    private final FieldTrackService fieldTrackService;

    @Operation(summary = "分页查询签到记录")
    @GetMapping
    public R<Page<FieldCheckinVO>> getCheckinPage(FieldCheckinQueryDTO queryDTO) {
        return R.success(fieldCheckinService.getCheckinPage(queryDTO));
    }

    @Operation(summary = "获取今日签到记录")
    @GetMapping("/today")
    public R<List<FieldCheckinVO>> getTodayCheckins() {
        Long userId = SecurityUtil.getCurrentUserId();
        return R.success(fieldCheckinService.getTodayCheckins(userId));
    }

    @Operation(summary = "获取签到详情")
    @GetMapping("/{id}")
    public R<FieldCheckinVO> getCheckinById(@PathVariable Long id) {
        return R.success(fieldCheckinService.getCheckinById(id));
    }

    @OperationLog(module = "外勤签到管理", value = "创建签到记录")
    @Operation(summary = "创建签到记录")
    @PostMapping
    public R<Long> createCheckin(@Valid @RequestBody FieldCheckinCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = fieldCheckinService.createCheckin(createDTO, userId, userName);
        return R.success(id);
    }

    @Operation(summary = "分页查询轨迹记录")
    @GetMapping("/tracks")
    public R<Page<FieldTrackVO>> getTrackPage(FieldTrackQueryDTO queryDTO) {
        return R.success(fieldTrackService.getTrackPage(queryDTO));
    }

    @Operation(summary = "获取用户某日轨迹")
    @GetMapping("/tracks/{userId}/{date}")
    public R<List<FieldTrackVO>> getTracksByUserAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return R.success(fieldTrackService.getTracksByUserAndDate(userId, date));
    }
}
