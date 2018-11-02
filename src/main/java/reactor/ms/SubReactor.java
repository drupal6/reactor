package reactor.ms;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SubReactor implements Runnable{

	private final Selector selector;
	private final ServerSocketChannel ssc;
	
	public SubReactor(Selector selector, ServerSocketChannel ssc) {
		this.selector = selector;
		this.ssc = ssc;
	}
	
	private boolean restart = false;
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			System.out.println("waiting for restart");
			while(!Thread.interrupted() && false == restart) {
				try {
					if(selector.select() == 0) {
						continue;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Set<SelectionKey> skeys = selector.selectedKeys();
				Iterator<SelectionKey> it = skeys.iterator();
				while(it.hasNext()) {
					dispatch(it.next());
					it.remove();
				}
			}
		}
	}

	private void dispatch(SelectionKey sk) {
		Runnable r = (Runnable)sk.attachment();
		if(r != null) {
			r.run();
		}
	}
	
	public void setRestart(boolean restart) {
		this.restart = restart;
	}

}
