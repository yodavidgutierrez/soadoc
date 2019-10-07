package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.TemplateType;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public interface ContentStamper extends Serializable {

    byte[] getStampedDocument(byte[] stampImg, byte[] contentBytes, String mimeType) throws SystemException;

    byte[] getStampedHtmlDocument(byte[] stampImg, byte[] contentBytes, TemplateType templateType) throws SystemException;

    byte[] getResizedPdfDocument(byte[] pdfContent) throws SystemException;

    byte [] convertPdfToPdfaDocument( byte[] streamOfPDFFiles) throws SystemException;
}
