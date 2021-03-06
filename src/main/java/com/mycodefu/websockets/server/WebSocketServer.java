package com.mycodefu.websockets.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutor;

import java.net.InetSocketAddress;

public class WebSocketServer {
    private final int initialPort;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final ServerBootstrap bootstrap;
    private Channel serverSocketChannel;
    private final ChannelGroup allChannels;

    public WebSocketServer(int initialPort, WebSocketServerHandler.ServerConnectionCallback callback) {
        this.initialPort = initialPort;
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        this.allChannels = new DefaultChannelGroup("WebSocketServerChannels", new DefaultEventExecutor());

        this.bootstrap = new ServerBootstrap();
        this.bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        allChannels.add(ch);

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        pipeline.addLast(new WebSocketServerCompressionHandler());
                        pipeline.addLast(new WebSocketServerHandler(allChannels, callback));
                    }
                });
    }

    public int getPort() {
        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            return ((InetSocketAddress)serverSocketChannel.localAddress()).getPort();
        } else {
            return 0;
        }
    }

    public void listen() {
        try {
            serverSocketChannel = this.bootstrap.bind(initialPort).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed attempting to listen on port " + initialPort + " for web socket connections.", e);
        }
    }

    public void close() {
        try {
            serverSocketChannel.close();
            serverSocketChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed close gracefully.", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void sendMessage(ChannelId id, String message) {
        Channel channel = allChannels.find(id);
        if (channel != null) {
            WebSocketFrame frame = new TextWebSocketFrame(message);
            channel.writeAndFlush(frame);
        }
    }
}
