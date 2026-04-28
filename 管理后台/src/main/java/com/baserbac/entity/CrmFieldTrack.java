package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_field_track")
public class CrmFieldTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String trackNo;

    private Long trackUserId;

    private String trackUserName;

    private LocalDate trackDate;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String locationAddress;

    private String locationProvince;

    private String locationCity;

    private String locationDistrict;

    private BigDecimal accuracy;

    private BigDecimal speed;

    private Integer direction;

    private LocalDateTime recordTime;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
