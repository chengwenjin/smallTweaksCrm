package com.baserbac.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.PrivateSeaConfigCreateDTO;
import com.baserbac.dto.PrivateSeaConfigQueryDTO;
import com.baserbac.dto.PrivateSeaConfigUpdateDTO;
import com.baserbac.entity.*;
import com.baserbac.mapper.*;
import com.baserbac.vo.PrivateSeaConfigVO;
import com.baserbac.vo.PrivateSeaUsageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateSeaConfigService {

    private final PrivateSeaConfigMapper privateSeaConfigMapper;
    private final CustomerMapper customerMapper;
    private final LeadMapper leadMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    public static final String[] CONFIG_TYPE_NAMES = {"", "全局配置", "角色配置", "用户配置"};
    public static final String[] STATUS_NAMES = {"禁用", "启用"};

    public PageResult<PrivateSeaConfigVO> pageConfigs(PrivateSeaConfigQueryDTO queryDTO) {
        Page<CrmPrivateSeaConfig> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmPrivateSeaConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getConfigType() != null, CrmPrivateSeaConfig::getConfigType, queryDTO.getConfigType())
                .eq(queryDTO.getRoleId() != null, CrmPrivateSeaConfig::getRoleId, queryDTO.getRoleId())
                .eq(queryDTO.getUserId() != null, CrmPrivateSeaConfig::getUserId, queryDTO.getUserId())
                .eq(queryDTO.getIsEnabled() != null, CrmPrivateSeaConfig::getIsEnabled, queryDTO.getIsEnabled())
                .orderByAsc(CrmPrivateSeaConfig::getSortOrder)
                .orderByDesc(CrmPrivateSeaConfig::getCreateTime);

        Page<CrmPrivateSeaConfig> result = privateSeaConfigMapper.selectPage(page, wrapper);

        List<PrivateSeaConfigVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    public PrivateSeaConfigVO getConfigById(Long id) {
        CrmPrivateSeaConfig config = privateSeaConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(config);
    }

    public CrmPrivateSeaConfig getConfigByUser(Long userId) {
        CrmPrivateSeaConfig config = privateSeaConfigMapper.selectOne(
                new LambdaQueryWrapper<CrmPrivateSeaConfig>()
                        .eq(CrmPrivateSeaConfig::getUserId, userId)
                        .eq(CrmPrivateSeaConfig::getIsEnabled, 1)
                        .last("LIMIT 1")
        );
        if (config != null) {
            return config;
        }

        SysUserRole userRole = userRoleMapper.selectOne(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
                        .last("LIMIT 1")
        );
        if (userRole != null && userRole.getRoleId() != null) {
            config = privateSeaConfigMapper.selectOne(
                    new LambdaQueryWrapper<CrmPrivateSeaConfig>()
                            .eq(CrmPrivateSeaConfig::getRoleId, userRole.getRoleId())
                            .eq(CrmPrivateSeaConfig::getIsEnabled, 1)
                            .last("LIMIT 1")
            );
            if (config != null) {
                return config;
            }
        }

        config = privateSeaConfigMapper.selectOne(
                new LambdaQueryWrapper<CrmPrivateSeaConfig>()
                        .eq(CrmPrivateSeaConfig::getConfigType, CrmPrivateSeaConfig.CONFIG_TYPE_GLOBAL)
                        .eq(CrmPrivateSeaConfig::getIsEnabled, 1)
                        .last("LIMIT 1")
        );
        if (config == null) {
            config = new CrmPrivateSeaConfig();
            config.setMaxCustomerCount(50);
            config.setMaxLeadCount(100);
            config.setAutoRecycleDays(30);
        }

        return config;
    }

    public PrivateSeaUsageVO getPrivateSeaUsage(Long userId) {
        CrmPrivateSeaConfig config = getConfigByUser(userId);

        Long customerCount = customerMapper.selectCount(
                new LambdaQueryWrapper<CrmCustomer>()
                        .eq(CrmCustomer::getOwnerUserId, userId)
                        .eq(CrmCustomer::getIsDeleted, 0)
        );

        Long leadCount = leadMapper.selectCount(
                new LambdaQueryWrapper<CrmLead>()
                        .eq(CrmLead::getAssignUserId, userId)
                        .eq(CrmLead::getIsPublic, 0)
                        .eq(CrmLead::getIsDeleted, 0)
        );

        int maxCustomerCount = config.getMaxCustomerCount() != null ? config.getMaxCustomerCount() : 50;
        int maxLeadCount = config.getMaxLeadCount() != null ? config.getMaxLeadCount() : 100;

        return PrivateSeaUsageVO.builder()
                .userId(userId)
                .customerCount(customerCount.intValue())
                .maxCustomerCount(maxCustomerCount)
                .customerRemaining(Math.max(0, maxCustomerCount - customerCount.intValue()))
                .leadCount(leadCount.intValue())
                .maxLeadCount(maxLeadCount)
                .leadRemaining(Math.max(0, maxLeadCount - leadCount.intValue()))
                .autoRecycleDays(config.getAutoRecycleDays() != null ? config.getAutoRecycleDays() : 30)
                .customerUsageRate(calculateUsageRate(customerCount.intValue(), maxCustomerCount))
                .leadUsageRate(calculateUsageRate(leadCount.intValue(), maxLeadCount))
                .build();
    }

    public boolean canAddCustomer(Long userId) {
        PrivateSeaUsageVO usage = getPrivateSeaUsage(userId);
        return usage.getCustomerRemaining() > 0;
    }

    public boolean canAddLead(Long userId) {
        PrivateSeaUsageVO usage = getPrivateSeaUsage(userId);
        return usage.getLeadRemaining() > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createConfig(PrivateSeaConfigCreateDTO createDTO) {
        CrmPrivateSeaConfig config = new CrmPrivateSeaConfig();
        config.setConfigType(createDTO.getConfigType());
        config.setRoleId(createDTO.getRoleId());
        config.setRoleName(createDTO.getRoleName());
        config.setUserId(createDTO.getUserId());
        config.setUserName(createDTO.getUserName());
        config.setMaxCustomerCount(createDTO.getMaxCustomerCount() != null ? createDTO.getMaxCustomerCount() : 50);
        config.setMaxLeadCount(createDTO.getMaxLeadCount() != null ? createDTO.getMaxLeadCount() : 100);
        config.setAutoRecycleDays(createDTO.getAutoRecycleDays() != null ? createDTO.getAutoRecycleDays() : 30);
        config.setIsEnabled(createDTO.getIsEnabled() != null ? createDTO.getIsEnabled() : 1);
        config.setDescription(createDTO.getDescription());
        config.setSortOrder(createDTO.getSortOrder() != null ? createDTO.getSortOrder() : 0);

        privateSeaConfigMapper.insert(config);
        return config.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(PrivateSeaConfigUpdateDTO updateDTO) {
        CrmPrivateSeaConfig config = privateSeaConfigMapper.selectById(updateDTO.getId());
        if (config == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (updateDTO.getConfigType() != null) {
            config.setConfigType(updateDTO.getConfigType());
        }
        if (updateDTO.getRoleId() != null) {
            config.setRoleId(updateDTO.getRoleId());
        }
        if (updateDTO.getRoleName() != null) {
            config.setRoleName(updateDTO.getRoleName());
        }
        if (updateDTO.getUserId() != null) {
            config.setUserId(updateDTO.getUserId());
        }
        if (updateDTO.getUserName() != null) {
            config.setUserName(updateDTO.getUserName());
        }
        if (updateDTO.getMaxCustomerCount() != null) {
            config.setMaxCustomerCount(updateDTO.getMaxCustomerCount());
        }
        if (updateDTO.getMaxLeadCount() != null) {
            config.setMaxLeadCount(updateDTO.getMaxLeadCount());
        }
        if (updateDTO.getAutoRecycleDays() != null) {
            config.setAutoRecycleDays(updateDTO.getAutoRecycleDays());
        }
        if (updateDTO.getIsEnabled() != null) {
            config.setIsEnabled(updateDTO.getIsEnabled());
        }
        if (updateDTO.getDescription() != null) {
            config.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getSortOrder() != null) {
            config.setSortOrder(updateDTO.getSortOrder());
        }

        privateSeaConfigMapper.updateById(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(Long id) {
        CrmPrivateSeaConfig config = privateSeaConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        privateSeaConfigMapper.deleteById(id);
    }

    private Double calculateUsageRate(int used, int max) {
        if (max <= 0) {
            return 0.0;
        }
        BigDecimal rate = BigDecimal.valueOf(used).divide(BigDecimal.valueOf(max), 4, RoundingMode.HALF_UP);
        return rate.multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    private PrivateSeaConfigVO convertToVO(CrmPrivateSeaConfig config) {
        return PrivateSeaConfigVO.builder()
                .id(config.getId())
                .configType(config.getConfigType())
                .configTypeName(config.getConfigType() != null && config.getConfigType() >= 1 && config.getConfigType() <= 3
                        ? CONFIG_TYPE_NAMES[config.getConfigType()] : null)
                .roleId(config.getRoleId())
                .roleName(config.getRoleName())
                .userId(config.getUserId())
                .userName(config.getUserName())
                .maxCustomerCount(config.getMaxCustomerCount())
                .maxLeadCount(config.getMaxLeadCount())
                .autoRecycleDays(config.getAutoRecycleDays())
                .isEnabled(config.getIsEnabled())
                .isEnabledName(config.getIsEnabled() != null ? STATUS_NAMES[config.getIsEnabled()] : null)
                .description(config.getDescription())
                .sortOrder(config.getSortOrder())
                .createTime(config.getCreateTime())
                .updateTime(config.getUpdateTime())
                .build();
    }
}
