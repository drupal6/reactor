package reactor.mul;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class MulAcceptor implements Runnable {

	private final Selector selector;
	private final ServerSocketChannel ssc;
	
	public MulAcceptor(Selector selector, ServerSocketChannel ssc) {
		this.selector = selector;
		this.ssc = ssc;
	}
	
	@Override
	public void run() {
		try{
			SocketChannel sc = ssc.accept();
			if(sc != null) {
				System.out.println(sc.socket().getRemoteSocketAddress().toString() + " is connected.");
				sc.configureBlocking(false);
				SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
				sk.selector().wakeup();
				sk.attach(new MulHandler(sk, sc));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
