package com.kakao.actionbase.v2.core.edge;

import com.kakao.actionbase.v2.core.code.CryptoUtils;
import com.kakao.actionbase.v2.core.code.EdgeEncoderFactory;
import com.kakao.actionbase.v2.core.code.IdEdgeEncoder;
import com.kakao.actionbase.v2.core.code.KeyValue;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;

public class CryptoUtilsTest {

  SecretKey generateAESKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    SecureRandom secureRandom = new SecureRandom();
    keyGenerator.init(128, secureRandom);
    return keyGenerator.generateKey();
  }

  @RepeatedTest(10)
  void testEncryptDecrypt() {
    String originalText = "0123456789";

    try {
      String encryptedText = CryptoUtils.encryptAndEncodeUrlSafe(originalText.getBytes());

      byte[] decryptedBytes = CryptoUtils.decodeAndDecryptUrlSafe(encryptedText);
      String decryptedText = new String(decryptedBytes);

      Assertions.assertEquals(originalText, decryptedText);
    } catch (Exception e) {
      Assertions.fail(
          "An exception occurred during the encrypt/decrypt process: " + e.getMessage());
    }
  }

  @RepeatedTest(10)
  void testEdgeId() {
    EdgeEncoderFactory factory = new EdgeEncoderFactory(1);
    IdEdgeEncoder encoder = (IdEdgeEncoder) factory.getBytesKeyValueEncoder();

    long src = 123;
    long dst = 456;

    String encryptedEdgeId = encoder.encode(src, dst);
    Assertions.assertNotNull(encryptedEdgeId);

    System.out.println(
        "Encrypted edge id: " + encryptedEdgeId + " (" + encryptedEdgeId.length() + " bytes)");

    KeyValue<Object> decodedEdgeId = encoder.decode(encryptedEdgeId);
    Assertions.assertNotNull(decodedEdgeId);
    Assertions.assertEquals(src, decodedEdgeId.getKey());
    Assertions.assertEquals(dst, decodedEdgeId.getValue());
  }

  @Disabled
  void testGenerateAESKey() {
    try {
      byte[] key = generateAESKey().getEncoded();
      Assertions.assertEquals(16, key.length);

      System.out.println(
          "new byte[] {"
              + key[0]
              + ", "
              + key[1]
              + ", "
              + key[2]
              + ", "
              + key[3]
              + ", "
              + key[4]
              + ", "
              + key[5]
              + ", "
              + key[6]
              + ", "
              + key[7]
              + ", "
              + key[8]
              + ", "
              + key[9]
              + ", "
              + key[10]
              + ", "
              + key[11]
              + ", "
              + key[12]
              + ", "
              + key[13]
              + ", "
              + key[14]
              + ", "
              + key[15]
              + "};");

    } catch (Exception e) {
      Assertions.fail("An exception occurred during the key generation process: " + e.getMessage());
    }
  }
}
