package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "查重结果VO")
public class LeadDuplicateCheckVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean hasDuplicate;
    private String duplicateType;
    private String duplicateMessage;
    private Long duplicateLeadId;
    private String duplicateLeadName;
    private String duplicateContactMobile;
}
