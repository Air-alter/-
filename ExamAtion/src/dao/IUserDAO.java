package dao;

import java.util.List;

import vo.User;

public interface IUserDAO {
	// 根据主键查找记录
	public User getByUserName(String userName);

	// 根据条件查找
	public List<User> query(User user);

	// 增加
	public int insert(User user);

	// 修改
	public int update(User user);

	// 删除指定主键的记录
	public int delete(String userName);

	// 根据条件删除记录
	public int deleteBatch(User user);
}
