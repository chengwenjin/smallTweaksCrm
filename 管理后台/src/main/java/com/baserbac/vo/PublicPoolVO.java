package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "公海线索VO")
public class PublicPoolVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long leadId;
    private String leadNo;
    private String leadName;
    private String contactName;
    private String contactMobile;
    private String province;
    private String city;
    private String industry;
    private Long sourceId;
    private String sourceName;
    private Integer level;
    private String levelName;
    private Integer status;
    private String statusName;
    private BigDecimal expectedAmount;
    private Long fromUserId;
    private String fromUserName;
    private Integer recycleType;
    private String recycleTypeName;
    private String recycleReason;
    private Integer isClaimed;
    private Long claimUserId;
    private String claimUserName;
    private LocalDateTime claimTime;
    private LocalDateTime createTime;
    private LocalDateTime publicTime;
}
