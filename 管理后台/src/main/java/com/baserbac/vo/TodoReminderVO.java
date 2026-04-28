package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoReminderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String todoNo;

    private Long userId;

    private String userName;

    private String title;

    private String content;

    private Integer priority;

    private String priorityName;

    private Integer status;

    private String statusName;

    private LocalDateTime remindTime;

    private LocalDateTime endTime;

    private LocalDateTime completeTime;

    private String completeRemark;

    private String businessType;

    private Long businessId;

    private String businessName;

    private Long followRecordId;

    private Integer remindType;

    private String remindTypeName;

    private Integer remindCount;

    private LocalDateTime lastRemindTime;

    private Integer isRecurring;

    private String recurringType;

    private String recurringConfig;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
