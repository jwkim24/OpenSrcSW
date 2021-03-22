package simpleIR;

public class Kuir {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		if (args[0].equals("-c")) {
			MakeCollection mc2 = new MakeCollection(args[1]);
		} else if (args[0].equals("-k")) {
			MakeKeyword mk = new MakeKeyword(args[1]);
		}
		
	}

}
