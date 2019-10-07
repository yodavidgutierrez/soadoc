package co.com.soaint.foundation.canonical.correspondencia.constantes;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum TipoRadicacionEnum {

    ENTRADA,
    SALIDA,
    INTERNA;

    public static TipoRadicacionEnum getTipoRadicacionBy(String nameOrKey) {
        if (null == nameOrKey || "".equals(nameOrKey.trim())) {
            return null;
        }
        final String stateString = nameOrKey.trim();
        final Stream<TipoRadicacionEnum> stream = Arrays.stream(TipoRadicacionEnum.values());
        final Optional<TipoRadicacionEnum> optionalType = stream
                .filter(type -> type.name().equalsIgnoreCase(stateString))
                .findFirst();
        return optionalType.orElse(null);
    }
}
