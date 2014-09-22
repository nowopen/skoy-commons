package com.nowopen.encrypt.web.bussiness;

import com.nowopen.encrypt.SecurityDESedeService;
import com.nowopen.encrypt.SecurityRSAService;
import com.nowopen.encrypt.SecurityService;
import static com.nowopen.encrypt.web.common.CodeEnum.EncryptAlgorithm;

/**
 * Created by pxie on 9/19/2014.
 */
public class EncryptInvoker {

    private SecurityService rsaSecurity = null;
    private SecurityService desSecurity = null;

    public EncryptInvoker(){
        rsaSecurity = SecurityRSAService.getInstance();
        desSecurity = SecurityDESedeService.getInstance();

    }

    public String  encrypt(final String contents,final EncryptAlgorithm encryptAlgorithm){
        if(null == encryptAlgorithm){
             return null;
        }

        String ret = null;
        switch (encryptAlgorithm){
            case  RSA:
                ret = rsaSecurity.encrypt(contents) ;
                break;
            case DES:
                ret = desSecurity.encrypt(contents);
                break;
            default:
                break;
        }
        return ret;
    }

    public String  deEncrypt(final String contents, final EncryptAlgorithm encryptAlgorithm){
        if(null == encryptAlgorithm){
            return null;
        }

        String ret = null;
        switch (encryptAlgorithm){
            case  RSA:
                ret = rsaSecurity.decrypt(contents) ;
                break;
            case DES:
                ret = desSecurity.decrypt(contents);
                break;
            default:
                break;
        }
        return ret;
    }

}
