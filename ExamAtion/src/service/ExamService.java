package service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dao.IExamDAO;
import util.MybatisUtils;
import vo.Exam;


public class ExamService {
	private IExamDAO examDAO = null;
	private SqlSession sqlSession = null;

	public ExamService() {
		this.sqlSession = MybatisUtils.getSqlSession();
		this.examDAO = sqlSession.getMapper(IExamDAO.class);
	}
	
	public Exam getExam(String exam_id) {
		Exam exam=null;
		try {
			exam=this.examDAO.getByExam_id(exam_id);
		} catch (Exception e) {
			exam=null;
		}finally {
			this.sqlSession.close();
		}
		return exam;
	}
	
	public Map<String, Object> addExam(Exam exam) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			Exam examfound=this.examDAO.getByExam_id(exam.getExam_id());
			if (examfound == null) {
				int a=this.examDAO.insert(exam);
				if (a <= 0) {
					mapResult.put("code", 1);
					mapResult.put("msg", "添加失败");
				} else {
					mapResult.put("code", 2);
					mapResult.put("msg", "添加成功");
				}
			} else {
				mapResult.put("code", 1);
				mapResult.put("msg", "添加失败,因为该题号已经存在");
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
