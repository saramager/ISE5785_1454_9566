/**
 * test of image writer basic picture 
 */
package unittests.renderer;

import org.junit.jupiter.api.Test;

import primitives.Color;
import renderer.ImageWriter;

/**
 * test of image writer basic picture
 */
class ImageWriterTest {

	/**
	 * Test method for {@link renderer.ImageWriter#writeToImage(java.lang.String)}.
	 */
	@Test
	void testWriteToImage() {
		int length = 500, width = 800;
		ImageWriter image = new ImageWriter(width, length);
		Color backgroundColor = new Color(200, 200, 255); // Light Purple background
		Color gridColor = Color.BLACK; // Black grid lines

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				image.writePixel(i, j, i % 50 == 0 || j % 50 == 0 ? gridColor : backgroundColor);
		}
		image.writeToImage("test");

	}

}
