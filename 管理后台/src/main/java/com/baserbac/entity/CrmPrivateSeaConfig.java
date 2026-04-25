package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_private_sea_config")
public class CrmPrivateSeaConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CONFIG_TYPE_GLOBAL = 1;
    public static final int CONFIG_TYPE_ROLE = 2;
    public static final int CONFIG_TYPE_USER = 3;

    public static final String[] CONFIG_TYPE_NAMES = {"", "全局配置", "角色配置", "用户配置"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer configType;

    private Long roleId;

    private String roleName;

    private Long userId;

    private String userName;

    private Integer maxCustomerCount;

    private Integer maxLeadCount;

    private Integer autoRecycleDays;

    private Integer isEnabled;

    private String description;

    private Integer sortOrder;

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
