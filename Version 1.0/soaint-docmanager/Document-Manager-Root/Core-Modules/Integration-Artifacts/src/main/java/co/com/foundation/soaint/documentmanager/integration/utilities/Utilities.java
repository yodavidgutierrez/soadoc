package co.com.foundation.soaint.documentmanager.integration.utilities;

import co.com.foundation.soaint.documentmanager.integration.domain.OrganigramaDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by sarias on 16/03/2017.
 */
public class Utilities {
    public static void ordenarListaOrganigrama(List<OrganigramaDTO> organigramaItem) {

        Collections.sort(organigramaItem, new Comparator<OrganigramaDTO>() {

            @Override
            public int compare(OrganigramaDTO o1, OrganigramaDTO o2) {
                return o1.getIdeOrgaAdmin().compareTo(o2.getIdeOrgaAdmin());
            }
        });
    }

    public static Date getDateFromString(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(date);
        } catch (ParseException e) {

        }
        return null;
    }

    public static String getDateToString(Date date, String format) {
        if (date == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
