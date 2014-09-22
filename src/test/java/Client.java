import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pxie on 9/19/2014.
 */
public class Client {

    public void run() throws Exception {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new HttpUploadClientIntializer(null));

        // Simple Post form: factory used for big attributes
        String host = "127.0.0.1";
        int port = 8080;
        URI uriSimple = new URI("/nowopen/encrypt");
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
        Map<String, String> headers = new HashMap<String, String>();
        List<InterfaceHttpData> bodylist = formpost(b, host, port, uriSimple, null, factory, headers.entrySet());
        if (bodylist == null) {
            return;
        }
    }

    private static List<InterfaceHttpData> formpost(
            Bootstrap bootstrap,
            String host, int port, URI uriSimple, File file, HttpDataFactory factory,
            Set<Map.Entry<String, String>> headers) throws Exception {
        // XXX /formpost
        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        // Wait until the connection attempt succeeds or fails.
        Channel channel = future.sync().channel();

        // Prepare the HTTP request.
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uriSimple.toASCIIString());

        // Use the PostBody encoder
        HttpPostRequestEncoder bodyRequestEncoder = new HttpPostRequestEncoder(factory, request, false);  // false => not multipart

        // it is legal to add directly header or cookie into the request until finalize
        for (Map.Entry<String, String> entry : headers) {
            request.headers().set(entry.getKey(), entry.getValue());
        }

        // add Form attribute
        bodyRequestEncoder.addBodyAttribute("data", "ascb");
        bodyRequestEncoder.addBodyAttribute("enct", "true");
        bodyRequestEncoder.addBodyAttribute("entype", "rsa");

        // finalize request
        request = bodyRequestEncoder.finalizeRequest();

        // Create the bodylist to be reused on the last version with Multipart support
        List<InterfaceHttpData> bodylist = bodyRequestEncoder.getBodyListAttributes();

        // send request
        channel.write(request);

        // test if request was chunked and if so, finish the write
        if (bodyRequestEncoder.isChunked()) { // could do either request.isChunked()
            // either do it through ChunkedWriteHandler
            channel.write(bodyRequestEncoder);
        }
        channel.flush();

        // Do not clear here since we will reuse the InterfaceHttpData on the next request
        // for the example (limit action on client side). Take this as a broadcast of the same
        // request on both Post actions.
        //
        // On standard program, it is clearly recommended to clean all files after each request
        // bodyRequestEncoder.cleanFiles();

        // Wait for the server to close the connection.
        channel.closeFuture().sync();
        return bodylist;
    }

    public static void main(String[] args) throws Exception {
          Client clt = new Client();
          clt.run();

    }
}
