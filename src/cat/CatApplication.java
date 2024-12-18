package cat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The main class to handle the act application. Flowing the singleton pattern
 * design to prevent too more one instance of this window. Show show the user a
 * slide show of cat images after downloading them.
 *
 * @author Aviadud
 */
public class CatApplication {
	public static final int NUMBER_OF_IMAGES = 25; ///< Default number of images to display.
	public static final int MAX_IMAGES = 100; ///< The limit of images that can be shown. (result of the limit on a single call to the CatAPI).
	public static final String WINDOW_TITLE = "Cat images application"; ///< Title for the application window.
	public static final String WELCOME_MESSAGE = "Welcom to my cat application. Click on the button to download and show a cat image."; ///< Welcome message to great users when starting the application.
	public static final String DOWNLOADING_MESSAGE = "Downloading json from CatAPI.."; ///< Message to show when downloading the json from the CatAPI.
	public static final String DOWNLOADING_IMAGE_MESSAGE = "Downloading cat image %d/%d"; ///< Message to show when downloading an image.
	public static final String CAT_IMAGE_MESSAGE = "Cat Image %d/%d"; ///< Message to show when cat image is shown.
	public static final String FAILED_TO_DOWNLOAD_URLS_MESSAGE = "Failed to get a response from CatAPI. Try later."; ///< Failure to show when can't get response from CatAPI.
	public static final String FAILED_TO_DOWNLOAD_IMAGE_MESSAGE = "Failed to download cat image %d/%d"; ///< Failure to show when can't download an image.
	public static final int MAX_IMAGE_HEIGHT = 1000; ///< The limit on image height to prevent the window to too big.

	private static CatApplication instance = null;
	private boolean initiated;
	private int numberOfImages = NUMBER_OF_IMAGES;
	private BufferedImage[] catImages;
	private JFrame frame;
	private JLabel textLabel;
	private JPanel buttonsPanel;
	private JButton firstButton;
	private JButton previousButton;
	private JButton nextButton;
	private JButton lastButton;
	private ImageIcon catImage;
	private JLabel imageLabel;
	private int currentImageIndex = 0;

	// Private constructor prevents instantiation from other classes.
	private CatApplication() {
		initiated = false;
		// Initiate frame
		frame = new JFrame(WINDOW_TITLE);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		// Create panels
		JPanel textPanel = new JPanel();
		textLabel = new JLabel(WELCOME_MESSAGE);
		textPanel.add(textLabel);
		frame.getContentPane().add(textPanel, BorderLayout.NORTH);

		buttonsPanel = new JPanel();
		firstButton = new JButton("Download cat images");
		buttonsPanel.add(firstButton);
		frame.getContentPane().add(buttonsPanel, BorderLayout.CENTER);

		catImage = new ImageIcon();
		imageLabel = new JLabel(catImage);
		frame.getContentPane().add(imageLabel, BorderLayout.SOUTH);
	}

	/**
	 * Returns the single instance of the CatApplication class.
	 * 
	 * @return CatApplication instance.
	 */
	public static synchronized CatApplication getInstance() {
		if (instance == null) {
			instance = new CatApplication();
		}
		return instance;
	}

	/**
	 * Initiate the cat application.
	 *
	 * @param numberOfImages the number of images to show.
	 * @return If application was initiated. Return false if failed to initiate
	 *         application or was already initiated.
	 */
	public synchronized boolean initiate(int numberOfImages) {
		if (initiated) {
			System.out.println("CatApplication was already initiated.");
			return false;
		}
		this.numberOfImages = numberOfImages < MAX_IMAGES ? numberOfImages : MAX_IMAGES;
		firstButton.addActionListener(new ActionListener() {
			private boolean clicked = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!clicked) {
					clicked = true;
					textLabel.setText(DOWNLOADING_MESSAGE);
					firstButton.setEnabled(false);
					new Thread() {
						@Override
						public void run() {
							dowladCatImages();
						}
					}.start(); // new thread so GUI won't be stuck
				}
			}
		});

		// Display the window.
		frame.pack();
		frame.setVisible(true);

		initiated = true;
		return true;
	}

	/**
	 * Initiate the cat application with default number of images.
	 *
	 * @return If application was initiated. Return false if failed to initiate
	 *         application or was already initiated.
	 */
	public synchronized boolean initiate() {
		return initiate(NUMBER_OF_IMAGES);
	}

	private void dowladCatImages() {
		List<String> catImageUrls = Utilities.getCatImageUrls(numberOfImages);
		catImages = new BufferedImage[numberOfImages];
		if (catImageUrls.isEmpty()) {
			textLabel.setText(FAILED_TO_DOWNLOAD_URLS_MESSAGE);
			return;
		}
		for (int i = 0; i < numberOfImages; i++) {
			String url = catImageUrls.get(i);
			try {
				textLabel.setText(String.format(DOWNLOADING_IMAGE_MESSAGE, i + 1, numberOfImages));
				catImages[i] = ImageIO.read(new URI(url).toURL());
				if (catImages[i].getHeight() > MAX_IMAGE_HEIGHT) {
					System.out.println(
							String.format("Changed image cat %d size to match GUI window size. Original height: %d", i,
									catImages[i].getHeight()));
					catImages[i] = Utilities.resizeImage(catImages[i], MAX_IMAGE_HEIGHT);
				}
			} catch (Exception e) {
				System.out.println(String.format("Failed to dowload cat images from the url %s. Original error:\n%s",
						url, e.getMessage()));
			}
		}
		showDowladedCats();
	}

	private void showDowladedCats() {
		firstButton.setText("First");
		firstButton.setEnabled(true);
		previousButton = new JButton("Previous");
		nextButton = new JButton("Next");
		lastButton = new JButton("Last");

		// show all buttons
		JButton[] buttonsArray = { previousButton, nextButton, lastButton };
		for (JButton button : buttonsArray) {
			buttonsPanel.add(button);
		}
		// change image to the first one
		currentImageIndex = 0;
		updateImage();
		// set buttons functionality
		firstButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentImageIndex = 0;
				updateImage();
			}
		});
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentImageIndex > 0) {
					currentImageIndex--;
					updateImage();
				}
			}
		});
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentImageIndex < numberOfImages - 1) {
					currentImageIndex++;
					updateImage();
				}
			}
		});
		lastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentImageIndex = numberOfImages - 1;
				updateImage();
			}
		});
	}

	private void updateImage() {
		if (catImages[currentImageIndex] != null) {
			textLabel.setText(String.format(CAT_IMAGE_MESSAGE, currentImageIndex + 1, numberOfImages));
			catImage.setImage(catImages[currentImageIndex]);
			imageLabel.setVisible(true);
		} else {
			imageLabel.setVisible(false);
			textLabel.setText(String.format(FAILED_TO_DOWNLOAD_IMAGE_MESSAGE, currentImageIndex + 1, numberOfImages));
		}
		frame.pack();
	}

}
