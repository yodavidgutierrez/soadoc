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
public enum TipoRemitenteEnum {

    EXTERNO("EXT", "EXTERNO"),
    INTERNO("INT", "INTERNO");

    private final String codigo;
    private final String nombre;

    public static TipoRemitenteEnum getTipoRemitenteBy(String nameOrKey) {
        if (null == nameOrKey || "".equals(nameOrKey.trim())) {
            return null;
        }
        final String stateString = nameOrKey.trim();
        final Stream<TipoRemitenteEnum> stream = Arrays.stream(TipoRemitenteEnum.values());
        final Optional<TipoRemitenteEnum> optionalType = stream
                .filter(type -> type.getNombre().equalsIgnoreCase(stateString)
                        || type.getCodigo().equalsIgnoreCase(stateString)
                        || type.name().equalsIgnoreCase(stateString))
                .findFirst();
        return optionalType.orElse(null);
    }
}
