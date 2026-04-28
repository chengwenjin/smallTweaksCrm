package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.TodoCompleteDTO;
import com.baserbac.dto.TodoReminderCreateDTO;
import com.baserbac.dto.TodoReminderQueryDTO;
import com.baserbac.dto.TodoReminderUpdateDTO;
import com.baserbac.entity.CrmTodoReminder;
import com.baserbac.mapper.TodoReminderMapper;
import com.baserbac.vo.TodoReminderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoReminderService {

    private final TodoReminderMapper todoReminderMapper;

    public Page<TodoReminderVO> getTodoPage(TodoReminderQueryDTO queryDTO) {
        Page<CrmTodoReminder> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<CrmTodoReminder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getUserId() != null, CrmTodoReminder::getUserId, queryDTO.getUserId())
               .eq(queryDTO.getStatus() != null, CrmTodoReminder::getStatus, queryDTO.getStatus())
               .eq(queryDTO.getPriority() != null, CrmTodoReminder::getPriority, queryDTO.getPriority())
               .eq(queryDTO.getBusinessType() != null, CrmTodoReminder::getBusinessType, queryDTO.getBusinessType())
               .eq(queryDTO.getBusinessId() != null, CrmTodoReminder::getBusinessId, queryDTO.getBusinessId())
               .ge(queryDTO.getRemindTimeStart() != null, CrmTodoReminder::getRemindTime, queryDTO.getRemindTimeStart())
               .le(queryDTO.getRemindTimeEnd() != null, CrmTodoReminder::getRemindTime, queryDTO.getRemindTimeEnd())
               .ge(queryDTO.getEndTimeStart() != null, CrmTodoReminder::getEndTime, queryDTO.getEndTimeStart())
               .le(queryDTO.getEndTimeEnd() != null, CrmTodoReminder::getEndTime, queryDTO.getEndTimeEnd())
               .orderByAsc(CrmTodoReminder::getRemindTime);
        
        Page<CrmTodoReminder> resultPage = todoReminderMapper.selectPage(page, wrapper);
        
        Page<TodoReminderVO> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    public List<TodoReminderVO> getPendingTodos(Long userId) {
        List<CrmTodoReminder> todos = todoReminderMapper.selectList(
                new LambdaQueryWrapper<CrmTodoReminder>()
                        .eq(CrmTodoReminder::getUserId, userId)
                        .eq(CrmTodoReminder::getStatus, CrmTodoReminder.STATUS_PENDING)
                        .orderByAsc(CrmTodoReminder::getRemindTime)
                        .last("LIMIT 50")
        );
        return todos.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public TodoReminderVO getTodoById(Long id) {
        CrmTodoReminder todo = todoReminderMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(todo);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createTodoReminder(TodoReminderCreateDTO createDTO, Long userId, String userName) {
        CrmTodoReminder todo = new CrmTodoReminder();
        todo.setTodoNo(generateTodoNo());
        todo.setUserId(userId);
        todo.setUserName(userName);
        todo.setTitle(createDTO.getTitle());
        todo.setContent(createDTO.getContent());
        todo.setPriority(createDTO.getPriority() != null ? createDTO.getPriority() : CrmTodoReminder.PRIORITY_MEDIUM);
        todo.setStatus(CrmTodoReminder.STATUS_PENDING);
        todo.setRemindTime(createDTO.getRemindTime());
        todo.setEndTime(createDTO.getEndTime());
        todo.setBusinessType(createDTO.getBusinessType());
        todo.setBusinessId(createDTO.getBusinessId());
        todo.setBusinessName(createDTO.getBusinessName());
        todo.setFollowRecordId(createDTO.getFollowRecordId());
        todo.setRemindType(createDTO.getRemindType() != null ? createDTO.getRemindType() : CrmTodoReminder.REMIND_TYPE_NOTIFICATION);
        todo.setRemindCount(0);
        todo.setIsRecurring(createDTO.getIsRecurring() != null ? createDTO.getIsRecurring() : 0);
        todo.setRecurringType(createDTO.getRecurringType());
        todo.setRecurringConfig(createDTO.getRecurringConfig());

        todoReminderMapper.insert(todo);
        return todo.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void createTodoReminderInternal(CrmTodoReminder todo) {
        if (todo.getTodoNo() == null) {
            todo.setTodoNo(generateTodoNo());
        }
        if (todo.getStatus() == null) {
            todo.setStatus(CrmTodoReminder.STATUS_PENDING);
        }
        if (todo.getRemindCount() == null) {
            todo.setRemindCount(0);
        }
        if (todo.getIsRecurring() == null) {
            todo.setIsRecurring(0);
        }
        todoReminderMapper.insert(todo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTodoReminder(Long id, TodoReminderUpdateDTO updateDTO) {
        CrmTodoReminder todo = todoReminderMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (updateDTO.getTitle() != null) todo.setTitle(updateDTO.getTitle());
        if (updateDTO.getContent() != null) todo.setContent(updateDTO.getContent());
        if (updateDTO.getPriority() != null) todo.setPriority(updateDTO.getPriority());
        if (updateDTO.getStatus() != null) todo.setStatus(updateDTO.getStatus());
        if (updateDTO.getRemindTime() != null) todo.setRemindTime(updateDTO.getRemindTime());
        if (updateDTO.getEndTime() != null) todo.setEndTime(updateDTO.getEndTime());
        if (updateDTO.getCompleteTime() != null) todo.setCompleteTime(updateDTO.getCompleteTime());
        if (updateDTO.getCompleteRemark() != null) todo.setCompleteRemark(updateDTO.getCompleteRemark());
        if (updateDTO.getRemindType() != null) todo.setRemindType(updateDTO.getRemindType());
        if (updateDTO.getIsRecurring() != null) todo.setIsRecurring(updateDTO.getIsRecurring());
        if (updateDTO.getRecurringType() != null) todo.setRecurringType(updateDTO.getRecurringType());
        if (updateDTO.getRecurringConfig() != null) todo.setRecurringConfig(updateDTO.getRecurringConfig());

        todoReminderMapper.updateById(todo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeTodo(Long id, TodoCompleteDTO completeDTO, Long userId) {
        CrmTodoReminder todo = todoReminderMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        todo.setStatus(CrmTodoReminder.STATUS_COMPLETED);
        todo.setCompleteTime(LocalDateTime.now());
        if (completeDTO != null && completeDTO.getCompleteRemark() != null) {
            todo.setCompleteRemark(completeDTO.getCompleteRemark());
        }

        todoReminderMapper.updateById(todo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelTodo(Long id) {
        CrmTodoReminder todo = todoReminderMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        todo.setStatus(CrmTodoReminder.STATUS_CANCELLED);
        todoReminderMapper.updateById(todo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTodo(Long id) {
        CrmTodoReminder todo = todoReminderMapper.selectById(id);
        if (todo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        todoReminderMapper.deleteById(id);
    }

    public void updateExpiredTodos() {
        LocalDateTime now = LocalDateTime.now();
        todoReminderMapper.update(
                null,
                new LambdaUpdateWrapper<CrmTodoReminder>()
                        .eq(CrmTodoReminder::getStatus, CrmTodoReminder.STATUS_PENDING)
                        .lt(CrmTodoReminder::getEndTime, now)
                        .set(CrmTodoReminder::getStatus, CrmTodoReminder.STATUS_EXPIRED)
        );
    }

    private TodoReminderVO convertToVO(CrmTodoReminder todo) {
        return TodoReminderVO.builder()
                .id(todo.getId())
                .todoNo(todo.getTodoNo())
                .userId(todo.getUserId())
                .userName(todo.getUserName())
                .title(todo.getTitle())
                .content(todo.getContent())
                .priority(todo.getPriority())
                .priorityName(todo.getPriority() != null && todo.getPriority() >= 1 && todo.getPriority() <= 3
                        ? CrmTodoReminder.PRIORITY_NAMES[todo.getPriority()] : null)
                .status(todo.getStatus())
                .statusName(todo.getStatus() != null && todo.getStatus() >= 0 && todo.getStatus() <= 3
                        ? CrmTodoReminder.STATUS_NAMES[todo.getStatus()] : null)
                .remindTime(todo.getRemindTime())
                .endTime(todo.getEndTime())
                .completeTime(todo.getCompleteTime())
                .completeRemark(todo.getCompleteRemark())
                .businessType(todo.getBusinessType())
                .businessId(todo.getBusinessId())
                .businessName(todo.getBusinessName())
                .followRecordId(todo.getFollowRecordId())
                .remindType(todo.getRemindType())
                .remindTypeName(todo.getRemindType() != null && todo.getRemindType() >= 1 && todo.getRemindType() <= 3
                        ? CrmTodoReminder.REMIND_TYPE_NAMES[todo.getRemindType()] : null)
                .remindCount(todo.getRemindCount())
                .lastRemindTime(todo.getLastRemindTime())
                .isRecurring(todo.getIsRecurring())
                .recurringType(todo.getRecurringType())
                .recurringConfig(todo.getRecurringConfig())
                .createTime(todo.getCreateTime())
                .updateTime(todo.getUpdateTime())
                .build();
    }

    private String generateTodoNo() {
        return "TD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
