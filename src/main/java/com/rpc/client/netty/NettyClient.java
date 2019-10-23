package com.rpc.client.netty;

import com.rpc.client.netty.handler.NettyClientChannelHandler;
import com.rpc.message.MessageRequest;
import com.rpc.message.MessageResponse;
import com.rpc.net.ServiceAddress;
import com.rpc.netty.channel.NettyChannel;
import com.rpc.serialize.MessageSerialize;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by T440 on 2019/4/13.
 */
public class NettyClient {

    private static final Class<?> decoderClass =  MessageResponse.class;

    private NettyChannel nettyChannel;

    private NettyClientChannelHandler clientChannelHandler;

    public NettyClientChannelHandler getClientChannelHandler() {
        return clientChannelHandler;
    }

    public NettyClient(MessageRequest messageRequest, MessageSerialize messageSerialize) {
        this.clientChannelHandler = new NettyClientChannelHandler(messageRequest);
        this.nettyChannel = new NettyChannel(messageSerialize, decoderClass, clientChannelHandler);
    }

    public void connect(ServiceAddress serviceAddress) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                            // 设置TCP连接超时时间
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(nettyChannel);
            // 发起异步连接操作（注意服务端是bind，客户端则需要connect）
            ChannelFuture f = b.connect(serviceAddress.getHostname(), serviceAddress.getPort()).sync();
            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
