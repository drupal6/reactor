package reactor.ms;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public interface HandlerState {

	public void handler(Handler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException;
	
}
