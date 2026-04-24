package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTicketVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private String ticketNo;

    private String ticketTitle;

    private String ticketType;

    private String ticketTypeName;

    private Integer priority;

    private String priorityName;

    private Integer status;

    private String statusName;

    private String description;

    private String solution;

    private Long assigneeUserId;

    private String assigneeUserName;

    private Long createUserId;

    private String createUserName;

    private LocalDateTime resolvedTime;

    private LocalDateTime closedTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
