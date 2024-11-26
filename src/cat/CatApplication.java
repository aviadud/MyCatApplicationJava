/**
 * 
 */
package cat;

/**
 * 
 */
public class CatApplication {
	private static CatApplication instance = null;

	private CatApplication() {

	}

	public static synchronized CatApplication getInstance() {
		if (instance == null)
			instance = new CatApplication();
		return instance;
	}

	public synchronized boolean initiate() {

		return false;
	}

}
