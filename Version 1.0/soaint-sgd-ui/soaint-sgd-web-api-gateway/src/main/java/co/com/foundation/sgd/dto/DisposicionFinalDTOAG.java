package co.com.foundation.sgd.dto;

import co.com.soaint.foundation.canonical.ecm.DisposicionFinalDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

@Data()
@ToString(callSuper = true)
@XmlRootElement
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
@Log4j2
public class DisposicionFinalDTOAG  extends DisposicionFinalDTO {

  public   static  DisposicionFinalDTOAG fromString(String json){

      ObjectMapper mapper = new ObjectMapper();

      try {
          return  mapper.readValue(json,DisposicionFinalDTOAG.class);
      } catch (IOException e) {

          log.info("Error:"+ e );
          return null;
      }
  }
}
