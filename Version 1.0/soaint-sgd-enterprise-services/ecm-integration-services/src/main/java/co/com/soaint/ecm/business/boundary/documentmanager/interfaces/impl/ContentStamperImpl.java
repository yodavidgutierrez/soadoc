package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentTemplate;
import co.com.soaint.ecm.domain.entity.ImagePositionType;
import co.com.soaint.ecm.domain.entity.TemplateType;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

@Log4j2
@Getter
@Service("contentStamper")
public final class ContentStamperImpl implements ContentStamper {

    private static final Long serialVersionUID = 155L;

    private final ImagePositionType positionType;
    private final ContentTemplate contentTemplate;

    @Autowired
    public ContentStamperImpl(@Value("${pdf.image.location}") String imagePosition,
                              @Qualifier("contentTemplate") ContentTemplate contentTemplate) {
        this.positionType = ImagePositionType.valueOf(imagePosition.toUpperCase());
        this.contentTemplate = contentTemplate;
    }

    /*
     @Override
    public byte[] getStampedDocument(final byte[] stamperImg, byte[] contentBytes, String mimeType) throws SystemException {
        log.info("Ejecutando el metodo que estampa una imagen en un documento HTML y luego lo convierte a PDF");
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            boolean isHtml = false;
            if (MimeTypes.getMIMEType("html").equals(mimeType)) {
                final Document document = new Document(PageSize.A4);
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                document.open();
                final Charset UTF8_CHARSET = Charset.forName("UTF-8");
                final String htmlCad = new String(contentBytes, UTF8_CHARSET);
                contentBytes = (top() + htmlCad + bottom()).getBytes(UTF8_CHARSET);
                final XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
                final InputStream is = new ByteArrayInputStream(contentBytes);
                worker.parseXHtml(writer, document, is, Charset.forName("UTF-8"));
                document.close();
                contentBytes = outputStream.toByteArray();
                outputStream.flush();
                outputStream.close();
                is.close();
                isHtml = true;
            }
            final PdfReader reader = new PdfReader(contentBytes);
            float absoluteY = 695F;
            if (!isHtml) {
                resizePdf(reader);
                absoluteY = 659.301F;
            }
            final PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);
            final Image image = getImage(stamperImg);
            image.setAbsolutePosition(370F, absoluteY); //695
            final PdfImage stream = new PdfImage(image, "", null);
            PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
            image.setDirectReference(ref.getIndirectReference());
            final PdfContentByte over = stamper.getOverContent(1);
            over.addImage(image);
            stamper.flush();
            stamper.close();
            reader.close();
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            log.error("Ocurrio un error al poner la etiqueta en el PDF");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }
    */

    @Override
    public byte[] getStampedDocument(final byte[] stamperImg, byte[] contentBytes, String mimeType) throws SystemException {
        log.info("Ejecutando el metodo que estampa una imagen en un documento HTML y luego lo convierte a PDF");
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            float absoluteY = 659.301F;
            if (MimeTypes.getMIMEType("html").equals(mimeType)) {
                /*
                final Document document = new Document(PageSize.A4);
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                document.open();
                final Charset utf8Charset = Charset.forName("UTF-8");
                final String htmlCad = new String(contentBytes, utf8Charset);
                //contentBytes = (top() + replaceBrTag(htmlCad) + bottom()).getBytes(utf8Charset);
                contentBytes = getFullTemplate(replaceBrTag(htmlCad)).getBytes(utf8Charset);
                final XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
                final InputStream is = new ByteArrayInputStream(contentBytes);
                worker.parseXHtml(writer, document, is, utf8Charset);
                document.close();
                contentBytes = outputStream.toByteArray();
                absoluteY = 695F;
                outputStream.flush();
                outputStream.close();
                is.close();*/
            }
            final PdfReader reader = new PdfReader(contentBytes);
            final PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);
            if (!ObjectUtils.isEmpty(stamperImg)) {
                final Image image = getImage(stamperImg);
                image.setAbsolutePosition(370F, absoluteY); //695
                final PdfImage stream = new PdfImage(image, "", null);
                PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
                image.setDirectReference(ref.getIndirectReference());
                final PdfContentByte over = stamper.getOverContent(1);
                over.addImage(image);
            }
            stamper.flush();
            stamper.close();
            reader.close();
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            log.error("Ocurrio un error al poner la etiqueta en el PDF");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
        // return null;
    }

    @Override
    public byte[] getStampedHtmlDocument(byte[] stampImg, byte[] contentBytes, TemplateType templateType) throws SystemException {

        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final Document document = new Document(PageSize.LETTER);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            final Charset utf8Charset = Charset.forName("UTF-8");
            final String htmlCad = new String(contentBytes, utf8Charset);
            //contentBytes = (top() + replaceBrTag(htmlCad) + bottom()).getBytes(utf8Charset);
            contentBytes = getFullTemplate(templateType, replaceBrTag(htmlCad)).getBytes(utf8Charset);
            final XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            final InputStream is = new ByteArrayInputStream(contentBytes);
            worker.parseXHtml(writer, document, is, utf8Charset);
            document.close();
            contentBytes = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            is.close();
            final PdfReader reader = new PdfReader(contentBytes);
            final PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);
            addWaterMark(reader, stamper, document, templateType);
            if (null != stampImg) {
                final Image image = getImage(stampImg);
                final PdfImage stream = new PdfImage(image, "", null);
                PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
                image.setDirectReference(ref.getIndirectReference());
                final PdfContentByte over = stamper.getOverContent(1);
                over.addImage(image);
            }

            stamper.flush();
            stamper.close();
            reader.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("Ocurrio un error al poner la etiqueta en el PDF");
            throw new SystemException(e.getMessage());
        }
    }

    //Conversion Pdf a Pdfa por: Erick Saavedra, Carlos Gutierrez
    @Override
    public byte[] convertPdfToPdfaDocument(byte[] streamOfPDFFiles) throws SystemException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Rectangle documentSize = new RectangleReadOnly(612,792);
            Document document = new Document(documentSize);
            PdfAWriter writer;
            writer = PdfAWriter.getInstance(document, outputStream, PdfAConformanceLevel.PDF_A_1B);
            writer.setPdfVersion(PdfWriter.VERSION_1_4);
            writer.createXmpMetadata();
            PdfReader pdfReader = new PdfReader(streamOfPDFFiles);
            writer.freeReader(pdfReader);
            document.open();
            com.itextpdf.text.pdf.PdfContentByte cb = writer.getDirectContent();

            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                document.newPage();

                PdfImportedPage page = writer.getImportedPage(pdfReader, i);
                /**Rectangle pagesize = new Rectangle(8.5f*72, 11f*72);
                float oWidth = pagesize.getWidth();
                float oHeight = pagesize.getHeight();
                float scale = getScale(oWidth, oHeight);
                cb.addTemplate(page, scale, 0, 0, scale, 0, 0);*/
                cb.addTemplate(page, 0, 0);
            }

            document.close();
            pdfReader.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    private static float getScale(float width, float height) {
        float scaleX = PageSize.A4.getWidth() / width;
        float scaleY = PageSize.A4.getHeight() / height;
        return Math.min(scaleX, scaleY);
    }


    @Override
    public byte[] getResizedPdfDocument(byte[] pdfContent) throws SystemException {
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final PdfReader reader = new PdfReader(pdfContent);
            resizePdf(reader);
            final PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);
            stamper.flush();
            stamper.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("Ocurrio un error al poner la etiqueta en el PDF");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private Image getImage(byte[] imageBytes) throws IOException, BadElementException {
        final Image image = Image.getInstance(imageBytes);
        image.setScaleToFitHeight(true);
        image.setScaleToFitLineWhenOverflow(true);
        //A4
        //image.setAbsolutePosition(315F, 670F); //695
        //Carta
        image.setAbsolutePosition(320F, 620F); //695
        image.scalePercent(50, 60);
//        image.scaleAbsolute(260F, 100F);
        return image;
    }

    private String getFullTemplate(TemplateType templateType, String bodyContent) throws SystemException {
        String htmlContent = contentTemplate.loadTemplateFromResources(templateType.name());
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlContent);
        bodyContent = bodyContent.contains("<body>")
                ? contentTemplate.getBodyContentFromHtml(bodyContent) : bodyContent;
        htmlDoc.body().html(bodyContent);
        return htmlDoc.html().replaceAll("<br>", "<br/>");
    }

    private void resizePdf(PdfReader reader) {

        float width = 8.5f * 72;
        float height = 11f * 72;

        final Rectangle cropBox = reader.getCropBox(1);
        float widthToAdd = width - cropBox.getWidth();
        float heightToAdd = height - cropBox.getHeight();
        float[] newBoxValues = new float[]{
                cropBox.getLeft() - widthToAdd / 2,
                cropBox.getBottom() - heightToAdd / 2,
                cropBox.getRight() + widthToAdd / 2,
                cropBox.getTop() + heightToAdd / 2
        };
        final PdfArray newBox = new PdfArray(newBoxValues);
        final PdfDictionary pageDict = reader.getPageN(1);

        pageDict.put(PdfName.CROPBOX, newBox);
        pageDict.put(PdfName.MEDIABOX, newBox);

        /*for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            Rectangle cropBox = reader.getCropBox(i);
            float widthToAdd = width - cropBox.getWidth();
            float heightToAdd = height - cropBox.getHeight();
            if (Math.abs(widthToAdd) > tolerance || Math.abs(heightToAdd) > tolerance) {
                float[] newBoxValues = new float[] {
                        cropBox.getLeft() - widthToAdd / 2,
                        cropBox.getBottom() - heightToAdd / 2,
                        cropBox.getRight() + widthToAdd / 2,
                        cropBox.getTop() + heightToAdd / 2
                };
                PdfArray newBox = new PdfArray(newBoxValues);

                PdfDictionary pageDict = reader.getPageN(i);
                pageDict.put(PdfName.CROPBOX, newBox);
                pageDict.put(PdfName.MEDIABOX, newBox);
            }
        }*/
    }

    private void addWaterMark(PdfReader reader, PdfStamper stamper, Document document, TemplateType templateType) throws SystemException {
        try {
            final Image waterMarkImage = contentTemplate.getWaterMarkImage(templateType);
            float w = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float h = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
            waterMarkImage.scaleToFit(w, h);

            w += 80;
            h += 80;

            // properties
            PdfContentByte under;
            Rectangle pagesize;
            float x, y;
            // loop under every page
            int n = reader.getNumberOfPages();

            for (int i = 1; i <= n; i++) {

                // get page size and position
                pagesize = reader.getPageSizeWithRotation(i);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                under = stamper.getUnderContent(i);
                under.saveState();
                final Rectangle readerPageSize = reader.getPageSize(i);
                readerPageSize.setTop(500);
                readerPageSize.setRight(500);
                reader.setPageContent(i, reader.getPageContent(i), 1);

                PdfContentByte over = stamper.getOverContent(i);

                // set transparency
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.5f);
                under.setGState(state);
                under.addImage(waterMarkImage, w, 0, 0, h, x - (w / 2), y - (h / 2));
//            under.addImage(waterMarkImage);
                under.restoreState();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }

    private String replaceBrTag(String htmlCad) {
        return htmlCad.replace("<br>", "<br/>");
    }

    /*private String top() {
        return "<!DOCTYPE html\n" +
                "        PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<head>\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>" +
                "<div style=\"font-size: 11px\">";
    }

    private String bottom() {
        return "</div>\n" +
                "</body>\n" +
                "</html>";
    }*/
}