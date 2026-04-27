package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "商机查询参数")
public class BusinessOpportunityQueryDTO extends PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商机名称（模糊查询）")
    private String opportunityName;

    @Schema(description = "客户名称（模糊查询）")
    private String customerName;

    @Schema(description = "销售阶段ID")
    private Long stageId;

    @Schema(description = "销售阶段编码")
    private String stageCode;

    @Schema(description = "商机状态：0-进行中 1-已赢单 2-已输单")
    private Integer status;

    @Schema(description = "分配负责人ID")
    private Long assignUserId;

    @Schema(description = "最小预计金额")
    private BigDecimal minExpectedAmount;

    @Schema(description = "最大预计金额")
    private BigDecimal maxExpectedAmount;

    @Schema(description = "创建开始时间")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建结束时间")
    private LocalDateTime createTimeEnd;

    @Schema(description = "标签（模糊查询）")
    private String tags;

    @Schema(description = "所属行业")
    private String industry;
}
