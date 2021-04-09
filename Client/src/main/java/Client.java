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
import javax.security.auth.callback.Callback;
import java.io.File;

public class Client {
    private SocketChannel channel;

    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public Client(Callback onMessageReceivedCallback) {
        Thread t = new Thread(() -> {
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(),
                                         new ClientHandler(onMessageReceivedCallback));
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
        try {
            FileUploadFile uploadFile = new FileUploadFile();
            File file = new File("c:/1.txt");
            String fileMd5 = file.getName();// имя файла
            uploadFile.setFile(file);
            uploadFile.setFile_md5(fileMd5);
            uploadFile.setStarPos(0); // Начальная позиция файла
            new Client().connect(PORT, "127.0.0.1", uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        channel.close();
    }

    public void sendMessage(String str) {
        channel.writeAndFlush(str);
    }
}
