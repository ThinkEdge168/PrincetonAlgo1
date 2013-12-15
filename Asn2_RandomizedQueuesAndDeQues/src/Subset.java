import java.util.Iterator;

public class Subset {
	public static void main(String[] args){
		int num = Integer.parseInt(args[0]);
		String inputString;
		RandomizedQueue<String> rQueue = new RandomizedQueue<String>();

		while(!StdIn.isEmpty()) {
			inputString = StdIn.readString();
			rQueue.enqueue(inputString);
		}
		
		Iterator<String> i = rQueue.iterator();
		while(num > 0){
			String str = i.next();
			StdOut.println(str);
			num--;
		}
	}
}