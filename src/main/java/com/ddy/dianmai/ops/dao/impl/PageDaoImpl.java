package com.ddy.dianmai.ops.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ddy.dianmai.ops.util.Page;


public class PageDaoImpl {

	
	/**
	 * 返回实体分页列表
	 * @param clazz  实体
	 * @param sql  查询语句
	 * @param pageNo  页码
	 * @param pageSize   每页数量
	 * @param values  参数
	 * @return  返回实体分页列表
	 */
	public static <T> Page<T> queryPages(JdbcTemplate jdbcTemplate, final Class<T> clazz, String sql, int pageNo, int pageSize, Object... values){
		final Page<T> page = new Page<T>(pageNo, pageSize);
		page.setTotalCount(totalCountResult(jdbcTemplate, sql, values));
		sql+=" limit "+page.getFirstIndex()+", "+pageSize;
		List<T> list = queryList(jdbcTemplate, clazz, sql, values);
		page.setList(list);
		return page;
	}
	
	
	
	/**
	 * 返回数据总数（SQL）
	 * @param sql
	 * @param values
	 * @return
	 */
	public static int totalCountResult(JdbcTemplate jdbcTemplate, String sql, Object... values) {
		return jdbcTemplate.queryForList(sql, values).size();
	}
	
	
	
	/**
	 * 查询实体列表
	 * @param <T> 
	 * @param clazz
	 * @param sql
	 * @param values
	 * @return
	 */
	public static <T> List<T> queryList(JdbcTemplate jdbcTemplate, final Class<T> clazz, String sql, Object... values){
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz), values);
	}
	
	
}
