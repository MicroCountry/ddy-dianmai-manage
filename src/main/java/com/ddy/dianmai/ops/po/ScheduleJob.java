package com.ddy.dianmai.ops.po;

import java.io.Serializable;


/**
 * @ClassName ScheduleJob.java
 * @Description 定时器
 *
 */
public class ScheduleJob implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 任务id */
	private Long scheduleJobId;

	/** 任务名称 */
	private String scheduleJobName;

	/** 任务分组 */
	private Long scheduleJobGroupId;

	/** 任务运行时间表达式 */
	private String scheduleJobCronExpression;

	/** 任务描述 */
	private String scheduleJobDescription;

	private String scheduleJobClass;

	private String scheduleJobMethod;
	
	public Integer status;
	
	public Long createTime;
	
	public String result;
	
	public Long getScheduleJobId() {
		return scheduleJobId;
	}

	public void setScheduleJobId(Long scheduleJobId) {
		this.scheduleJobId = scheduleJobId;
	}

	public String getScheduleJobName() {
		return scheduleJobName;
	}

	public void setScheduleJobName(String scheduleJobName) {
		this.scheduleJobName = scheduleJobName;
	}

	public Long getScheduleJobGroupId() {
		return scheduleJobGroupId;
	}

	public void setScheduleJobGroupId(Long scheduleJobGroupId) {
		this.scheduleJobGroupId = scheduleJobGroupId;
	}

	public String getScheduleJobMethod() {
		return scheduleJobMethod;
	}

	public void setScheduleJobMethod(String scheduleJobMethod) {
		this.scheduleJobMethod = scheduleJobMethod;
	}

	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getScheduleJobCronExpression() {
		return scheduleJobCronExpression;
	}

	public void setScheduleJobCronExpression(String scheduleJobCronExpression) {
		this.scheduleJobCronExpression = scheduleJobCronExpression;
	}

	public String getScheduleJobDescription() {
		return scheduleJobDescription;
	}

	public String getScheduleJobClass() {
		return scheduleJobClass;
	}

	public void setScheduleJobClass(String scheduleJobClass) {
		this.scheduleJobClass = scheduleJobClass;
	}

	public void setScheduleJobDescription(String scheduleJobDescription) {
		this.scheduleJobDescription = scheduleJobDescription;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
