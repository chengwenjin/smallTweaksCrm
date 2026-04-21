package com.baserbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baserbac.entity.CrmLead;
import org.apache.ibatis.annotations.Mapper;

/**
 * 线索 Mapper
 */
@Mapper
public interface LeadMapper extends BaseMapper<CrmLead> {

}
