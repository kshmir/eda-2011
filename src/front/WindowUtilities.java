package front;

import javax.swing.*;

import front.ExitListener;

import java.awt.*;

/**
 * @author Murciélagos
 *
 * This class helps to build the main frame, it only contains static functions, it shouldn't be instanced.
 */
public class WindowUtilities {

	public static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
	}

	public static JFrame openInJFrame(Container content, int width, int height,
			String title, Color bgColor, JFrame frame) {
		
		frame.setBackground(bgColor);
		content.setBackground(bgColor);
		frame.setSize(width, height);
		frame.setContentPane(content);
		frame.addWindowListener(new ExitListener());
		frame.setVisible(true);
		return (frame);
		
	}


	public static JFrame openInJFrame(Container content, int width, int height,
			String title, JFrame panel) {
		return (openInJFrame(content, width, height, title, Color.white, panel ));
	}


}