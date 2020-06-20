package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dao.ITranscriptDAO;
import util.MybatisUtils;
import vo.Transcript;

public class TranscriptService {
	private ITranscriptDAO transcriptDAO = null;
	private SqlSession sqlSession = null;

	public TranscriptService() {
		this.sqlSession = MybatisUtils.getSqlSession();
		this.transcriptDAO = sqlSession.getMapper(ITranscriptDAO.class);
	}
	
	public List<Transcript> query(Transcript transcript){
		List<Transcript> list=null;
		try {
			list=this.transcriptDAO.query(transcript);
		} catch (Exception e) {
			list=null;
		}finally {
			this.sqlSession.close();
		}
		return list;
	}
	
	public Map<String, Object> addTranscript(Transcript transcript){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			int a= this.transcriptDAO.insert(transcript);
			if (a <= 0) {
				mapResult.put("code", 1);
				mapResult.put("msg", "添加失败");
			} else {
				mapResult.put("code", 2);
				mapResult.put("msg", "已成功将本次成绩导入数据库！");
			}
		} catch (Exception e) {
			mapResult.put("code", 0);
			mapResult.put("msg", "添加过程中出现错误");
		}finally {
			this.sqlSession.close();
		}
		return mapResult;
	}
}
