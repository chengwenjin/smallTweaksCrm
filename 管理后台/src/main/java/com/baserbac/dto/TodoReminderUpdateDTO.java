package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "待办提醒更新参数")
public class TodoReminderUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "待办标题")
    private String title;

    @Schema(description = "待办内容")
    private String content;

    @Schema(description = "优先级：1-高 2-中 3-低")
    private Integer priority;

    @Schema(description = "状态：0-待处理 1-已完成 2-已取消 3-已过期")
    private Integer status;

    @Schema(description = "提醒时间")
    private LocalDateTime remindTime;

    @Schema(description = "截止时间")
    private LocalDateTime endTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "完成备注")
    private String completeRemark;

    @Schema(description = "提醒方式")
    private Integer remindType;

    @Schema(description = "是否重复")
    private Integer isRecurring;

    @Schema(description = "重复类型")
    private String recurringType;

    @Schema(description = "重复配置")
    private String recurringConfig;
}
