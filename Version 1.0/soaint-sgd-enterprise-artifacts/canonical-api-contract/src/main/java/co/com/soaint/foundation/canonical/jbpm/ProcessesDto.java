package co.com.soaint.foundation.canonical.jbpm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessesDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String processId;

	private String processName;

	private String processVersion;

	private BigInteger startDate;

	private String initiator;

	private String packages;

	private InstanceDto instance;

	private List<TaskDto> taskList;

	private ParametrosDto parametros;

	private String deploymentId;


	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * @return the processVersion
	 */
	public String getProcessVersion() {
		return processVersion;
	}

	/**
	 * @param processVersion the processVersion to set
	 */
	public void setProcessVersion(String processVersion) {
		this.processVersion = processVersion;
	}

	/**
	 * @return the packages
	 */
	public String getPackages() {
		return packages;
	}

	/**
	 * @param packages the packages to set
	 */
	public void setPackages(String packages) {
		this.packages = packages;
	}

	/**
	 * @return the instance
	 */
	public InstanceDto getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(InstanceDto instance) {
		this.instance = instance;
	}

	/**
	 * @return the taskList
	 */
	public List<TaskDto> getTaskList() {
		return taskList;
	}

	/**
	 * @param taskList the taskList to set
	 */
	public void setTaskList(List<TaskDto> taskList) {
		this.taskList = taskList;
	}

	/**
	 * @return the startDate
	 */
	public BigInteger getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(BigInteger startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the initiator
	 */
	public String getInitiator() {
		return initiator;
	}

	/**
	 * @param initiator the initiator to set
	 */
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	/**
	 * 
	 * @return the parametros
	 */
	public ParametrosDto getParametros() {
		return parametros;
	}

	/**
	 * 
	 * @param parametros the parametros to set
	 */
	public void setParametros(ParametrosDto parametros) {
		this.parametros = parametros;
	}


}
