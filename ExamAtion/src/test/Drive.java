package test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import service.ChoiceQstService;
import service.ExamService;
import service.TranscriptService;
import service.UserService;
import vo.Choiceqst;
import vo.Exam;
import vo.Transcript;
import vo.User;

public class Drive {
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		User user = new User();// 存放当前登陆信息
		boolean y = false;
		if (login(user)) {// 退出方式是因为登陆成功
			if (user.getRole().equals("学生")) {
				main_student(user);
			} else {
				main_teacher(user);
			}
		}
		System.out.println("已退出系统！");
	}

	// 输出符合正则表达式的字符串
	static String EnterString(String regex) {
		String str = scan.next();
		while (!str.matches(regex)) {
			System.out.println("格式不匹配，请重新输入");
			str = scan.next();
		}
		return str;
	}


	// 登陆
	static boolean login(User user) throws Exception {
		String userName, password;
		System.out.println("请输入用户名和密码：");
		for (int i = 0; i < 3; i++) {
			userName = scan.next();
			password = scan.next();
			user.setUserName(userName);
			user.setPassword(password);
			Map<String, Object> mapResult = new UserService().checkUser(user);
			if ((int) mapResult.get("code") == 2) {
				System.out.print("登陆成功,欢迎你" + user.getChrName());
				if (user.getChrName().equals("学生")) {
					System.out.println("同学");
				} else {
					System.out.println("老师");
				}
				return true;
			} else {
				System.out.println(mapResult.get("msg"));
			}
			if (i <= 1) {
				System.out.println("请重新输入");
			}
		}
		System.out.println("最多只能尝试三次！");
		return false;
	}

	// 修改密码
	static void changePassword(User user) {
		String password = null;
		System.out.println("请输入当前用户的原密码");
		password = scan.next();
		while (!user.getPassword().equals(password)) {
			System.out.println("密码错误，请重新输入！");
			password = scan.next();
		}
		System.out.println("请设置新的密码(包含数字，大小写字符的6位字符串)");
		password = EnterString("^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,}$");
		System.out.println("请确认新密码");
		String str = null;
		while (!(str = scan.next()).equals(password)) {
			System.out.println("密码不一致，请重新输入！");
		}
		user.setPassword(password);
		if (new UserService().changePassword(user)) {
			System.out.println("您已成功修改密码，请谨记");
		} else {
			System.out.println("修改失败");
		}

	}

	// 成绩查询
	static List<Transcript> CheckResults(Transcript transcript) {
		List<Transcript> list = new TranscriptService().query(transcript);
		return list;
	}

	// 学生操作测试
	static void main_student(User user) throws Exception {
		boolean y = true;
		while (y) {
			int t = menu_student();
			switch (t) {
			case 1:// 考试
				examination(user);
				break;
			case 2:// 成绩查询
				StuCheckResults(user);
				break;
			case 3:// 修改密码
				changePassword(user);
				break;
			case 4:// 退出
				System.out.println("确定要退出吗？（y：是，n：否）");
				String str = EnterString("y|n");
				if (str.equals("y"))
					y = false;
				break;
			}
		}
	}

	// 学生操作菜单
	static int menu_student() {
		int t;
		System.out.println("===========在线考试系统==============");
		System.out.println("1,考试");
		System.out.println("2,成绩查询");
		System.out.println("3，修改密码");
		System.out.println("4，退出");
		System.out.println("请选择（1-4）：");
		t = scan.nextInt();
		while (t < 1 || t > 4) {
			System.out.println("错误选择，请重新输入");
			t = scan.nextInt();
		}
		return t;
	}

	// 考试
	static void examination(User user) {
		System.out.println("请输入考试号");
		String exam_id = scan.next();
		Exam exam = new ExamService().getExam(exam_id);
		if (exam == null) {
			System.out.println("该场考试不存在");
		} else {
			// 当前时间可以考试考试
			if (exam.exan_check()) {
				String[] topic_ids = exam.getExam_Topic().split("-");
				String[] topic_scores = exam.getExam_Score().split("-");
				Transcript transcript = new Transcript();
				Double score = 0.0;
				for (int i = 0; i < topic_ids.length; i++) {
					Choiceqst topic = new ChoiceQstService().getTopicById(topic_ids[i]);
					if (topic == null) {
						System.out.println("该道题可能已被删除，所以本题按满分计算");
					} else {
						System.out.println((i + 1) + "," + topic.toString());
						System.out.println("请答题:");
						String Myansw = scan.next();
						score += topic.Revise(Double.parseDouble(topic_scores[i]), Myansw);
					}
				}
				System.out.println("考试结束，你的得分为" + score);
				transcript.setUserName(user.getUserName());
				transcript.setExam_id(exam.getExam_id());
				transcript.setTotalscore(score);
				Map<String, Object> map = new TranscriptService().addTranscript(transcript);
				System.out.println(map.get("msg"));
			} else {
				System.out.println("当前时间无法开始考试,因为已经过了截止日期");
			}
		}
	}

	// 学生成绩管理菜单
	static int menu_student_CheckResults() {
		System.out.println("=============学生成绩管理=================");
		System.out.println("\t1，查询所有考试成绩");
		System.out.println("\t2，查询指定考试成绩");
		System.out.println("\t3，回主菜单");
		System.out.println("\t请选择（1-3）：");
		int t = scan.nextInt();
		while (t < 1 || t > 3) {
			System.out.println("格式错误，请重新选择");
			t = scan.nextInt();
		}
		return t;
	}

	// 学生成绩管理
	static void StuCheckResults(User user) {
		boolean y = true;
		Transcript transcript = new Transcript();
		transcript.setUserName(user.getUserName());
		while (y) {
			int t = menu_student_CheckResults();
			List<Transcript> list = null;
			switch (t) {
			case 1:
				list = CheckResults(transcript);
				if (list != null) {
					System.out.println("考试号\t\t用户名\t姓名\t成绩");
					for (Transcript transcript2 : list) {
						System.out.println(transcript2.toString(user.getChrName()));
					}
				} else {
					System.out.println("没有查到有效成绩");
				}
				break;
			case 2:
				System.out.println("请输入考试号");
				String exam_id = EnterString(".*");
				transcript.setExam_id(exam_id);
				list = CheckResults(transcript);
				if (list != null) {
					System.out.println("考试号\t\t用户名\t姓名\t成绩");
					for (Transcript transcript2 : list) {
						System.out.println(transcript2.toString(user.getChrName()));
					}
				} else {
					System.out.println("没有查到有效成绩");
				}
				break;
			case 3:
				y = false;
				break;
			}
		}
	}

	// 教师操作测试
	static void main_teacher(User user) throws Exception {
		boolean y = true;
		while (y) {
			int t = menu_teacher();
			switch (t) {
			case 1:// 题库管理
				teacher_QuestionBank_Management(user);
				break;
			case 2:// 考试管理
				teacher_exam_Management();
				break;
			case 3:
				teacher_CheckResult();
				break;
			case 4:// 修改密码
				changePassword(user);
				break;
			case 5:
				y = false;
				break;

			}
		}
	}

	// 教师操作菜单
	static int menu_teacher() {
		System.out.println("===========在线考试系统==============");
		System.out.println("\t1，题库管理");
		System.out.println("\t2，考试管理");
		System.out.println("\t3，成绩管理");
		System.out.println("\t4，修改密码");
		System.out.println("\t5退出");
		System.out.println("请选择（1-5）：");
		int t = scan.nextInt();
		while (t < 1 || t > 5) {
			System.out.println("错误选择，请重新选择");
			t = scan.nextInt();
		}
		return t;
	}

	// 题库管理
	static void teacher_QuestionBank_Management(User user) {
		boolean y = true;
		while (y) {
			int t = menu_teacher_QuestionBank_Management();
			switch (t) {
			case 1:// 删除题目
				teacher_Delete_Topic();
				break;
			case 2:// 添加题目
				teacher_Add_Topic();
				break;
			case 3:// 更新题目
				teacher_Update_topic();
				break;
			case 4:
				y = false;
				break;
			}
		}
	}

	// 题库管理菜单
	static int menu_teacher_QuestionBank_Management() {
		System.out.println("==========题库管理============");
		System.out.println("1，删除题目");
		System.out.println("2，添加题目");
		System.out.println("3，更新题目");
		System.out.println("4，回主菜单");
		int t = scan.nextInt();
		while (t < 1 || t > 4) {
			System.out.println("错误选择，请重新输入");
			t = scan.nextInt();
		}
		return t;
	}

	// 删除题目
	static void teacher_Delete_Topic() {
		System.out.println("请输入要删除题目的题号");
		String topic_id = EnterString("(a|b).*");
		Map<String, Object> map = new ChoiceQstService().deleteTopic(topic_id);
		System.out.println(map.get("msg"));
	}

	// 添加题目
	static void teacher_Add_Topic() {
		System.out.println("请选择要添加的题型（a:单选,b:多选");
		String str = EnterString("a|b");
		if (str.equals("a")) {// 单选
			Choiceqst choiceqst = new Choiceqst();
			choiceqst.setChoiceQst_id("a" + choiceqst.getOnlyId());
			System.out.println("请输入题干：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_Info(str);
			System.out.println("请输入A选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPA(str);
			System.out.println("请输入B选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPB(str);
			System.out.println("请输入C选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPC(str);
			System.out.println("请输入D选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPD(str);
			System.out.println("请输入答案");
			str = EnterString("A|B|C|D");
			choiceqst.setChoiceQst_answ(str);
			Map<String, Object> map = new ChoiceQstService().addTopic(choiceqst);
			System.out.println(map.get("msg"));
		} else if (str.equals("b")) {// 多选
			Choiceqst choiceqst = new Choiceqst();
			choiceqst.setChoiceQst_id("b" + choiceqst.getOnlyId());
			System.out.println("请输入题干：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_Info(str);
			System.out.println("请输入A选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPA(str);
			System.out.println("请输入B选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPB(str);
			System.out.println("请输入C选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPC(str);
			System.out.println("请输入D选项：");
			str = EnterString(".*");
			choiceqst.setChoiceQst_OPD(str);
			System.out.println("请输入答案");
			str = EnterString("[ABCD]{2,4}");
			choiceqst.setChoiceQst_answ(str);
			Map<String, Object> map = new ChoiceQstService().addTopic(choiceqst);
			System.out.println(map.get("msg"));
		} 
	}

	// 更新题目
	static void teacher_Update_topic() {
		System.out.println("输入要更新的题号");
		String topic_id = EnterString(".*");
		Choiceqst topic = new ChoiceQstService().getTopicById(topic_id);
		if (topic == null) {
			System.out.println("该题号不存在！");
		} else {
			String str = "";
			if (topic_id.substring(0,1).equals("a_choiceqst")) {// 单选
				Choiceqst choiceqst = (Choiceqst) topic;
				System.out.println("请输入题干：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_Info(str);
				System.out.println("请输入A选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPA(str);
				System.out.println("请输入B选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPB(str);
				System.out.println("请输入C选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPC(str);
				System.out.println("请输入D选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPD(str);
				System.out.println("请输入答案");
				str = EnterString("A|B|C|D");
				choiceqst.setChoiceQst_answ(str);
				Map<String, Object> map = new ChoiceQstService().updateTopic(choiceqst);
				System.out.println(map.get("msg"));
			} else if (topic_id.substring(0,1).equals("b")) {// 多选
				Choiceqst choiceqst = (Choiceqst) topic;
				System.out.println("请输入题干：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_Info(str);
				System.out.println("请输入A选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPA(str);
				System.out.println("请输入B选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPB(str);
				System.out.println("请输入C选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPC(str);
				System.out.println("请输入D选项：");
				str = EnterString(".*");
				choiceqst.setChoiceQst_OPD(str);
				System.out.println("请输入答案");
				str = EnterString("[ABCD]{2,4}");
				choiceqst.setChoiceQst_answ(str);
				Map<String, Object> map = new ChoiceQstService().updateTopic(choiceqst);
				System.out.println(map.get("msg"));
			} 
		}
	}

	// 考试管理
	static void teacher_exam_Management() throws Exception {
		System.out.println("请输入考题数目");
		int topic_Num = scan.nextInt();
		Exam exam = new Exam();
		String exam_Topic = "";
		String exam_Score = "";
		int a_choiceqst = 0;// 单选题数目
		int b_choiceqst = 0;// 多选题数目
		double scores = 0;
		for (int i = 0; i < topic_Num; i++) {
			System.out.println("请输入试题编号");
			String topic_id = EnterString("(a|b|t|f).*");
			if (new ChoiceQstService().getTopicById(topic_id) != null) {
				exam_Topic = exam_Topic + topic_id + "-";
				if (topic_id.substring(0,1).equals("a")) {
					a_choiceqst++;
				} else if (topic_id.substring(0,1).equals("a")) {
					b_choiceqst++;
				} 
				System.out.println("请输入该题所占的分值：");
				double score = scan.nextDouble();
				exam_Score = exam_Score + score + "-";
				scores += score;
			} else {
				System.out.println("不存在该题号，请重新输入");
				i--;
			}
		}
		System.out.println("请输入考试最晚截止时间(yyyymmddhhmmss)");
		String regex = "[0-9]{4}((0[1-9])|(1[0-2]))(([0-2][0-9])|(30))(([0-1][0-9])|(2[0-3]))[0-5][0-9][0-5][0-9]";
		String examTime = EnterString(regex);
		System.out.println("请输入考试时长");
		int exam_duration = scan.nextInt();
		exam.setExam_Score(exam_Score);
		exam.setExam_Topic(exam_Topic);
		exam.setExam_duration(exam_duration);
		exam.setExam_Time(examTime);
		Map<String, Object> map = new ExamService().addExam(exam);
		if ((int) map.get("code") == 2) {
			String str = "试卷" + exam.getExam_id() + "已创建完成。\n";
			str += "其中单选题" + a_choiceqst + "道";
			str += "多选题" + b_choiceqst + "道";
			str += "总分为" + scores + "分";
			str += "考试截止日期为" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(exam.getExam_Time());
			str += "考试时间" + exam.getExam_duration() + "min";
			System.out.println(str);
		} else {
			System.out.println(map.get("msg"));
		}
	}

	// 教师成绩管理
	static void teacher_CheckResult() throws Exception {
		boolean y = true;
		String exam_id = null;
		String userName = null;
		List<Transcript> list = null;
		Transcript transcript = null;
		while (y) {
			int t = menu_teacher_CheckResult();
			switch (t) {
			case 1:// 查询某次考试成绩
				System.out.println("请输入考试编号");
				exam_id = EnterString(".*");
				transcript = new Transcript();
				transcript.setExam_id(exam_id);
				list = CheckResults(transcript);
				if (list != null) {
					System.out.println("考试号\t\t用户名\t成绩");
					for (Transcript transcript2 : list) {
						System.out.println(transcript2.toString());
					}
				} else {
					System.out.println("没有查到有效成绩");
				}
				break;
			case 2:// 查询学生所有考试成绩
				System.out.println("请输入学生用户名");
				userName = EnterString(".*");
				transcript = new Transcript();
				transcript.setUserName(userName);
				list = CheckResults(transcript);
				if (list != null) {
					System.out.println("考试号\t\t用户名\t成绩");
					for (Transcript transcript2 : list) {
						System.out.println(transcript2.toString());
					}
				} else {
					System.out.println("没有查到有效成绩");
				}
				break;
			case 3:// 查询学生指定考试成绩
				transcript = new Transcript();
				System.out.println("请输入考试编号");
				transcript.setExam_id(exam_id);
				exam_id = EnterString(".*");
				System.out.println("请输入学生用户名");
				userName = EnterString(".*");
				transcript.setUserName(userName);
				list = CheckResults(transcript);
				if (list != null) {
					System.out.println("考试号\t\t用户名\t成绩");
					for (Transcript transcript2 : list) {
						System.out.println(transcript2.toString());
					}
				} else {
					System.out.println("没有查到有效成绩");
				}
				break;
			case 4:// 成绩统计
				transcript = new Transcript();
				System.out.println("请输入考试号：");
				exam_id = EnterString(".*");
				transcript.setExam_id(exam_id);
				Exam exam = new ExamService().getExam(exam_id);
				int Score = 0;
				if (exam != null) {
					String[] scores = exam.getExam_Score().split("-");
					for (String score : scores) {
						Score += Double.parseDouble(score);
					}
					int a, b, c, d, e;
					a = b = c = d = e = 0;
					list = new TranscriptService().query(transcript);
					for (Transcript tran : list) {
						if (tran.getTotalscore() >= (Score * 0.9)) {
							a++;
						} else if (tran.getTotalscore() < (Score * 0.9) && tran.getTotalscore() >= (Score * 0.8)) {
							b++;
						} else if (tran.getTotalscore() < (Score * 0.8) && tran.getTotalscore() >= (Score * 0.7)) {
							c++;
						} else if (tran.getTotalscore() < (Score * 0.7) && tran.getTotalscore() >= (Score * 0.6)) {
							d++;
						} else {
							e++;
						}
					}
					System.out.println("考试"+exam_id+"答题人数为"+(a+b+c+d+e)+"人，分析如下：");
					System.out.println("90%"+a+"人");
					System.out.println("90%-80%"+b+"人");
					System.out.println("80%-70%"+c+"人");
					System.out.println("70%-60%"+d+"人");
					System.out.println("60以下"+e+"人");
				}else {
					System.out.println("考试号不存在！");
				}
				break;
			case 5:
				y = false;
				break;
			}
		}
	}

	// 教师成绩管理菜单
	static int menu_teacher_CheckResult() {
		System.out.println(" ==================学生成绩管理=============");
		System.out.println("\t1，查询某次考试成绩");
		System.out.println("\t2，查询学生所有考试成绩");
		System.out.println("\t3，查询学生指定考试成绩");
		System.out.println("\t4，成绩统计");
		System.out.println("\t5，回主菜单");
		System.out.println("请选择（1-5）");
		int t = scan.nextInt();
		while (t < 1 || t > 5) {
			System.out.println("无效选择，请重新输入");
			t = scan.nextInt();
		}
		return t;
	}
}