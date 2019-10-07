package co.com.soaint.foundation.canonical.jbpm;


import lombok.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class ProcessResponseDto implements Serializable {


	private static final long serialVersionUID = 1L;

	private List<ContainerDto> containers;

	private ResponseDto response;


	/**
	 * @return the containers
	 */
	public List<ContainerDto> getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public void setContainers(List<ContainerDto> containers) {
		this.containers = containers;
	}

	/**
	 * @return the response
	 */
	public ResponseDto getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(ResponseDto response) {
		this.response = response;
	}
	
}
