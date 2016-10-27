package com.ddy.dianmai.ops.service;

import java.util.List;

import com.ddy.dianmai.ops.annotation.Operator;
import com.ddy.dianmai.ops.po.ScheduleJobGroup;


/**
 * @ClassName ScheduleJobGroupService.java
 * @Description 定时器分组服务层接口
 * 
 * @author 1904
 * @version V1.0
 * @Date 2015年7月22日 下午6:05:44
 */
@Operator(name = "定时任务分组管理")
public interface ScheduleJobGroupService {

	/**
	 * 根据组名得到对象（组名唯一）
	 * 
	 * @param scheduleJobGroupName
	 * @return
	 */
	public ScheduleJobGroup getScheduleJobGroup(String scheduleJobGroupName);

	/**
	 * 根据ID得到对象
	 * 
	 * @param scheduleJobGroupId
	 * @return
	 */
	public ScheduleJobGroup getScheduleJobGroup(Long scheduleJobGroupId);

	/**
	 * 组列表
	 * 
	 * @return
	 */
	public List<ScheduleJobGroup> getScheduleJobGroups();

	/**
	 * 添加
	 * 
	 * @param scheduleJobGroup
	 */
	@Operator(story = "添加定时任务组")
	public ScheduleJobGroup insert(ScheduleJobGroup scheduleJobGroup);

	/**
	 * 更新
	 * 
	 * @param scheduleJobGroup
	 */
	@Operator(story = "更新定时任务组")
	public void update(ScheduleJobGroup scheduleJobGroup);

	/**
	 * 删除
	 * 
	 * @param scheduleJobGroup
	 */
	@Operator(story = "删除定时任务组")
	public void delete(ScheduleJobGroup scheduleJobGroup);

}
