package com.mycodefu;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

public class LanListener {
    private LanEventListener lanEventListener;
    private Map<String, Channel> connections;

    public void listenForMessages(int port, LanEventListener lanEventListener) {
        this.lanEventListener = lanEventListener;
        this.connections = new ConcurrentHashMap<>();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer());

            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed attempting to listen on port " + port + " for web socket connections.");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void sendMessage(String id, String message) {
        if (connections.containsKey(id)) {
            WebSocketFrame frame = new TextWebSocketFrame(message);
            connections.get(id).writeAndFlush(frame);
        }
    }

    private class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpObjectAggregator(65536));
            pipeline.addLast(new WebSocketServerCompressionHandler());
            pipeline.addLast(new WebSocketServerHandler());
        }
    }

    private class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);

            String id = ctx.channel().id().asLongText();
            connections.remove(id);
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
            String id = channelHandlerContext.channel().id().asLongText();

            String ip = channelHandlerContext.channel().remoteAddress().toString();
            lanEventListener.messageReceived(id, ip, "WebSocket\n" + msg.content().toString(CharsetUtil.UTF_8));
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

                    String id = channelHandlerContext.channel().id().asLongText();
                    connections.put(id, channelHandlerContext.channel());
                    lanEventListener.connectionReceived(id);
                }
            }
        }

        private String getWebSocketLocation(FullHttpRequest req) {
            String location = req.headers().get(HOST);
            return "ws://" + location;
        }
    }
}
