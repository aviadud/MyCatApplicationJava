package cat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.IOException;
import java.net.URI;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utilities {

	public static final String CAT_API_TOKEN_PATH = "CatApiToken";
	private static final String CAT_API_URL = "https://api.thecatapi.com/v1/images/search?limit=%d&api_key=%s";

	public static String getCatApiToken() {
		Path catApiTokenPath = Paths.get(CAT_API_TOKEN_PATH);
		try {
			if (!Files.exists(catApiTokenPath)) {
				System.err.println(String.format("File %s don't exists.", CAT_API_TOKEN_PATH));
				return "";
			}

			return Files.readString(catApiTokenPath).trim();
		} catch (IOException e) {
			System.err.println(String.format("Error reading the token file at %s\nError message: %s ",
					CAT_API_TOKEN_PATH, e.getMessage()));
			return "";
		}
	}

	public static ArrayList<String> getCatImageUrls(int numberOfImages) {
		numberOfImages = (numberOfImages > 0) ? numberOfImages : 1; // if numberOfImages has invalid value - change to 1
		ArrayList<String> result = new ArrayList<String>(numberOfImages);
		try {
			// Create URL object
			String apiKey = getCatApiToken();
			if (apiKey.isBlank())
				return result;
			URI uri = new URI(String.format(CAT_API_URL, numberOfImages, apiKey));
			// get Json
			ObjectMapper objectMapper = new ObjectMapper();
			// parse urls from Json
			JsonNode rootNode = objectMapper.readTree(uri.toURL());
			if (rootNode.isArray()) {
				for (JsonNode node : rootNode) {
					String catUrl = node.get("url").asText();
					result.add(catUrl);
				}
			}
		} catch (Exception e) {
			System.err.println(
					String.format("Failed to get cat images from the cat API. Original error:\n%s", e.getMessage()));
		}
		return result;
	}
	
	
	/**
	 * Resize image while maintaining its original aspect ratio according to given maximum dimension.
	 * @param originalImage - The original image.
	 * @param maxDimension - The maximum dimension that need to be kept in the result image.
	 * @return The original image after resizing to the given maximum dimension.
	 */
	public static BufferedImage resizeImage(BufferedImage originalImage, int maxDimension) {
		int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculate the new dimensions while maintaining the aspect ratio
        int newWidth, newHeight;
        if (originalWidth > originalHeight) {
            newWidth = maxDimension;
            newHeight = (originalHeight * maxDimension) / originalWidth;
        } else {
            newHeight = maxDimension;
            newWidth = (originalWidth * maxDimension) / originalHeight;
        }

        // Create a new buffered image with the calculated dimensions
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        // Draw the original image into the resized image
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();

        return resizedImage;
    }

}
