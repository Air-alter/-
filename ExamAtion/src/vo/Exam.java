package vo;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Exam {
	String exam_id;     // 考试编号
	String exam_Topic;  // 试题编号信息
	String exam_Score;  // 分值编号信息
	Date exam_Time;     // 开始时间
	int exam_duration;// 考试时长

	//new SimpleDateFormat("yyyyMMddHHmmss").format(this.saleTime)
	public Exam() {
		//生成考试号
		this.exam_id="e"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public String getExam_id() {
		return exam_id;
	}

	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}

	public String getExam_Topic() {
		return exam_Topic;
	}

	public void setExam_Topic(String exam_Topic) {
		this.exam_Topic = exam_Topic;
	}

	public String getExam_Score() {
		return exam_Score;
	}

	public void setExam_Score(String exam_Score) {
		this.exam_Score = exam_Score;
	}

	public Date getExam_Time() {
		return exam_Time;
	}
	
	public void setExam_Time(Date exam_Time) {
		this.exam_Time = exam_Time;
	}
	
	public void setExam_Time(String examTime) throws Exception{
		this.exam_Time = new SimpleDateFormat("yyyyMMddHHmmss").parse(examTime);
	}

	public int getExam_duration() {
		return exam_duration;
	}

	public void setExam_duration(int exam_duration) {
		this.exam_duration = exam_duration;
	}

	@Override
	public String toString() {
		String str="";
		str+="考试号："+this.exam_id+"  ";
		str=str+"截止时间："+new SimpleDateFormat("yyyyMMddHHmmss").format(this.exam_Time)+" ";
		str=str+"考试时间："+this.exam_duration+"min";
		return str;
	}
	
	//判断当前时间是否可以开始考试
	public boolean exan_check() {
		if(this.exam_Time.after(new Date())) {
			return true;
		}else {
			return false;
		}
	}
	
}
