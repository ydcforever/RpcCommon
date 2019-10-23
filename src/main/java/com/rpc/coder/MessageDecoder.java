package com.rpc.coder;

import com.rpc.serialize.MessageSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * Created by T440 on 2019/4/12.
 */
public class MessageDecoder extends ByteToMessageDecoder{

    private MessageSerialize messageSerialize;

    private Class clazz;

    public MessageDecoder(MessageSerialize messageSerialize, Class clazz) {
        this.messageSerialize = messageSerialize;
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf input, List<Object> output) throws Exception {
        if (input.readableBytes() < MessageSerialize.MESSAGE_LENGTH) {
            return;
        }

        input.markReaderIndex();
        //读取消息的内容长度
        int messageLength = input.readInt();
        if (messageLength < 0) {
            channelHandlerContext.close();
        }

        //读到的消息长度和报文头的已知长度不匹配。那就重置一下ByteBuf读索引的位置
        if (input.readableBytes() < messageLength) {
            input.resetReaderIndex();
        } else {
            byte[] messageBody = new byte[messageLength];
            input.readBytes(messageBody);
            Object obj = messageSerialize.deserialize(messageBody, clazz);
            output.add(obj);
        }
    }
}
