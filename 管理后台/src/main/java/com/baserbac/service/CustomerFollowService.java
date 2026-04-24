package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.CustomerFollowCreateDTO;
import com.baserbac.entity.CrmCustomer;
import com.baserbac.entity.CrmCustomerFollow;
import com.baserbac.mapper.CustomerFollowMapper;
import com.baserbac.mapper.CustomerMapper;
import com.baserbac.vo.CustomerFollowVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFollowService {

    private final CustomerFollowMapper customerFollowMapper;
    private final CustomerMapper customerMapper;

    public static final int TYPE_PHONE = 1;
    public static final int TYPE_WECHAT = 2;
    public static final int TYPE_EMAIL = 3;
    public static final int TYPE_VISIT = 4;
    public static final int TYPE_OTHER = 5;

    public static final String[] TYPE_NAMES = {"", "电话", "微信", "邮件", "拜访", "其他"};

    public List<CustomerFollowVO> getFollowsByCustomerId(Long customerId) {
        List<CrmCustomerFollow> follows = customerFollowMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerFollow>()
                        .eq(CrmCustomerFollow::getCustomerId, customerId)
                        .orderByDesc(CrmCustomerFollow::getCreateTime)
                        .last("LIMIT 50")
        );
        return follows.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createFollow(CustomerFollowCreateDTO createDTO, Long userId, String userName) {
        CrmCustomer customer = customerMapper.selectById(createDTO.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        customerFollowMapper.update(
                null,
                new LambdaUpdateWrapper<CrmCustomerFollow>()
                        .eq(CrmCustomerFollow::getCustomerId, createDTO.getCustomerId())
                        .eq(CrmCustomerFollow::getIsLast, 1)
                        .set(CrmCustomerFollow::getIsLast, 0)
        );

        CrmCustomerFollow follow = new CrmCustomerFollow();
        follow.setCustomerId(createDTO.getCustomerId());
        follow.setFollowUserId(userId);
        follow.setFollowUserName(userName);
        follow.setFollowType(createDTO.getFollowType() != null ? createDTO.getFollowType() : TYPE_OTHER);
        follow.setFollowContent(createDTO.getFollowContent());
        follow.setNextFollowTime(createDTO.getNextFollowTime());
        follow.setNextFollowRemark(createDTO.getNextFollowRemark());
        follow.setIsLast(1);

        customerFollowMapper.insert(follow);

        customer.setLastContactTime(LocalDateTime.now());
        customerMapper.updateById(customer);

        return follow.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFollow(Long id) {
        CrmCustomerFollow follow = customerFollowMapper.selectById(id);
        if (follow == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        customerFollowMapper.deleteById(id);
    }

    private CustomerFollowVO convertToVO(CrmCustomerFollow follow) {
        return CustomerFollowVO.builder()
                .id(follow.getId())
                .customerId(follow.getCustomerId())
                .followUserId(follow.getFollowUserId())
                .followUserName(follow.getFollowUserName())
                .followType(follow.getFollowType())
                .followTypeName(follow.getFollowType() != null && follow.getFollowType() >= 1 && follow.getFollowType() <= 5
                        ? TYPE_NAMES[follow.getFollowType()] : null)
                .followContent(follow.getFollowContent())
                .nextFollowTime(follow.getNextFollowTime())
                .nextFollowRemark(follow.getNextFollowRemark())
                .isLast(follow.getIsLast())
                .createTime(follow.getCreateTime())
                .updateTime(follow.getUpdateTime())
                .build();
    }
}
