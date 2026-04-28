package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "待办提醒创建参数")
public class TodoReminderCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "待办标题不能为空")
    @Schema(description = "待办标题", required = true)
    private String title;

    @Schema(description = "待办内容")
    private String content;

    @Schema(description = "优先级：1-高 2-中 3-低", defaultValue = "2")
    private Integer priority = 2;

    @Schema(description = "提醒时间")
    private LocalDateTime remindTime;

    @Schema(description = "截止时间")
    private LocalDateTime endTime;

    @Schema(description = "关联业务类型：customer-客户, lead-线索, opportunity-商机, follow-跟进")
    private String businessType;

    @Schema(description = "关联业务ID")
    private Long businessId;

    @Schema(description = "关联业务名称")
    private String businessName;

    @Schema(description = "关联跟进记录ID")
    private Long followRecordId;

    @Schema(description = "提醒方式：1-系统通知 2-短信 3-邮件", defaultValue = "1")
    private Integer remindType = 1;

    @Schema(description = "是否重复：0-否 1-是", defaultValue = "0")
    private Integer isRecurring = 0;

    @Schema(description = "重复类型：daily-每日, weekly-每周, monthly-每月")
    private String recurringType;

    @Schema(description = "重复配置（JSON）")
    private String recurringConfig;
}
