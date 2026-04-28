package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "外勤轨迹创建参数")
public class FieldTrackCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "纬度不能为空")
    @Schema(description = "纬度", required = true)
    private BigDecimal latitude;

    @NotNull(message = "经度不能为空")
    @Schema(description = "经度", required = true)
    private BigDecimal longitude;

    @Schema(description = "位置地址")
    private String locationAddress;

    @Schema(description = "省份")
    private String locationProvince;

    @Schema(description = "城市")
    private String locationCity;

    @Schema(description = "区县")
    private String locationDistrict;

    @Schema(description = "定位精度（米）")
    private BigDecimal accuracy;

    @Schema(description = "移动速度（km/h）")
    private BigDecimal speed;

    @Schema(description = "方向角度（0-360）")
    private Integer direction;

    @Schema(description = "记录时间")
    private LocalDateTime recordTime;
}
