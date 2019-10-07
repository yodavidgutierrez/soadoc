package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by esanchez on 8/8/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadoAgenteEnum {

    SIN_ASIGNAR("SA", "SIN ASIGNAR"),
    REGISTRADO("RG", "REGISTRADO"),
    ASIGNADO("AS", "ASIGNADO"),
    DEVUELTO("DV", "DEVUELTO"),
    TRAMITADO("TD", "TRAMITADO");

    private final String codigo;
    private final String nombre;

    public static EstadoAgenteEnum getEstadoAgenteBy(String nameOrKey) {
        if (null == nameOrKey || "".equals(nameOrKey.trim())) {
            return null;
        }
        final String stateString = nameOrKey.trim();
        final Stream<EstadoAgenteEnum> stream = Arrays.stream(EstadoAgenteEnum.values());
        final Optional<EstadoAgenteEnum> optionalType = stream
                .filter(type -> type.getNombre().equalsIgnoreCase(stateString)
                        || type.getCodigo().equalsIgnoreCase(stateString)
                        || type.name().equalsIgnoreCase(stateString))
                .findFirst();
        return optionalType.orElse(null);
    }
}
