package com.ddy.dianmai.ops.dao;

import java.util.List;
import java.util.Map;

import com.ddy.dianmai.ops.po.Role;
import com.ddy.dianmai.ops.util.Page;

/**
 */
public interface RoleDao {

	/**
	 * 创建角色
	 * 
	 * @param role
	 * @return
	 */
	public Role createRole(Role role);

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	public Role updateRole(Role role);

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 */
	public void deleteRole(Long roleId);

	/**
	 * 查找单一角色
	 * 
	 * @param roleId
	 * @return
	 */
	public Role findOne(Long roleId);

	/**
	 * 查找全部角色
	 * 
	 * @return
	 */
	public List<Role> findAll();
	
	/**
	 * 分页查询
	 * @param pageNo  当前页
	 * @param pageSize  每页条数
	 * @param map  参数
	 * @return
	 */
	public Page<Role> fingPages(int pageNo, int pageSize, Map<String, String> map);
}
