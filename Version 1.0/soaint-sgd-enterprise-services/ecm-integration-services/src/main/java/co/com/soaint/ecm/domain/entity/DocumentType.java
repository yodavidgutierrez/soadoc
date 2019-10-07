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
public enum DocumentType implements Serializable {

    MAIN("Principal"),
    ATTACHED("Anexo");

    private static Long serialVersionUID = 133L;
    private final String type;

    public static DocumentType getDocumentTypeBy(String typeName) {
        if (StringUtils.isEmpty(typeName)) {
            return null;
        }
        final String tmpType = typeName;
        final Stream<DocumentType> stream = Arrays.stream(DocumentType.values());
        final Optional<DocumentType> optionalType = stream
                .filter(type -> type.getType().equalsIgnoreCase(tmpType)
                        || type.name().equalsIgnoreCase(tmpType))
                .findFirst();
        return optionalType.orElse(null);
    }
}
