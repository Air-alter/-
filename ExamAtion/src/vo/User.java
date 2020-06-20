package vo;

public class User {
	private String userName; //用户名
	private String password; //密码
	private String chrName; //中文名
	private String role; //身份

	public User() {
		super();
	}
	
	public User(String userName,String password,String chrName,String role){
		this.userName=userName;
		this.chrName=chrName;
		this.password=password;
		this.role=role;
	}


	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChrName() {
		return chrName;
	}

	public void setChrName(String chrName) {
		this.chrName = chrName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", chrName=" + chrName + ", role=" + role
				+ "]";
	}
}















