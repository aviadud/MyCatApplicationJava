/**
 * A lightweight Java Swing application that displays cat images retrieved from the cat API.
 */
package cat;

/**
 * The entry point of the cat application.
 * <p>
 * This application uses the catAPI to get cat images and display them to the
 * user using java Swing. It should also provide user the ability to navigate
 * between the cat images.
 * </p>
 * 
 * @author Aviadud
 */
public class Main {
	public static final int DEFAULT_NUMBER_OF_CATS = 30; /// < The default number of cat images to show

	/**
	 * The main method to launch the Cat application. The number of images is
	 * determine by the value given by user, but can't be over the limit. If a value
	 * is not given by the user, a default number of images will be shown.
	 * 
	 * @param args command-line arguments. The first value given will determine the
	 *             number of images that will be shown if given.
	 */
	public static void main(String[] args) {
		int numberOfCats = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_NUMBER_OF_CATS;
		if (numberOfCats > CatApplication.MAX_IMAGES) {
			System.err.println(String.format(
					"The value given for number of cat images, %d, is over the limit of %d images. Only %d images will be shown.",
					numberOfCats, CatApplication.MAX_IMAGES, CatApplication.MAX_IMAGES));
			numberOfCats = CatApplication.MAX_IMAGES;
		}
		CatApplication app = CatApplication.getInstance();
		app.initiate(numberOfCats);

	}

}
