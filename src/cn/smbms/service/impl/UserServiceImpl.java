package cn.smbms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.smbms.dao.UserMapper;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	@Override
	public User queryByUserCodeAndPwd(String userCode, String userPassword) {
		return userMapper.queryByUserCodeAndPwd(userCode, userPassword);
	}

	@Override
	public Integer getTotalCount(String userName, Integer roleId) {
		return userMapper.getTotalCount(userName, roleId);
	}

	@Override
	public List<User> queryByCondition(String userName, Integer roleId,
			Integer from, Integer pageSize) {
		return userMapper.queryByCondition(userName, roleId, from, pageSize);
	}

	@Override
	public List<Role> queryAllRole() {
		return userMapper.queryAllRole();
	}

	//true表示新增成功，false表示新增失败
	@Override
	public boolean addUser(User user) {
		if (userMapper.addUser(user) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public User queryUserById(Integer uid) {
		return userMapper.queryUserById(uid);
	}

	@Override
	public boolean updateUser(User user) {
		if (userMapper.updateUser(user) > 0) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

}
