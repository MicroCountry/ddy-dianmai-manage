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

import com.ddy.dianmai.ops.dao.ScheduleJobDao;
import com.ddy.dianmai.ops.exception.ScheduleException;
import com.ddy.dianmai.ops.po.ScheduleJob;

@Repository
public class ScheduleJobDaoImpl extends PageDaoImpl implements ScheduleJobDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ScheduleJob insert(ScheduleJob scheduleJob) {
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            SqlParameterSource paramSource = new BeanPropertySqlParameterSource(scheduleJob);
            String sql = "insert into sys_schedulejob(scheduleJobId, scheduleJobName, scheduleJobGroupId, status, scheduleJobDescription, createTime, scheduleJobCronExpression, scheduleJobMethod, scheduleJobClass) " +
                         "values (:scheduleJobId ,:scheduleJobName ,:scheduleJobGroupId ,:status ,:scheduleJobDescription ,:createTime ,:scheduleJobCronExpression, :scheduleJobMethod, :scheduleJobClass)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
            scheduleJob.setScheduleJobId(keyHolder.getKey().longValue());
            return scheduleJob;
        }catch (DuplicateKeyException e){
        	throw new DuplicateKeyException("定时器名称已经存在");
        }catch (DataIntegrityViolationException e){
        	e.printStackTrace();
        	throw new DataIntegrityViolationException("必填字段为空");
        }catch (Exception e) {
        	throw new ScheduleException("添加定时器失败");
		}
	}

	
	@Override
	public ScheduleJob update(ScheduleJob scheduleJob, boolean is_update) {
		try{
			String sql = "update sys_schedulejob set scheduleJobName=?, scheduleJobGroupId=?, status=?, scheduleJobDescription=?, scheduleJobCronExpression=?, scheduleJobMethod=?, scheduleJobClass=? where scheduleJobId=?";
			jdbcTemplate.update(sql, scheduleJob.getScheduleJobName(),
					scheduleJob.getScheduleJobGroupId(), 
					scheduleJob.getStatus(),
					scheduleJob.getScheduleJobDescription(),
					scheduleJob.getScheduleJobCronExpression(),
					scheduleJob.getScheduleJobMethod(),
					scheduleJob.getScheduleJobClass(),
					scheduleJob.getScheduleJobId());
			return scheduleJob;
		}catch(Exception e){
			throw new ScheduleException("更新定时器状态失败");
		}
	}

	@Override
	public void delete(ScheduleJob scheduleJob) {
		String sql = "delete from sys_schedulejob where scheduleJobId=?";
		jdbcTemplate.update(sql, scheduleJob.getScheduleJobId());
	}

	@Override
	public ScheduleJob getScheduleJob(Long scheduleJobId) {
		String sql = "select * from sys_schedulejob where scheduleJobId=?";
		List<ScheduleJob> scheduleJobs = jdbcTemplate.query(sql,
				BeanPropertyRowMapper.newInstance(ScheduleJob.class),
				scheduleJobId);
		if (scheduleJobs.size() == 0) {
			return null;
		}
		return scheduleJobs.get(0);
	}


	@Override
	public List<ScheduleJob> findAll() {
		String sql = "select * from sys_schedulejob";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ScheduleJob.class));
	}


	@Override
	public List<ScheduleJob> findScheduleJobByGroupId(Long scheduleJobGroupId) {
		String sql = "select * from sys_schedulejob where scheduleJobGroupId=?";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ScheduleJob.class), scheduleJobGroupId);
	}

}
