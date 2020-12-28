package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.util.Arrays;

@Component
//@EnableConfigurationProperties(SecureRestTemplateProperties.class)
@Slf4j
public class SecureRestTemplateCustomizer implements RestTemplateCustomizer {

    /**
     * URL location, typically with file:// scheme, of a CA trust store file in JKS format.
     */
    @Value("secure-rest.trustStore")
    String trustStore;

    /**
     * The store password of the given trust store.
     */
    @Value("secure-rest.trustStorePassword")
    char[] trustStorePassword;

    /**
     * One of the SSLContext algorithms listed at
     * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SSLContext .
     */
    @Value("secure-rest.protocol")
    String protocol = "TLSv1.2";

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        customize(restTemplate);
        return restTemplate;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        final SSLContext sslContext;
        try {
            sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(new URL(trustStore), trustStorePassword)
                    .setProtocol(protocol)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to setup client SSL context", e);
        } finally {
            // it's good security practice to zero out passwords,
            // which is why they're char[]
            Arrays.fill(trustStorePassword, (char) 0);
        }

        final HttpClient httpClient = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .build();

        final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        log.info("Registered SSL truststore {} for client requests", trustStore);
        restTemplate.setRequestFactory(requestFactory);
    }
}
