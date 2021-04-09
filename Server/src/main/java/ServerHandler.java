import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler  extends ChannelInboundHandlerAdapter {
    private static final List<Channel> channels = new ArrayList<>();
    private  String clientName;
    private static int newClientIndex =1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("Клиент подключился" + ctx);
        channels.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex;
        newClientIndex++;
        broadcastMessage("SERVER", "Подключился новый клиент "+ clientName);
    }

    public void broadcastMessage(String clientName, String message){
        String out = String.format("[%s]: %s\n", clientName,message);
        for (Channel c:channels) {
          c.writeAndFlush(out);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes()>0){
            System.out.print((char)buf.readByte());
        }
        buf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        System.out.println("Клиент " + clientName + "отвалился");
        channels.remove(ctx.channel());
        broadcastMessage("SERVER", "Клиент " + clientName + "отвалился");
        ctx.close();
    }
}
