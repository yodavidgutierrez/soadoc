package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Log4j2
@AllArgsConstructor
public enum TransferType implements Serializable {

    PRIMARY("primaria"),
    SECONDARY("secundaria");

    private static final Long serialVersionUID = 199L;

    private final String transferName;

    public static TransferType getTransferBy(@NotNull String transferName) {
        final Stream<TransferType> stream = Arrays
                .stream(TransferType.values());
        final Optional<TransferType> optionalType = stream
                .filter
                        (type -> type.getTransferName().equalsIgnoreCase(transferName)
                                || type.name().equalsIgnoreCase(transferName))
                .findFirst();
        return optionalType.orElse(null);
    }
}
