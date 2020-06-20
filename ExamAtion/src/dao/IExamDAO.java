package dao;

import vo.Exam;

public interface IExamDAO {
	//通过考试号获取试卷信息
	public Exam getByExam_id(String exam_id);
	//插入考试
	public int insert(Exam exam);
}
