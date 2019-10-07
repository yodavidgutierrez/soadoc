
package co.com.soaint.foundation.canonical.jbpm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * @author ovillamil
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceDto implements Serializable {


	private static final long serialVersionUID = 1L;

	private String instanceId;


	/**
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
}
