package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AccionUsuario implements Serializable {

    ABRIR("Abierta"),
    CERRAR("Cerrada"),
    REACTIVAR("Reactivada");

    private static Long serialVersionUID = 133L;
    private final String state;

    public static AccionUsuario getActionBy(String actionName) {
        if (StringUtils.isEmpty(actionName)) {
            return null;
        }
        final Stream<AccionUsuario> stream = Arrays.stream(AccionUsuario.values());
        final Optional<AccionUsuario> optionalType = stream
                .filter(type -> type.getState().equalsIgnoreCase(actionName)
                        || type.name().equalsIgnoreCase(actionName))
                .findFirst();
        return optionalType.orElse(null);
    }
}
