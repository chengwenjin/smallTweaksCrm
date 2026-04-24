package com.baserbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.CustomerContactCreateDTO;
import com.baserbac.dto.CustomerContactUpdateDTO;
import com.baserbac.entity.CrmCustomerContact;
import com.baserbac.mapper.CustomerContactMapper;
import com.baserbac.vo.CustomerContactVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerContactService {

    private final CustomerContactMapper customerContactMapper;

    public List<CustomerContactVO> getContactsByCustomerId(Long customerId) {
        List<CrmCustomerContact> contacts = customerContactMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerContact>()
                        .eq(CrmCustomerContact::getCustomerId, customerId)
                        .orderByAsc(CrmCustomerContact::getSortOrder)
                        .orderByDesc(CrmCustomerContact::getIsPrimary)
        );
        return contacts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public CustomerContactVO getContactById(Long id) {
        CrmCustomerContact contact = customerContactMapper.selectById(id);
        if (contact == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(contact);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createContact(CustomerContactCreateDTO createDTO) {
        CrmCustomerContact contact = new CrmCustomerContact();
        contact.setCustomerId(createDTO.getCustomerId());
        contact.setContactName(createDTO.getContactName());
        contact.setContactPosition(createDTO.getContactPosition());
        contact.setContactDepartment(createDTO.getContactDepartment());
        contact.setMobile(createDTO.getMobile());
        contact.setPhone(createDTO.getPhone());
        contact.setEmail(createDTO.getEmail());
        contact.setWechat(createDTO.getWechat());
        contact.setQq(createDTO.getQq());
        contact.setIsKeyContact(createDTO.getIsKeyContact() != null ? createDTO.getIsKeyContact() : 0);
        contact.setIsPrimary(createDTO.getIsPrimary() != null ? createDTO.getIsPrimary() : 0);
        contact.setSortOrder(createDTO.getSortOrder() != null ? createDTO.getSortOrder() : 0);
        contact.setGender(createDTO.getGender());
        contact.setBirthday(createDTO.getBirthday());
        contact.setDescription(createDTO.getDescription());

        if (contact.getIsPrimary() != null && contact.getIsPrimary() == 1) {
            customerContactMapper.update(
                    null,
                    new LambdaUpdateWrapper<CrmCustomerContact>()
                            .eq(CrmCustomerContact::getCustomerId, createDTO.getCustomerId())
                            .eq(CrmCustomerContact::getIsPrimary, 1)
                            .set(CrmCustomerContact::getIsPrimary, 0)
            );
        }

        customerContactMapper.insert(contact);
        return contact.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateContact(CustomerContactUpdateDTO updateDTO) {
        CrmCustomerContact contact = customerContactMapper.selectById(updateDTO.getId());
        if (contact == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (updateDTO.getContactName() != null) {
            contact.setContactName(updateDTO.getContactName());
        }
        if (updateDTO.getContactPosition() != null) {
            contact.setContactPosition(updateDTO.getContactPosition());
        }
        if (updateDTO.getContactDepartment() != null) {
            contact.setContactDepartment(updateDTO.getContactDepartment());
        }
        if (updateDTO.getMobile() != null) {
            contact.setMobile(updateDTO.getMobile());
        }
        if (updateDTO.getPhone() != null) {
            contact.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getEmail() != null) {
            contact.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getWechat() != null) {
            contact.setWechat(updateDTO.getWechat());
        }
        if (updateDTO.getQq() != null) {
            contact.setQq(updateDTO.getQq());
        }
        if (updateDTO.getIsKeyContact() != null) {
            contact.setIsKeyContact(updateDTO.getIsKeyContact());
        }
        if (updateDTO.getIsPrimary() != null) {
            contact.setIsPrimary(updateDTO.getIsPrimary());
        }
        if (updateDTO.getSortOrder() != null) {
            contact.setSortOrder(updateDTO.getSortOrder());
        }
        if (updateDTO.getGender() != null) {
            contact.setGender(updateDTO.getGender());
        }
        if (updateDTO.getBirthday() != null) {
            contact.setBirthday(updateDTO.getBirthday());
        }
        if (updateDTO.getDescription() != null) {
            contact.setDescription(updateDTO.getDescription());
        }

        if (contact.getIsPrimary() != null && contact.getIsPrimary() == 1) {
            customerContactMapper.update(
                    null,
                    new LambdaUpdateWrapper<CrmCustomerContact>()
                            .eq(CrmCustomerContact::getCustomerId, contact.getCustomerId())
                            .ne(CrmCustomerContact::getId, contact.getId())
                            .eq(CrmCustomerContact::getIsPrimary, 1)
                            .set(CrmCustomerContact::getIsPrimary, 0)
            );
        }

        customerContactMapper.updateById(contact);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteContact(Long id) {
        CrmCustomerContact contact = customerContactMapper.selectById(id);
        if (contact == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        customerContactMapper.deleteById(id);
    }

    private CustomerContactVO convertToVO(CrmCustomerContact contact) {
        return CustomerContactVO.builder()
                .id(contact.getId())
                .customerId(contact.getCustomerId())
                .contactName(contact.getContactName())
                .contactPosition(contact.getContactPosition())
                .contactDepartment(contact.getContactDepartment())
                .mobile(contact.getMobile())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .wechat(contact.getWechat())
                .qq(contact.getQq())
                .isKeyContact(contact.getIsKeyContact())
                .isKeyContactName(contact.getIsKeyContact() != null && contact.getIsKeyContact() == 1 ? "是" : "否")
                .isPrimary(contact.getIsPrimary())
                .isPrimaryName(contact.getIsPrimary() != null && contact.getIsPrimary() == 1 ? "是" : "否")
                .sortOrder(contact.getSortOrder())
                .gender(contact.getGender())
                .genderName(contact.getGender() != null && contact.getGender() >= 0 && contact.getGender() <= 2
                        ? new String[]{"未知", "男", "女"}[contact.getGender()] : null)
                .birthday(contact.getBirthday())
                .description(contact.getDescription())
                .createTime(contact.getCreateTime())
                .updateTime(contact.getUpdateTime())
                .build();
    }
}
