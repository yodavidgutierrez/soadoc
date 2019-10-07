package co.com.soaint.foundation.canonical.jbpm;

import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class TaskDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer taskId;
	
	private String taskName;
	
	private String taskSubject;
	
	private String taskDescription;
	
	private String taskStatus;
	
	private Integer taskPriority;
	
	private String taskActualOwner;
	
	private String taskCreatedBy;
	
	private String taskCreatedOn;
	
	private String taskActivationTime;
	
	private String taskExpirationTime;
	
	private String taskProcDefId;
	
	private String instanceId;

	private ParametrosDto parametros;


	public ParametrosDto getParametros() {
		return parametros;
	}

	public void setParametros(ParametrosDto parametros) {
		this.parametros = parametros;
	}

	/**
	 * @return the taskId
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the taskSubject
	 */
	public String getTaskSubject() {
		return taskSubject;
	}

	/**
	 * @param taskSubject the taskSubject to set
	 */
	public void setTaskSubject(String taskSubject) {
		this.taskSubject = taskSubject;
	}

	/**
	 * @return the taskDescription
	 */
	public String getTaskDescription() {
		return taskDescription;
	}

	/**
	 * @param taskDescription the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	/**
	 * @return the taskStatus
	 */
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the taskPriority
	 */
	public Integer getTaskPriority() {
		return taskPriority;
	}

	/**
	 * @param taskPriority the taskPriority to set
	 */
	public void setTaskPriority(Integer taskPriority) {
		this.taskPriority = taskPriority;
	}

	/**
	 * @return the taskActualOwner
	 */
	public String getTaskActualOwner() {
		return taskActualOwner;
	}

	/**
	 * @param taskActualOwner the taskActualOwner to set
	 */
	public void setTaskActualOwner(String taskActualOwner) {
		this.taskActualOwner = taskActualOwner;
	}

	/**
	 * @return the taskCreatedBy
	 */
	public String getTaskCreatedBy() {
		return taskCreatedBy;
	}

	/**
	 * @param taskCreatedBy the taskCreatedBy to set
	 */
	public void setTaskCreatedBy(String taskCreatedBy) {
		this.taskCreatedBy = taskCreatedBy;
	}

	/**
	 * @return the taskCreatedOn
	 */
	public String getTaskCreatedOn() {
		return taskCreatedOn;
	}

	/**
	 * @param taskCreatedOn the taskCreatedOn to set
	 */
	public void setTaskCreatedOn(String taskCreatedOn) {
		this.taskCreatedOn = taskCreatedOn;
	}

	/**
	 * @return the taskActivationTime
	 */
	public String getTaskActivationTime() {
		return taskActivationTime;
	}

	/**
	 * @param taskActivationTime the taskActivationTime to set
	 */
	public void setTaskActivationTime(String taskActivationTime) {
		this.taskActivationTime = taskActivationTime;
	}

	/**
	 * @return the taskExpirationTime
	 */
	public String getTaskExpirationTime() {
		return taskExpirationTime;
	}

	/**
	 * @param taskExpirationTime the taskExpirationTime to set
	 */
	public void setTaskExpirationTime(String taskExpirationTime) {
		this.taskExpirationTime = taskExpirationTime;
	}

	/**
	 * 
	 * @return the taskProcDefId
	 */
	public String getTaskProcDefId() {
		return taskProcDefId;
	}

	/**
	 * 
	 * @param taskProcDefId the taskProcDefId to set
	 */
	public void setTaskProcDefId(String taskProcDefId) {
		this.taskProcDefId = taskProcDefId;
	}

		/**
	 * 
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * 
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
