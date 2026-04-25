package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_transfer_detail")
public class CrmTransferDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int RESOURCE_TYPE_CUSTOMER = 1;
    public static final int RESOURCE_TYPE_LEAD = 2;
    public static final int RESOURCE_TYPE_CONTRACT = 3;
    public static final int RESOURCE_TYPE_FOLLOW = 4;

    public static final String[] RESOURCE_TYPE_NAMES = {"", "客户", "线索", "合同", "跟进记录"};

    public static final int TRANSFER_STATUS_TRANSFERRED = 1;
    public static final int TRANSFER_STATUS_RECYCLED = 2;

    public static final String[] TRANSFER_STATUS_NAMES = {"", "已转移", "已回收"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long recordId;

    private String transferNo;

    private Integer resourceType;

    private String resourceTypeName;

    private Long resourceId;

    private String resourceNo;

    private String resourceName;

    private Long fromUserId;

    private String fromUserName;

    private Long toUserId;

    private String toUserName;

    private Integer transferStatus;

    private LocalDateTime transferTime;

    private String remark;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
