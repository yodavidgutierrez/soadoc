package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by esanchez on 7/4/2017.
 */
@Getter
@AllArgsConstructor
public enum TipoAgenteEnum {

    REMITENTE("TP-AGEE", "REMITENTE"),
    DESTINATARIO("TP-AGEI", "DESTINATARIO");

    private final String codigo;
    private final String nombre;

    public static TipoAgenteEnum getTipoAgenteBy(String nameOrKey) {
        if (null == nameOrKey || "".equals(nameOrKey.trim())) {
            return null;
        }
        final String stateString = nameOrKey.trim();
        final Stream<TipoAgenteEnum> stream = Arrays.stream(TipoAgenteEnum.values());
        final Optional<TipoAgenteEnum> optionalType = stream
                .filter(type -> type.getNombre().equalsIgnoreCase(stateString)
                        || type.getCodigo().equalsIgnoreCase(stateString)
                        || type.name().equalsIgnoreCase(stateString))
                .findFirst();
        return optionalType.orElse(null);
    }
}
