package com.mycodefu.websockets.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private final ChannelGroup allChannels;
    private final ServerConnectionCallback callback;

    public WebSocketServerHandler(ChannelGroup allChannels, ServerConnectionCallback callback) {
        this.allChannels = allChannels;
        this.callback = callback;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        callback.clientDisconnected(ctx.channel().id());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(channelHandlerContext, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketRequest(channelHandlerContext, (WebSocketFrame) msg);
        }
    }

    private void handleWebSocketRequest(ChannelHandlerContext channelHandlerContext, WebSocketFrame msg) {
        String ip = channelHandlerContext.channel().remoteAddress().toString();
        callback.messageReceived(channelHandlerContext.channel().id(), ip, "WebSocket\n" + msg.content().toString(CharsetUtil.UTF_8));
    }

    private void handleHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest msg) {
        String ip = channelHandlerContext.channel().remoteAddress().toString();

        System.out.println("Received HTTP Request from " + ip);

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(msg), null, true);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(msg);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channelHandlerContext.channel());
        } else {
            ChannelFuture channelFuture = handshaker.handshake(channelHandlerContext.channel(), msg);
            if (channelFuture.isSuccess()) {
                System.out.println(channelHandlerContext.channel() + " Connected");

                callback.clientConnected(channelHandlerContext.channel().id());
            }
        }
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

    public interface ServerConnectionCallback {
        void clientConnected(ChannelId id);
        void messageReceived(ChannelId id, String sourceIpAddress, String message);
        void clientDisconnected(ChannelId id);
    }
}
