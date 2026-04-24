package com.baserbac.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.CustomerCreateDTO;
import com.baserbac.dto.CustomerQueryDTO;
import com.baserbac.dto.CustomerUpdateDTO;
import com.baserbac.entity.*;
import com.baserbac.mapper.*;
import com.baserbac.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerLevelMapper customerLevelMapper;
    private final CustomerTagMapper customerTagMapper;
    private final CustomerContactMapper customerContactMapper;
    private final CustomerFollowMapper customerFollowMapper;
    private final CustomerQuoteMapper customerQuoteMapper;
    private final CustomerContractMapper customerContractMapper;
    private final CustomerPaymentMapper customerPaymentMapper;
    private final CustomerTicketMapper customerTicketMapper;
    private final UserMapper userMapper;

    public static final String[] STATUS_NAMES = {"潜在", "合作中", "已流失", "休眠"};
    public static final String[] TYPE_NAMES = {"", "企业客户", "个人客户"};

    public PageResult<CustomerVO> pageCustomers(CustomerQueryDTO queryDTO) {
        Page<CrmCustomer> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getCustomerName()), CrmCustomer::getCustomerName, queryDTO.getCustomerName())
                .like(StrUtil.isNotBlank(queryDTO.getShortName()), CrmCustomer::getShortName, queryDTO.getShortName())
                .like(StrUtil.isNotBlank(queryDTO.getCreditCode()), CrmCustomer::getCreditCode, queryDTO.getCreditCode())
                .eq(StrUtil.isNotBlank(queryDTO.getLevelCode()), CrmCustomer::getLevelCode, queryDTO.getLevelCode())
                .eq(queryDTO.getStatus() != null, CrmCustomer::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getOwnerUserId() != null, CrmCustomer::getOwnerUserId, queryDTO.getOwnerUserId())
                .like(StrUtil.isNotBlank(queryDTO.getIndustry()), CrmCustomer::getIndustry, queryDTO.getIndustry())
                .like(StrUtil.isNotBlank(queryDTO.getProvince()), CrmCustomer::getProvince, queryDTO.getProvince())
                .like(StrUtil.isNotBlank(queryDTO.getCity()), CrmCustomer::getCity, queryDTO.getCity())
                .like(StrUtil.isNotBlank(queryDTO.getTags()), CrmCustomer::getTags, queryDTO.getTags())
                .like(StrUtil.isNotBlank(queryDTO.getSource()), CrmCustomer::getSource, queryDTO.getSource())
                .orderByDesc(CrmCustomer::getCreateTime);

        Page<CrmCustomer> result = customerMapper.selectPage(page, wrapper);

        List<CustomerVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    public CustomerVO getCustomerById(Long id) {
        CrmCustomer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToDetailVO(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createCustomer(CustomerCreateDTO createDTO, Long userId, String userName) {
        CrmCustomer customer = buildCustomerFromCreateDTO(createDTO, userId, userName);
        customerMapper.insert(customer);
        return customer.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCustomer(CustomerUpdateDTO updateDTO, Long userId, String userName) {
        CrmCustomer customer = customerMapper.selectById(updateDTO.getId());
        if (customer == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (updateDTO.getCustomerName() != null) {
            customer.setCustomerName(updateDTO.getCustomerName());
        }
        if (updateDTO.getShortName() != null) {
            customer.setShortName(updateDTO.getShortName());
        }
        if (updateDTO.getCustomerType() != null) {
            customer.setCustomerType(updateDTO.getCustomerType());
        }
        if (updateDTO.getCreditCode() != null) {
            customer.setCreditCode(updateDTO.getCreditCode());
        }
        if (updateDTO.getLegalPerson() != null) {
            customer.setLegalPerson(updateDTO.getLegalPerson());
        }
        if (updateDTO.getRegisteredCapital() != null) {
            customer.setRegisteredCapital(updateDTO.getRegisteredCapital());
        }
        if (updateDTO.getEstablishDate() != null) {
            customer.setEstablishDate(updateDTO.getEstablishDate());
        }
        if (updateDTO.getBusinessStatus() != null) {
            customer.setBusinessStatus(updateDTO.getBusinessStatus());
        }
        if (updateDTO.getBusinessScope() != null) {
            customer.setBusinessScope(updateDTO.getBusinessScope());
        }
        if (updateDTO.getRegisteredAddress() != null) {
            customer.setRegisteredAddress(updateDTO.getRegisteredAddress());
        }
        if (updateDTO.getIndustry() != null) {
            customer.setIndustry(updateDTO.getIndustry());
        }
        if (updateDTO.getProvince() != null) {
            customer.setProvince(updateDTO.getProvince());
        }
        if (updateDTO.getCity() != null) {
            customer.setCity(updateDTO.getCity());
        }
        if (updateDTO.getDistrict() != null) {
            customer.setDistrict(updateDTO.getDistrict());
        }
        if (updateDTO.getAddress() != null) {
            customer.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getWebsite() != null) {
            customer.setWebsite(updateDTO.getWebsite());
        }
        if (updateDTO.getEmail() != null) {
            customer.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhone() != null) {
            customer.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getFax() != null) {
            customer.setFax(updateDTO.getFax());
        }
        if (updateDTO.getEmployeeCount() != null) {
            customer.setEmployeeCount(updateDTO.getEmployeeCount());
        }
        if (updateDTO.getAnnualRevenue() != null) {
            customer.setAnnualRevenue(updateDTO.getAnnualRevenue());
        }
        if (updateDTO.getCompanyScale() != null) {
            customer.setCompanyScale(updateDTO.getCompanyScale());
        }
        if (updateDTO.getLevelCode() != null) {
            customer.setLevelCode(updateDTO.getLevelCode());
        }
        if (updateDTO.getOwnerUserId() != null) {
            customer.setOwnerUserId(updateDTO.getOwnerUserId());
            SysUser user = userMapper.selectById(updateDTO.getOwnerUserId());
            if (user != null) {
                customer.setOwnerUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }
        if (updateDTO.getTags() != null) {
            customer.setTags(updateDTO.getTags());
        }
        if (updateDTO.getSource() != null) {
            customer.setSource(updateDTO.getSource());
        }
        if (updateDTO.getStatus() != null) {
            customer.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getDescription() != null) {
            customer.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getRemark() != null) {
            customer.setRemark(updateDTO.getRemark());
        }
        if (updateDTO.getParentCustomerId() != null) {
            customer.setParentCustomerId(updateDTO.getParentCustomerId());
        }

        customerMapper.updateById(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(Long id, Long userId, String userName) {
        CrmCustomer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        customerMapper.deleteById(id);
    }

    public List<CustomerLevelVO> getAllCustomerLevels() {
        List<CrmCustomerLevel> levels = customerLevelMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerLevel>()
                        .eq(CrmCustomerLevel::getIsEnabled, 1)
                        .orderByAsc(CrmCustomerLevel::getSortOrder)
        );
        return levels.stream()
                .map(this::convertLevelToVO)
                .collect(Collectors.toList());
    }

    public List<CustomerTagVO> getAllCustomerTags() {
        List<CrmCustomerTag> tags = customerTagMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerTag>()
                        .eq(CrmCustomerTag::getIsEnabled, 1)
                        .orderByAsc(CrmCustomerTag::getSortOrder)
        );
        return tags.stream()
                .map(this::convertTagToVO)
                .collect(Collectors.toList());
    }

    private CrmCustomer buildCustomerFromCreateDTO(CustomerCreateDTO createDTO, Long userId, String userName) {
        CrmCustomer customer = new CrmCustomer();
        customer.setCustomerNo(generateCustomerNo());
        customer.setCustomerName(createDTO.getCustomerName());
        customer.setShortName(createDTO.getShortName());
        customer.setCustomerType(createDTO.getCustomerType() != null ? createDTO.getCustomerType() : "1");
        customer.setCreditCode(createDTO.getCreditCode());
        customer.setLegalPerson(createDTO.getLegalPerson());
        customer.setRegisteredCapital(createDTO.getRegisteredCapital());
        customer.setEstablishDate(createDTO.getEstablishDate());
        customer.setBusinessStatus(createDTO.getBusinessStatus());
        customer.setBusinessScope(createDTO.getBusinessScope());
        customer.setRegisteredAddress(createDTO.getRegisteredAddress());
        customer.setIndustry(createDTO.getIndustry());
        customer.setProvince(createDTO.getProvince());
        customer.setCity(createDTO.getCity());
        customer.setDistrict(createDTO.getDistrict());
        customer.setAddress(createDTO.getAddress());
        customer.setWebsite(createDTO.getWebsite());
        customer.setEmail(createDTO.getEmail());
        customer.setPhone(createDTO.getPhone());
        customer.setFax(createDTO.getFax());
        customer.setEmployeeCount(createDTO.getEmployeeCount());
        customer.setAnnualRevenue(createDTO.getAnnualRevenue());
        customer.setCompanyScale(createDTO.getCompanyScale());
        customer.setLevelCode(createDTO.getLevelCode() != null ? createDTO.getLevelCode() : "D");
        customer.setOwnerUserId(createDTO.getOwnerUserId());
        if (createDTO.getOwnerUserId() != null) {
            SysUser user = userMapper.selectById(createDTO.getOwnerUserId());
            if (user != null) {
                customer.setOwnerUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }
        customer.setTags(createDTO.getTags());
        customer.setSource(createDTO.getSource());
        customer.setStatus(createDTO.getStatus() != null ? createDTO.getStatus() : 0);
        customer.setFirstContactTime(LocalDateTime.now());
        customer.setDescription(createDTO.getDescription());
        customer.setRemark(createDTO.getRemark());
        customer.setParentCustomerId(createDTO.getParentCustomerId());
        customer.setIsDeleted(0);
        return customer;
    }

    private String generateCustomerNo() {
        return "CUS" + System.currentTimeMillis() + IdUtil.nanoId(4);
    }

    private CustomerVO convertToVO(CrmCustomer customer) {
        CustomerVO.CustomerVOBuilder builder = CustomerVO.builder()
                .id(customer.getId())
                .customerNo(customer.getCustomerNo())
                .customerName(customer.getCustomerName())
                .shortName(customer.getShortName())
                .customerType(customer.getCustomerType())
                .customerTypeName(customer.getCustomerType() != null && customer.getCustomerType().length() > 0
                        ? TYPE_NAMES[Integer.parseInt(customer.getCustomerType())] : null)
                .creditCode(customer.getCreditCode())
                .legalPerson(customer.getLegalPerson())
                .registeredCapital(customer.getRegisteredCapital())
                .establishDate(customer.getEstablishDate())
                .businessStatus(customer.getBusinessStatus())
                .industry(customer.getIndustry())
                .province(customer.getProvince())
                .city(customer.getCity())
                .district(customer.getDistrict())
                .address(customer.getAddress())
                .website(customer.getWebsite())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .employeeCount(customer.getEmployeeCount())
                .annualRevenue(customer.getAnnualRevenue())
                .companyScale(customer.getCompanyScale())
                .levelCode(customer.getLevelCode())
                .ownerUserId(customer.getOwnerUserId())
                .ownerUserName(customer.getOwnerUserName())
                .tags(customer.getTags())
                .source(customer.getSource())
                .status(customer.getStatus())
                .statusName(customer.getStatus() != null && customer.getStatus() >= 0 && customer.getStatus() <= 3
                        ? STATUS_NAMES[customer.getStatus()] : null)
                .firstContactTime(customer.getFirstContactTime())
                .lastContactTime(customer.getLastContactTime())
                .description(customer.getDescription())
                .remark(customer.getRemark())
                .parentCustomerId(customer.getParentCustomerId())
                .createTime(customer.getCreateTime())
                .updateTime(customer.getUpdateTime());

        if (StrUtil.isNotBlank(customer.getTags())) {
            builder.tagList(Arrays.asList(customer.getTags().split(",")));
        }

        StringBuilder fullAddress = new StringBuilder();
        if (StrUtil.isNotBlank(customer.getProvince())) {
            fullAddress.append(customer.getProvince());
        }
        if (StrUtil.isNotBlank(customer.getCity())) {
            fullAddress.append(customer.getCity());
        }
        if (StrUtil.isNotBlank(customer.getDistrict())) {
            fullAddress.append(customer.getDistrict());
        }
        if (StrUtil.isNotBlank(customer.getAddress())) {
            fullAddress.append(customer.getAddress());
        }
        if (fullAddress.length() > 0) {
            builder.fullAddress(fullAddress.toString());
        }

        return builder.build();
    }

    private CustomerVO convertToDetailVO(CrmCustomer customer) {
        CustomerVO vo = convertToVO(customer);

        List<CrmCustomerContact> contacts = customerContactMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerContact>()
                        .eq(CrmCustomerContact::getCustomerId, customer.getId())
                        .orderByAsc(CrmCustomerContact::getSortOrder)
                        .orderByDesc(CrmCustomerContact::getIsPrimary)
        );
        vo.setContacts(contacts.stream().map(this::convertContactToVO).collect(Collectors.toList()));

        List<CrmCustomerFollow> follows = customerFollowMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerFollow>()
                        .eq(CrmCustomerFollow::getCustomerId, customer.getId())
                        .orderByDesc(CrmCustomerFollow::getCreateTime)
                        .last("LIMIT 20")
        );
        vo.setFollows(follows.stream().map(this::convertFollowToVO).collect(Collectors.toList()));

        List<CrmCustomerQuote> quotes = customerQuoteMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerQuote>()
                        .eq(CrmCustomerQuote::getCustomerId, customer.getId())
                        .orderByDesc(CrmCustomerQuote::getQuoteDate)
        );
        vo.setQuotes(quotes.stream().map(this::convertQuoteToVO).collect(Collectors.toList()));

        List<CrmCustomerContract> contracts = customerContractMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerContract>()
                        .eq(CrmCustomerContract::getCustomerId, customer.getId())
                        .orderByDesc(CrmCustomerContract::getSignedDate)
        );
        vo.setContracts(contracts.stream().map(this::convertContractToVO).collect(Collectors.toList()));

        List<CrmCustomerPayment> payments = customerPaymentMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerPayment>()
                        .eq(CrmCustomerPayment::getCustomerId, customer.getId())
                        .orderByDesc(CrmCustomerPayment::getPaymentDate)
        );
        vo.setPayments(payments.stream().map(this::convertPaymentToVO).collect(Collectors.toList()));

        List<CrmCustomerTicket> tickets = customerTicketMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerTicket>()
                        .eq(CrmCustomerTicket::getCustomerId, customer.getId())
                        .orderByDesc(CrmCustomerTicket::getCreateTime)
        );
        vo.setTickets(tickets.stream().map(this::convertTicketToVO).collect(Collectors.toList()));

        return vo;
    }

    private CustomerContactVO convertContactToVO(CrmCustomerContact contact) {
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

    private CustomerFollowVO convertFollowToVO(CrmCustomerFollow follow) {
        return CustomerFollowVO.builder()
                .id(follow.getId())
                .customerId(follow.getCustomerId())
                .followUserId(follow.getFollowUserId())
                .followUserName(follow.getFollowUserName())
                .followType(follow.getFollowType())
                .followTypeName(follow.getFollowType() != null && follow.getFollowType() >= 1 && follow.getFollowType() <= 5
                        ? CustomerFollowService.TYPE_NAMES[follow.getFollowType()] : null)
                .followContent(follow.getFollowContent())
                .nextFollowTime(follow.getNextFollowTime())
                .nextFollowRemark(follow.getNextFollowRemark())
                .isLast(follow.getIsLast())
                .createTime(follow.getCreateTime())
                .updateTime(follow.getUpdateTime())
                .build();
    }

    private CustomerQuoteVO convertQuoteToVO(CrmCustomerQuote quote) {
        return CustomerQuoteVO.builder()
                .id(quote.getId())
                .customerId(quote.getCustomerId())
                .quoteNo(quote.getQuoteNo())
                .quoteName(quote.getQuoteName())
                .quoteAmount(quote.getQuoteAmount())
                .quoteDate(quote.getQuoteDate())
                .validDate(quote.getValidDate())
                .status(quote.getStatus())
                .statusName(quote.getStatus() != null && quote.getStatus() >= 0 && quote.getStatus() <= 3
                        ? new String[]{"待确认", "已接受", "已拒绝", "已过期"}[quote.getStatus()] : null)
                .description(quote.getDescription())
                .createTime(quote.getCreateTime())
                .updateTime(quote.getUpdateTime())
                .build();
    }

    private CustomerContractVO convertContractToVO(CrmCustomerContract contract) {
        return CustomerContractVO.builder()
                .id(contract.getId())
                .customerId(contract.getCustomerId())
                .contractNo(contract.getContractNo())
                .contractName(contract.getContractName())
                .contractType(contract.getContractType())
                .contractAmount(contract.getContractAmount())
                .signedDate(contract.getSignedDate())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .status(contract.getStatus())
                .statusName(contract.getStatus() != null && contract.getStatus() >= 0 && contract.getStatus() <= 3
                        ? new String[]{"待执行", "执行中", "已完成", "已终止"}[contract.getStatus()] : null)
                .description(contract.getDescription())
                .createTime(contract.getCreateTime())
                .updateTime(contract.getUpdateTime())
                .build();
    }

    private CustomerPaymentVO convertPaymentToVO(CrmCustomerPayment payment) {
        return CustomerPaymentVO.builder()
                .id(payment.getId())
                .customerId(payment.getCustomerId())
                .contractId(payment.getContractId())
                .paymentNo(payment.getPaymentNo())
                .paymentAmount(payment.getPaymentAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .description(payment.getDescription())
                .createTime(payment.getCreateTime())
                .updateTime(payment.getUpdateTime())
                .build();
    }

    private CustomerTicketVO convertTicketToVO(CrmCustomerTicket ticket) {
        return CustomerTicketVO.builder()
                .id(ticket.getId())
                .customerId(ticket.getCustomerId())
                .ticketNo(ticket.getTicketNo())
                .ticketTitle(ticket.getTicketTitle())
                .ticketType(ticket.getTicketType())
                .priority(ticket.getPriority())
                .priorityName(ticket.getPriority() != null && ticket.getPriority() >= 1 && ticket.getPriority() <= 3
                        ? new String[]{"", "高", "中", "低"}[ticket.getPriority()] : null)
                .status(ticket.getStatus())
                .statusName(ticket.getStatus() != null && ticket.getStatus() >= 0 && ticket.getStatus() <= 3
                        ? new String[]{"待处理", "处理中", "已解决", "已关闭"}[ticket.getStatus()] : null)
                .description(ticket.getDescription())
                .solution(ticket.getSolution())
                .assigneeUserId(ticket.getAssigneeUserId())
                .assigneeUserName(ticket.getAssigneeUserName())
                .createUserId(ticket.getCreateUserId())
                .createUserName(ticket.getCreateUserName())
                .resolvedTime(ticket.getResolvedTime())
                .closedTime(ticket.getClosedTime())
                .createTime(ticket.getCreateTime())
                .updateTime(ticket.getUpdateTime())
                .build();
    }

    private CustomerLevelVO convertLevelToVO(CrmCustomerLevel level) {
        return CustomerLevelVO.builder()
                .id(level.getId())
                .levelCode(level.getLevelCode())
                .levelName(level.getLevelName())
                .sortOrder(level.getSortOrder())
                .description(level.getDescription())
                .isEnabled(level.getIsEnabled())
                .createTime(level.getCreateTime())
                .updateTime(level.getUpdateTime())
                .build();
    }

    private CustomerTagVO convertTagToVO(CrmCustomerTag tag) {
        return CustomerTagVO.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagColor(tag.getTagColor())
                .tagCategory(tag.getTagCategory())
                .sortOrder(tag.getSortOrder())
                .description(tag.getDescription())
                .isEnabled(tag.getIsEnabled())
                .createTime(tag.getCreateTime())
                .updateTime(tag.getUpdateTime())
                .build();
    }
}
