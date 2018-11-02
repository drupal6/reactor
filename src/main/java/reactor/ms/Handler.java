package reactor.ms;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Handler implements Runnable {

	private final SelectionKey sk;
	private final SocketChannel sc;
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	HandlerState state;
	
	public Handler(SelectionKey sk, SocketChannel sc) {
		this.sk = sk;
		this.sc = sc;
		state = new ReadState();
	}
	
	@Override
	public void run() {
		try {
			state.handler(this, sk, sc, pool);
		} catch (IOException e) {
			e.printStackTrace();
			closeChannel();
		}
	}
	
	public void closeChannel() {
		try {
			sk.cancel();
			sc.close();
		} catch (IOException e) {
			System.out.println("[Warning!] A client has been closed.");
		}
	}

	public void setState(HandlerState state) {
		this.state = state;
	}
}
