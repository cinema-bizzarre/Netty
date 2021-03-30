import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

public class Client {
    private SocketChannel channel;


            EventLoopGroup workerGroup = new NioEventLoopGroup();
            static final String URL = System.getProperty("url", "http://127.0.0.1:8000/");
            URI uri = new URI(URL);
            String scheme = uri.getScheme() == null? "http" : uri.getScheme();
            final boolean ssl = "https".equalsIgnoreCase(scheme);
            final SslContext sslCtx;
            if (ssl) {
                try {
                    sslCtx = SslContextBuilder.forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                } catch (SSLException e) {
                    e.printStackTrace();
                }
            } else {
                sslCtx = null;
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception{
                               channel = socketChannel;
                            }
                        });
                ChannelFuture future = b.connect("localhost",8000).sync();
                future.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                workerGroup.shutdownGracefully();
            }
        HttpRequest request = new DefaultFullHttpRequest(
                                    HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath(), Unpooled.EMPTY_BUFFER);
                    request.headers().set(HttpHeaderNames.HOST, host);
                   request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                      request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

                    // Set some example cookies.
                     request.headers().set(
                                     HttpHeaderNames.COOKIE,
                                 ClientCookieEncoder.STRICT.encode(
                                                  new DefaultCookie("my-cookie", "foo"),
                                             new DefaultCookie("another-cookie", "bar")));

                 // Send the HTTP request.
                   ch.writeAndFlush(request);

                  // Wait for the server to close the connection.
                     ch.closeFuture().sync();
              } finally {
                   // Shut down executor threads to exit.
                  group.shutdownGracefully();
               }

    }


}
