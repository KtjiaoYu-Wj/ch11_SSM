package cn.smbms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;

public interface UserMapper {

	/**
	 * 通过用户名和用户密码查询用户详细对象信息
	 * @param userCode 用户名
	 * @param userPassword 用户密码
	 * @return 返回查询到的用户详细对象信息
	 */
	User queryByUserCodeAndPwd(@Param("userCode") String userCode, 
			@Param("userPassword") String userPassword);
	
	
	/**
	 * 求用户表的总记录数
	 * @param userName 按用户名模糊查询 
	 * @param roleId 按照用户角色ID精确查询
	 * @return 查询后的用户总记录数
	 */
	Integer getTotalCount(
			@Param("userName") String userName,
			@Param("roleId") Integer roleId);
	
	/**
	 * 按用户名与角色名字进行查询(分页查询)
	 * @param userName  用户名
	 * @param roleId  角色id
	 * @return 返回分页查询到的用户列表信息
	 */
	List<User> queryByCondition(
			@Param("userName") String userName,
			@Param("roleId") Integer roleId,
			@Param("from") Integer from ,
			@Param("pageSize") Integer pageSize);
	
	
	//查询所有的角色列表，用户绑定用户角色下拉框
	List<Role> queryAllRole();

	//新增用户
	int addUser(User user);
	
	//通过用户ID查询出用户详情信息
	User queryUserById(Integer uid);
	
	//修改用户信息
	int updateUser(User user);
	
	
}