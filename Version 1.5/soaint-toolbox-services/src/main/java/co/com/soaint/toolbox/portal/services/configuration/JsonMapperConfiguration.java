package co.com.soaint.toolbox.portal.services.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonMapperConfiguration {

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

}
