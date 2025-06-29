package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Test class for creating a snow globe scene with transparency and reflections
 */
class SnowGlobeTest {

	/**
	 * Camera setup for the snow globe scene
	 */
	private final Camera.Builder camera = Camera.getBuilder().setLocation(new Point(0, 20, 100))
			.setDirection(new Point(0, -10, -50), Vector.AXIS_Y).setVpDistance(500).setVpSize(500, 500);

	/**
	 * Test method for snow globe scene
	 */
	@Test
	void snowGlobeTest() {
		Scene scene = new Scene("Snow Globe Test").setBackground(new Color(20, 30, 50))
				.setAmbientLight(new AmbientLight(new Color(15, 20, 30)));

		// Materials
		Material glass = new Material().setKD(0.1).setKS(0.9).setShininess(100).setKR(0.3).setKT(0.7);
		Material snow = new Material().setKD(0.8).setKS(0.2).setShininess(30).setKR(0.1);
		Material metal = new Material().setKD(0.3).setKS(0.7).setShininess(80).setKR(0.6);
		Material crystal = new Material().setKD(0.2).setKS(0.8).setShininess(120).setKR(0.4).setKT(0.5);

		// Main snow globe (transparent sphere)
		scene.geometries
				.add(new Sphere(new Point(0, 0, -50), 30.0).setEmission(new Color(5, 10, 15)).setMaterial(glass));

		// Snowman inside globe
		// Bottom sphere
		scene.geometries
				.add(new Sphere(new Point(0, -15, -50), 6.0).setEmission(new Color(240, 240, 240)).setMaterial(snow));
		// Middle sphere
		scene.geometries
				.add(new Sphere(new Point(0, -5, -50), 4.5).setEmission(new Color(235, 235, 235)).setMaterial(snow));
		// Head sphere
		scene.geometries
				.add(new Sphere(new Point(0, 5, -50), 3.5).setEmission(new Color(230, 230, 230)).setMaterial(snow));

		// Snowman eyes
		scene.geometries.add(new Sphere(new Point(-1, 6, -47), 0.4).setEmission(new Color(20, 20, 20))
				.setMaterial(new Material().setKD(0.9).setKS(0.1)));
		scene.geometries.add(new Sphere(new Point(1, 6, -47), 0.4).setEmission(new Color(20, 20, 20))
				.setMaterial(new Material().setKD(0.9).setKS(0.1)));

		// Carrot nose (triangle)
		scene.geometries.add(new Triangle(new Point(0, 5, -46.5), new Point(-0.3, 4.7, -44), new Point(0.3, 4.7, -44))
				.setEmission(new Color(255, 140, 0))
				.setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(20)));

		// Snow particles inside globe (small spheres)
		for (int i = 0; i < 50; i++) {
			double angle = Math.random() * 2 * Math.PI;
			double radius = Math.random() * 25;
			double height = Math.random() * 50 - 25;

			double x = radius * Math.cos(angle);
			double z = -50 + radius * Math.sin(angle);
			double y = height;

			scene.geometries.add(new Sphere(new Point(x, y, z), 0.3 + Math.random() * 0.5)
					.setEmission(new Color(220, 230, 240)).setMaterial(crystal));
		}

		// Base of snow globe (using triangles to form octagon)
		for (int i = 0; i < 8; i++) {
			double angle1 = 2 * Math.PI * i / 8;
			double angle2 = 2 * Math.PI * (i + 1) / 8;

			double x1 = 35 * Math.cos(angle1);
			double z1 = -50 + 35 * Math.sin(angle1);
			double x2 = 35 * Math.cos(angle2);
			double z2 = -50 + 35 * Math.sin(angle2);

			// Base triangle
			scene.geometries.add(new Triangle(new Point(0, -30, -50), new Point(x1, -30, z1), new Point(x2, -30, z2))
					.setEmission(new Color(139, 69, 19))
					.setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(20)));
		}

		// Decorative elements outside globe
		scene.geometries.add(
				new Sphere(new Point(-50, -10, -30), 5.0).setEmission(new Color(150, 150, 200)).setMaterial(metal));

		scene.geometries
				.add(new Sphere(new Point(50, -5, -70), 4.0).setEmission(new Color(200, 150, 150)).setMaterial(metal));

		// Triangular decorations
		scene.geometries.add(new Triangle(new Point(-30, -25, -20), new Point(-20, -25, -10), new Point(-25, -5, -15))
				.setEmission(new Color(100, 150, 200)).setMaterial(crystal));

		scene.geometries.add(new Triangle(new Point(25, -20, -15), new Point(35, -20, -25), new Point(30, 0, -20))
				.setEmission(new Color(200, 150, 100)).setMaterial(crystal));

		// Floor plane
		scene.geometries.add(new Plane(new Point(0, -40, 0), new Vector(0, 1, 0)).setEmission(new Color(50, 50, 70))
				.setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60).setKR(0.2)));

		// Light sources
		scene.lights.add(new DirectionalLight(new Color(120, 150, 180), new Vector(1, -1, -1)));

		scene.lights.add(new PointLight(new Color(200, 180, 150), new Point(0, 30, -20)).setKl(0.0005).setKq(0.0001));

		scene.lights.add(new PointLight(new Color(100, 150, 200), new Point(-40, 10, -30)).setKl(0.0008).setKq(0.0002));

		scene.lights.add(new PointLight(new Color(200, 150, 100), new Point(40, 15, -70)).setKl(0.0006).setKq(0.0001));

		scene.lights.add(new SpotLight(new Color(180, 200, 220), new Point(0, 50, -10), new Vector(0, -1, -0.5))
				.setKl(0.0003).setKq(0.00005));

		// Render the scene
		camera.setRayTracer(scene, RayTracerType.GRID).setResolution(600, 600).setMultithreading(-1).setDebugPrint(0.1)
				.build().renderImage().writeToImage("Snow Globe Test");
	}
}