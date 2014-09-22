package com.nowopen.encrypt;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by admin@OYJ on 2014/9/19.
 */
public class SecurityDESedeService implements SecurityService {
    private final Logger logger = Logger.getLogger(SecurityDESedeService.class);

    private static SecurityDESedeService instance = new SecurityDESedeService();
    private SecurityDESedeService() {
    }

    public static SecurityDESedeService getInstance() {
        return instance;
    }

    @Override
    public String encrypt(String plainString) {
        try {
            int num = SecurityUtil.getInstance().getDesKeys().size();
            int line = plainString.hashCode() % num;
            line = line < 0 ? -line : line;

            byte[] data = encrypt(plainString.getBytes(SecurityUtil.CHAR_ENCODING), SecurityUtil.getInstance().getDesKeys().get(line));
            byte[] index = ByteBuffer.allocate(4).putInt(line).array();
            byte[] total = SecurityUtil.concat(index, data);

            return new String(Base64.encode(total),SecurityUtil.CHAR_ENCODING);
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
            return new String(data,SecurityUtil.CHAR_ENCODING);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private byte[] encrypt(byte[] data, String theKey) throws Exception {
        byte[] key = Base64.decode(theKey.getBytes());
        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SecurityUtil.KEY_ALGORITHM_DESEDE);
        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(SecurityUtil.KEY_ALGORITHM_DESEDE);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    private byte[] decrypt(byte[] data, String theKey) throws Exception {
        byte[] key = Base64.decode(theKey.getBytes());
        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SecurityUtil.KEY_ALGORITHM_DESEDE);
        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(SecurityUtil.KEY_ALGORITHM_DESEDE);
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

}
