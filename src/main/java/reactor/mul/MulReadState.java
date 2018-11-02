package reactor.mul;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class MulReadState implements MulHandlerState {
	
	private SelectionKey sk;

	@Override
	public void handler(MulHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
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
			h.setState(new MulWorkState());
			pool.execute(new WorkerThread(h, str));
		}
	}
	
	private synchronized void process(MulHandler h, String str) {
		h.setState(new MulWriteState());
		sk.interestOps(SelectionKey.OP_WRITE);
		sk.selector().wakeup();
	}
	
	class WorkerThread implements Runnable {

		private String str;
		private MulHandler h;
		
		public WorkerThread(MulHandler h, String str) {
			this.h = h;
			this.str = str;
		}
		
		@Override
		public void run() {
			process(h, str);
		}
	}
}
