package com.ddy.dianmai.ops.service;

import java.util.List;
import java.util.Map;

import com.ddy.dianmai.ops.annotation.Operator;
import com.ddy.dianmai.ops.po.OperationLog;
import com.ddy.dianmai.ops.util.Page;


/**
 * @ClassName OperationService.java
 * @Description 日志记录接口
 * 
 */
@Operator(name = "操作日志管理")
public interface OperationLogService {

	/**
	 * 添加记录
	 * 
	 * @param operation
	 * @return
	 */
	public OperationLog insert(OperationLog operation);

	/**
	 * 删除记录
	 * 
	 * @param id
	 */
	@Operator(story = "删除日志")
	public void del(Long id);

	/**
	 * 查询记录
	 * 
	 * @return
	 */
	public List<OperationLog> findOperations(Map<String, String> map);

	
	/**
	 * 根据ID查找对象
	 * @param id
	 * @return
	 */
	public OperationLog getOperationLog(Long id);
	
	
	/**
	 * 分页查询
	 * @param pageNo  当前页
	 * @param pageSize  每页条数
	 * @param map  参数
	 * @return
	 */
	public Page<OperationLog> fingPages(int pageNo, int pageSize, Map<String, String> map);
}
