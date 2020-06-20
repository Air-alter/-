package vo;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class Transcript {
	String exam_id;
	String userName;
	Double Totalscore;

	public Transcript() {
		this.exam_id = null;
		this.userName = null;
		this.Totalscore = null;
	}

	@Override
	public String toString() {

		String str = "";
		str = str + exam_id + "\t" + userName + "\t"
				+ new BigDecimal(Totalscore).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return str;
	}

	public String toString(String name) {

		String str = "";
		str = str + exam_id + "\t" + userName + "\t" + name + "\t"
				+ new BigDecimal(Totalscore).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return str;
	}

	public String getExam_id() {
		return exam_id;
	}

	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getTotalscore() {
		return Totalscore;
	}

	public void setTotalscore(Double totalscore) {
		Totalscore = totalscore;
	}



}
