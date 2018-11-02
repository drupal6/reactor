package reactor.ms;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class ReadState implements HandlerState {

	private SelectionKey sk;
	
	@Override
	public void handler(Handler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
		this.sk = sk;
		byte[] arr = new byte[1024];
		ByteBuffer buf = ByteBuffer.wrap(arr);
		int num = sc.read(buf);
		if(num == -1) {
			h.closeChannel();
			return;
		}
		String str = new String(arr);
		if(str != null && false == str.isEmpty()) {
			System.out.println(sc.getRemoteAddress().toString() + ">" + str);
			h.setState(new WorkState());
			pool.execute(new WorkThread(h, str));
		}
	}
	
	private void process(Handler h, String str) {
		h.setState(new WriteState());
		sk.interestOps(SelectionKey.OP_WRITE);
		sk.selector().wakeup();
	}
	
	class WorkThread implements Runnable {
		private Handler h;
		private String str;
		
		public WorkThread(Handler h, String str) {
			this.h = h;
			this.str = str;
		}
		
		@Override
		public void run() {
			process(h, str);
		}
	}
}
