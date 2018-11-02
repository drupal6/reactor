package reactor.mul;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MulHandler implements Runnable{
	
	private final SelectionKey sk;
	private final SocketChannel sc;
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	MulHandlerState state;
	
	public MulHandler(SelectionKey sk, SocketChannel sc) {
		this.sk = sk;
		this.sc = sc;
		state = new MulReadState();
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
			e.printStackTrace();
			System.out.println("[Warning!] A clinet has been closed.");
		}
	}

	public void setState(MulHandlerState state) {
		this.state = state;
	}
}
