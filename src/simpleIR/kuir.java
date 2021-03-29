package simpleIR;

public class kuir {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		if (args[0].equals("-c")) {
			MakeCollection mc = new MakeCollection(args[1]);
		} else if (args[0].equals("-k")) {
			MakeKeyword mk = new MakeKeyword(args[1]);
		} else if (args[0].equals("-i")) {
			indexer midx = new indexer(args[1]);
		}
		
	}

}
