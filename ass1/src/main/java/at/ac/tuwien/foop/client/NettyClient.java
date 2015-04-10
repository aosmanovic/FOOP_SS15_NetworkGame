package at.ac.tuwien.foop.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.foop.message.MessageEncoder;

public class NettyClient implements Runnable {
	private static Logger log = LoggerFactory.getLogger(NettyClient.class);

	private final int MAX_RETRY = 10;
	private final int RETRY_TIME_MS = 1000;
	private final CountDownLatch latch = new CountDownLatch(1);

	private String host = "localhost";
	private int port = 20150;
	private int retry = 0;
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private Channel channel;
	private boolean connected = false;

//	private Game game;

	private ClientHandler clientHandler;

	public NettyClient(ClientHandler clientHandler, String host, int port) {
//		this.game = game;
		this.host = host;
		this.port = port;
		this.clientHandler = clientHandler;
	}

	public void run() {
		log.info("start client");
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(256));
						ch.pipeline().addLast(
								new StringDecoder(CharsetUtil.UTF_8));
						ch.pipeline().addLast(
								new StringEncoder(CharsetUtil.UTF_8));
						ch.pipeline().addLast(new MessageEncoder());
						ch.pipeline().addLast(clientHandler);
					}
				});
		connect(bootstrap);
		
		// wait till the connection process is done
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// if connected then wait
		if (connected) {
			try {
				log.debug("connected, and waiting for close signal...");
				channel.closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.debug("client channel closed!");
			connected = false;
		}
		
		workerGroup.shutdownGracefully();
		log.info("client down");
	}

	private ChannelFuture connect(Bootstrap bootstrap) {
		log.debug("connect to {}:{}", host, port);
		ChannelFuture future = bootstrap.connect(host, port);
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				log.info("connection operation completed...");
				if (!future.isSuccess()) {
					// give up
					if (retry >= MAX_RETRY) {
						log.warn("could not connect to server, give up :(!");
						latch.countDown();
						future.channel().close();
						return;
					}

					// retry
					retry++;
					log.warn("could not connect, will retry ({}/{})!", retry,
							MAX_RETRY);
					Thread.sleep(RETRY_TIME_MS);
					connect(bootstrap);
				} else {
					// connected
					connected = true;
					channel = future.channel();
					latch.countDown();
				}
			}
		});
		return future;
	}
}