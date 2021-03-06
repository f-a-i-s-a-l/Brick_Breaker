package brick_break;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	
	// Flag to check if game is running; to ensure game doesn't start automatically
	private boolean play = false;	
	
	private int score = 0; // Store score
	private int totalBricks;	// Total bricks
	private int scoreBoost = 0;	// Increase score if bricks are hit consecutively. Reset to 0 if paddle is hit
			
	private Timer timer;			// The timer helps creating speed for ball
	private int delay = 1;		// Delay will be used to adjust speed
	
	private int playerX = 310;		// 	Starting point of slider
	private int ballPosX = (int) (Math.random()*500);		//	Starting position of ball in X
	private int ballPosY = (int) (Math.random()*50) + 350;		//	Starting position of ball in Y
	private int ballXDir = -1;		// 	Ball direction in X
	private int ballYDir = -2;		// 	Ball direction in Y
	
	// Map Generator instance
	private MapGenerator map;
	
	Gameplay(){
		map = new MapGenerator(3,7);
		totalBricks = map.returnBricks();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		timer = new Timer(delay, this);
		timer.start();
	}

	
	public void paint(Graphics g) {
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// draw the map
		map.draw((Graphics2D)g);
		
		// borders
		g.setColor(Color.red);
		g.fillRect(0, 0, 3, 592);		// Left Border
		g.fillRect(0, 0, 692, 3);		// Top Border
		g.fillRect(684, 0, 3, 592);		// Right Border
		
		// Scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Score: " + score, 550, 30);
		
		
		// The paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		// The ball
		g.setColor(Color.yellow);
		g.fillOval(ballPosX, ballPosY, 20, 20);
		
		// Print total bricks Remaining
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Bricks Left: " + totalBricks, 30, 30);
		
		
		// Check if ball is out of game, i.e., game is lost
		if(ballPosY > 570) {
			play = false;		// Stop the game. This enables restart by pressing Enter
			
			// Freeze the ball movement
			ballXDir = 0;
			ballYDir =0;
			
			// Game Over message
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over. Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart ", 220, 350);
		}
		
		// if all bricks are broken, i.e., game is complete
		if(totalBricks == 0) {
			play = false;		// Stop the game
			
			// Freese the ball movement
			ballXDir = 0;
			ballYDir =0;
			
			// Game Won message
			g.setColor(Color.green);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Won! Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart ", 220, 350);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		// Add movement to the ball
		if(play) {
			// Changeing ball direction on hitting paddle
			if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYDir = -ballYDir;
				scoreBoost = 0;
			}
			
			// Adding score and remove bricks when hit by ball
			A: for(int i=0 ; i<map.map.length ; i++) {
				for(int j=0 ; j<map.map[0].length ; j++) {
					if(map.map[i][j]>0) {	// i.e., if the brick is still present
						// Get the position and dimensions of the brick
						int brickX = j*map.brickwidth + 80;
						int brickY = i*map.brickheight + 50;
						int brickwidth = map.brickwidth;
						int brickheight = map.brickheight;
						
						// Create rectangles to detect collision
						Rectangle rect = new Rectangle(brickX, brickY, brickwidth, brickheight);
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
						Rectangle brickRect = rect;
						
						// If rectangles collide:
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);		// Set the brick value to 0
							totalBricks--;		// Brick count reduced by 1
							score+=5*(++scoreBoost);			// 5 points added to score
							
							// Add recoil collision for breaking the brick
							if(ballPosX + 19 <= brickRect.x || ballPosX +1 >= brickRect.x + brickRect.width) {
								ballXDir = -ballXDir;
							} else {
								ballYDir = -ballYDir;
							}
							
							// Randomly Generate a new brick at a random empty spot
							int randX = (int) (Math.random()*3);	// A random Row value
							int randY = (int) (Math.random()*7);	// A random Col value
							if(map.map[randX][randY]<1) {
								int isNewBrick = (int) (Math.random()*2);
								if(isNewBrick == 1) {
									map.setBrickValue(1, randX, randY);
									totalBricks++;									
								}
							}
							
							// Break out of the entire loop once the brick is removed
							break A;
						}
					}
						
				}
			}
			
			ballPosX += ballXDir;
			ballPosY += ballYDir;
			
			// Add wall collision effect
			if(ballPosX < 0)
				ballXDir = -ballXDir;
			if(ballPosY < 0)
				ballYDir = -ballYDir;
			if(ballPosX > 670)
				ballXDir = -ballXDir;
		}
		repaint();	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			if(playerX >= 580) {
				playerX = 580;
			}else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			if(playerX <= 10) {
				playerX = 10;
			}else {
				moveLeft();
			}
		}
		
		// Code to restart the game when Game is over
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballPosX = (int) (Math.random()*500);		//	Starting position of ball in X
				ballPosY = (int) (Math.random()*50) + 350;		//	Starting position of ball in Y
				ballXDir = -1;		// 	Ball direction in X
				ballYDir = -2;		// 	Ball direction in Y
				playerX = 310;
				score =0;
				totalBricks = 21;
				map = new MapGenerator(3,7);
				
				repaint();
			}
		}
	}
	
	// Method to move right
	public void moveRight() {
		play = true;
		playerX += 20;
	}
	
	// Method to move left
	public void moveLeft() {
		play = true;
		playerX -= 20;
	}
	
	// keyTyped() and keyReleased are not required
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}



}
