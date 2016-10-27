package com.ddy.dianmai.ops.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ddy.dianmai.ops.dao.ScheduleJobGroupDao;
import com.ddy.dianmai.ops.exception.ScheduleException;
import com.ddy.dianmai.ops.po.ScheduleJobGroup;


@Repository
public class ScheduleJobGroupDaoImpl extends PageDaoImpl implements ScheduleJobGroupDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public ScheduleJobGroup getScheduleJobGroup(String scheduleJobGroupName) {
		String sql = "select * from sys_schedulejob_group where scheduleJobGroupName=?";
		List<ScheduleJobGroup> scheduleJobGroups = jdbcTemplate.query(sql,
				BeanPropertyRowMapper.newInstance(ScheduleJobGroup.class),
				scheduleJobGroupName);
		if (scheduleJobGroups.size() == 0) {
			return null;
		}
		return scheduleJobGroups.get(0);
	}

	@Override
	public ScheduleJobGroup getScheduleJobGroup(Long scheduleJobGroupId) {
		String sql = "select * from sys_schedulejob_group where scheduleJobGroupId=?";
		List<ScheduleJobGroup> scheduleJobGroups = jdbcTemplate.query(sql,
				BeanPropertyRowMapper.newInstance(ScheduleJobGroup.class),
				scheduleJobGroupId);
		if (scheduleJobGroups.size() == 0) {
			return null;
		}
		return scheduleJobGroups.get(0);
	}

	@Override
	public List<ScheduleJobGroup> getScheduleJobGroups() {
		String sql = "select * from sys_schedulejob_group";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ScheduleJobGroup.class));
	}

	@Override
	public ScheduleJobGroup insert(ScheduleJobGroup scheduleJobGroup) {
		try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            SqlParameterSource paramSource = new BeanPropertySqlParameterSource(scheduleJobGroup);
            String sql = "insert into sys_schedulejob_group(scheduleJobGroupId, scheduleJobGroupName, scheduleJobGroupDescription, status, createTime) " +
                         "values (:scheduleJobGroupId ,:scheduleJobGroupName ,:scheduleJobGroupDescription ,:status ,:createTime)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
            scheduleJobGroup.setScheduleJobGroupId(keyHolder.getKey().longValue());
            return scheduleJobGroup;
        }catch (DuplicateKeyException e){
        	throw new DuplicateKeyException("定时器组名称已经存在");
        }catch (DataIntegrityViolationException e){
        	e.printStackTrace();
        	throw new DataIntegrityViolationException("必填字段为空");
        }catch (Exception e) {
        	throw new ScheduleException("添加定时器组失败");
		}

	}

	@Override
	public void update(ScheduleJobGroup scheduleJobGroup) {
		String sql = "update sys_schedulejob_group set scheduleJobGroupName=?, scheduleJobGroupDescription=?, status=? where scheduleJobGroupId=?";
		jdbcTemplate.update(sql, scheduleJobGroup.getScheduleJobGroupName(),
				scheduleJobGroup.getScheduleJobGroupDescription(), 
				scheduleJobGroup.getStatus(),
				scheduleJobGroup.getScheduleJobGroupId());
	}

	@Override
	public void delete(ScheduleJobGroup scheduleJobGroup) {
		String sql = "delete from sys_schedulejob_group where scheduleJobGroupId=?";
		jdbcTemplate.update(sql, scheduleJobGroup.getScheduleJobGroupId());

	}
}
