package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContactVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private String contactName;

    private String contactPosition;

    private String contactDepartment;

    private String mobile;

    private String phone;

    private String email;

    private String wechat;

    private String qq;

    private Integer isKeyContact;

    private String isKeyContactName;

    private Integer isPrimary;

    private String isPrimaryName;

    private Integer sortOrder;

    private Integer gender;

    private String genderName;

    private LocalDate birthday;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
