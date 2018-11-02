package reactor.mul;

import java.io.IOException;

public class MulMain {

	public static void main(String[] args) throws IOException {
		MulReactor reactor = new MulReactor(8083);
		new Thread(reactor).start();
	}

}
