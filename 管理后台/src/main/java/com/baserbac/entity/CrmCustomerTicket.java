package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer_ticket")
public class CrmCustomerTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TYPE_CONSULT = 1;
    public static final int TYPE_FAULT = 2;
    public static final int TYPE_COMPLAINT = 3;
    public static final int TYPE_SUGGESTION = 4;

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_RESOLVED = 2;
    public static final int STATUS_CLOSED = 3;

    public static final String[] TYPE_NAMES = {"", "咨询", "故障", "投诉", "建议"};
    public static final String[] PRIORITY_NAMES = {"", "高", "中", "低"};
    public static final String[] STATUS_NAMES = {"待处理", "处理中", "已解决", "已关闭"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private String ticketNo;

    private String ticketTitle;

    private String ticketType;

    private Integer priority;

    private Integer status;

    private String description;

    private String solution;

    private Long assigneeUserId;

    private String assigneeUserName;

    private Long createUserId;

    private String createUserName;

    private LocalDateTime resolvedTime;

    private LocalDateTime closedTime;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
