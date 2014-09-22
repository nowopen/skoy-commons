package com.nowopen.encrypt;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by admin@OYJ on 2014/9/19.
 */
public class SecurityAESService implements SecurityService {
    private final Logger logger = Logger.getLogger(SecurityAESService.class);

    private static SecurityAESService instance = new SecurityAESService();

    private SecurityAESService() {
    }

    public static SecurityAESService getInstance() {
        return instance;
    }

    @Override
    public String encrypt(String plainString) {
        try {
            int num = SecurityUtil.getInstance().getAesKeys().size();
            int line = plainString.hashCode() % num;
            line = line < 0 ? -line : line;

            byte[] data = encrypt(plainString.getBytes(SecurityUtil.CHAR_ENCODING), SecurityUtil.getInstance().getDesKeys().get(line));
            byte[] index = ByteBuffer.allocate(4).putInt(line).array();
            byte[] total = SecurityUtil.concat(index, data);

            return new String(Base64.encode(total), SecurityUtil.CHAR_ENCODING);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String decrypt(String encryptedString) {
        try {
            byte[] total = Base64.decode(encryptedString.getBytes(SecurityUtil.CHAR_ENCODING));
            int index = ByteBuffer.wrap(Arrays.copyOfRange(total, 0, 4)).getInt();
            byte[] data = decrypt(Arrays.copyOfRange(total, 4, total.length), SecurityUtil.getInstance().getDesKeys().get(index));
            return new String(data, SecurityUtil.CHAR_ENCODING);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] decrypt(byte[] encryptedData, String theKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(SecurityUtil.KEY_ALGORITHM_AES);
        kgen.init(128, new SecureRandom(theKey.getBytes()));
        SecretKey secretKey = kgen.generateKey();

        SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), SecurityUtil.KEY_ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(SecurityUtil.KEY_ALGORITHM_AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(encryptedData);
        return result;
    }

    private byte[] encrypt(byte[] data, String theKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(SecurityUtil.KEY_ALGORITHM_AES);
        kgen.init(128, new SecureRandom(theKey.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        
        SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), SecurityUtil.KEY_ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(SecurityUtil.KEY_ALGORITHM_AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(data);
        return result;
    }

}
