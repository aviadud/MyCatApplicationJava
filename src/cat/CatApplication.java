/**
 * 
 */
package cat;

import javax.swing.*;
import java.awt.*;

/**
 * 
 */
public class CatApplication {
	public static final String WINDOW_TITLE = "Cat images application";
	public static final String WELCOME_MESSAGE = "Welcom to my cat application. Click on the button to download and show a cat image.";
	
	
	private static CatApplication instance = null;
	private boolean initiated;
	private JFrame frame;
	private JLabel textLabel;
	private JPanel buttonsPanel;
	private JButton firstButton;
	private JButton previousButton;
	private JButton nextButton;
	private JButton lastButton;
	

	private CatApplication() {
		initiated = false;
		// Initiate frame 
		frame = new JFrame(WINDOW_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		// Create panels
		JPanel textPanel = new JPanel();
		textLabel = new JLabel(WELCOME_MESSAGE);
		textPanel.add(textLabel);
		frame.getContentPane().add(textPanel, BorderLayout.NORTH);
		
		buttonsPanel = new JPanel();
		firstButton = new JButton("First");
		previousButton = new JButton("Previous");
		nextButton = new JButton("Next");
		lastButton = new JButton("Last");
		JButton[] buttonsArray = {firstButton, previousButton, nextButton, lastButton};
		for (Component componnet : buttonsArray) {
			buttonsPanel.add(componnet);
		}
		frame.getContentPane().add(buttonsPanel, BorderLayout.CENTER);
		
		
	}

	public static synchronized CatApplication getInstance() {
		if (instance == null)
			instance = new CatApplication();
		return instance;
	}

	/**
	 * Initiate the cat application.
	 * @return If application was initiated. Return false if failed to initiate application or was already initiated.
	 */
	public synchronized boolean initiate() {
		if (initiated) {
			System.out.println("CatApplication was already initiated.");
			return false;
		}
		
        
      //Display the window.
        frame.pack();
        frame.setVisible(true);
        
		initiated = true;
		return true;
	}

}
