/**
 * 
 */
package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.Camera.Builder;
import renderer.RayTracerType;
import scene.Scene;

/**
 * 
 */
class GrigTest {

	@Test
	public void testBlurryGlassWithout() {
		setSceneForDiffusiveTest();
		updateCameraBuilderForDiffusiveTest()//
				.setAntiAliasingRays(9)//
				.setMultithreading(-1).setDebugPrint(0.1)//
				.build().renderImage().writeToImage("grid");
	}

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
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0)));

//					// Polygon remains at Y = -5
//					new Polygon(new Point(5 * i - 4, -5, -12), new Point(5 * i - 4, -5, 6), new Point(5 * i + 4, -5, 6),
//							new Point(5 * i + 4, -5, -12)).setEmission(new Color(230, 250, 215).reduce(2))
//							.setMaterial(new Material().setKD(0.001).setKS(0.002).setShininess(1).setKT(0.98)
//									.setTAngle(i * 5 + 20)));
		}

//		scene.geometries.add(//
//				// The wall
//				new Plane(new Point(0, 10, 0), Vector.AXIS_Y)//
//						.setEmission(Color.BLACK)//
//						.setMaterial(new Material().setKD(0.2).setKT(0d)),
//				// The floor
//				new Plane(new Point(0, -5, -12.1), new Vector(0, -0.5, 1))//
//						.setEmission(new Color(100, 100, 100))//
//						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));
//				// the mirror
//				new Polygon(new Point(-12, -55, -37), new Point(12, -55, -37), //
//						new Point(12, -5, -12), new Point(-12, -5, -12))//
//						.setEmission(new Color(50, 50, 50))
//						.setMaterial(new Material().setKD(0.1).setKS(1).setShininess(100).setKR(0.8).setRAngle(10)));

		scene.lights.add(new DirectionalLight(new Color(255, 255, 255).reduce(1), new Vector(-0.4, 1, 0)));

		scene.lights.add(new PointLight(new Color(255, 200, 200), new Point(20, -5, 8)).setKl(0.05).setKq(0.01));

		scene.lights.add(new PointLight(new Color(200, 255, 200), new Point(-20, -5, 8)).setKl(0.05).setKq(0.01));

		scene.lights.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(0, 15, 0), new Vector(0, -1, 0))
				.setKl(0.1).setKc(0.1));

		scene.lights.add(new PointLight(new Color(200, 200, 255), new Point(0, -5, -15)).setKl(0.08).setKq(0.015));
	}

	/** Camera builder for the tests with triangles */

	private Builder updateCameraBuilderForDiffusiveTest() {
		return cameraBuilder//
				.setLocation(new Point(0, -1000, 0))//
				.setDirection(new Point(0, 0, -12), Vector.AXIS_Z)//
				.setVpDistance(1000).setVpSize(70, 50)//
				.setResolution(700, 500)//
		;
	}

	/** Scene for the tests */
	private final Scene scene = new Scene("Test scene");
	private final Camera.Builder cameraBuilder = Camera.getBuilder() //
			.setRayTracer(scene, RayTracerType.GRID);

}
