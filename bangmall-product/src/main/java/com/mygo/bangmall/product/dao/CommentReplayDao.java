package com.mygo.bangmall.product.dao;

import com.mygo.bangmall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 15:33:07
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
