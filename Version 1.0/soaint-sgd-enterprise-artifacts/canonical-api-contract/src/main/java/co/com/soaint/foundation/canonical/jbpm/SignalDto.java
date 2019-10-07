
package co.com.soaint.foundation.canonical.jbpm;

import co.com.soaint.foundation.canonical.jbpm.ParametrosDto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class SignalDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String idDespliegue;

	private String signalName;
	
	private ParametrosDto parametros;

	public String getIdDespliegue() {
		return idDespliegue;
	}

	public void setIdDespliegue(String idDespliegue) {
		this.idDespliegue = idDespliegue;
	}

	/**
	 * @return the signalName
	 */
	public String getSignalName() {
		return signalName;
	}

	/**
	 * @param signalName the signalName to set
	 */
	public void setSignalName(String signalName) {
		this.signalName = signalName;
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
	
}
