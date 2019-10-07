package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by esanchez on 7/3/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadoCorrespondenciaEnum {
    RADICADO("RD", "RADICADO"),
    REGISTRADO("RG", "REGISTRADO"),
    SIN_ASIGNAR("SA", "SIN ASIGNAR"),
    ASIGNACION("AS", "ASIGNACION");

    private final String codigo;
    private final String nombre;

    public static EstadoCorrespondenciaEnum getEstadoCorrespondenciaBy(String nameOrKey) {
        if (null == nameOrKey || "".equals(nameOrKey.trim())) {
            return null;
        }
        final String stateString = nameOrKey.trim();
        final Stream<EstadoCorrespondenciaEnum> stream = Arrays.stream(EstadoCorrespondenciaEnum.values());
        final Optional<EstadoCorrespondenciaEnum> optionalType = stream
                .filter(type -> type.getNombre().equalsIgnoreCase(stateString)
                        || type.getCodigo().equalsIgnoreCase(stateString)
                        || type.name().equalsIgnoreCase(stateString))
                .findFirst();
        return optionalType.orElse(null);
    }
}
