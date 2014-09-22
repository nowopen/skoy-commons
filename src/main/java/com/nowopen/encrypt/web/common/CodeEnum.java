package com.nowopen.encrypt.web.common;

/**
 * Created by pxie on 9/19/2014.
 */
public class CodeEnum {

    public static class ResponseCode{
        public static final int OK = 1000;
        public static final int INVALID_VALIDATION = 1001;
        public static final int INVALID_URL = 1002;
        public static final int INVALID_HTTP_REQ_METHOD = 1003;
        public static final int INVALID_HTTP_PROTOCOL = 1004;
        public static final int INVALID_HTTP_REQ = 1005;


    }


    public static class EncryptParam{
        public static String REQ_DATA = "data";
        public static String REQ_ENCRYPT_OR_NOT = "enct";
        public static String REQ_ENCRYPT_TYPE = "entype";
    }

    public enum EncryptAlgorithm {
         RSA("rsa"), DES("des");

        private String encryptName;
        private EncryptAlgorithm(final String encryptName){
            this.encryptName = encryptName;
        }
        public String getEncryptName(){
            return encryptName;
        }



    }


}
