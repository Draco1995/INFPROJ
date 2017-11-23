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
		RikudoMap rm = new RikudoMap();
		rm.readFile(path);
		rm.checkMapAvailable();
		rm.printMap(sideLength);
		rm.backtracking();
		rm.printMap(sideLength);
	}

}
