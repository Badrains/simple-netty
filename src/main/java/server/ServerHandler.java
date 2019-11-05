package server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //-----为新连接发送庆祝
        ctx.write("welcome to" + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + "now.\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        String response;
        boolean close = false;
        if(s.isEmpty()){
            response = "please tupe something.\r\n";
        }else if("bye".equals(s.toLowerCase())){
            response = "have a good day!\r\n";
            close = true;
        }else {
            response = "Did you say '" + s + "'?\r\n";
        }
        ChannelFuture future = ctx.write(response);
        if(close){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
