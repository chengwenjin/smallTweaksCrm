package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 线索导入结果 DTO
 */
@Data
@Schema(description = "线索导入结果")
public class LeadImportResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总条数")
    private Integer totalCount;

    @Schema(description = "成功条数")
    private Integer successCount;

    @Schema(description = "失败条数")
    private Integer failCount;

    @Schema(description = "成功的线索ID列表")
    private List<Long> successIds;

    @Schema(description = "失败详情列表")
    private List<FailDetail> failDetails;

    @Data
    @Schema(description = "导入失败详情")
    public static class FailDetail implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "行号")
        private Integer rowNum;

        @Schema(description = "数据内容")
        private String data;

        @Schema(description = "失败原因")
        private String reason;
    }
}
