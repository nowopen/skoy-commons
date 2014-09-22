package com.nowopen.encrypt;

/**
 * Created by admin@OYJ on 2014/9/18.
 */
public interface SecurityService {

    /**
     *
     * @param plainString - 需要被加密的字符串
     * @return String - 加密后的BASE64编码过的字符串
     */
    String encrypt(String plainString);

    /**
     *
     * @param encryptedString - BASE64编码的加密字符串
     * @return String - 解密后的明文字符串
     */
    String decrypt(String encryptedString);
}
