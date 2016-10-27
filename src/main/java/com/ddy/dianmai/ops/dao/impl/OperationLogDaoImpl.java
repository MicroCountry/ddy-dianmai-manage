package com.ddy.dianmai.ops.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ddy.dianmai.ops.dao.OperationLogDao;
import com.ddy.dianmai.ops.exception.ScheduleException;
import com.ddy.dianmai.ops.po.OperationLog;
import com.ddy.dianmai.ops.util.Page;


@Repository
public class OperationLogDaoImpl extends PageDaoImpl implements OperationLogDao {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public OperationLog insert(OperationLog operationLog) {
		try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            SqlParameterSource paramSource = new BeanPropertySqlParameterSource(operationLog);
            String sql = "insert into sys_operation_log(id, userId, story, createTime, afterContent) " +
            		"values (:id ,:userId ,:story ,:createTime ,:afterContent)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
            operationLog.setId(keyHolder.getKey().longValue());
            return operationLog;
        }catch (Exception e) {
        	throw new ScheduleException("添加操作记录失败");
		}
	}

	@Override
	public void del(Long id) {
		String sql = "delete from sys_operation_log where id=?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public List<OperationLog> findOperations(Map<String, String> map) {
		String sql = "select * from sys_operation_log where 1=1";
		if(map != null){
			if(StringUtils.isNotBlank(map.get("userId"))){
				sql += " and userId="+map.get("userId");
			}
			if(StringUtils.isNotBlank(map.get("story"))){
				sql += " and story like '%"+map.get("story")+"%'";	
			}
			String beginTime = map.get("beginTime");
			String endTime = map.get("endTime");
			if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)){
				sql += " and createTime >= "+beginTime+" and createTime <= "+endTime+"";
			}
		}
		sql += " order by createTime desc";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(OperationLog.class));
	}

	@Override
	public OperationLog getOperationLog(Long id) {
		String sql = "select * from sys_operation_log where id=?";
		List<OperationLog> operationLogs = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(OperationLog.class), id);
		if (operationLogs.size() == 0) {
			return null;
		}
		return operationLogs.get(0);
	}

	@Override
	public Page<OperationLog> fingPages(int pageNo, int pageSize, Map<String, String> map) {
		String sql = "select * from sys_operation_log where 1=1";
		if(map != null){
			if(StringUtils.isNotBlank(map.get("userId"))){
				sql += " and userId="+map.get("userId");
			}
			if(StringUtils.isNotBlank(map.get("story"))){
				sql += " and story like '%"+map.get("story")+"%'";	
			}
			String beginTime = map.get("beginTime");
			String endTime = map.get("endTime");
			if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)){
				sql += " and createTime >= "+beginTime+" and createTime <= "+endTime+"";
			}
		}
		sql += " order by createTime desc";
		Page<OperationLog> page = super.queryPages(jdbcTemplate, OperationLog.class, sql, pageNo, pageSize);
		return page;
	}

}
