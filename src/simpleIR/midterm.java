package simpleIR;

public class midterm {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if(args.length>0) {
			if (args[0].equals("-s")) {
				if (args[2].equals("-q")) {
					 genSnippet search = new genSnippet(args[1],args[3]);
				}
			}
				
		}
	}

}
