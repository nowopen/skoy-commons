package com.nowopen.encrypt.web.http;

import com.nowopen.encrypt.web.bussiness.BIHandler;
import com.nowopen.encrypt.web.bussiness.EncryptHandler;
import com.nowopen.encrypt.web.common.CodeEnum;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

/**
 * Created by pxie on 9/19/2014.
 */
public class HttpIncomeRequestHandler extends SimpleChannelInboundHandler<HttpObject> {


    private static final Logger logger = LoggerFactory.getLogger(HttpIncomeRequestHandler.class);

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(false);

    private Map<Channel, ReqInfo> decoderMap = null;

    private static Map<String, EncryptHandler> urlMapping = new HashMap<String, EncryptHandler>();
    static {
        urlMapping.put("/nowopen/encrypt", new EncryptHandler());
    }

    public HttpIncomeRequestHandler(){
        decoderMap = new HashMap<Channel, ReqInfo>();

    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.getUri());
            if (!urlMapping.containsKey(uri.toString())) {
                sendResponse(ctx, HttpResponseStatus.NOT_FOUND,
                        ReponseGenerator.getResponse(CodeEnum.ResponseCode.INVALID_URL, null));      // Invalid URL
                return;
            }

            // only support POST
            if (!request.getMethod().equals(HttpMethod.POST)) {     // Invalid request method
                sendResponse(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED,
                        ReponseGenerator.getResponse(CodeEnum.ResponseCode.INVALID_HTTP_REQ_METHOD, null));
                return;
            }

            try {
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
                decoderMap.put(ctx.channel(), new ReqInfo(uri, decoder));

            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
                logger.error("failure to decoder the http post request", e1);
                sendResponse(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                        ReponseGenerator.getResponse(CodeEnum.ResponseCode.INVALID_HTTP_PROTOCOL, new String[]{e1.getMessage()}));
                close(ctx);
                return;
            }
        }
        ReqInfo reqInfo =  decoderMap.get(ctx.channel());
        if (reqInfo != null) {
            HttpPostRequestDecoder decoder = reqInfo.getDecoder();
            if (msg instanceof HttpContent) {
                HttpContent chunk = (HttpContent) msg;
                try {
                    decoder.offer(chunk);
                    List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
                    Map<String, List<String>> paramMap = getValue(paramList);
                    HttpParams httpParams = new HttpParams(paramMap);

                    BIHandler biHandler = urlMapping.get(reqInfo.getUri().toString());
                    ByteBuf byteBuf = biHandler.process(httpParams);

                    sendResponse(ctx,HttpResponseStatus.OK, byteBuf); // send response to client
                } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
                    logger.error("Exception throwed by HttpPostRequestDecoder", e1);
                    sendResponse(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, ReponseGenerator.getResponse(CodeEnum.ResponseCode.INVALID_HTTP_PROTOCOL, new String[]{e1.getMessage()}));
                    close(ctx);
                    return;
                }
            }

        }else{
            logger.warn("No request decoder found for current request, so close it");
            sendResponse(ctx, HttpResponseStatus.FORBIDDEN,
                    ReponseGenerator.getResponse(CodeEnum.ResponseCode.INVALID_HTTP_REQ, new String[]{"Invalid request"}));
            close(ctx);
        }


    }

    private Map<String, List<String>> getValue(List<InterfaceHttpData> dataList) throws IOException {
        Map<String, List<String>> results = new HashMap<String, List<String>>();

        for(InterfaceHttpData  httpData : dataList){
            if (httpData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                Attribute attribute = (Attribute) httpData;
                String value = attribute.getValue();

                List<String> valueList = results.get(httpData.getName());
                if(null == valueList){
                    valueList = new ArrayList<String>();
                    results.put(httpData.getName(), valueList);
                }
                valueList.add(value);
            }
        }
        return results;
    }


    private void sendResponse(ChannelHandlerContext ctx, final HttpResponseStatus status, final JsonObject response) {
        ByteBuf buf = copiedBuffer(response.toString(), CharsetUtil.UTF_8);
        sendResponse(ctx, status, buf);
    }

    private void sendResponse(ChannelHandlerContext ctx, final HttpResponseStatus status, final ByteBuf buf) {
        // Build the response object.
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, buf);

        httpResponse.headers().set(CONTENT_TYPE, "text/json; charset=UTF-8");
        httpResponse.headers().set(CONTENT_LENGTH, buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(httpResponse);
    }

    private void close(ChannelHandlerContext ctx) {
        ctx.channel().close();
    }

    private void writeMenu(ChannelHandlerContext ctx) {
        // print several HTML forms
        // Convert the response content to a ChannelBuffer.
        StringBuffer responseContent = new StringBuffer();
        responseContent.setLength(0);

        // create Pseudo Menu
        responseContent.append("<html>");
        responseContent.append("<head>");
        responseContent.append("<title>Netty Test Form</title>\r\n");
        responseContent.append("</head>\r\n");
        responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr>");
        responseContent.append("<td>");
        responseContent.append("<h1>Netty Test Form</h1>");
        responseContent.append("Choose one FORM");
        responseContent.append("</td>");
        responseContent.append("</tr>");
        responseContent.append("</table>\r\n");

        // POST
        responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/enniuencrypt\" METHOD=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"enct\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"enct\" size=20></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        // POST with enctype="multipart/form-data"
        responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent
                .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        responseContent.append("</body>");
        responseContent.append("</html>");

        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("exceptionCaught", cause);
        ctx.channel().close();
    }

    private class ReqInfo{
        private URI uri;
        private HttpPostRequestDecoder decoder;

        ReqInfo(final URI uri, final HttpPostRequestDecoder decoder) {
            this.uri = uri;
            this.decoder = decoder;
        }

        public URI getUri(){
            return uri;
        }

        public HttpPostRequestDecoder getDecoder(){
            return decoder;
        }

    }

}
