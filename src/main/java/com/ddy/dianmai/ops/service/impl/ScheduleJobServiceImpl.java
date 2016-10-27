package com.ddy.dianmai.ops.service.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddy.dianmai.ops.dao.ScheduleJobDao;
import com.ddy.dianmai.ops.dao.ScheduleJobGroupDao;
import com.ddy.dianmai.ops.exception.ScheduleException;
import com.ddy.dianmai.ops.po.ScheduleJob;
import com.ddy.dianmai.ops.po.ScheduleJobGroup;
import com.ddy.dianmai.ops.quartz.SchedulerFactory;
import com.ddy.dianmai.ops.service.ScheduleJobService;
import com.ddy.dianmai.ops.util.BeanUtil;


@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

	@Autowired
	private ScheduleJobDao scheduleJobDao;
	
	@Autowired
	private ScheduleJobGroupDao scheduleJobGroupDao;
	
	private BeanUtil beanUtil = new BeanUtil();
	
	@Override
	public ScheduleJob insert(ScheduleJob scheduleJob) {
		verifySchedule(scheduleJob);//验证是否通过验证
		SchedulerFactory.verifyTrigger(scheduleJob);//验证执行方法是否正确
		return scheduleJobDao.insert(scheduleJob);
	}

	
	@Override
	public ScheduleJob update(ScheduleJob scheduleJob, boolean is_update) {
		if(scheduleJob == null){
			throw new ScheduleException("更新错误,定时器不存在");
		}
		
		verifySchedule(scheduleJob);//验证是否通过验证
		SchedulerFactory.verifyTrigger(scheduleJob);//验证执行方法是否正确
		if(is_update){
			SchedulerFactory.add(scheduleJob, false);//添加到执行队列中
			scheduleJob.setStatus(1);
		}
		return scheduleJobDao.update(scheduleJob, is_update);
	}

	
	
	@Override
	public void delete(ScheduleJob scheduleJob) {
		SchedulerFactory.del(scheduleJob);//删除定时器
		scheduleJobDao.delete(scheduleJob);
	}

	
	@Override
	public void congeal(ScheduleJob scheduleJob) {
		if(scheduleJob == null){
			throw new ScheduleException("停止错误,定时器不存在");
		}
		
		ScheduleJobGroup group = scheduleJobGroupDao.getScheduleJobGroup(scheduleJob.getScheduleJobGroupId());
		if(group.getStatus() == 1){
			throw new ScheduleException("停止失败,请对所在定时器类型进行解冻操作.");
		}
		
		verifySchedule(scheduleJob);//执行方法验证是否通过验证
		SchedulerFactory.stop(scheduleJob);//在队列中停止
		scheduleJob.setStatus(1);
		update(scheduleJob, false);
	}
	
	

	@Override
	public void thaw(ScheduleJob scheduleJob) {
		if(scheduleJob == null){
			throw new ScheduleException("启动错误,定时器不存在");
		}
		
		ScheduleJobGroup group = scheduleJobGroupDao.getScheduleJobGroup(scheduleJob.getScheduleJobGroupId());
		if(group.getStatus() == 1){
			throw new ScheduleException("启动失败,请对所在定时器类型进行解冻操作.");
		}
		
		verifySchedule(scheduleJob);//执行方法验证是否通过验证
		SchedulerFactory.verifyTrigger(scheduleJob);//验证表达式是否正确
		SchedulerFactory.add(scheduleJob, true);//添加到执行队列中
		
		scheduleJob.setStatus(0);
		update(scheduleJob, false);
	}

	
	
	@Override
	public void restart(Long scheduleJobId) {
		ScheduleJob scheduleJob = getScheduleJob(scheduleJobId);
		if(scheduleJob == null){
			throw new ScheduleException("重启错误,定时器不存在");
		}
		
		ScheduleJobGroup group = scheduleJobGroupDao.getScheduleJobGroup(scheduleJob.getScheduleJobGroupId());
		if(group.getStatus() == 1){
			throw new ScheduleException("重启失败,请对所在定时器类型进行解冻操作.");
		}
		SchedulerFactory.reStart(scheduleJob);//重启定时器
		
		scheduleJob.setStatus(0);
		update(scheduleJob, false);
	}

	
	
	@Override
	public void async() {
		List<ScheduleJob> scheduleJobs = findAll();
		for(ScheduleJob scheduleJob : scheduleJobs){
			verifySchedule(scheduleJob);//执行方法验证是否通过验证
			SchedulerFactory.verifyTrigger(scheduleJob);//验证表达式是否正确
			if(scheduleJob.getStatus() == 1){
				SchedulerFactory.add(scheduleJob,false);//添加到执行队列中
			}else if(scheduleJob.getStatus() == 0){
				SchedulerFactory.add(scheduleJob,true);//添加到执行队列中
			}
		}
	}

	
	@Override
	public ScheduleJob getScheduleJob(Long scheduleJobId) {
		return scheduleJobDao.getScheduleJob(scheduleJobId);
	}

	@Override
	public List<ScheduleJob> findAll() {
		return scheduleJobDao.findAll();
	}
	
	public void verifySchedule(ScheduleJob scheduleJob){
		Class<?> class1 = beanUtil.classExists(scheduleJob.getScheduleJobClass());
		if(class1!=null){
			Method method = null;
			if(null!=(method=beanUtil.methodExists(class1,scheduleJob.getScheduleJobMethod()))){
				if(!beanUtil.parameterTypesExists(method)){
					throw new ScheduleException("执行方法中不能存有任何参数.");
				}
			}else{
				throw new ScheduleException("执行方法不存在此调用类中.");
			}
		}else{
			throw new ScheduleException("调用类不存在此系统中.");
		}
	}


	@Override
	public List<ScheduleJob> findScheduleJobByGroupId(Long scheduleJobGroupId) {
		return scheduleJobDao.findScheduleJobByGroupId(scheduleJobGroupId);
	}

}
