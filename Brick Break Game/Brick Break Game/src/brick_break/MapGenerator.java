package brick_break;

import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;

public class MapGenerator {
	public int map[][];
	public int brickwidth;
	public int brickheight;
	private int brickCount;
	public MapGenerator(int row, int col) {
		brickCount = 0;
		map = new int[row][col];
		for(int i=0 ; i< map.length ; i++) {
			for(int j=0 ; j<map[0].length; j++) {
				if((int)(Math.random()*3 + 1) > 2){ 
					map[i][j] = 1; 
					brickCount++;
				}
				else{ 
					map[i][j] = (int) (Math.random()*2);		// 1 = Ball is not hit. 0 = Ball is hit, it will be removed
					if(map[i][j] == 1) brickCount++;
				}
			}
		}
		
		brickwidth = 540/col;
		brickheight = 150/row;
	}
	
	public void draw(Graphics2D g) {
		for(int i=0 ; i< map.length ; i++) {
			for(int j=0 ; j<map[0].length; j++) {
				if(map[i][j] > 0) {
					g.setColor(Color.white);
					g.fillRect(j*brickwidth+80, i*brickheight+50, brickwidth, brickheight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j*brickwidth+80, i*brickheight+50, brickwidth, brickheight);
				}
			}
		}
	}
	
	public int returnBricks() {
		return brickCount;
	}
	
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
}
