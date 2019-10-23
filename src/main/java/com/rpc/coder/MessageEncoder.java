package com.rpc.coder;

import com.rpc.serialize.MessageSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by T440 on 2019/4/12.
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

    private MessageSerialize messageSerialize;

    public MessageEncoder(MessageSerialize messageSerialize) {
        this.messageSerialize = messageSerialize;
    }

    @Override
    protected void encode(final ChannelHandlerContext channelHandlerContext, final Object msg,
                          final ByteBuf output) throws Exception {
        byte[] data = messageSerialize.serialize(msg);
        output.writeInt(data.length);
        output.writeBytes(data);
    }
}
