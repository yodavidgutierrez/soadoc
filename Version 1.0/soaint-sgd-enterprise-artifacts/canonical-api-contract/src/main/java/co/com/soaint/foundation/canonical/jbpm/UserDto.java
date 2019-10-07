package co.com.soaint.foundation.canonical.jbpm;


import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class UserDto implements Serializable {
	

	private static final long serialVersionUID = 1L;

	private String user;
	
	private String password;
	
	private String targetUser;



	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the targetUser
	 */
	public String getTargetUser() {
		return targetUser;
	}

	/**
	 * @param targetUser the targetUser to set
	 */
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	/**
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}