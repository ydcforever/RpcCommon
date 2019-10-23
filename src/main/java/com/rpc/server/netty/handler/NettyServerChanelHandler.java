package com.rpc.server.netty.handler;

import com.rpc.message.MessageRequest;
import com.rpc.message.MessageResponse;
import com.rpc.transport.TransferMethod;
import com.rpc.transport.impl.DefaultTransferMethod;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by T440 on 2019/4/13.
 */
@ChannelHandler.Sharable
public class NettyServerChanelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = Logger.getLogger(NettyServerChanelHandler.class);

    private Map<String, Object> services;

    private static final TransferMethod transferMethod = new DefaultTransferMethod();

    public NettyServerChanelHandler(Map<String, Object> services) {
        this.services = services;
    }

    public void channelActive(ChannelHandlerContext ctx)   {
        LOG.info("客户端连接成功 " + ctx.channel().remoteAddress());
    }

    public void channelInactive(ChannelHandlerContext ctx)   {
        LOG.info("客户端断开连接 "+ ctx.channel().remoteAddress() );
        ctx.channel().close();
    }

    public void channelRead(ChannelHandlerContext ctx, Object request) throws Exception {
        // 接收到的对象的类型为MessageRequest
        MessageRequest messageRequest = (MessageRequest) request;
        // 创建传输的user对象载体EchoRequest对象
        MessageResponse messageResponse = new MessageResponse();
        // 设置responseId
        messageResponse.setRequestId(messageRequest.getRequestId());
        // 设置需要传输的对象
        try {
            Object object = transferMethod.handleRequest(services, messageRequest);
            messageResponse.setResult(object);
        } catch (Throwable throwable) {
            messageResponse.setError(throwable);
        }
        // 调用writeAndFlush将数据发送到socketChannel
        ctx.writeAndFlush(messageResponse);
        LOG.info("Server has return response.");
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state()== IdleState.ALL_IDLE){
                LOG.info("客户端已超过60秒未读写数据,"+ ctx.channel().remoteAddress() + "关闭连接");
                ctx.channel().close();
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.info(cause.getMessage());
        ctx.close();
    }
}
