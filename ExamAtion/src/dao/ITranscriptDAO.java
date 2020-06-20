package dao;

import java.util.List;

import com.mysql.cj.Query;

import vo.Transcript;

public interface ITranscriptDAO {
	//查询成绩
	public List<Transcript> query(Transcript transcript);
	//添加成绩
	public int insert(Transcript transcript);
}
