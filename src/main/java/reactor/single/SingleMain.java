package reactor.single;

import java.io.IOException;

public class SingleMain {

	public static void main(String[] args) throws IOException {
		SingleReactor reactor = new SingleReactor(8083);
		reactor.run();
	}

}
