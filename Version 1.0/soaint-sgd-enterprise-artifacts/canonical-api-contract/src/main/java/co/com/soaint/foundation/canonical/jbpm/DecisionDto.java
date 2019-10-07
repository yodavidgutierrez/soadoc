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
public class DecisionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String decisionId;

	private String decisionName;

	private boolean decisionResult;

	private String decisionStatus;


	/**
	 * 
	 * @return the decisionId
	 */
	public String getDecisionId() {
		return decisionId;
	}

	/**
	 * 
	 * @param decisionId the decisionId to set
	 */
	public void setDecisionId(String decisionId) {
		this.decisionId = decisionId;
	}

	/**
	 * 
	 * @return the decisionName
	 */
	public String getDecisionName() {
		return decisionName;
	}

	/**
	 * 
	 * @param decisionName the decisionName to set
	 */
	public void setDecisionName(String decisionName) {
		this.decisionName = decisionName;
	}

	/**
	 * 
	 * @return the decisionResult
	 */
	public boolean isDecisionResult() {
		return decisionResult;
	}

	/**
	 * 
	 * @param decisionResult the decisionResult to set
	 */
	public void setDecisionResult(boolean decisionResult) {
		this.decisionResult = decisionResult;
	}

	/**
	 * 
	 * @return the decisionStatus
	 */
	public String getDecisionStatus() {
		return decisionStatus;
	}

	/**
	 * 
	 * @param decisionStatus the decisionStatus to set
	 */
	public void setDecisionStatus(String decisionStatus) {
		this.decisionStatus = decisionStatus;
	}
	
}
