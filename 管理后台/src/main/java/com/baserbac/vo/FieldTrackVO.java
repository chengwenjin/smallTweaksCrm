package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldTrackVO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private LocalDateTime createTime;
}
