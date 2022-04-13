/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.tools.security;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyTools {
  public static class Algorithm {
    public final static String DIFFIE_HELLMAN = "DH";
    public final static String RSA = "RSA";
    public final static String AES = "AES";
  }

  public static SecretKey computeSecretKey(
    PrivateKey privateDhKey,
    PublicKey publicDhKey
  ) throws Exception {
    KeyAgreement authAgreement = KeyAgreement.getInstance(Algorithm.DIFFIE_HELLMAN);
    authAgreement.init(privateDhKey);
    authAgreement.doPhase(publicDhKey, true);
    byte[] sharedSecret = authAgreement.generateSecret();
    return new SecretKeySpec(sharedSecret, 0, 32, Algorithm.AES);
  }

  /**
   * Converts string which contains public key in PEM format to PublicKey object
   *
   * @param pem public key in PEM format
   * @return PublicKey or null
   */
  public static PublicKey convertPemToPublicKey(String pem, String algorithm) {
    try {
      String keyContent = pem
        .replace("\\n", "")
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "");
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      KeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(keyContent));
      return kf.generatePublic(keySpecX509);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Converts string which contains private key (PKCS8) in PEM format to PrivateKey object
   *
   * @param pem private key in PEM format
   * @return PrivateKey or null
   */
  public static PrivateKey convertPemToPrivateKey(String pem, String algorithm) {
    try {
      String keyContent = pem.replace("\\n", "")
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("-----BEGIN RSA PRIVATE KEY-----", "")
        .replace("-----END RSA PRIVATE KEY-----", "");
      KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(keyContent));
      return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
