package reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleReactor implements Runnable{

	private final Selector selector;
	private final ServerSocketChannel ssc;
	
	public SingleReactor(int port) throws IOException {
		selector = Selector.open();
		ssc = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(port);
		ssc.socket().bind(addr);
		ssc.configureBlocking(false);
		SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new SingleAcceptor(selector, ssc));
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			System.out.println("Waiting for new event on port:" + ssc.socket().getLocalPort());
			try {
				if(selector.select() == 0) {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while(it.hasNext()) {
				dispatch(it.next());
				it.remove();
			}
		}
	}
	
	private void dispatch(SelectionKey sk) {
		Runnable r = (Runnable)sk.attachment();
		if(r != null) {
			r.run();
		}
	}

}
