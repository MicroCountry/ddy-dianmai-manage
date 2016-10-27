package com.ddy.dianmai.ops.dao;

import java.util.List;

import com.ddy.dianmai.ops.po.Resource;


/**
 */
public interface ResourceDao {

	/**
	 * 增加资源
	 * 
	 * @param resource
	 * @return
	 */
	public Resource createResource(Resource resource);

	/**
	 * 更新资源
	 * 
	 * @param resource
	 * @return
	 */
	public Resource updateResource(Resource resource);

	/**
	 * 删除资源
	 * 
	 * @param resourceId
	 */
	public void deleteResource(Long resourceId);

	/**
	 * 查找单一资源
	 * 
	 * @param resourceId
	 * @return
	 */
	public Resource findOne(Long resourceId);

	/**
	 * 查找全部资源
	 * 
	 * @return
	 */
	public List<Resource> findAll();
	
	
	/**
	 * 查找子菜单
	 * @param parent_id
	 * @return
	 */
	public List<Resource> findMenus(Long parent_id);

}
