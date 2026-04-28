package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "外勤签到创建参数")
public class FieldCheckinCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "签到类型：1-签到 2-签退", defaultValue = "1")
    private Integer checkinType = 1;

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

    @Schema(description = "现场照片URL列表")
    private List<String> photoUrls;

    @Schema(description = "关联业务类型：customer-客户, lead-线索, opportunity-商机")
    private String businessType;

    @Schema(description = "关联业务ID")
    private Long businessId;

    @Schema(description = "关联业务名称")
    private String businessName;

    @Schema(description = "拜访目的")
    private String purpose;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "设备信息")
    private String deviceInfo;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "网络类型：wifi, 4g, 5g")
    private String networkType;

    @Schema(description = "电量百分比")
    private Integer batteryLevel;
}
