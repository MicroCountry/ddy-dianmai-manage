package com.ddy.dianmai.ops.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import com.ddy.dianmai.ops.exception.ScheduleException;
import com.ddy.dianmai.ops.po.ScheduleJob;
import com.ddy.dianmai.ops.util.SpringUtils;

public class SchedulerFactory {
	
	public static Scheduler scheduler = SpringUtils.getBean("scheduler");
	
	/**
	 * 验证定时器表达式
	 * @param scheduleJob
	 * @return
	 */
	public static CronScheduleBuilder verifyTrigger(ScheduleJob scheduleJob){
		CronScheduleBuilder scheduleBuilder= null;
		try {
			scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getScheduleJobCronExpression());
		} catch (Exception e) {
			throw new ScheduleException("表达式不正确,请重新输入.");
		}
		return scheduleBuilder;
	}
	
	
	/**
	 * 定时器所有列表
	 * @return
	 */
	public static List<ScheduleJob> getAllJob() {
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					ScheduleJob scheduleJob = new ScheduleJob();
					scheduleJob.setScheduleJobId(Long.parseLong(jobKey.getName()));
					scheduleJob.setScheduleJobGroupId(Long.parseLong(jobKey.getGroup()));
					scheduleJob.setScheduleJobDescription(String.valueOf(trigger.getKey()));
					
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					scheduleJob.setStatus(triggerState.name().equals("NORMAL")?0:1);
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						scheduleJob.setScheduleJobCronExpression(cronExpression);
					}
					jobList.add(scheduleJob);
				}
			}
		} catch (Exception e) {
			throw new ScheduleException("获取所有定时任务失败");
		}
		return jobList;
	}
	
	
	
	/**
	 * 获取所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public static List<ScheduleJob> getRunningJob(){
		try{
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				ScheduleJob job = new ScheduleJob();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				job.setScheduleJobId(Long.parseLong(jobKey.getName()));
				job.setScheduleJobGroupId(Long.parseLong(jobKey.getGroup()));
				job.setScheduleJobDescription("触发器:" + trigger.getKey());
				
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setStatus(triggerState.name().equals("NORMAL")?0:1);
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setScheduleJobCronExpression(cronExpression);
				}
				jobList.add(job);
			}
			return jobList;
		}catch(Exception e){
			throw new ScheduleException("获取所有正在运行的任务失败");
		}
	}
	
	
	/**
	 * 添加定时器
	 * @param scheduleJob  定时任务
	 * @param start  是否立即执行  true-是   false-否
	 */
	public synchronized static void add(ScheduleJob scheduleJob, boolean start){
		
		TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getScheduleJobGroupId()));//组合名称（定时器名称+分组名称）
		
		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger=null;
		try {
			trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			throw new ScheduleException("表达式不正确,请重新输入.");
		}
		
		// 不存在，创建一个
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
					.withIdentity(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getScheduleJobGroupId())).build();
			jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
			
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder= verifyTrigger(scheduleJob);
			
			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(scheduleJob.getScheduleJobId()), scheduleJob.getScheduleJobGroupId().toString()).withSchedule(scheduleBuilder).build();
			
			try {
				scheduler.scheduleJob(jobDetail, trigger);
				if(!start){
					stop(scheduleJob);
				}
			} catch (SchedulerException e) {
				throw new ScheduleException(e);
			}
		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = verifyTrigger(scheduleJob);
			
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			
			// 按新的trigger重新设置job执行
			try {
				scheduler.rescheduleJob(triggerKey, trigger);
				if(!start){
					stop(scheduleJob);
				}
			} catch (SchedulerException e) {
				throw new ScheduleException("trigger执行失败.");
			}
		}
	}
	
	
	
	/**
	 * 停止定时器
	 * @param scheduleJob
	 */
	public static void stop(ScheduleJob scheduleJob) {
		try {
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getScheduleJobGroupId()));
			scheduler.pauseJob(jobKey);
		} catch (Exception e) {
			throw new ScheduleException("定时器停止失败.");
		}
	}
	
	
	/**
	 * 重启定时器
	 * @param scheduleJob
	 */
	public static void reStart(ScheduleJob scheduleJob){
		try {
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getScheduleJobGroupId()));
			scheduler.resumeJob(jobKey);
		} catch (Exception e) {
			throw new ScheduleException("定时器重启失败.");
		}
	}
	
	
	/**
	 * 删除定时器
	 * @param scheduleJob
	 */
	public static void del(ScheduleJob scheduleJob) {
		try {
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getScheduleJobGroupId()));
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			throw new ScheduleException("定时器删除失败.");
		}
	}
	
	
	/**
	 * 立即执行任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runAJobNow(ScheduleJob scheduleJob){
		try{
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getScheduleJobGroupId()));
			scheduler.triggerJob(jobKey);
		} catch (Exception e) {
			throw new ScheduleException("立即执行任务失败.");
		}
	}
	
}
