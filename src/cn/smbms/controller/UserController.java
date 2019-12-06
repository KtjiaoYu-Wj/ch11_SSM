package cn.smbms.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.smbms.constant.Constants;
import cn.smbms.pojo.Page;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.UserService;

import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	//查询用户详情
	@RequestMapping("/queryUserById/{uid}")
	public String queryUserById(@PathVariable Integer uid, Model model) {
		User user = userService.queryUserById(uid);
		model.addAttribute("user", user);
		return "userview";  //跳转到用户详情信息页面
	}
	//负责跳转到修改页面，在跳转到修改页面之前，通过用户ID将用户的详情信息查询出来后在修改页面中进行显示
	@RequestMapping(value = "/toUserModify/{uid}", method = RequestMethod.GET)
	public String toUserModify(@PathVariable Integer uid,Model model) {
		User user = userService.queryUserById(uid);  //通过用户ID将用户的详情信息查询出来
		List<Role> roleList = userService.queryAllRole();  //查询所有的用户角色列表
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("user", user);
		return "usermodify";  //跳转到修改页面
	}
	
	//保存用户的修改操作
	@RequestMapping(value = "/userModifySave", method = RequestMethod.POST)
	public String userModifySave(User user, HttpSession session) {
		Integer uid = ((User)session.getAttribute(Constants.USER_SESSION)).getId();
		user.setModifyBy(uid);  //将当前登录用户的ID作为本次修改用户信息的ID、
		user.setModifyDate(new Date());  //修改时间
		if (userService.updateUser(user)) {
			return "redirect:/user/userlist"; //修改成功后，跳转到用户列表页面
		} else {
			return "redirect:/user/toUserModify?uid=" + user.getId(); //修改失败，又跳转到修改页面
		}
	}
	
	//method = RequestMethod.GET 请求方式是GET请求
	//负责跳转到useradd.jsp页面，即用户添加页面
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public String addUser(Model model) {
		//在跳转到useradd.jsp用户新增页面之前将所有的角色信息给查询出来，并在useradd.jsp页面中显示
		List<Role> roleList = userService.queryAllRole();
		model.addAttribute("roleList", roleList);
		return "useradd";
	}
	
	//用于保存用户的新增记录
	@RequestMapping(value = "/userSave", method = RequestMethod.POST)
	public String userSave(
			User user,
			HttpSession session,HttpServletRequest request,
			@RequestParam(value = "a_idPicPath", required = false) MultipartFile attachFile) {
		
		String idPicPath = "";  //保存文件的相对路径
		//判断当前文件是否为空
		if (!attachFile.isEmpty()) {
			//说明用户上传文件
			
			//设置上传的文件的保存地址， path的格式为：
			//本人的tomcat路径为： G:\\tool\\01-zip\\apache-tomcat-7.0.62
			//session.getServletContext().getRealPath("statics")得到的路径格式：
			//G:\\tool\\01-zip\\apache-tomcat-7.0.62\\ch11-SSM\\statics
			//File.separator 取到的是分隔符号\\
			String path = session.getServletContext().getRealPath("statics" + 
					File.separator + "uploadfile");
			//以后项目开发完后，整个项目要部署到linux操作系统 。  linux操作系统它是没有盘符
			String oldFileName = attachFile.getOriginalFilename();  //得到上传文件的原文件名
			String suffix = FilenameUtils.getExtension(oldFileName);  //得到文件名里的后缀。 commons-lang包
			if (attachFile.getSize() > 5000000) {
				//判断当前文件的尺寸是否大于500KB
				request.setAttribute("uploadFileError", "您上传的文件尺寸超过500KB，上传失败");
				return "useradd"; 
			} else if (suffix.equalsIgnoreCase("jpg") || 
					   suffix.equalsIgnoreCase("png") ||
					   suffix.equalsIgnoreCase("jpeg") ||
					   suffix.equalsIgnoreCase("pneg")) {
				//说明当前文件尺寸合适、后缀合法。
				//重构文件名（保证文件名的唯一性）
				//从1977年开始到当前的百万级别毫秒数，是Long类型，如1574393039155
				String fileName = System.currentTimeMillis() 
						+ RandomUtils.nextInt(100000000) + "_Personal.jpg";
				//根据文件名和文件的保存路径创建一个File对象
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {  //判断当前文件保存路径是否存在，如果不存在就新建
					targetFile.mkdirs();  //创建新目录
				}
				
				try {
					attachFile.transferTo(targetFile);  //执行文件上传
				} catch (Exception e) {
					request.setAttribute("uploadFileError", "文件上传失败！" + e.getMessage());
					return "useradd"; 
				} 
				//request.getContextPath() 拿到上下文件路径即当前项目的名字
				idPicPath = request.getContextPath() + File.separator + "statics" 
						+ File.separator + "uploadfile" + File.separator + fileName;
				 //idPicPath的值格式如：/ch11-SSM/statics/uploadfile/1557846017276_Personal.jpg
				
			} else {
				request.setAttribute("uploadFileError", "您上传的文件格式不正确！");
				return "useradd"; 
			}
		}
		
		//通过Session作用域要得到当前登录用户，以当前登录用户的ID作为本次创建者的ID， createBy字段是创建者ID
		Integer userId = ((User)session.getAttribute(Constants.USER_SESSION)).getId();//得到当前登录用户的ID
		user.setCreatedBy(userId);  //将当前登录用户的ID作为创建者ID
		user.setCreationDate(new Date());  //将当前时间作为用户创建的时间
		user.setIdPicPath(idPicPath);  //设置上传文件的相对路径
		if (userService.addUser(user)) {
			return "redirect:/user/userlist"; //新增成功后，跳转到用户列表页面
		} else {
			return "useradd";  //新增失败后，直接又跳出新增页面，继续进行新增操作
		}
	}
	
	
	/**
	 * 当用户在首页中点击 "用户管理"按扭，调用下面的方法，实现分页查询用户列表信息
	 * @param queryname 从userlist.jsp页面中传递过来的"用户名"参数
	 * @param queryUserRole 从userlist.jsp页面中传递过来的"用户角色ID"参数
	 * @param pageIndex 从userlist.jsp页面中传递过来的"当前页码"参数
	 * @param model Model对象是用于保存数据
	 * @return 返回查询到的分页用户列表信息
	 */
	@RequestMapping("/userlist")
	public String userList(
			@RequestParam(value = "queryname", required = false) String queryname,
			@RequestParam(value = "queryUserRole", required = false) String queryUserRole,
			@RequestParam(value = "pageIndex", required = false) String pageIndex,Model model) {
		Integer roleId = 0;  //用于保存角色ID
		Integer currPageNo = 1;  //用于保存当前页码
		//1、先处理请求参数
		if (!StringUtils.isNullOrEmpty(queryUserRole)) {
			roleId = Integer.valueOf(queryUserRole);
		}
		if (!StringUtils.isNullOrEmpty(pageIndex)) {
			currPageNo = Integer.valueOf(pageIndex);
		}
		
		//2、调用service层的查询总记录数的方法，获取总的记录数
		Integer totalCount = userService.getTotalCount(queryname, roleId);
		Page page = new Page();
		page.setCurrPageNo(currPageNo);
		page.setPageSize(5);  //每页显示5行数据
		page.setTotalCount(totalCount);  //总记录
		Integer from = (page.getCurrPageNo() - 1) * page.getPageSize(); //获取分页的起始位置
		//3、调用service层方法，实分页查询用户列表信息
		List<User> userList = userService.queryByCondition(queryname, roleId, from, page.getPageSize());
		
		//4、将所有用户角色列表信息查询 
		List<Role> roleList = userService.queryAllRole();
		
		//5、保存数据，供userlist.jsp页面显示
		model.addAttribute("page", page); //将分页实体类对象保存到model中，供userlist.jsp页面显示数据
		model.addAttribute("userList", userList);  //保存用户列表信息
		model.addAttribute("queryname", queryname);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("roleList", roleList);//用户角色列表
		
		//6、跳转页面，返回逻辑视图名
		return "userlist";
	}
	
	// http://localhost:8080/ch10_SSM/user/login
	//负责跳转到登录页面
	@RequestMapping("/login")
	public String login(){
		return "login";  //返回逻辑视图名
	}
	
	/**
	 * 定义一个处理登录请求
	 * @param userCode 用户编码
	 * @param userPassword 用户密码
	 * @return 如果登录成功跳到首页，如果登录失败再跳回到登录页面继续登录 
	 */
	@RequestMapping("/doLogin")
	public String doLogin(@RequestParam("userCode")String userCode, 
			@RequestParam("userPassword")String userPassword, 
			HttpSession session,HttpServletRequest request) {
		User user = userService.queryByUserCodeAndPwd(userCode, userPassword);
		if (user != null) {
			//登录成功后，要将登录用户名保存到Session作用域
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/user/main";  //redirect: 重定向， 浏览器客户端向服务器发送了一次新的请求
		} else {
			request.setAttribute("error", "用户名或密码不正确!");
		}
		return "login";  //登录 失败又跳回登录页面，继续登录 
  	}
	
	//跳转到首页
	@RequestMapping("/main")
	public String main() {
		return "frame";  //跳转到 /WEB-INF/jsp/frame.jsp页面
	}
}