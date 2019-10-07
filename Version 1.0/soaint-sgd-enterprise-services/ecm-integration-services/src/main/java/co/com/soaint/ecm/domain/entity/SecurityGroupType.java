package co.com.soaint.ecm.domain.entity;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Log4j2
@AllArgsConstructor
public enum SecurityGroupType implements Serializable {

    RESERVED("R", "Reservado"),
    PUBLIC("P", "Publico"),
    CLASSIFIED("C", "Clasificado");

    private static final Long serialVersionUID = 2121L;

    private final String abbr;
    private final String securityName;

    public static SecurityGroupType getSecurityBy(String secType) {
        if (StringUtils.isEmpty(secType)) {
            return null;
        }
        final Stream<SecurityGroupType> stream = Arrays.stream(SecurityGroupType.values());
        final Optional<SecurityGroupType> optionalType = stream
                .filter(type -> type.getAbbr().equalsIgnoreCase(secType)
                        || type.name().equalsIgnoreCase(secType)
                        || type.getSecurityName().equalsIgnoreCase(secType))
                .findFirst();
        return optionalType.orElse(null);
    }

    public static List<SecurityGroupType> getSecurityListBy(List<String> roleList) {
        if (ObjectUtils.isEmpty(roleList)) {
            return null;
        }
        return Arrays.stream(SecurityGroupType.values()).filter(securityGroupType -> roleList.stream().anyMatch(role ->
                StringUtils.endsWithIgnoreCase(Utilities.reemplazarCaracteresRaros(role),
                        securityGroupType.getSecurityName()))).collect(Collectors.toList());
    }
}
