package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.TemplateType;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.Image;

import java.io.Serializable;

public interface ContentTemplate extends Serializable {

    String loadTemplateFromResources(String filename) throws SystemException;

    String getBodyContentFromHtml(String htmlContent);

    Image getWaterMarkImage(TemplateType templateType) throws SystemException;
}
