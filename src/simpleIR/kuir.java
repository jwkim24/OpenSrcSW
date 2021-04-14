package simpleIR;

public class kuir {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		if(args!=null) {
			if (args[0].equals("-c")) {
				MakeCollection mc = new MakeCollection(args[1]);
			} else if (args[0].equals("-k")) {
				MakeKeyword mk = new MakeKeyword(args[1]);
			} else if (args[0].equals("-i")) {
				indexer midx = new indexer(args[1]);
			} else if (args[0].equals("-s")) {
				if (args[2].equals("-q")) {
					 searcher search = new searcher(args[1],args[3]);
				}
			}	
		}
	}

}
