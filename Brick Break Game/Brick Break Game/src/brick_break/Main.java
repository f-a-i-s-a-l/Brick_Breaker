package brick_break;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		//The Main Frame
		JFrame jfrm = new JFrame();
		
		// An instance of Gameplay class
		Gameplay gameplay = new Gameplay();
		
		jfrm.setBounds(0, 0, 700, 600);
		jfrm.setLocationRelativeTo(null);
		jfrm.setTitle("Brick Break");
		jfrm.setResizable(false);
		jfrm.setVisible(true);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add Gameplay to JFrame
		jfrm.add(gameplay);
		
	}

}
