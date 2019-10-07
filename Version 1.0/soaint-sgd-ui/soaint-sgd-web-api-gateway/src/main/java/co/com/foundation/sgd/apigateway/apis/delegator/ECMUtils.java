package co.com.foundation.sgd.apigateway.apis.delegator;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.MultivaluedMap;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by diony on 05/02/2018.
 */
public final class ECMUtils {

    public static  Map<String,InputPart> findFiles( MultipartFormDataInput file){

        Map<String, InputPart> files = new HashMap<>();
        Collection<List<InputPart>> inputParts = file.getFormDataMap().values();
        inputParts.forEach(parts -> parts.forEach(part -> {
            String name = findName(part);
            if (!"".equals(name)) files.put(name, part);
        }));

        return files;
    }


    static String findName(InputPart part) {
        String fileName = "";
        MultivaluedMap<String, String> headers = part.getHeaders();
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                fileName = tmp[1].trim().replaceAll("\"", "");
                try {
                    fileName = URLDecoder.decode(fileName,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

}
