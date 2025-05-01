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
		ImageWriter image = new ImageWriter(800, 500);
		Color backgroundColor = new Color(200, 200, 255); // Light Purple background
		Color gridColor = Color.BLACK; // Black grid lines

		for (int i = 0; i < 800; i++) {
			for (int j = 0; j < 500; j++)
				if (i % 50 == 0 || j % 50 == 0)
					image.writePixel(i, j, gridColor);
				else
					image.writePixel(i, j, backgroundColor);
		}
		image.writeToImage("test");

	}

}
