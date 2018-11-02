package reactor.mul;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class MulWriteState implements MulHandlerState {

	@Override
	public void handler(MulHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
		String str = "Your message has been send to " + sc.getLocalAddress().toString() + "\r\n";
		ByteBuffer buff = ByteBuffer.wrap(str.getBytes());
		while(buff.hasRemaining()) {
			sc.write(buff);
		}
		h.setState(new MulReadState());
		sk.interestOps(SelectionKey.OP_READ);
		sk.selector().wakeup();
	}
}
