package Image;

import java.awt.Color;

public class ColoredString {
	int x,y;
	String str;
	Color color;
	int fontSize;
	ColoredString(String str,int x, int y, Color color,int fontSize){
		this.str = str;
		this.x = x;
		this.y = y;
		this.color = color;
		this.fontSize = fontSize;
	}
}
