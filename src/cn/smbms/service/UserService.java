package cn.smbms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;

public interface UserService {

	/**
	 * 通过用户名和用户密码查询用户详细对象信息
	 * @param userCode 用户名
	 * @param userPassword 用户密码
	 * @return 返回查询到的用户详细对象信息
	 */
	User queryByUserCodeAndPwd(String userCode,String userPassword);

	//求用户表的总记录数
	Integer getTotalCount( String userName, Integer roleId);
	
	/**
	 * 按用户名与角色名字进行查询(分页查询)
	 * @param userName  用户名
	 * @param roleId  角色id
	 * @return
	 */
	List<User> queryByCondition(String userName, Integer roleId, Integer from ,Integer pageSize);
	
	
	//查询所有的角色列表，用户绑定用户角色下拉框
	List<Role> queryAllRole();
	
	//新增用户
	boolean addUser(User user);
	
	//通过用户ID查询出用户详情信息
	User queryUserById(Integer uid);
	
	//修改用户信息
	boolean updateUser(User user);
}
