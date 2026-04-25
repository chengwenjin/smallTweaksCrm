package com.baserbac.dto;

import lombok.Data;

@Data
public class TransferRecordQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer transferType;
    private Long fromUserId;
    private Long toUserId;
    private Integer status;
    private String transferNo;
    private String keyword;
}
