package co.com.soaint.foundation.canonical.jbpm;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class FullTaskDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TaskDto> tareas;
	private Integer count;

}
