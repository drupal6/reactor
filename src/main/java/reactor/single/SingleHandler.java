package reactor.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SingleHandler implements Runnable {
	
	private final SelectionKey sk;
	private final SocketChannel sc;
	
	int state;
	
	public SingleHandler(SelectionKey sk, SocketChannel sc) {
		this.sk = sk;
		this.sc = sc;
		state = 0;
	}

	@Override
	public void run() {
		try {
			if(state == 0) {
				read();
			} else {
				write();
			}
		} catch (IOException e) {
			e.printStackTrace();
			closeChannel();
		}
	}
	
	public void closeChannel() {
		try {
			sk.cancel();
			sc.close();
		} catch (IOException e) {
			System.out.println("[Warning!] A client has been closed.");
		}
	}

	private void read() throws IOException {
		byte[] arr = new byte[1024];
		ByteBuffer buff = ByteBuffer.wrap(arr);
		int num = sc.read(buff);
		if(num == -1) {
			System.out.println("[Warning!] A client has been closed.");
			closeChannel();
			return;
		}
		String str = new String(arr);
		if(str != null && false == str.isEmpty()) {
			process(str);
			System.out.println(sc.getRemoteAddress().toString() + ">" + str);
			state = 1;
			sk.interestOps(SelectionKey.OP_WRITE);
			sk.selector().wakeup();
		}
	}
	
	private void write() throws IOException{
		String str = "Your message has send to " + sc.getLocalAddress().toString() + "\r\n";
		ByteBuffer buff = ByteBuffer.wrap(str.getBytes());
		while(buff.hasRemaining()) {
			sc.write(buff);
		}
		state = 0;
		sk.interestOps(SelectionKey.OP_READ);
		sk.selector().wakeup();
	}
	
	private void process(String str) {
		
	}
}