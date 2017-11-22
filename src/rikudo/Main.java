/*
 * Info project:rikudo
 * Author: Xinzhi MU, Xiang Chen
 * 
 */

package rikudo;


public class Main {
	static String path = "resource/a.txt";
	static int sideLength = 60;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RikudoMap rm = new RikudoMap();
		rm.readFile(path);
		rm.printMap(sideLength);
	}

}
