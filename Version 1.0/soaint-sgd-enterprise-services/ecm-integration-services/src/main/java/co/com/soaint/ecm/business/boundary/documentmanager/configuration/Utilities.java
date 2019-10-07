/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author ADMIN
 */
@Log4j2
public class Utilities {

    private Utilities() {
        /**/
    }

    /**
     * Función que elimina acentos y caracteres especiales de
     * una cadena de texto.
     *
     * @param texto
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String reemplazarCaracteresRaros(String texto) {
        String resultText = Normalizer.normalize(texto, Normalizer.Form.NFD);
        resultText = resultText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return resultText;
    }

    public static boolean isEqualOrAfterDate(Calendar dispActionDate) {
        return isEqualOrAfterDate(toLocalDate(dispActionDate));
    }

    public static boolean isEqualOrAfterDate(Temporal temporal) {
        final LocalDate currentDate = LocalDate.now();
        LocalDate localDate = null;
        if (temporal instanceof LocalDateTime) {
            localDate = ((LocalDateTime) temporal).toLocalDate();
        } else if (temporal instanceof LocalDate) {
            localDate = (LocalDate) temporal;
        }
        return localDate != null && (currentDate.isEqual(localDate) || currentDate.isAfter(localDate));
    }

    public static LocalDateTime getDispDateTimeEcm(String xDispDateEcm) {
        try {
            if (!StringUtils.isEmpty(xDispDateEcm)) {
                return LocalDateTime.parse(xDispDateEcm);
            }
        } catch (DateTimeParseException d) {
            log.error("Error while parsing date: " + xDispDateEcm);
        }
        return null;
    }

    public static String removeFileExtension(String filename) {
        if (!StringUtils.isEmpty(filename)) {
            int index = filename.lastIndexOf('.');
            if (index == -1) {
                return filename;
            }
            String extension = filename.substring(index + 1);
            String mimeType = MimeTypes.getMIMEType(extension);
            if (MimeTypes.getMIMEType("").equals(mimeType)) {
                return filename;
            }
            return filename.substring(0, index);
        }
        return "";
    }

    public static Calendar sumarDiasAFecha(Calendar calendar, int dias) {
        if (dias == 0) return calendar;
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return toLocalDateTime(calendar);
    }

    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    private static LocalDate toLocalDate(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        Date input = calendar.getTime();
        Instant instant = input.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        return zdt.toLocalDate();
    }
}
