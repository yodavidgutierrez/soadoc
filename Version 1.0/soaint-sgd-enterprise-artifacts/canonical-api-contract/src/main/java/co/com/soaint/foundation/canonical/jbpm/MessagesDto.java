package co.com.soaint.foundation.canonical.jbpm;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class MessagesDto implements Serializable {


	private static final long serialVersionUID = 1L;

	private String severity;

	private BigInteger fecha;

	private List<String> content;

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * @return the fecha
	 */
	public BigInteger getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(BigInteger fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the content
	 */
	public List<String> getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(List<String> content) {
		this.content = content;
	}

}
