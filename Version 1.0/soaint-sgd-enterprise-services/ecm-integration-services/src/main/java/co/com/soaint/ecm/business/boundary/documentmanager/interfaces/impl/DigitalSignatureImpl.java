package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.DigitalSignature;
import co.com.soaint.foundation.canonical.ecm.FirmaDigitalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

@Log4j2
@Service("digitalSignature")
public class DigitalSignatureImpl implements DigitalSignature {

    private final String digitalSignatureUrl;

    public DigitalSignatureImpl(@Value("${digital.signature.url}") String digitalSignatureUrl) {
        this.digitalSignatureUrl = digitalSignatureUrl;
    }

    @Override
    public byte[] signPDF(FirmaDigitalDTO firmaDigitalDTO) throws SystemException {
        log.info("DigitalSignatureImpl - signPDF == Ingreso al servicio de firma Digital");
        try {
            //byte[] documento = new byte[0];
            /*WSDigitalPDF_Service service  = new WSDigitalPDF_Service();
            ElectronicSignature electronicSignature = new ElectronicSignature();
            electronicSignature.setElectronicData(firmaDigitalDTO.getPdfDocument());
             = firmaDigitalDTO.getPdfDocument();
            for (String politica :firmaDigitalDTO.getIdPolitica()) {
                Respuesta sgdea = service.getWSDigitalPDFPort().procesarPDF(
                        firmaDigitalDTO.getIdCliente(),
                        firmaDigitalDTO.getPasswordCliente(),
                        politica,
                        documento,
                        "","","","",
                        electronicSignature);
                documento=sgdea.getDocumento();
            }*/

            byte[] pdfToSign = firmaDigitalDTO.getPdfDocument();
            char[] charArrayKeystorePassword = "admin123".toCharArray();
            KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
            ks.load(new FileInputStream("/usr/lib/jvm/java-1.8.0-openjdk-amd64/bin/keysoaint.jks"), charArrayKeystorePassword);


            String alias = (String) ks.aliases().nextElement();
            PrivateKey key = (PrivateKey) ks.getKey(alias, charArrayKeystorePassword);
            Certificate[] chain = ks.getCertificateChain(alias);

            PdfReader reader = new PdfReader(pdfToSign);
            FileOutputStream fout = new FileOutputStream("/sdo/app/wildfly-10.0.0.Final/tmp/pdfSign.pdf");


            PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');

            PdfSignatureAppearance sap = stp.getSignatureAppearance();

            sap.setReason("Documento PDF SOADOC ");
            sap.setLocation("Colombia");
            sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
            sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
            sap.setVisibleSignature(new Rectangle(400, 510, 500, 560), 1, null);

            ExternalSignature es = new PrivateKeySignature(key, "SHA-256", "BC");
            ExternalDigest digest = new BouncyCastleDigest();
            MakeSignature.signDetached(sap, digest, es, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

            File pdfFirmado = new File("/sdo/app/wildfly-10.0.0.Final/tmp/pdfSign.pdf");

            byte[] pdfSign = Files.readAllBytes(pdfFirmado.toPath());
            if (pdfFirmado.delete()){
                log.info("pdf firmado ha sido eliminado del la carpeta temporal");
            }else{
                log.info("error eliminando el pdf firmado de la carpeta temporal");
            }

            return pdfSign;

            //return firmaDigitalDTO.getPdfDocument();

        } catch (Exception e) {
            log.error("No se pudo establecer la conexion con el servicio expuesto '{}'", digitalSignatureUrl);
            log.warn("No se le ha realizado la firma digital al documento");
            log .info(e.getMessage());
            return null;
        }
    }

}
