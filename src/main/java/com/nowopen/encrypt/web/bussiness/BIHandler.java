package com.nowopen.encrypt.web.bussiness;

import io.netty.buffer.ByteBuf;

/**
 * Created by pxie on 9/19/2014.
 */
public interface BIHandler {
    public ByteBuf process(final BIParams args);
}
