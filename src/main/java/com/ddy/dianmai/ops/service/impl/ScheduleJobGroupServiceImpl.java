package com.ddy.dianmai.ops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddy.dianmai.ops.dao.ScheduleJobGroupDao;
import com.ddy.dianmai.ops.po.ScheduleJobGroup;
import com.ddy.dianmai.ops.service.ScheduleJobGroupService;


@Service("scheduleJobGroupService")
public class ScheduleJobGroupServiceImpl implements ScheduleJobGroupService {

	@Autowired
	private ScheduleJobGroupDao scheduleJobGroupDao;
	
	@Override
	public ScheduleJobGroup getScheduleJobGroup(String scheduleJobGroupName) {
		return scheduleJobGroupDao.getScheduleJobGroup(scheduleJobGroupName);
	}

	@Override
	public ScheduleJobGroup getScheduleJobGroup(Long scheduleJobGroupId) {
		return scheduleJobGroupDao.getScheduleJobGroup(scheduleJobGroupId);
	}

	@Override
	public List<ScheduleJobGroup> getScheduleJobGroups() {
		return scheduleJobGroupDao.getScheduleJobGroups();
	}

	@Override
	public ScheduleJobGroup insert(ScheduleJobGroup scheduleJobGroup) {
		return scheduleJobGroupDao.insert(scheduleJobGroup);
	}

	@Override
	public void update(ScheduleJobGroup scheduleJobGroup) {
		scheduleJobGroupDao.update(scheduleJobGroup);
	}

	@Override
	public void delete(ScheduleJobGroup scheduleJobGroup) {
		scheduleJobGroupDao.delete(scheduleJobGroup);
	}
}
