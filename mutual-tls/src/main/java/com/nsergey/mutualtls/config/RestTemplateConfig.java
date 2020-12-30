package com.nsergey.mutualtls.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RestTemplateConfig {

    /*@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }*/

    /*@Bean
    public SecureRestTemplateCustomizer customRestTemplateCustomizer() {
        return new SecureRestTemplateCustomizer();
    }*/

    /*private static Resource makeResource(final String path) {
        if (path.startsWith(FILE_RESOURCE_PREFIX)) {
            return new FileResource(new File(path.substring(FILE_RESOURCE_PREFIX.length())));
        } else if (path.startsWith(CLASSPATH_RESOURCE_PREFIX)) {
            return new ClassPathResource(path.substring(CLASSPATH_RESOURCE_PREFIX.length()));
        }
        // Assume a path without a known prefix is a file
        return new FileResource(new File(path));
    }

    private HttpClient createHttpClient(final String keyAlias) {
        log.info("Creating HTTP client using keystore={} and alias={}", keystorePath, keyAlias);
        final KeyStore trustStore = new KeyStoreFactoryBean(
                makeResource("classpath:/truststore.jks"), "JKS", "changeit").newInstance();
        final KeyStore keyStore = new KeyStoreFactoryBean(
                makeResource(keystorePath), keystoreType, keystorePassword).newInstance();
        final SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> keyAlias)
                    .loadTrustMaterial(trustStore, (x509Certificates, s) -> false)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            throw new IllegalStateException("Error loading key or trust material", e);
        }
        final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[] { "TLSv1.2" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(httpClientPoolSize);
        connectionManager.setDefaultMaxPerRoute(httpClientPoolSize);
        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setConnectionManager(connectionManager)
                .build();
    }*/

}
