package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_todo_reminder")
public class CrmTodoReminder implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    public static final String[] PRIORITY_NAMES = {"", "高", "中", "低"};

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_COMPLETED = 1;
    public static final int STATUS_CANCELLED = 2;
    public static final int STATUS_EXPIRED = 3;

    public static final String[] STATUS_NAMES = {"待处理", "已完成", "已取消", "已过期"};

    public static final int REMIND_TYPE_NOTIFICATION = 1;
    public static final int REMIND_TYPE_SMS = 2;
    public static final int REMIND_TYPE_EMAIL = 3;

    public static final String[] REMIND_TYPE_NAMES = {"", "系统通知", "短信", "邮件"};

    public static final String RECURRING_TYPE_DAILY = "daily";
    public static final String RECURRING_TYPE_WEEKLY = "weekly";
    public static final String RECURRING_TYPE_MONTHLY = "monthly";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String todoNo;

    private Long userId;

    private String userName;

    private String title;

    private String content;

    private Integer priority;

    private Integer status;

    private LocalDateTime remindTime;

    private LocalDateTime endTime;

    private LocalDateTime completeTime;

    private String completeRemark;

    private String businessType;

    private Long businessId;

    private String businessName;

    private Long followRecordId;

    private Integer remindType;

    private Integer remindCount;

    private LocalDateTime lastRemindTime;

    private Integer isRecurring;

    private String recurringType;

    private String recurringConfig;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
