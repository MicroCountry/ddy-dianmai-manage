package com.ddy.dianmai.ops.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ddy.dianmai.ops.annotation.Operator;
import com.ddy.dianmai.ops.po.Role;
import com.ddy.dianmai.ops.util.Page;

/**
 */
@Operator(name = "角色管理")
public interface RoleService {

	/**
	 * 创建角色
	 * 
	 * @param role
	 * @return
	 */
	@Operator(story = "创建角色")
	public Role createRole(Role role);

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	@Operator(story = "更新角色")
	public Role updateRole(Role role);

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 */
	@Operator(story = "删除角色")
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
	 * 根据角色编号得到角色标识符列表
	 * 
	 * @param roleIds
	 * @return
	 */
	public Set<String> findRoles(Long... roleIds);

	/**
	 * 根据角色编号得到权限字符串列表
	 * 
	 * @param roleIds
	 * @return
	 */
	public Set<String> findPermissions(Long[] roleIds);
	
	/**
	 * 分页查询
	 * @param pageNo  当前页
	 * @param pageSize  每页条数
	 * @param map  参数
	 * @return
	 */
	public Page<Role> fingPages(int pageNo, int pageSize, Map<String, String> map);
}
