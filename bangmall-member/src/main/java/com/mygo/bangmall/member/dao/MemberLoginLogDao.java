package com.mygo.bangmall.member.dao;

import com.mygo.bangmall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:24:57
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
