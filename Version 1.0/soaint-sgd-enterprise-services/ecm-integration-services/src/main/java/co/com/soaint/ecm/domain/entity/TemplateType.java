package co.com.soaint.ecm.domain.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum TemplateType implements Serializable {

    OFICIO,
    MEMORANDO;

    private final String templateFolderPath = "plantillas/";

    private final String htmlFolderPath = templateFolderPath + "html/";
    private final String imageFolderPath = templateFolderPath + "image/";

    private static final Long serialVersionUID = 1L;

    public static TemplateType getTemplateNameBy(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        final String tmpTemplate = name;
        final Stream<TemplateType> stream = Arrays.stream(TemplateType.values());
        final Optional<TemplateType> optionalType = stream
                .filter(type -> type.name().equalsIgnoreCase(tmpTemplate))
                .findFirst();
        return optionalType.orElse(null);
    }

    public String getHtmlTempate() {
        return htmlFolderPath + name().toLowerCase() + ".html";
    }

    public String getWaterMark() {
        return imageFolderPath + name().toLowerCase() + ".jpg";
    }
}
