package com.rpc.netty.channel;

import com.rpc.coder.MessageDecoder;
import com.rpc.coder.MessageEncoder;
import com.rpc.serialize.MessageSerialize;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * Created by T440 on 2019/4/13.
 */
public class NettyChannel extends ChannelInitializer<SocketChannel> {

    private MessageSerialize messageSerialize;
    private Class decoderClass;
    private ChannelInboundHandlerAdapter chanelHandler;
    private boolean isIdleState;

    public NettyChannel(MessageSerialize messageSerialize, Class decoderClass, ChannelInboundHandlerAdapter chanelHandler) {
        this.messageSerialize = messageSerialize;
        this.decoderClass = decoderClass;
        this.chanelHandler = chanelHandler;
        this.isIdleState = false;
    }

    public NettyChannel(MessageSerialize messageSerialize, Class decoderClass, ChannelInboundHandlerAdapter chanelHandler, boolean isIdleState) {
        this.messageSerialize = messageSerialize;
        this.decoderClass = decoderClass;
        this.chanelHandler = chanelHandler;
        this.isIdleState = isIdleState;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        if (isIdleState) {
            channelPipeline.addLast(new IdleStateHandler(0, 0, 60));
        }
        // 添加解码器
        channelPipeline.addLast(new MessageDecoder(messageSerialize, decoderClass));
        // 添加编码器
        channelPipeline.addLast(new MessageEncoder(messageSerialize));
        // 添加业务处理handler
        channelPipeline.addLast(chanelHandler);
    }
}
