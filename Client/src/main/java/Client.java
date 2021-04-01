import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

public class Client {
    private SocketChannel channel;
    private static final String HOST = "localhost";
    private static final int PORT = 8000;

        public Client(){
            new Thread(()->{
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    channel = socketChannel;
                                    socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder());
                                }
                            });
                            ChannelFuture future = b.connect(HOST, PORT).sync();
                            future.channel().closeFuture().sync();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    workerGroup.shutdownGracefully();
                }
            }).start();

        }

    public void sendMessage(String str) {
            channel.writeAndFlush(str);

    }
}