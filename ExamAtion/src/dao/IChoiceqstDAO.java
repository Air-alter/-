package dao;

import vo.Choiceqst;

public interface IChoiceqstDAO {
	//获取
	public Choiceqst getById(String topic_id);
	//插入
	public int insert(Choiceqst topic);
	//修改
	public int update(Choiceqst topic);
	//删除指定主键的记录
	public int delete(String topic_id);
}
