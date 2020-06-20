package service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dao.IUserDAO;
import util.MybatisUtils;
import vo.User;

public class UserService {
	private IUserDAO userDAO = null;
	private SqlSession sqlSession = null;

	public UserService() {
		this.sqlSession = MybatisUtils.getSqlSession();
		this.userDAO = sqlSession.getMapper(IUserDAO.class);
	}
	
	public User getByUserName(String userName) {
		User user=null;
		try {
			user=this.userDAO.getByUserName(userName);
		} catch (Exception e) {
			user=null;
		}finally {
			this.sqlSession.close();
		}
		return user;
	}
	
	//code为1表示登陆失败，为2表示登陆成功，为0表示存在异常，msg存放描述信息
	public Map<String, Object> checkUser(User user) throws Exception {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			User foundUser = this.userDAO.getByUserName(user.getUserName());
			if (foundUser == null) {// 不存在该用户名
				mapResult.put("code", 1);
				mapResult.put("msg", "用户名不存在！");
			} else {// 存在用户名但密码不正确
				//加密后比较
				if (!foundUser.getPassword().equals(user.getPassword())) {
					mapResult.put("code", 1);
					mapResult.put("msg", "用户名存在,但密码不正确！");
				} else {// 用户名和密码正确
					mapResult.put("code", 2);
					mapResult.put("msg", "用户名和密码均正确！");
					user.setChrName(foundUser.getChrName());
					user.setRole(foundUser.getRole());
				}
			}
		} catch (Exception e) {
			mapResult.put("code", 0);// 登陆存在异常
			mapResult.put("msg", e.getMessage());
		} finally { // 无论是否有异常，都需要关闭数据库连接
			this.sqlSession.close();
		}
		return mapResult;
	}
	
	//修改密码
	public boolean changePassword(User user)  {
		String password=user.getPassword();
		try {
			//user.setPassword(MDSUtil.md5(user.getPassword()));
			if(this.userDAO.update(user)>0)return true;
		    else return false;
		} catch (Exception e) {
			return false;
		} finally { // 无论是否有异常，都需要关闭数据库连接
			this.sqlSession.close();
			user.setPassword(password);
			System.out.println(user.toString());
		}
	}
	
	public Map<String, Object> registered(User user) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			//user.setPassword(MDSUtil.md5(user.getPassword()));
			User foundUser = this.userDAO.getByUserName(user.getUserName());
			if (foundUser == null) {// 不存在该用户名
				if(this.userDAO.insert(user)>0) {
					mapResult.put("code", 2);
					mapResult.put("msg", "插入成功");
				}else {
					mapResult.put("code", 1);
					mapResult.put("msg", "插入失败");
				}
			}else {
				mapResult.put("code", 1);
				mapResult.put("msg", "该用户名已经存在");
			} 
		} catch (Exception e) {
			mapResult.put("code", 0);
			mapResult.put("msg", "插入过程出现错误");
		} finally { // 无论是否有异常，都需要关闭数据库连接
			this.sqlSession.close();
		}
		return mapResult;
	}
}
