package com.nowopen.encrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.*;
import java.util.ArrayList;

/**
 * Created by admin@OYJ on 2014/9/19.
 *
 Every implementation of the Java platform is required to support the following standard Cipher transformations with the keysizes in parentheses:
 AES/CBC/NoPadding (128)
 AES/CBC/PKCS5Padding (128)
 AES/ECB/NoPadding (128)
 AES/ECB/PKCS5Padding (128)
 DES/CBC/NoPadding (56)
 DES/CBC/PKCS5Padding (56)
 DES/ECB/NoPadding (56)
 DES/ECB/PKCS5Padding (56)
 DESede/CBC/NoPadding (168)
 DESede/CBC/PKCS5Padding (168)
 DESede/ECB/NoPadding (168)
 DESede/ECB/PKCS5Padding (168)
 RSA/ECB/PKCS1Padding (1024, 2048)
 RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
 RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
 *
 */
public class SecurityUtil {
    private final static int lines = 5000;
    private final  ArrayList<String> priKeyList;
    private final  ArrayList<String> pubKeyList;
    private final  ArrayList<String> desKeyList;
    private final  ArrayList<String> aesKeyList;

    public final static String CHAR_ENCODING = "UTF-8";
    public static final String KEY_ALGORITHM_RSA = "RSA";
    public static final String KEY_ALGORITHM_AES = "AES";
    public static final String KEY_ALGORITHM_DESEDE = "DESede";

    private static volatile SecurityUtil instance = null;
    private SecurityUtil() {
        priKeyList = new ArrayList<String>(lines);
        pubKeyList = new ArrayList<String>(lines);
        aesKeyList = new ArrayList<String>(lines);
        desKeyList = new ArrayList<String>(lines);

        buildKeysList();
    }

    public static SecurityUtil getInstance() {
        if(null == instance) {
            synchronized (SecurityUtil.class) {
                if (null == instance) {
                    instance = new SecurityUtil();
                }
            }
        }
        return instance;
    }

    public ArrayList<String> getPublickKeys() {
        return pubKeyList;
    }

    public ArrayList<String> getPrivateKeys() {
        return priKeyList;
    }

    public ArrayList<String> getDesKeys() {
        return desKeyList;
    }

    public ArrayList<String> getAesKeys() {
        return aesKeyList;
    }

    public static void main(String[] args) {
        int lines = PropertiesUtil.getInstance().getSysConfig().getInt("key.lines");

        String pubFile = PropertiesUtil.getInstance().getSysConfig().getString("key.pubFile");
        String priFile = PropertiesUtil.getInstance().getSysConfig().getString("key.priFile");
        generateRSAKeys(lines, 1024, pubFile, priFile);

        String keyFile = PropertiesUtil.getInstance().getSysConfig().getString("key.aes.keyFile");
        generateKeys(lines, 256, keyFile, KEY_ALGORITHM_AES);

        keyFile = PropertiesUtil.getInstance().getSysConfig().getString("key.des.keyFile");
        generateKeys(lines, 168, keyFile, KEY_ALGORITHM_DESEDE);

    }

    private void buildKeysList() {
        try {
            String pubFile = PropertiesUtil.getInstance().getSysConfig().getString("key.pubFile");
            String priFile = PropertiesUtil.getInstance().getSysConfig().getString("key.priFile");
            String aesFile = PropertiesUtil.getInstance().getSysConfig().getString("key.aes.keyFile");
            String desFile = PropertiesUtil.getInstance().getSysConfig().getString("key.des.keyFile");

            BufferedReader pubKey = new BufferedReader(new FileReader(pubFile));
            BufferedReader priKey = new BufferedReader(new FileReader(priFile));
            BufferedReader aesKey = new BufferedReader(new FileReader(aesFile));
            BufferedReader desKey = new BufferedReader(new FileReader(desFile));

            String pubK = null;
            String priK = null;
            while ((pubK = pubKey.readLine()) != null && (priK = priKey.readLine()) != null) {
                pubKeyList.add(pubK);
                priKeyList.add(priK);
            }

            String aesK = null;
            while ( (aesK = aesKey.readLine()) != null) {
                aesKeyList.add(aesK);
            }

            String desK = null;
            while ( (desK = desKey.readLine()) != null) {
                desKeyList.add(desK);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private static void generateRSAKeys(int lines, int size, String pubFile, String priFile) {
        int i = 0;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(SecurityUtil.KEY_ALGORITHM_RSA);

            FileWriter pubF = new FileWriter(pubFile);
            FileWriter priF = new FileWriter(priFile);
            while (i < lines) {
                SecureRandom random=new SecureRandom();
                keyGen.initialize(size, random);

                KeyPair keyPair = keyGen.genKeyPair();
                PrivateKey privateKey = keyPair.getPrivate();
                PublicKey publicKey = keyPair.getPublic();

                priF.write(new String(Base64.encode(privateKey.getEncoded())) + System.getProperty("line.separator"));
                pubF.write(new String(Base64.encode(publicKey.getEncoded())) + System.getProperty("line.separator"));
                i++;

            }
            priF.close();
            pubF.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void generateKeys(int lines, int size, String keyFile, String algorithm) {
        int i = 0;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            FileWriter keyF = new FileWriter(keyFile);
            while (i < lines) {
                SecureRandom random=new SecureRandom();
                keyGenerator.init(size, random);

                SecretKey secretKey = keyGenerator.generateKey();
                keyF.write(new String(Base64.encode(secretKey.getEncoded())) + System.getProperty("line.separator"));
                i++;
            }
            keyF.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] concat(byte[] A, byte[] B) {
        int aLen = A.length;
        int bLen = B.length;
        byte[] C = new byte[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
