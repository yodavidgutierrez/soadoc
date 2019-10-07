package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by amartinez on 14/02/2018.
 */
@Getter
@AllArgsConstructor
public enum DiposicionFinalEnum {
    RETENER("0", "retain"),
    CONSERVACION_TOTAL("1", "transfer"),
    ELIMINACION("2", "destroy"),
    PORDEFINIR("3", "select"),
    //PORDEFINIR("3", "denifir"),
    INTERRUMPIR("4", "cutoff"),
    INCORPORACION("5", "accession");

    private final String codigo;
    private final String nombre;

    public static DiposicionFinalEnum obtenerClave(String codeOrName) {
        if (StringUtils.isEmpty(codeOrName)) {
            return null;
        }
        final Stream<DiposicionFinalEnum> stream = Arrays.stream(DiposicionFinalEnum.values());
        final Optional<DiposicionFinalEnum> optionalType = stream
                .filter(type -> type.getCodigo().equalsIgnoreCase(codeOrName)
                        || type.name().equalsIgnoreCase(codeOrName))
                .findFirst();
        return optionalType.orElse(null);
    }

}

