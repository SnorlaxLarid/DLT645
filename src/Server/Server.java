package  Server;

import WindowsFrame.WindowsFrame;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
*  netty的基本配置项，生成一个新线程来管理
*    by zxy on 2018.10.2
* */
public class Server extends Thread
{



    /**定义一个map，key存放loginCode，value存放ctx**/
    //保证线程安全使用ConcurrentHashMap
    public static ConcurrentHashMap <String,ChannelHandlerContext> loginCodeMap = new ConcurrentHashMap<>();

    public Server()
    {

    }
    public  void run()
    {

        EventLoopGroup pGroup = new NioEventLoopGroup(); // 一组线程负责用于处理Client端的连接请求
        EventLoopGroup cGroup = new NioEventLoopGroup(); // 另一组线程负责信息的交互
        ServerBootstrap b = new ServerBootstrap();       // 辅助工具类，用于服务器通道的一系列配置

        b.group(pGroup,cGroup)
                .channel(NioServerSocketChannel.class)           // 指定NIO的模式
//                .option(ChannelOption.SO_BACKLOG,1024)   // 设置tcp缓存区
//                .option(ChannelOption.SO_SNDBUF,32*1024) // 设置发送缓冲的大小
//                .option(ChannelOption.SO_RCVBUF,32*1024) // 设置接收缓冲的大小
//                .option(ChannelOption.SO_KEEPALIVE,true)  // 保持连接
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception
                    {
                        sc.pipeline().addLast(new ServerHandler());     // 配置具体数据接收方法的处理
                    }

                });
        int port=Integer.parseInt(WindowsFrame.port.getText());

        try {

            ChannelFuture cf1 = b.bind(port).sync();
            //System.out.println(Thread.currentThread().getName());


            cf1.channel().closeFuture().sync();                           // 等待关闭，异步，阻塞当前线程
            pGroup.shutdownGracefully();                                // 清空线程池
            cGroup.shutdownGracefully();
        }catch (Exception e)
        {
            WindowsFrame.Message.setText(WindowsFrame.port.getText()+" port has been used!!!");
        }



    }
}
