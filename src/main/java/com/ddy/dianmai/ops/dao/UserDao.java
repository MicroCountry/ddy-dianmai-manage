package com.ddy.dianmai.ops.dao;

import java.util.List;
import java.util.Map;

import com.ddy.dianmai.ops.po.User;
import com.ddy.dianmai.ops.util.Page;

/**
 */
public interface UserDao {

	/**
	 * 创建用户
	 * 
	 * @param user
	 */
	public User createUser(User user);

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	public User updateUser(User user);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 */
	public void deleteUser(Long userId);

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
	 * 分页查询
	 * 
	 * @param pageNo 当前页
	 * @param pageSize 每页条数
	 * @param map 参数
	 * @return
	 */
	public Page<User> fingPages(int pageNo, int pageSize, Map<String, String> map);

}
