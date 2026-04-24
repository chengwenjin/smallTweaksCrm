package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.CustomerTagCreateDTO;
import com.baserbac.entity.CrmCustomerLevel;
import com.baserbac.mapper.CustomerLevelMapper;
import com.baserbac.vo.CustomerLevelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerLevelService {

    private final CustomerLevelMapper customerLevelMapper;

    public List<CustomerLevelVO> getAllLevels() {
        List<CrmCustomerLevel> levels = customerLevelMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerLevel>()
                        .eq(CrmCustomerLevel::getIsEnabled, 1)
                        .orderByAsc(CrmCustomerLevel::getSortOrder)
        );
        return levels.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private CustomerLevelVO convertToVO(CrmCustomerLevel level) {
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
}
