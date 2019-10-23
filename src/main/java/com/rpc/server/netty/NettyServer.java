package com.rpc.server.netty;

import com.rpc.annotion.AnnotationUtil;
import com.rpc.message.MessageRequest;
import com.rpc.net.ServiceAddress;
import com.rpc.netty.channel.NettyChannel;
import com.rpc.serialize.MessageSerialize;
import com.rpc.serialize.impl.ProtoStuffMessageSerialize;
import com.rpc.server.netty.handler.NettyServerChanelHandler;
import com.rpc.service.ServiceCache;
import com.rpc.service.management.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;

import java.util.Map;

/**
 * Created by T440 on 2019/4/13.
 */
public class NettyServer {

    private MessageSerialize messageSerialize = new ProtoStuffMessageSerialize();

    private static final Class<?> decoderClass =  MessageRequest.class;

    private NettyChannel nettyChannel;

    private ServiceRegistry serviceRegistry;

    private String basePath;

    private static final String DEFAULT_ROOT = "/registry";

    public NettyServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public NettyServer messageSerialize(MessageSerialize messageSerialize) {
        this.messageSerialize = messageSerialize;
        return this;
    }

    public NettyServer basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public NettyServer build() {
        ServiceCache serviceCache = new ServiceCache();
        Map<String, Object> servicesMap = serviceCache.getServices();
        this.nettyChannel = new NettyChannel(messageSerialize, decoderClass, new NettyServerChanelHandler(servicesMap), true);
        if(StringUtil.isNullOrEmpty(basePath)) {
            basePath = DEFAULT_ROOT;
        }
        return this;
    }

    public void publish(Object object, ServiceAddress serviceAddress) {
        // 配置服务端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(nettyChannel);
            // 绑定端口，同步等待成功，该方法是同步阻塞的，绑定成功后返回一个ChannelFuture
            ChannelFuture f = b.bind(serviceAddress.getHostname(), serviceAddress.getPort()).sync();
            if(serviceRegistry != null) {
                String serviceName = AnnotationUtil.getRpcServiceName(object.getClass());
                serviceRegistry.register(serviceName, serviceAddress.getAddress(), basePath, "");
            }
            // 等待服务端监听端口关闭，阻塞，等待服务端链路关闭之后main函数才退出
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
