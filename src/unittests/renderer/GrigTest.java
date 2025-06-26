/**
 * 
 */
package unittests.renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * 
 */
class GrigTest {
	private final Scene scene = new Scene("Test scene");

	@Test
	public void testBlurryGlassWithout2() {

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30).reduce(2)));
		for (int i = -4; i < -3; i += 4) {
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

			scene.lights.add(new DirectionalLight(new Color(255, 255, 255).reduce(1), new Vector(-0.4, 1, 0)));

			scene.lights.add(new PointLight(new Color(255, 200, 200), new Point(20, -5, 8)).setKl(0.05).setKq(0.01));

			scene.lights.add(new PointLight(new Color(200, 255, 200), new Point(-20, -5, 8)).setKl(0.05).setKq(0.01));

			scene.lights
					.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(0, 15, 0), new Vector(0, -1, 0))
							.setKl(0.1).setKc(0.1));

			scene.lights.add(new PointLight(new Color(200, 200, 255), new Point(0, -5, -15)).setKl(0.08).setKq(0.015));
			Camera.Builder cameraBuilder = Camera.getBuilder() //
					.setRayTracer(scene, RayTracerType.GRID);
			cameraBuilder//
					.setResolution(500, 500)//
					.setLocation(new Point(0, -1000, 0))//
					.setDirection(new Point(0, 0, -12), Vector.AXIS_Z)//
					.setVpDistance(1000).setVpSize(70, 50)//
					.setResolution(700, 500).setAntiAliasingRays(9)//
					.setMultithreading(-1).setDebugPrint(0.1)//
					.build().renderImage().writeToImage("blurryGlassWithout");
		}
	}

	@Test
	public void renderSingleSphereReflectionWith() {

		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7).setRAngle(5)));

		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d).setEmission(new Color(0, 100, 200))
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9)));

		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008));

		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));
		Camera.Builder cameraBuilder = Camera.getBuilder() //
				.setRayTracer(scene, RayTracerType.GRID);
		cameraBuilder.setLocation(new Point(0, -120, 40)).setDirection(new Point(0, 0, 20), new Vector(0, 0, 1))
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).setMultithreading(-1).setDebugPrint(0.1)
				.build().renderImage().writeToImage("grigShpere");
	}

	@Test
	void sphereDirectional() {
		/** First scene for some of tests */
		Scene scene1 = new Scene("Test scene");
		scene1.geometries.add(new Sphere(new Point(0, 0, -50), 50.0).setEmission(new Color(BLUE).reduce(2))
				.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(301)));

		/** Position of the light in tests with sphere */
		Point sphereLightPosition = new Point(-50, -50, 25);
		/** Light direction (directional and spot) in tests with sphere */
		Vector sphereLightDirection = new Vector(1, 1, -0.5);

		scene1.lights.add(new SpotLight(new Color(800, 500, 0), sphereLightPosition, sphereLightDirection) //
				.setKl(0.001).setKq(0.0001));

		Camera.getBuilder() //
				.setRayTracer(scene1, RayTracerType.GRID) //
				.setLocation(new Point(0, 0, 1000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpSize(150, 150).setVpDistance(1000) //
				.setResolution(500, 500) //
				.setDebugPrint(0.1)//
				.build() //
				.renderImage() //
				.writeToImage("lightSphereDirectional222");

		/** First camera builder for some of tests */

	}

	@Test
	void trianglesSpotSharp() {

		Scene scene2 = new Scene("Test scene").setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

		Color trianglesLightColor = new Color(800, 500, 250);
		Point[] vertices = {
				// the shared left-bottom:
				new Point(-110, -110, -150),
				// the shared right-top:
				new Point(95, 100, -150),
				// the right-bottom
				new Point(110, -110, -150),
				// the left-top
				new Point(-75, 78, 100) };
		/** Diffusion attenuation factor for some of the geometries in the tests */
		Double3 KD3 = new Double3(0.2, 0.6, 0.4);

		/** Specular attenuation factor for some of the geometries in the tests */
		Double3 KS3 = new Double3(0.2, 0.4, 0.3);

		/** Material for some of the geometries in the tests */
		Material material = new Material().setKD(KD3).setKS(KS3).setShininess(301);

		/** The first triangle in appropriate tests */
		Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2]).setMaterial(material);
		/** The first triangle in appropriate tests */
		Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3]).setMaterial(material);

		Point trianglesLightPosition = new Point(30, 10, -100);
		/** Light direction (directional and spot) in tests with triangles */
		Vector trianglesLightDirection = new Vector(-2, -2, -2);

		scene2.geometries.add(triangle1, triangle2);
		scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
				.setKl(0.001).setKq(0.00004).setNarrowBeam(10));

		Camera.getBuilder() //
				.setRayTracer(scene2, RayTracerType.GRID) //
				.setLocation(new Point(0, 0, 1000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpSize(200, 200).setVpDistance(1000) //
				.setResolution(500, 500) //
				.setDebugPrint(0.1)//
				.build() //
				.renderImage() //
				.writeToImage("lightTrianglesSpotSharp222");
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a Sphere
	 * producing a shading
	 */
	@Test
	void trianglesSphere() {
		scene.geometries //
				.add( //
						new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
								.setMaterial(new Material().setKS(0.8).setShininess(60)), //
						new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
								.setMaterial(new Material().setKS(0.8).setShininess(60)), //
						new Sphere(new Point(0, 0, -11), 30d) //
								.setEmission(new Color(BLUE)) //
								.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)) //
				);
		scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
		scene.lights //
				.add(new SpotLight(new Color(700, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4)) //
						.setKl(4E-4).setKq(2E-5));
		Camera.getBuilder().setLocation(new Point(0, 0, 1000)).setDirection(Point.ZERO, Vector.AXIS_Y)
				.setVpDistance(1000).setVpSize(200, 200).setRayTracer(scene, RayTracerType.GRID) //
				.setResolution(600, 600) //
				.setDebugPrint(0.1)//
				.build() //
				.renderImage() //
				.writeToImage("shadowTrianglesSphere111");
	}

	/** Produce a picture of a sphere and triangle with point light and shade */
	@Test
	void sphereTriangleInitial() {

		Triangle triangle = new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)); //
		Point spotLocation = new Point(-100, -100, 200);

		Intersectable sphere = new Sphere(new Point(0, 0, -200), 60d).setEmission(new Color(BLUE))
				.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));

		Material trMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);

		scene.geometries.add(sphere, triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial));

		scene.lights //
				.add(new SpotLight(new Color(400, 240, 0), spotLocation, new Vector(1, 1, -3)) //
						.setKl(1E-5).setKq(1.5E-7));

		Camera.getBuilder().setLocation(new Point(0, 0, 1000)).setDirection(Point.ZERO, Vector.AXIS_Y)
				.setVpDistance(1000).setVpSize(200, 200).setRayTracer(scene, RayTracerType.GRID) //
				.setResolution(10, 10) //
				.setDebugPrint(0.1)//
				.build() //
				.renderImage() //
				.writeToImage("shadowSphereTriangleInitial222");
	}

	/** Produce a picture of a sphere lighted by a spot light */
	@Test
	void twoSpheres() {
		/** Scene for the tests */
		Scene scene = new Scene("Test scene");
		/** Camera builder for the tests with triangles */

		scene.geometries.add( //
				new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
				new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
						.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKl(0.0004).setKq(0.0000006));

		Camera.Builder cameraBuilder = Camera.getBuilder() //
				.setRayTracer(scene, RayTracerType.GRID);

		cameraBuilder.setLocation(new Point(0, 0, 1000)) //
				.setDirection(new Point(25, 25, 0), Vector.AXIS_Y) //
				.setVpDistance(10000).setVpSize(150, 150) //
				.setResolution(300, 300) //
				.setDebugPrint(0.1)//
				.build() //
				.renderImage() //
				.writeToImage("refractionTwoSpheres222");

		cameraBuilder.setRayTracer(scene, RayTracerType.SIMPLE).build() //
				.renderImage() //
				.writeToImage("refractionTwoSpheres111");
	}

	/** Produce a picture of a sphere lighted by a spot light */
	@Test
	void twoSpheresOnMirrors() {
		scene.geometries.add( //
				new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
						.setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
								.setKT(new Double3(0.5, 0, 0))), //
				new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
						.setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
						new Point(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(1)), //
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
						new Point(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
		scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
		scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKl(0.00001).setKq(0.000005));
		Camera.Builder cameraBuilder = Camera.getBuilder() //
				.setRayTracer(scene, RayTracerType.GRID);

		cameraBuilder.setLocation(new Point(0, 0, 10000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpDistance(10000).setVpSize(2500, 2500) //
				.setResolution(500, 500) //
				.setDebugPrint(0.1)//
				.build() //
				.renderImage() //
				.writeToImage("reflectionTwoSpheresMirrored333");
	}

	@Test
	public void trianglesLights() {
		Scene scene2 = new Scene("Test scene").setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
		Color trianglesLightColor = new Color(800, 500, 250);
		Point trianglesLightPosition = new Point(30, 10, -100);
		/** Light direction (directional and spot) in tests with triangles */
		Vector trianglesLightDirection = new Vector(-2, -2, -2);
		Point[] vertices = {
				// the shared left-bottom:
				new Point(-110, -110, -150),
				// the shared right-top:
				new Point(95, 100, -150),
				// the right-bottom
				new Point(110, -110, -150),
				// the left-top
				new Point(-75, 78, 100) };
		/** Diffusion attenuation factor for some of the geometries in the tests */
		Double3 KD3 = new Double3(0.2, 0.6, 0.4);

		/** Specular attenuation factor for some of the geometries in the tests */
		Double3 KS3 = new Double3(0.2, 0.4, 0.3);

		/** Material for some of the geometries in the tests */
		Material material = new Material().setKD(KD3).setKS(KS3).setShininess(301);
		/** The first triangle in appropriate tests */
		Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2]).setMaterial(material);
		/** The first triangle in appropriate tests */
		Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3]).setMaterial(material);

		scene2.geometries.add(triangle1, triangle2);

		scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
				.setKl(0.001).setKq(0.0001));
		scene2.lights.add(new PointLight(new Color(250, 2, 250), new Point(80, 80, 60)).setKl(0.0003).setKq(0.00003));
		scene2.lights.add(new DirectionalLight(new Color(50, 100, 200), new Vector(0, 1, 0)));
		/** Second camera builder for some of tests */
		Camera.Builder camera2 = Camera.getBuilder() //
				.setRayTracer(scene2, RayTracerType.GRID) //
				.setLocation(new Point(0, 0, 1000)) //
				.setDirection(Point.ZERO, Vector.AXIS_Y) //
				.setVpSize(200, 200).setVpDistance(1000);
		camera2.setResolution(500, 500) //
				.build() //
				.renderImage() //
				.writeToImage("Triangles lights11");
	}
}
