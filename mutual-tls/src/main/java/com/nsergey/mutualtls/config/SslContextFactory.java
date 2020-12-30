package com.nsergey.mutualtls.config;

import org.springframework.context.annotation.Configuration;

import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class SslContextFactory {

   // @Bean
    public TrustManagerFactory init() throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init((KeyStore) null);
        //factory.getTrustManagers() .filterIsInstance<X509TrustManager>().first()
        return factory;
    }
}
