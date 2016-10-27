package com.ddy.dianmai.ops.service;

import java.util.List;

import com.ddy.dianmai.ops.annotation.Operator;
import com.ddy.dianmai.ops.po.ScheduleJob;


/**
 * @ClassName ScheduleJobService.java
 * @Description 定时器服务层接口
 * 
 * @author 1904
 * @version V1.0
 * @Date 2015年7月22日 下午6:02:06
 */
@Operator(name = "定时任务管理")
public interface ScheduleJobService {

	/**
	 * 添加定时器
	 * 
	 * @param scheduleJob
	 */
	@Operator(story = "添加定时任务")
	public ScheduleJob insert(ScheduleJob scheduleJob);

	/**
	 * 修改定时器信息
	 * 
	 * @param scheduleJob
	 *            定时任务
	 * @param is_update
	 *            是否纯粹的更新功能
	 * @return
	 */
	@Operator(story = "修改定时任务")
	public ScheduleJob update(ScheduleJob scheduleJob, boolean is_update);

	/**
	 * 删除定时器信息
	 * 
	 * @param scheduleJob
	 */
	@Operator(story = "删除定时任务")
	public void delete(ScheduleJob scheduleJob);

	/**
	 * 停止定时器
	 * 
	 * @param scheduleJob
	 */
	@Operator(story = "停止定时任务")
	public void congeal(ScheduleJob scheduleJob);

	/**
	 * 启动定时器
	 * 
	 * @param scheduleJob
	 */
	@Operator(story = "启动定时任务")
	public void thaw(ScheduleJob scheduleJob);

	/**
	 * 重启定时器
	 * 
	 * @param scheduleJobId
	 */
	@Operator(story = "重启定时任务")
	public void restart(Long scheduleJobId);

	/**
	 * 同步定时器
	 */
	@Operator(story = "同步定时任务")
	public void async();

	/**
	 * 根据ID返回定时器对象
	 * 
	 * @param scheduleJobId
	 * @return
	 */
	public ScheduleJob getScheduleJob(Long scheduleJobId);

	/**
	 * 定时器列表
	 * 
	 * @return
	 */
	public List<ScheduleJob> findAll();

	/**
	 * 得到定时器组下的定时任务
	 * 
	 * @param scheduleJobGroupId
	 * @return
	 */
	public List<ScheduleJob> findScheduleJobByGroupId(Long scheduleJobGroupId);

}
