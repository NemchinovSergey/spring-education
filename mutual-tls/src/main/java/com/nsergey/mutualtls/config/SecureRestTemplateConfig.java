package com.nsergey.mutualtls.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;

@Slf4j
@Configuration
public class SecureRestTemplateConfig {

    public static final String SUN_X_509 = "SunX509";

    @Value("${secure-rest.keyStore}")
    String keyStore;

    @Value("${secure-rest.keyStorePassword}")
    char[] keyStorePassword;

    @Value("${secure-rest.keyPassword}")
    char[] keyPassword;

    /**
     * URL location, typically with file:// scheme, of a CA trust store file in JKS format.
     */
    @Value("${secure-rest.trustStore}")
    String trustStore;

    /**
     * The store password of the given trust store.
     */
    @Value("${secure-rest.trustStorePassword}")
    char[] trustStorePassword;

    /**
     * One of the SSLContext algorithms listed at
     * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SSLContext .
     */
    @Value("${secure-rest.protocol}")
    String protocol = "TLSv1.2";

    @Bean
    public RestTemplate restTemplate() {
        try {
            File keyStoreFile = new DefaultResourceLoader(null).getResource(keyStore).getFile();

            SSLContext sslContext = SSLContextBuilder.create()
                    .setKeyStoreType(KeyStore.getDefaultType()) // JKS or p12
                    .setKeyManagerFactoryAlgorithm(SUN_X_509)
                    .setTrustManagerFactoryAlgorithm(SUN_X_509)
                    .loadKeyMaterial(keyStoreFile, keyStorePassword, keyPassword)
                    .loadTrustMaterial(new TrustAllStrategy())
                    .setProtocol(protocol)
                    .build();

            LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            final HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
            return restTemplate;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to setup client SSL context", e);
        } finally {
            // it's a good security practice to zero out passwords,
            // which is why they're char[]
            Arrays.fill(keyStorePassword, (char) 0);
            Arrays.fill(keyPassword, (char) 0);
            Arrays.fill(trustStorePassword, (char) 0);
        }
    }

    /**
     * <pre>
     * KeyStore keyStore = loadKeyStore("/keystore.jks", keyStorePassword);
     * Key key = keyStore.getKey("keyAlias", keyPassword);
     * log.info("Key: {}", key);
     * </pre>
     */
    private KeyStore loadKeyStore(String keystorePath, String keystorePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        try(InputStream keystoreInputStream = SecureRestTemplateConfig.class.getResourceAsStream(keystorePath)) {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(keystoreInputStream, keystorePassword.toCharArray());
            return keystore;
        }
    }
}
