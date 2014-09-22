package com.nowopen.encrypt;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * Created by admin@OYJ on 2014/9/18.
 */
public class SecurityRSAService implements SecurityService {
    private final Logger logger = Logger.getLogger(SecurityRSAService.class);

    private static SecurityRSAService instance = new SecurityRSAService();
    private SecurityRSAService() {
    }

    private static final int MAX_ENCRYPT_BLOCK = 117;//117 for init size 1024; 245 for 2048
    private static final int MAX_DECRYPT_BLOCK = 128;//128 for init size 1024; 256 for 2048

    public static SecurityRSAService getInstance() {
        return instance;
    }

    /**
     * @param plainString - 需要被加密的字符串
     * @return String - 加密后的BASE64编码过的字符串
     */
    @Override
    public String encrypt(String plainString) {
        try {
            int num = SecurityUtil.getInstance().getPrivateKeys().size();
            if(num != SecurityUtil.getInstance().getPublickKeys().size()) {
                throw new Exception("ERROR: PrivateKey doesn't match the PublicKey!");
            }
            int line = plainString.hashCode() % num;
            line = line < 0 ? -line : line;

            byte[] data = encryptByPublicKey(plainString.getBytes(SecurityUtil.CHAR_ENCODING), SecurityUtil.getInstance().getPublickKeys().get(line));
            byte[] index = ByteBuffer.allocate(4).putInt(line).array();
            byte[] total = SecurityUtil.concat(index, data);

            return new String(Base64.encode(total), SecurityUtil.CHAR_ENCODING);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param encryptedString - BASE64编码的加密字符串
     * @return String - 解密后的明文字符串
     */
    @Override
    public String decrypt(String encryptedString) {
        try {
            byte[] total = Base64.decode(encryptedString.getBytes(SecurityUtil.CHAR_ENCODING));
            int index = ByteBuffer.wrap(Arrays.copyOfRange(total, 0, 4)).getInt();
            byte[] data = decryptByPrivateKey(Arrays.copyOfRange(total, 4, total.length), SecurityUtil.getInstance().getPrivateKeys().get(index));
            return new String(data, SecurityUtil.CHAR_ENCODING);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityUtil.KEY_ALGORITHM_RSA);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityUtil.KEY_ALGORITHM_RSA);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityUtil.KEY_ALGORITHM_RSA);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityUtil.KEY_ALGORITHM_RSA);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

}
