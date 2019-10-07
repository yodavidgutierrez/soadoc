package co.com.soaint.correspondencia.Utils;

import lombok.extern.log4j.Log4j2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Log4j2
public class DateUtils {

    public  static Calendar DateToCalendar(Date date) {
        try{

            Calendar  cal=Calendar.getInstance();
            cal.setTime(date);

               return  cal;
        }
        catch(Exception e){

            log.info("Excepción al transformar fecha :" + e);

            return null;
        }
    }

    public static Calendar Now(){

        try{

            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date = (Date) formatter.parse(LocalDateTime.now().toString());

            Calendar  cal=Calendar.getInstance();
            cal.setTime(date);

            return  cal;
        }
        catch(Exception e){

            return null;
        }
    }

    public static String DateToString(String format){

        Calendar calendar = Now();

        if(calendar == null)
             return  null;

        DateFormat dateFormat = new SimpleDateFormat(format);
        return  dateFormat.format(calendar.getTime());
    }

    public static String DateToString(Calendar calendar,String format){

        DateFormat dateFormat = new SimpleDateFormat(format);
        return  dateFormat.format(calendar.getTime());
    }

    public static Date SetTimeZone (String format, Date fecha, TimeZone tz) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(tz);
        String date = sdf.format(fecha);

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date finalDate = formatter.parse(date);
        return  finalDate;
    }


}
