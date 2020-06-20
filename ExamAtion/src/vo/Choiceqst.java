package vo;

import java.util.UUID;

public class Choiceqst {
	private String ChoiceQst_id;
	private String ChoiceQst_Info;
	private String ChoiceQst_OPA;
	private String ChoiceQst_OPB;
	private String ChoiceQst_OPC;
	private String ChoiceQst_OPD;
	private String ChoiceQst_answ;

	public Choiceqst() {
		super();
	}
	
	public Choiceqst(String id) {
		this.ChoiceQst_id=id;
	}
	
	//生成唯一的序列代码
		public String getOnlyId(){
			String uuid = UUID.randomUUID().toString();
			uuid = uuid.replaceAll("-", "");          
			if(uuid.length()>32)uuid=uuid.substring(0,32);
			return uuid;
		}
	
	//用于通过父类获取子类的题号
	public String getTopic_id() {
		return this.getChoiceQst_id();
	}

	//获取题型（通过题号的第一位字符）
	public String getQuestionType() {
		if(this.ChoiceQst_id.substring(0,1).equals("a")) {
			return "a_choiceqst";
		}else if (this.ChoiceQst_id.substring(0,1).equals("b")){
			return "b_choiceqst";
		}
		return null;
	}
	
	public  double Revise(double score,String Myansw) {
		if(Myansw.length()>this.ChoiceQst_answ.length()) {
			return 0.0;
		}else if(Myansw.length()==this.ChoiceQst_answ.length()){
			for(int i=0;i<Myansw.length();i++) {
				if(!this.ChoiceQst_answ.contains(Myansw.substring(i, i+1)))return 0;
			}
			return score;
		}else {
			for(int i=0;i<Myansw.length();i++) {
				if(!this.ChoiceQst_answ.contains(Myansw.substring(i, i+1)))return 0;
			}
			return score*0.4;
		}
	}
	
	public String getChoiceQst_id() {
		return ChoiceQst_id;
	}

	public void setChoiceQst_id(String choiceQst_id) {
		ChoiceQst_id = choiceQst_id;
	}

	public String getChoiceQst_Info() {
		return ChoiceQst_Info;
	}

	public void setChoiceQst_Info(String choiceQst_Info) {
		ChoiceQst_Info = choiceQst_Info;
	}

	public String getChoiceQst_OPA() {
		return ChoiceQst_OPA;
	}

	public void setChoiceQst_OPA(String choiceQst_OPA) {
		ChoiceQst_OPA = choiceQst_OPA;
	}

	public String getChoiceQst_OPB() {
		return ChoiceQst_OPB;
	}

	public void setChoiceQst_OPB(String choiceQst_OPB) {
		ChoiceQst_OPB = choiceQst_OPB;
	}

	public String getChoiceQst_OPC() {
		return ChoiceQst_OPC;
	}

	public void setChoiceQst_OPC(String choiceQst_OPC) {
		ChoiceQst_OPC = choiceQst_OPC;
	}

	public String getChoiceQst_OPD() {
		return ChoiceQst_OPD;
	}

	public void setChoiceQst_OPD(String choiceQst_OPD) {
		ChoiceQst_OPD = choiceQst_OPD;
	}

	public String getChoiceQst_answ() {
		return ChoiceQst_answ;
	}

	public void setChoiceQst_answ(String choiceQst_answ) {
		ChoiceQst_answ = choiceQst_answ;
	}

	@Override
	public String toString() {
		String str="";
		str=ChoiceQst_Info+"?";
		if(this.ChoiceQst_id.substring(0,1).equals("a")) {
			str=str+"(单选)\n";
		}else {
			str=str+"(多选)\n";
		}
		str=str+"A:"+ChoiceQst_OPA+"\n";
		str=str+"B:"+ChoiceQst_OPB+"\n";
		str=str+"C:"+ChoiceQst_OPC+"\n";
		str=str+"D:"+ChoiceQst_OPD+"\n";
		return str;
	}

}
