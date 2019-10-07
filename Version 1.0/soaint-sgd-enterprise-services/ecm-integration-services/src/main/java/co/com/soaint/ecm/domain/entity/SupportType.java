package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum SupportType implements Serializable {

    PHYSICAL("Fisico"),
    ELECTRONIC("Electronico"),
    HYBRID("Hibrido");

    private static Long serialVersionUID = 133L;
    private final String support;

    public static SupportType getActionBy(String support) {
        if (StringUtils.isEmpty(support)) {
            return null;
        }
        final Stream<SupportType> stream = Arrays.stream(SupportType.values());
        final Optional<SupportType> optionalType = stream
                .filter(type -> type.getSupport().equalsIgnoreCase(support)
                        || type.name().equalsIgnoreCase(support))
                .findFirst();
        return optionalType.orElse(null);
    }
}
