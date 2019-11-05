package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author wangdong
 */
public class Server {

    public static void main(String[] args) {
        //-----创建两个EventLoopGroup
        //-----创建boss线程组，用于接收客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //-----创建worker线程组，用于进行SocketChannel的数据读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //-----创建ServerBootstrap对象，用于配置
            ServerBootstrap b = new ServerBootstrap();
            //-----设置使用的EventLoopGroup
            b.group(bossGroup, workerGroup)
                    //-----设备要被实例化的为NioServerSocketChannel类
                    .channel(NioServerSocketChannel.class)
                    //-----设置NioServerSocketChannel的处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //-----设置连入服务端的client的SocketChannel的处理器
                    .childHandler(new ServerInitializer());
            ChannelFuture f = b.bind(8888);
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //-----优雅关闭两个EventLoopGroup对象
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
