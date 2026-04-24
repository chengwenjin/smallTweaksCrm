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
public class CustomerFollowVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private Long followUserId;

    private String followUserName;

    private Integer followType;

    private String followTypeName;

    private String followContent;

    private LocalDateTime nextFollowTime;

    private String nextFollowRemark;

    private Integer isLast;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
