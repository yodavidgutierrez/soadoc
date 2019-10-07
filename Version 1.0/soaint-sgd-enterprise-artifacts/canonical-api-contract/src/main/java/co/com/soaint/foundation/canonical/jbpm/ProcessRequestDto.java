package co.com.soaint.foundation.canonical.jbpm;

import lombok.*;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class ProcessRequestDto implements Serializable {
	

	private static final long serialVersionUID = 1L;

	private String containerId;

	private String processesId;

	private String processInstance;

	private String taskId;

	private String taskStatus;

	private UserDto ownerUser;

	private UserDto assignment;

	private ParametrosDto parametros;

	private SignalDto signal;

	private List<String> groups;

	private List<String> taskStates;

	private int page;

	private int pageSize;

	private MultiValueMap<String, Object> queryParams;

	public MultiValueMap<String, Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(MultiValueMap<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * 
	 */
	public ProcessRequestDto() {
		super();
		//pageSize = -1; //lista todas por defecto
//		pageSize = 10;
		//page=0; //pagina inicial por defecto
		
		//Carga lista completa de estados por defecto
		taskStates = new ArrayList<String>();
		taskStates.add("Created");
		taskStates.add("Ready");
		taskStates.add("Reserved");
		taskStates.add("InProgress");
		taskStates.add("Suspended");
		taskStates.add("Completed");
		taskStates.add("Failed");
		taskStates.add("Error");
		taskStates.add("Exited");
		taskStates.add("Obsolete");
	}

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
	 * @return the processesId
	 */
	public String getProcessesId() {
		return processesId;
	}

	/**
	 * @param processesId the processesId to set
	 */
	public void setProcessesId(String processesId) {
		this.processesId = processesId;
	}

	/**
	 * @return the processInstance
	 */
	public String getProcessInstance() {
		return processInstance;
	}

	/**
	 * @param processInstance the processInstance to set
	 */
	public void setProcessInstance(String processInstance) {
		this.processInstance = processInstance;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	 * @return the ownerUser
	 */
	public UserDto getOwnerUser() {
		return ownerUser;
	}

	/**
	 * @param ownerUser the ownerUser to set
	 */
	public void setOwnerUser(UserDto ownerUser) {
		this.ownerUser = ownerUser;
	}

	/**
	 * @return the assignment
	 */
	public UserDto getAssignment() {
		return assignment;
	}

	/**
	 * @param assignment the assignment to set
	 */
	public void setAssignment(UserDto assignment) {
		this.assignment = assignment;
	}

	/**
	 * @return the parametros
	 */
	public ParametrosDto getParametros() {
		return parametros;
	}

	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(ParametrosDto parametros) {
		this.parametros = parametros;
	}

	public SignalDto getSignal() {
		return signal;
	}

	public void setSignal(SignalDto signal) {
		this.signal = signal;
	}

	/**
	 * 
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * 
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * 
	 * @param groups the groups to set
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	/**
	 * 
	 * @return the taskStates
	 */
	public List<String> getTaskStates() {
		return taskStates;
	}

	/**
	 * 
	 * @param taskStates the taskStates to set
	 */
	public void setTaskStates(List<String> taskStates) {
		this.taskStates = taskStates;
	}

	
}
