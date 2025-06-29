package unittests.renderer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.Camera.Builder;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Tests for rendering scenes with glossy surfaces and diffused blurry
 * reflections. This class contains tests for rendering scenes with spheres,
 * planes, and polygons, focusing on glossy surfaces and diffused blurry
 * reflections.
 */
class GlossySurfacesAndDiffusedBlurry {
	/** Scene for the tests */
	private final Scene scene = new Scene("Test scene");
	/** Camera builder for the tests with triangles */
	private final Camera.Builder cameraBuilder = Camera.getBuilder() //
			.setRayTracer(scene, RayTracerType.GRID);

	/**
	 * Builds and renders a scene with a single large sphere and a reflective plane.
	 */
	@Test
	@Disabled("This test is disabled because it takes a long time to run")
	void renderSingleSphereReflection() {

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

//mirror plane
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7)));
//Sphere in center
		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d).setEmission(new Color(0, 100, 200))
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9)));

		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008));

		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		cameraBuilder.setLocation(new Point(0, -120, 40)).setDirection(new Point(0, 0, 20), new Vector(0, 0, 1))
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).setMultithreading(-1).setDebugPrint(0.1)
				.build().renderImage().writeToImage("singleSphereReflection");
	}

	/**
	 * Builds and renders a scene with a single large sphere and a reflective plane,
	 * without using the anti-aliasing feature.
	 */
	// @Test
	void renderSingleSphereReflectionWith() {

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7).setRAngle(5)));

		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d).setEmission(new Color(0, 100, 200))
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9)));

		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008));

		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		cameraBuilder.setLocation(new Point(0, -120, 40)).setDirection(new Point(0, 0, 20), new Vector(0, 0, 1))
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).setMultithreading(-1).setDebugPrint(0.1)
				.build().renderImage().writeToImage("si");
	}

	/**
	 * Builds and renders a scene with multiple spheres and a polygon, with a
	 * reflective plane.
	 */

	private void setSceneForDiffusiveTest() {
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30).reduce(2)));

		for (int i = -4; i < 6; i += 4) {
			scene.geometries.add(
					// Red sphere - closest to camera, lowest height
					new Sphere(new Point(5 * i, -8, -9), 3.0).setEmission(new Color(255, 0, 0).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0.4)),

					// Green sphere - behind polygon, middle height
					new Sphere(new Point(5 * i, 0, -3), 3.0).setEmission(new Color(0, 255, 0).reduce(4).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0)),

					// Blue sphere - furthest from camera, highest
					new Sphere(new Point(5 * i, 5, 3), 3.0).setEmission(new Color(0, 0, 255).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0)),

					// Polygon remains at Y = -5
					new Polygon(new Point(5 * i - 4, -5, -12), new Point(5 * i - 4, -5, 6), new Point(5 * i + 4, -5, 6),
							new Point(5 * i + 4, -5, -12)).setEmission(new Color(230, 250, 215).reduce(2))
							.setMaterial(new Material().setKD(0.001).setKS(0.002).setShininess(1).setKT(0.98)
									.setTAngle(i * 5 + 20)));
		}

		scene.geometries.add(//
				// The wall
				new Plane(new Point(0, 10, 0), Vector.AXIS_Y)//
						.setEmission(Color.BLACK)//
						.setMaterial(new Material().setKD(0.2).setKT(0d)),
				// The floor
				new Plane(new Point(0, -5, -12.1), new Vector(0, -0.5, 1))//
						.setEmission(new Color(100, 100, 100))//
						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)),
				// the mirror
				new Polygon(new Point(-12, -55, -37), new Point(12, -55, -37), //
						new Point(12, -5, -12), new Point(-12, -5, -12))//
						.setEmission(new Color(50, 50, 50))
						.setMaterial(new Material().setKD(0.1).setKS(1).setShininess(100).setKR(0.8).setRAngle(10)));

		scene.lights.add(new DirectionalLight(new Color(255, 255, 255).reduce(1), new Vector(-0.4, 1, 0)));

		scene.lights.add(new PointLight(new Color(255, 200, 200), new Point(20, -5, 8)).setKl(0.05).setKq(0.01));

		scene.lights.add(new PointLight(new Color(200, 255, 200), new Point(-20, -5, 8)).setKl(0.05).setKq(0.01));

		scene.lights.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(0, 15, 0), new Vector(0, -1, 0))
				.setKl(0.1).setKc(0.1));

		scene.lights.add(new PointLight(new Color(200, 200, 255), new Point(0, -5, -15)).setKl(0.08).setKq(0.015));
	}

	/**
	 * Updates the camera builder for the diffusive test with specific parameters.
	 * 
	 * @return the updated camera builder
	 */
	private Builder updateCameraBuilderForDiffusiveTest() {
		return cameraBuilder//
				.setResolution(500, 500)//
				.setLocation(new Point(0, -1000, 0))//
				.setDirection(new Point(0, 0, -12), Vector.AXIS_Z)//
				.setVpDistance(1000).setVpSize(70, 50)//
				.setResolution(700, 500)//
		;
	}

	/**
	 * Builds and renders a scene with multiple spheres and a polygon, with a
	 * reflective plane.
	 */
	@Test
	public void testBlurryGlass() {
		setSceneForDiffusiveTest();
		updateCameraBuilderForDiffusiveTest()//
				.setGlossyAndDiffuseRays(289)// 289
//				.setAntiAliasingRays(81)//
				.setMultithreading(-1).setDebugPrint(0.1)//
				.build().renderImage().writeToImage("blurryGlass2");
	}

	/**
	 * Test for rendering a scene with blurry glass without using the anti-aliasing
	 * feature.
	 */
	@Test
	public void testBlurryGlassWithout() {
		setSceneForDiffusiveTest();
		updateCameraBuilderForDiffusiveTest()//
				.setRayTracer(scene, RayTracerType.GRID).setAntiAliasingRays(81)//
				.setMultithreading(-1).setDebugPrint(0.1)//
				.build().renderImage().writeToImage("blurryGlassWithout");
	}
}
