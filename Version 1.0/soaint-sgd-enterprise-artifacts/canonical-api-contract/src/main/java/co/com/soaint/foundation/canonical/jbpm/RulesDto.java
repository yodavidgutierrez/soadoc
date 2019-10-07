package co.com.soaint.foundation.canonical.jbpm;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class RulesDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ruleNamespace;
	
	private String ruleName;
	
	private String ruleId;
	
	private List<DecisionDto> decisionList;
	
	private List<InputDto> inputList;
	
	private Map<String, Object> context;

	/**
	 * 
	 * @return the RuleNamespace
	 */
	public String getRuleNamespace() {
		return ruleNamespace;
	}

	/**
	 * 
	 * @param ruleNamespace the RuleNamespace to set
	 */
	public void setRuleNamespace(String ruleNamespace) {
		this.ruleNamespace = ruleNamespace;
	}

	/**
	 * 
	 * @return the RuleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * 
	 * @param ruleName the RuleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * 
	 * @return the ruleId
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * 
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * 
	 * @return the decisionList
	 */
	public List<DecisionDto> getDecisionList() {
		return decisionList;
	}

	/**
	 * 
	 * @param decisionList the decisionList to set
	 */
	public void setDecisionList(List<DecisionDto> decisionList) {
		this.decisionList = decisionList;
	}

	/**
	 * 
	 * @return the inputList
	 */
	public List<InputDto> getInputList() {
		return inputList;
	}

	/**
	 * 
	 * @param inputList the inputList to set
	 */
	public void setInputList(List<InputDto> inputList) {
		this.inputList = inputList;
	}

	/**
	 * 
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}

	/**
	 * 
	 * @param context the context to set
	 */
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
}
