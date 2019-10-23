package com.rpc.client.netty.handler;

import com.rpc.message.MessageRequest;
import com.rpc.message.MessageResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by T440 on 2019/4/13.
 */
@ChannelHandler.Sharable
public class NettyClientChannelHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(NettyClientChannelHandler.class);

    private MessageRequest messageRequest;

    private MessageResponse messageResponse;

    public NettyClientChannelHandler(MessageRequest messageRequest) {
        this.messageRequest = messageRequest;
    }

    public void channelActive(ChannelHandlerContext ctx) {
        messageRequest.setRequestId(UUID.randomUUID().toString());
        ctx.writeAndFlush(messageRequest);
        LOG.info("Channel active and request has send.");
    }

//    public void channelInactive(ChannelHandlerContext ctx)   {
//        LOG.info("与服务端断开连接 "+ ctx.channel().remoteAddress() );
//        ctx.channel().close();
//    }

    public void channelRead(ChannelHandlerContext ctx, Object response) throws Exception {
        this.messageResponse = (MessageResponse) response;
        LOG.info("Response has received.");
        ctx.close();
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }
}
