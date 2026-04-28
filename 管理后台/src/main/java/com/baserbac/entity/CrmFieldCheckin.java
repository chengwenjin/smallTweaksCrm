package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("crm_field_checkin")
public class CrmFieldCheckin implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CHECKIN_TYPE_CHECKIN = 1;
    public static final int CHECKIN_TYPE_CHECKOUT = 2;

    public static final String[] CHECKIN_TYPE_NAMES = {"", "签到", "签退"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String checkinNo;

    private Long checkinUserId;

    private String checkinUserName;

    private Integer checkinType;

    private LocalDateTime checkinTime;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String locationAddress;

    private String locationProvince;

    private String locationCity;

    private String locationDistrict;

    private String photoUrls;

    private String businessType;

    private Long businessId;

    private String businessName;

    private String purpose;

    private String remark;

    private String deviceInfo;

    private String ipAddress;

    private String networkType;

    private Integer batteryLevel;

    private Integer isAbnormal;

    private String abnormalReason;

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
