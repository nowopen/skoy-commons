package com.nowopen.encrypt.web.bussiness;

import com.nowopen.encrypt.web.common.CodeEnum;
import com.nowopen.encrypt.web.http.ReponseGenerator;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.nowopen.encrypt.web.common.CodeEnum.*;
import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * Created by pxie on 9/19/2014.
 */
public class EncryptHandler implements BIHandler {
    private static final Logger logger = LoggerFactory.getLogger(EncryptHandler.class);

    private EncryptInvoker encryptInvoker = null;

    public EncryptHandler(){
        encryptInvoker = new EncryptInvoker();
    }

    @Override
    public ByteBuf process(BIParams args) {
        String[] contents = args.getArray(EncryptParam.REQ_DATA);
        Boolean isToBeEncrypting = args.getBoolean(EncryptParam.REQ_ENCRYPT_OR_NOT);
        String encriptType = args.getString(EncryptParam.REQ_ENCRYPT_TYPE);
        if(null == contents || null == isToBeEncrypting || null == encriptType){
            throw new IllegalArgumentException(String.format("Illegal arguments : %s=%s, %s=%s, %s=%s",
                    EncryptParam.REQ_DATA, contents,
                    EncryptParam.REQ_ENCRYPT_OR_NOT, isToBeEncrypting.toString(),
                    EncryptParam.REQ_ENCRYPT_TYPE, encriptType));
        }

        EncryptAlgorithm encryptAlgorithm = EncryptAlgorithm.valueOf(encriptType.toUpperCase());

        String []  dataAfterEncrypt = new String[contents.length];
        try {
            for (int index = 0; index < contents.length; index++) {
                if (isToBeEncrypting) {
                    dataAfterEncrypt[index] = encryptInvoker.encrypt(contents[index], encryptAlgorithm);
                } else {
                    dataAfterEncrypt[index] = encryptInvoker.deEncrypt(contents[index], encryptAlgorithm);
                }
            }
        }catch (Exception e){
            logger.error(String.format("failure to encrypt/decrypt for %s", Arrays.toString(contents)), e);
            dataAfterEncrypt = null;
        }


        JsonObject jsonObjectResp = ReponseGenerator.getResponse(CodeEnum.ResponseCode.OK, dataAfterEncrypt);
        ByteBuf buf = copiedBuffer(jsonObjectResp.toString(), CharsetUtil.UTF_8);
        return buf;
    }
}
