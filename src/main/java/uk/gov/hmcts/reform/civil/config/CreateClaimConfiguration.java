package uk.gov.hmcts.reform.civil.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CreateClaimConfiguration {

    private final String url;

    public CreateClaimConfiguration(@Value("${civil-service.create_claim.url}") String url) {
        this.url = url;
    }

}