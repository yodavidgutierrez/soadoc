package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentTemplate;
import co.com.soaint.ecm.domain.entity.TemplateType;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.Image;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;

@Log4j2
@Getter
@Service("contentTemplate")
public class ContentTemplateImpl implements ContentTemplate {

    private static final Long serialVersionUID = 1547564564L;

    private final ClassLoader classLoader = getClass().getClassLoader();

    @Override
    public String loadTemplateFromResources(String filename) throws SystemException {
        final TemplateType templateType = TemplateType.getTemplateNameBy(filename);
        try {
            if (null == templateType) {
                throw new SystemException("Wrong template name!! must be oficio or memorando, current value: " + filename);
            }
            final URL url = classLoader.getResource(templateType.getHtmlTempate());
            final byte[] htmlContentBytes = null != url ? IOUtils.toByteArray(url) : new byte[0];
            final String htmlContent = new String(htmlContentBytes, StandardCharsets.UTF_8);
            return formatHtmlContent(htmlContent);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }

    @Override
    public String getBodyContentFromHtml(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Element body = doc.body();
        return !StringUtils.isEmpty(body) ? body.html() : "";

    }

    @Override
    public Image getWaterMarkImage(TemplateType templateType) throws SystemException {
        final URL url = classLoader.getResource(templateType.getWaterMark());
        if (null == url) {
            throw new SystemException("");
        }
        try {
            return Image.getInstance(IOUtils.toByteArray(url));
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }

    private String formatHtmlContent(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        doc.getElementsByTag("meta").remove();
        return doc.html();
    }
}
