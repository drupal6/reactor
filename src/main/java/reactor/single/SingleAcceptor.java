package reactor.single;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SingleAcceptor implements Runnable {

	private final Selector selector;
	private final ServerSocketChannel ssc;
	
	public SingleAcceptor(Selector selector, ServerSocketChannel ssc) {
		this.selector = selector;
		this.ssc = ssc;
	}

	
	@Override
	public void run() {
		try {
			SocketChannel sc = ssc.accept();
			System.out.println(sc.socket().getRemoteSocketAddress().toString() + " is connected.");
			if(sc != null) {
				sc.configureBlocking(false);
				SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
				selector.wakeup();
				sk.attach(new SingleHandler(sk, sc));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
