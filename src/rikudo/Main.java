/*
 * Info project:rikudo
 * Author: Xinzhi MU, Xiang Chen
 * 
 */

package rikudo;


public class Main {
	static String path = "resource/b.txt";
	static int sideLength = 40;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RikudoMap rm = new RikudoMap(sideLength);
		
		rm.readImage("resource/bowtie.png", 15);
		rm.checkMapAvailability();
		rm.printMap(sideLength/2);
		rm.createPuzzle();
		rm.printMap(sideLength);

		/*
		rm.readFile(path);
		
		
		rm.createPuzzle();
		
		rm.printMap(sideLength);

		/*
		rm.applyConstraints();
		rm.printMap(sideLength);
		rm.checkMapAvailability();
		rm.backtracking();*/
	}

}
