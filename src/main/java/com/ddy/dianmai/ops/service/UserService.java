package com.ddy.dianmai.ops.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ddy.dianmai.ops.annotation.Operator;
import com.ddy.dianmai.ops.po.User;
import com.ddy.dianmai.ops.util.Page;

/**
 */
@Operator(name = "用户管理")
public interface UserService {

	/**
	 * 创建用户
	 * 
	 * @param user
	 */
	@Operator(story = "创建用户信息")
	public User createUser(User user);

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	@Operator(story = "更新用户信息")
	public User updateUser(User user);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 */
	@Operator(story = "删除用户信息")
	public void deleteUser(Long userId);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param newPassword
	 */
	@Operator(story = "修改用户密码")
	public void changePassword(Long userId, String newPassword);

	/**
	 * 根据ID得到对象
	 * 
	 * @param userId
	 * @return
	 */
	public User findOne(Long userId);

	/**
	 * 返回所有用户列表
	 * 
	 * @return
	 */
	public List<User> findAll();

	/**
	 * 根据用户名查找用户
	 * 
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);

	/**
	 * 根据用户名查找其角色
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findRoles(String username);

	/**
	 * 根据用户名查找其权限
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findPermissions(String username);

	/**
	 * 分页查询
	 * 
	 * @param pageNo 当前页
	 * @param pageSize 每页条数
	 * @param map 参数
	 * @return
	 */
	public Page<User> fingPages(int pageNo, int pageSize, Map<String, String> map);

}
