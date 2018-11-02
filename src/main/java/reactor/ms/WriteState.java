package reactor.ms;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class WriteState implements HandlerState {

	@Override
	public void handler(Handler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
		String str = "Your message has send to port " + sc.socket().getLocalPort() + "\r\n";
		ByteBuffer buf = ByteBuffer.wrap(str.getBytes());
		while(buf.hasRemaining()) {
			sc.write(buf);
		}
		sk.interestOps(SelectionKey.OP_READ);
		sk.selector().wakeup();
	}
}
