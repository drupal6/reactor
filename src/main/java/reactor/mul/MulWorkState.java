package reactor.mul;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class MulWorkState implements MulHandlerState {

	@Override
	public void handler(MulHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
