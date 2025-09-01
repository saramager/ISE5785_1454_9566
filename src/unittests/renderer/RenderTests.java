package unittests.renderer;

import static java.awt.Color.YELLOW;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.LoaderXml;
import scene.Scene;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
public class RenderTests {
	/** White color for the background of the scene */
	private static final java.awt.Color WHITE = new java.awt.Color(255, 255, 255);
	/** blue color **/
	private static final java.awt.Color BLUE = new java.awt.Color(0, 0, 255);
	/** red color **/
	private static final java.awt.Color RED = new java.awt.Color(255, 0, 0);
	/** green color **/
	private static final java.awt.Color GREEN = new java.awt.Color(0, 255, 0);

	/** Default constructor to satisfy JavaDoc generator */
	public RenderTests() {
		/* to satisfy JavaDoc generator */ }

	/** Camera builder of the tests */
	private final Camera.Builder camera = Camera.getBuilder() //
			.setLocation(Point.ZERO).setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
			.setVpDistance(100) //
			.setVpSize(500, 500);

	/**
	 * Produce a scene with basic 3D model and render it into a png image with a
	 * grid
	 */
	@Test
	public void renderTwoColorTest() {
		Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191)));
		scene.geometries //
				.add(// center
						new Sphere(new Point(0, 0, -100), 50d),
						// up left
						new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
						// down left
						new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
						// down right
						new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(YELLOW)) //
				.writeToImage("Two color render test");
	}

	// For stage 6 - please disregard in stage 5
	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	/*
	 * @Test public void renderMultiColorTest() { Scene scene = new
	 * Scene("Multi color").setAmbientLight(new AmbientLight(new Color(51, 51,
	 * 51))); scene.geometries // .add(// center new Sphere(new Point(0, 0, -100),
	 * 50), // up left new Triangle(new Point(-100, 0, -100), new Point(0, 100,
	 * -100), new Point(-100, 100, -100)) // .setEmission(new Color(GREEN)), // down
	 * left new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new
	 * Point(-100, -100, -100)) // .setEmission(new Color(RED)), // down right new
	 * Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100,
	 * -100, -100)) // .setEmission(new Color(BLUE)));
	 * 
	 * camera // .setRayTracer(scene, RayTracerType.SIMPLE) // .setResolution(1000,
	 * 1000) // .build() // .renderImage() // .printGrid(100, new Color(WHITE)) //
	 * .writeToImage("color render test"); }
	 */

	/** Test for XML based scene - for bonus */

	@Test
	public void basicRenderXml() {
		Scene scene = new Scene("Using XML");
		try {
			scene = LoaderXml.loadFromXml("xml//renderTestTwoColors.xml");
		} catch (Exception e) {

			e.printStackTrace();
		}
		// enter XML file name and parse from XML file into scene object instead of the
		// new Scene above,
		// Use the code you added in appropriate packages // ...
		// NB: unit tests is not the correct place to put XML parsing code

		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(1000, 1000).build().renderImage()
				.printGrid(100, new Color(YELLOW)).writeToImage("xml render test");
	}

	/** Test for JSON based scene - for bonus */

	/*
	 * @Test public void basicRenderJson() { Scene scene = new Scene("Using Json");
	 * // enter XML file name and parse from JSON file into scene object instead of
	 * the // new Scene above, // Use the code you added in appropriate packages //
	 * ... // NB: unit tests is not the correct place to put XML parsing code
	 * 
	 * camera // .setRayTracer(scene, RayTracerType.SIMPLE) // .setResolution(1000,
	 * 1000) // .build() // .renderImage() // .printGrid(100, new Color(YELLOW)) //
	 * .writeToImage("xml render test"); }
	 */

	// For stage 6 - please disregard in stage 5
	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	@Test
	void renderMultiColorTest() {
		Scene scene = new Scene("Multi color").setAmbientLight(new AmbientLight(new Color(51, 51, 51)));
		scene.geometries //
				.add(// center
						new Sphere(new Point(0, 0, -100), 50.0),
						// up left
						new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)) //
								.setEmission(new Color(GREEN)),
						// down left
						new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)) //
								.setEmission(new Color(RED)),
						// down right
						new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)) //
								.setEmission(new Color(BLUE)));

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(WHITE)) //
				.writeToImage("color render test");

	}

	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	@Test
	void ourRenderMultiColorTest() {
		Scene scene = new Scene("Multi color").setAmbientLight(new AmbientLight((new Color(WHITE))));
		scene.geometries //
				.add(// center
						new Sphere(new Point(0, 0, -100), 50.0).setMaterial(new Material().setKA(0.4)),

						// up left
						new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
								.setMaterial(new Material().setKA(new Double3(0, 0.8, 0))), //
						// down left
						new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
								.setMaterial(new Material().setKA(new Double3(0.8, 0, 0))), //

						// down right
						new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
								.setMaterial(new Material().setKA(new Double3(0, 0, 0.8)))); //

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(WHITE)) //
				.writeToImage("our color render test");

	}

//	@Test
	/**
	 * Test for camera rotation and translation
	 */
	public void testCameraRotationAndTranslation() {
		// Scene basic setup
		Scene scene = new Scene("Rotation and Translation Test").setBackground(new Color(75, 127, 90))
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191)));

		// Add a few objects to the scene
		scene.geometries.add(new Sphere(new Point(0, 0, -100), 50d),
				new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
				new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
				new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

		// Define the initial camera settings
		Camera.Builder camera4 = camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(1000, 1000);

		// 1. Render image with no transformation (original position)
		Camera.Builder camera1 = camera4;
		camera1.build().renderImage().writeToImage("renderOriginal.png");

		// 2. Translate camera in the X direction and render image
		Camera.Builder camera2 = camera4;
		camera2.setTranslation(new Vector(50, 0, 0));
		camera2.build().renderImage().writeToImage("renderTranslatedX.png");

		// 3. Translate camera in the Y direction and render image
		Camera.Builder camera3 = camera4;

		camera3.setTranslation(new Vector(0, 50, 0));
		camera3.build().renderImage().writeToImage("renderTranslatedY.png");

		// 4. Rotate camera around the Y axis by 45 degrees and render image
		Camera.Builder camera5 = camera4;
		camera5.setRotation(45, new Vector(0, 1, 0)); // Rotate around Y axis by 45 degrees
		camera5.build().writeToImage("renderRotated45.png");

		// 5. Rotate camera around the Y axis by 90 degrees and render image
		Camera.Builder camera6 = camera4;
		camera6.setRotation(90, new Vector(0, 1, 0)); // Rotate around Y axis by 90 degrees
		camera6.build().writeToImage("renderRotated90.png");

		// 6. Translate camera and rotate, then render image
		Camera.Builder camera7 = camera4;

		camera7.setTranslation(new Vector(50, 50, 0)).setRotation(45, new Vector(0, 1, 0));
		camera7.build().renderImage().writeToImage("renderTranslatedAndRotated45.png");
	}

}
