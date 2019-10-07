
package co.com.soaint.foundation.canonical.jbpm;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

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
public class ParametrosDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ValueDto value;
	
		private Map<String, Object> values;



	/**
	 * @return the values
	 */
	public Map<String, Object> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

	/**
	 * @return the value
	 */
	public ValueDto getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(ValueDto value) {
		this.value = value;
	}
	
}