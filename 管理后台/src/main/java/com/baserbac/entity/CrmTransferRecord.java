package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_transfer_record")
public class CrmTransferRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TRANSFER_TYPE_RESIGN = 1;
    public static final int TRANSFER_TYPE_MANUAL = 2;
    public static final int TRANSFER_TYPE_AUTO = 3;
    public static final int TRANSFER_TYPE_TRANSFER = 4;

    public static final String[] TRANSFER_TYPE_NAMES = {"", "离职回收", "手动转移", "自动回收", "调岗转移"};

    public static final int TRANSFER_METHOD_ALL = 1;
    public static final int TRANSFER_METHOD_PART = 2;
    public static final int TRANSFER_METHOD_TO_PUBLIC = 3;

    public static final String[] TRANSFER_METHOD_NAMES = {"", "全部转移", "部分转移", "移入公海"};

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_COMPLETED = 1;
    public static final int STATUS_CANCELLED = 2;

    public static final String[] STATUS_NAMES = {"待确认", "已完成", "已取消"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String transferNo;

    private Integer transferType;

    private String transferTypeName;

    private Long fromUserId;

    private String fromUserName;

    private Long fromDepartmentId;

    private String fromDepartmentName;

    private Long toUserId;

    private String toUserName;

    private Long toDepartmentId;

    private String toDepartmentName;

    private Integer transferMethod;

    private Integer customerCount;

    private Integer leadCount;

    private Integer contractCount;

    private Integer followCount;

    private Integer status;

    private String reason;

    private String remark;

    private Long operatorId;

    private String operatorName;

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

    private String ext1;

    private String ext2;

    private String ext3;
}
