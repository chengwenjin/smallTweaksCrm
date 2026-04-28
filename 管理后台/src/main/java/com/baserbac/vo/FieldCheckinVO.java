package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldCheckinVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String checkinNo;

    private Long checkinUserId;

    private String checkinUserName;

    private Integer checkinType;

    private String checkinTypeName;

    private LocalDateTime checkinTime;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String locationAddress;

    private String locationProvince;

    private String locationCity;

    private String locationDistrict;

    private List<String> photoUrls;

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
