package reactor.ms;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
	
	private final ServerSocketChannel ssc;
	private static final int core = Runtime.getRuntime().availableProcessors();
	private static Selector[] selectors = new Selector[core];
	private static SubReactor[] sr = new SubReactor[core];
	private static Thread[] ts = new Thread[core];
	
	int sIndex;
	
	public Acceptor(ServerSocketChannel ssc) throws IOException {
		this.ssc = ssc;
		for(int i = 0; i < core; i++) {
			selectors[i] = Selector.open();
			sr[i] = new SubReactor(selectors[i], ssc);
			ts[i] = new Thread(sr[i]);
			ts[i].start();
		}
		sIndex = 0;
	}

	@Override
	public synchronized void run() {
		try {
			SocketChannel sc = ssc.accept();
			if(sc != null) {
				System.out.println(sc.getRemoteAddress().toString() + " is connected.");
				sc.configureBlocking(false);
				sr[sIndex].setRestart(true);
				selectors[sIndex].wakeup();
				SelectionKey sk = sc.register(selectors[sIndex], SelectionKey.OP_READ);
				selectors[sIndex].wakeup();
				sr[sIndex].setRestart(false);
				sk.attach(new Handler(sk, sc));
				if(++sIndex == core) {
					sIndex = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
