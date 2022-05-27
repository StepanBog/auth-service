package ru.bogdanov.diplom.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author Sbgdanov
 * Набор методов для работы с криптографическими функциями и сертефикатами
 */
@Slf4j
@UtilityClass
public class CryptoUtils {

    private final String PATH_TO_TEST_PUBLIC_KEY = "cert/test_certificate.crt";
    private final String PATH_TO_TEST_PRIVATE_KEY = "cert/test_private_key.key";

    private final String SIGN_PRIVATE_KEY_ALIAS = "sign-private_key";
    private final String SIGN_PUBLIC_KEY_ALIAS = "sign-public_key";

    private static KeyStore keyStore = null;

    public static void initKeyStore(char[] keyStorePassword) throws Exception {
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, keyStorePassword);
        keyStore.setKeyEntry(SIGN_PRIVATE_KEY_ALIAS,
                readPrivateKey(), keyStorePassword, new Certificate[]{readCertificate()});
        keyStore.setCertificateEntry(SIGN_PUBLIC_KEY_ALIAS,
                readCertificate());
    }

    private static Key readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File f = new File("C:\\Users\\stepa\\IdeaProjects\\Diplom\\auth-service\\src\\main\\resources\\cert\\test_private_key.key");
        try (InputStream is = new FileInputStream(f)) {
            String privateKeyContent = new String(is.readAllBytes())
                    .replaceAll("\\n", "")
                    .replaceAll("\\r", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpecX509 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
            return kf.generatePrivate(keySpecX509);
        }
    }

    private static Certificate readCertificate() throws IOException, CertificateException {
        File f = new File("C:\\Users\\stepa\\IdeaProjects\\Diplom\\auth-service\\src\\main\\resources\\cert\\test_certificate.crt");
        try (InputStream is = new FileInputStream(f)) {
            return CertificateFactory.getInstance("X509").generateCertificate(is);
        }
    }

    public static Key getSignPrivateKey(char[] keyStorePassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        return keyStore.getKey(SIGN_PRIVATE_KEY_ALIAS, keyStorePassword);
    }

    public static X509Certificate getSignCertificate() throws KeyStoreException {
        return (X509Certificate) keyStore.getCertificate(SIGN_PUBLIC_KEY_ALIAS);
    }
}
