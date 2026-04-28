package com.baserbac.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.annotation.OperationLog;
import com.baserbac.common.result.R;
import com.baserbac.common.util.SecurityUtil;
import com.baserbac.dto.TodoCompleteDTO;
import com.baserbac.dto.TodoReminderCreateDTO;
import com.baserbac.dto.TodoReminderQueryDTO;
import com.baserbac.dto.TodoReminderUpdateDTO;
import com.baserbac.service.TodoReminderService;
import com.baserbac.vo.TodoReminderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "待办提醒管理")
@RestController
@RequestMapping("/api/crm/todo-reminders")
@RequiredArgsConstructor
public class TodoReminderController {

    private final TodoReminderService todoReminderService;

    @Operation(summary = "分页查询待办提醒")
    @GetMapping
    public R<Page<TodoReminderVO>> getTodoPage(TodoReminderQueryDTO queryDTO) {
        return R.success(todoReminderService.getTodoPage(queryDTO));
    }

    @Operation(summary = "获取当前用户待办列表")
    @GetMapping("/pending")
    public R<List<TodoReminderVO>> getPendingTodos() {
        Long userId = SecurityUtil.getCurrentUserId();
        return R.success(todoReminderService.getPendingTodos(userId));
    }

    @Operation(summary = "获取待办详情")
    @GetMapping("/{id}")
    public R<TodoReminderVO> getTodoById(@PathVariable Long id) {
        return R.success(todoReminderService.getTodoById(id));
    }

    @OperationLog(module = "待办提醒管理", value = "创建待办提醒")
    @Operation(summary = "创建待办提醒")
    @PostMapping
    public R<Long> createTodoReminder(@Valid @RequestBody TodoReminderCreateDTO createDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        String userName = SecurityUtil.getCurrentUsername();
        Long id = todoReminderService.createTodoReminder(createDTO, userId, userName);
        return R.success(id);
    }

    @OperationLog(module = "待办提醒管理", value = "更新待办提醒")
    @Operation(summary = "更新待办提醒")
    @PutMapping("/{id}")
    public R<Void> updateTodoReminder(@PathVariable Long id, @Valid @RequestBody TodoReminderUpdateDTO updateDTO) {
        todoReminderService.updateTodoReminder(id, updateDTO);
        return R.success();
    }

    @OperationLog(module = "待办提醒管理", value = "完成待办")
    @Operation(summary = "完成待办")
    @PostMapping("/{id}/complete")
    public R<Void> completeTodo(@PathVariable Long id, @RequestBody(required = false) TodoCompleteDTO completeDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        todoReminderService.completeTodo(id, completeDTO, userId);
        return R.success();
    }

    @OperationLog(module = "待办提醒管理", value = "取消待办")
    @Operation(summary = "取消待办")
    @PostMapping("/{id}/cancel")
    public R<Void> cancelTodo(@PathVariable Long id) {
        todoReminderService.cancelTodo(id);
        return R.success();
    }

    @OperationLog(module = "待办提醒管理", value = "删除待办")
    @Operation(summary = "删除待办")
    @DeleteMapping("/{id}")
    public R<Void> deleteTodo(@PathVariable Long id) {
        todoReminderService.deleteTodo(id);
        return R.success();
    }
}
