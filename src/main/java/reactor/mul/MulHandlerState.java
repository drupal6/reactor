package reactor.mul;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public interface MulHandlerState {

	
	public void handler(MulHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException;
}
