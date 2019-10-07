
package co.com.soaint.foundation.canonical.jbpm;


import lombok.*;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class ContainerDto implements Serializable {
	

	private static final long serialVersionUID = 1L;

	private String containerId;

	private String version;

	private String status;

	private String containerAlias;

	private List<ProcessesDto> processes;

	private List<MessagesDto> messages;

	private List<RulesDto> rules;

	/**
	 * @return the containerId
	 */
	public String getContainerId() {
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the containerAlias
	 */
	public String getContainerAlias() {
		return containerAlias;
	}

	/**
	 * @param containerAlias the containerAlias to set
	 */
	public void setContainerAlias(String containerAlias) {
		this.containerAlias = containerAlias;
	}

	/**
	 * @return the processes
	 */
	public List<ProcessesDto> getProcesses() {
		return processes;
	}

	/**
	 * @param processes the processes to set
	 */
	public void setProcesses(List<ProcessesDto> processes) {
		this.processes = processes;
	}

	/**
	 * @return the messages
	 */
	public List<MessagesDto> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<MessagesDto> messages) {
		this.messages = messages;
	}

	/**
	 * 
	 * @return the rules
	 */
	public List<RulesDto> getRules() {
		return rules;
	}

	/**
	 * 
	 * @param rules the rules to set
	 */
	public void setRules(List<RulesDto> rules) {
		this.rules = rules;
	}
	
}
