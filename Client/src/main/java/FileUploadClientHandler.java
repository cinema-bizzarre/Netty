import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUploadClientHandler extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private FileUploadFile fileUploadFile;

    public FileUploadClientHandler(FileUploadFile fUF) {
        if (fUF.getFile().exists()){
            if (!fUF.getFile().isFile()){
                System.out.println("Нет файла :" + fUF.getFile());
                return;
            }
        }
        this.fileUploadFile = fUF;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
            randomAccessFile.seek(fileUploadFile.getStarPos());
            lastLength = (int) randomAccessFile.length();
            byte[] bytes = new byte[lastLength];
            if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                fileUploadFile.setEndPos(byteRead);
                fileUploadFile.setBytes(bytes);
                ctx.writeAndFlush(fileUploadFile);
            } else {
                System.out.println("Файл прочитан");
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Integer){
            start = (Integer) msg;
            if (start !=-1){
                int a = (int) randomAccessFile.length()/10;
                int b = (int) randomAccessFile.length() - start;
                randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
                randomAccessFile.seek(start);
                System.out.println("Длина файла :" + (a));
                System.out.println("Длина :" + (b));
                if (a < b) {
                    lastLength = a;
                }
                byte[] bytes = new byte[lastLength];
                System.out.println("---------------- " + bytes.length);
                if ((byteRead = randomAccessFile.read(bytes)) != -1 &&
                        (randomAccessFile.length() - start) >0){
                    System.out.println("Длина байта :" + bytes.length);
                    fileUploadFile.setEndPos(byteRead);
                    fileUploadFile.setBytes(bytes);
                    try {
                        ctx.writeAndFlush(fileUploadFile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    randomAccessFile.close();
                    ctx.close();
                    System.out.println("Файл прочитан ------" + byteRead);
                }


            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
