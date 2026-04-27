package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "销售漏斗统计信息")
public class SalesFunnelStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总商机数")
    private Long totalOpportunityCount;

    @Schema(description = "总预计金额")
    private BigDecimal totalExpectedAmount;

    @Schema(description = "总预期成交金额（加权）")
    private BigDecimal totalForecastedAmount;

    @Schema(description = "赢单商机数")
    private Long wonCount;

    @Schema(description = "赢单金额")
    private BigDecimal wonAmount;

    @Schema(description = "输单商机数")
    private Long lostCount;

    @Schema(description = "进行中商机数")
    private Long inProgressCount;

    @Schema(description = "各阶段统计数据")
    private List<StageStatisticsVO> stageStatistics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageStatisticsVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "阶段ID")
        private Long stageId;

        @Schema(description = "阶段编码")
        private String stageCode;

        @Schema(description = "阶段名称")
        private String stageName;

        @Schema(description = "阶段排序")
        private Integer sortOrder;

        @Schema(description = "阶段赢率")
        private BigDecimal winProbability;

        @Schema(description = "该阶段商机数")
        private Long opportunityCount;

        @Schema(description = "该阶段预计总金额")
        private BigDecimal expectedAmount;

        @Schema(description = "该阶段预期成交金额（加权）")
        private BigDecimal forecastedAmount;

        @Schema(description = "占比（商机数百分比）")
        private BigDecimal countPercentage;

        @Schema(description = "占比（金额百分比）")
        private BigDecimal amountPercentage;
    }
}
