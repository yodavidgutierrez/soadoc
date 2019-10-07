package co.com.soaint.foundation.canonical.jbpm;

import lombok.*;

import java.io.Serializable;

/**
 * 
 * @author jjmorales
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class InputDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String inputId;

	private String inputName;
	


	/**
	 * 
	 * @return the inputId
	 */
	public String getInputId() {
		return inputId;
	}

	/**
	 * 
	 * @param inputId the inputId to set
	 */
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	/**
	 * 
	 * @return the inputName
	 */
	public String getInputName() {
		return inputName;
	}

	/**
	 * 
	 * @param inputName the inputName to set
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

}
