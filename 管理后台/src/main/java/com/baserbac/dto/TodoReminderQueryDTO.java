package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "待办提醒查询参数")
public class TodoReminderQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "状态：0-待处理 1-已完成 2-已取消 3-已过期")
    private Integer status;

    @Schema(description = "优先级：1-高 2-中 3-低")
    private Integer priority;

    @Schema(description = "关联业务类型")
    private String businessType;

    @Schema(description = "关联业务ID")
    private Long businessId;

    @Schema(description = "提醒时间开始")
    private LocalDateTime remindTimeStart;

    @Schema(description = "提醒时间结束")
    private LocalDateTime remindTimeEnd;

    @Schema(description = "截止时间开始")
    private LocalDateTime endTimeStart;

    @Schema(description = "截止时间结束")
    private LocalDateTime endTimeEnd;

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", defaultValue = "10")
    private Integer pageSize = 10;
}
