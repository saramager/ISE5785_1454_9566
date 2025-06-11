package unittests.renderer;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

class GlossySurfacesAndDiffusedBlurry {
	/** Scene for the tests */
	private final Scene scene = new Scene("Test scene");
	/** Camera builder for the tests with triangles */
	private final Camera.Builder cameraBuilder = Camera.getBuilder() //
			.setRayTracer(scene, RayTracerType.SIMPLE);

	/**
	 * Builds and renders a scene with a single large sphere and a reflective plane.
	 */
	@Test
	void renderSingleSphereReflection() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// --- הוספת גופים ---

		// 1. מישור מחזיר אור (לצורך ההשתקפות) - נחזיר רצפה פשוטה עם חומר מראה
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7))); // חומר מחזיר אור

		// 2. הספרה (עיגול) - מוגדלת ומוצבת במרכז
		// מיקום: קרוב למרכז הבמה וקצת מעל הרצפה (Z=30)
		// רדיוס: מוגדל ל-25 יחידות (היה 10)
		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d) // מוגדל וממוקם במרכז
				.setEmission(new Color(0, 100, 200)) // כחול יפה
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9))); // חומר מראה חזק

		// --- כל שאר הגופים (קובייה, פירמידה, ספרות נוספות, מצולעים נוספים) - הוסרו ---

		// --- הוספת תאורה ---
		// נשמור על תאורה שמתאימה להדגשת השתקפויות
		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008)); // אור
																														// נקודתי
																														// חזק
																														// יותר
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		// --- הגדרת מצלמה ורינדור ---
		// נמקם את המצלמה כך שתתמקד בספרה וברצפה כדי לראות את ההשתקפות
		cameraBuilder.setLocation(new Point(0, -120, 40)) // קרובה יותר, קצת מעל
				.setDirection(new Point(0, 0, 20), new Vector(0, 0, 1)) // כיוון מבט אל מרכז הספרה
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).build().renderImage()
				.writeToImage("singleSphereReflection"); // שם קובץ חדש
	}

	@Test
	void renderSingleSphereReflectionWith() {
		// --- הגדרות סצנה בסיסיות ---
		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

		// --- הוספת גופים ---

		// 1. מישור מחזיר אור (לצורך ההשתקפות) - נחזיר רצפה פשוטה עם חומר מראה
		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)).setEmission(new Color(30, 30, 30))
				.setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(100).setKR(0.7).setRAngle(100))); // חומר
																													// מחזיר
																													// אור

		// 2. הספרה (עיגול) - מוגדלת ומוצבת במרכז
		// מיקום: קרוב למרכז הבמה וקצת מעל הרצפה (Z=30)
		// רדיוס: מוגדל ל-25 יחידות (היה 10)
		scene.geometries.add(new Sphere(new Point(0, 0, 25), 25d) // מוגדל וממוקם במרכז
				.setEmission(new Color(0, 100, 200)) // כחול יפה
				.setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(150).setKR(0.9))); // חומר מראה חזק

		// --- כל שאר הגופים (קובייה, פירמידה, ספרות נוספות, מצולעים נוספים) - הוסרו ---

		// --- הוספת תאורה ---
		// נשמור על תאורה שמתאימה להדגשת השתקפויות
		scene.lights.add(new PointLight(new Color(600, 400, 0), new Point(0, -50, 70)).setKl(0.0008).setKq(0.00008)); // אור
																														// נקודתי
																														// חזק
																														// יותר
		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -1, -0.7)));
		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(40, 40, 40), new Vector(-0.5, -0.5, -0.8))
				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));

		// --- הגדרת מצלמה ורינדור ---
		// נמקם את המצלמה כך שתתמקד בספרה וברצפה כדי לראות את ההשתקפות
		cameraBuilder.setLocation(new Point(0, -120, 40)) // קרובה יותר, קצת מעל
				.setDirection(new Point(0, 0, 20), new Vector(0, 0, 1)) // כיוון מבט אל מרכז הספרה
				.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500).setMultithreading(2).setDebugPrint(1)
				.build().renderImage().writeToImage("si"); // שם קובץ חדש
	}

	@Test
	public void testBlurryGlass() {

		Vector vTo = new Vector(0, 1, 0);

		scene.setAmbientLight(new AmbientLight(new Color(150, 150, 150).reduce(2)));

		for (int i = -4; i < 6; i += 4) {
			scene.geometries.add(
					// Red sphere - closest to camera, lowest height
					new Sphere(new Point(5 * i, -8, -9), 3.0).setEmission(new Color(255, 0, 0).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),

					// Green sphere - behind polygon, middle height
					new Sphere(new Point(5 * i, 0, -3), 3.0).setEmission(new Color(0, 255, 0).reduce(4).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),

					// Blue sphere - furthest from camera, highest
					new Sphere(new Point(5 * i, 5, 3), 3.0).setEmission(new Color(0, 0, 255).reduce(2))
							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),

					// Polygon remains at Y = -5
					new Polygon(new Point(5 * i - 4, -5, -12), new Point(5 * i - 4, -5, 6), new Point(5 * i + 4, -5, 6),
							new Point(5 * i + 4, -5, -12)).setEmission(new Color(250, 235, 215).reduce(2))
							.setMaterial(new Material().setKD(0.001).setKS(0.002).setShininess(1).setKT(0.95)
									.setTAngle(i * 5 + 20)));
		}

		scene.geometries.add(
				new Plane(new Point(1, 10, 1), new Point(2, 10, 1), new Point(5, 10, 0))
						.setEmission(new Color(255, 255, 255).reduce(3))
						.setMaterial(new Material().setKD(0.2).setKS(0).setShininess(0).setKT(0d)), // המישור החדש
																									// שהכדורים ירחפו
																									// מעליו
				new Plane(new Point(0, 0, -12), new Point(1, 0, -12), new Point(0, 1, -12))
						.setEmission(new Color(100, 100, 100))
						.setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20).setKT(0d)),
				// מראה מרובעת על המישור התחתון - הועברה קדימה יותר לכיון המצלמה
				new Polygon(new Point(-8, -23, -11.9), new Point(8, -23, -11.9), new Point(8, -5, -11.9),
						new Point(-8, -5, -11.9)).setEmission(new Color(50, 50, 50))
						.setMaterial(new Material().setKD(0.1).setKS(1).setShininess(100).setKR(0.8)));

		scene.lights.add(new DirectionalLight(new Color(255, 255, 255).reduce(1), new Vector(-0.4, 1, 0)));
//		scene.lights.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(20.43303, -7.37104, 13.77329),
//				new Vector(-20.43, 7.37, -13.77)).setKl(0.6));

		// תאורת Point מהצד הימני - תיצור צללים חדים
		scene.lights.add(new PointLight(new Color(255, 200, 200), new Point(20, -5, 8)).setKl(0.05).setKq(0.01));

		// תאורת Point מהצד השמאלי - תיצור צללים מהכיוון הנגדי
		scene.lights.add(new PointLight(new Color(200, 255, 200), new Point(-20, -5, 8)).setKl(0.05).setKq(0.01));

		// תאורת ספוט מלמעלה - להדגשת המראה
		scene.lights.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(0, 15, 0), new Vector(0, -1, 0))
				.setKl(0.1).setKc(0.1));

		// תאורת Point נוספת מאחור - להאיר את החלק האחורי
		scene.lights.add(new PointLight(new Color(200, 200, 255), new Point(0, -5, -15)).setKl(0.08).setKq(0.015));

		cameraBuilder.setResolution(500, 500).setLocation(new Point(0, -50, 0)).setDirection(vTo, new Vector(0, 0, 1))
				.setVpSize(200d, 200).setVpDistance(1000).setVpDistance(100).setVpSize(150, 150).setResolution(500, 500)
				.setMultithreading(2).setDebugPrint(1).build().renderImage().writeToImage("blurryGlass2");
	}
//	@Test
//	public void testBlurryGlass() {
//
//		Vector vTo = new Vector(0, 1, 0);
//
//		scene.setAmbientLight(new AmbientLight(new Color(150, 150, 150).reduce(2)));
//
//		for (int i = -4; i < 6; i += 4) {
//			scene.geometries.add(
//					new Sphere(new Point(5 * i, 5 /*-1.50*/, -3), 3.0)
//							.setEmission(new Color(0, 255, 0).reduce(4).reduce(2))
//							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),
//
//					new Sphere(new Point(5 * i, 5, 3), 3.0).setEmission(new Color(0, 0, 255).reduce(2))
//							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),
//					new Sphere(new Point(5 * i, -8, -8), 3.0).setEmission(new Color(255, 0, 0).reduce(2))
//							.setMaterial(new Material().setKD(0.2).setKS(1).setShininess(80).setKT(0d)),
//
//					new Polygon(new Point(5 * i - 4, -5, -11), new Point(5 * i - 4, -5, 5), new Point(5 * i + 4, -5, 5),
//							new Point(5 * i + 4, -5, -11)).setEmission(new Color(250, 235, 215).reduce(2))
//							.setMaterial(new Material().setKD(0.001).setKS(0.002).setShininess(1).setKT(0.95)
//									.setTAngle(i * 5 + 20))
//
//			);
//		}
//
//		scene.geometries.add(new Plane(new Point(1, 10, 1), new Point(2, 10, 1), new Point(5, 10, 0))
//				.setEmission(new Color(255, 255, 255).reduce(3))
//				.setMaterial(new Material().setKD(0.2).setKS(0).setShininess(0).setKT(0d))
//
//		);
//
//		// scene.lights.add(new PointLight(new Color(100, 100, 150), new Point(0, 6,
//		// 0)));
//		scene.lights.add(new DirectionalLight(new Color(255, 255, 255).reduce(1), new Vector(-0.4, 1, 0)));
//		scene.lights.add(new SpotLight(new Color(255, 255, 255).reduce(2), new Point(20.43303, -7.37104, 13.77329),
//				new Vector(-20.43, 7.37, -13.77)).setKl(0.6));
//		cameraBuilder.setResolution(500, 500).setLocation(new Point(0, -50, 0)).setDirection(vTo, new Vector(0, 0, 1))
//				.setVpSize(200d, 200).setVpDistance(1000).setVpDistance(100).setVpSize(150, 150).setResolution(500, 500)
//				.setMultithreading(2).setDebugPrint(1).build().renderImage().writeToImage("blurryGlass2");
//
//	}
}
//import org.junit.jupiter.api.Test;
//
//import geometries.Plane; // ייבוא Plane
//import geometries.Polygon;
//import geometries.Sphere;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.DirectionalLight;
//import lighting.PointLight;
//import lighting.SpotLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import renderer.Camera;
//import renderer.RayTracerType;
//import scene.Scene;
//
//public class GlossySurfacesAndDiffusedBlurry {
//
//	private Scene scene = new Scene("PreciseGardenScene");
//	private Camera.Builder cameraBuilder = new Camera.Builder().setRayTracer(scene, RayTracerType.SIMPLE)
//			.setVpDistance(100).setVpSize(150, 150).setResolution(500, 500);
//
//	@Test
//	void renderGardenScene() {
//		// --- 1. הגדרות סצנה בסיסיות ---
//		// החזרת ה-AmbientLight לערך סביר יותר
//		scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30))); // חזרה לאור סביבה עדין
//
//		// קרקע (Plane אינסופי) - דשא ירוק כהה
//		scene.geometries.add(new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)) // נקודה על המישור (0,0,0) ונורמל מעלה
//																				// (0,0,1)
//				.setEmission(new Color(50, 120, 20)) // דשא ירוק כהה
//				.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));
//
//		// שמיים (Plane אינסופי) - תכלת בהיר
//		// מישור אינסופי גבוה יותר, עם נורמל כלפי מטה (0,0,-1) כדי שהצד הגלוי יהיה למטה
//		scene.geometries.add(new Plane(new Point(0, 0, 100), new Vector(0, 0, -1)) // נקודה גבוהה על המישור (0,0,100)
//																					// ונורמל למטה
//				.setEmission(new Color(178, 204, 229)) // צבע שמיים תכלת בהיר
//				.setMaterial(new Material().setKD(1.0).setKS(0.0).setShininess(1))); // חומר שלא משתקף כמעט
//
//		// --- 2. הוספת מקורות תאורה ---
//
//		// אור שמש - Directional Light
//		scene.lights.add(new DirectionalLight(new Color(150, 150, 150), new Vector(0.5, -0.7, -1)));
//
//		// פנס בתוך הביתן - PointLight
//		scene.lights
//				.add(new PointLight(new Color(200, 160, 120), new Point(-1.5, 1.5, 1.2)).setKl(0.0008).setKq(0.00008));
//
//		// תאורת ספוט מעל הבריכה - SpotLight
//		scene.lights.add(new SpotLight(new Color(250, 100, 100), new Point(0, 0, 3.0), new Vector(0, 0, -1))
//				.setKl(0.0005).setKq(0.00005).setNarrowBeam(15));
//
//		// --- 3. הוספת גופים (שינוי צבעי ה-setEmission) ---
//
//		// בריכה/מזרקה (מצולע - משושה) - מים כחולים
//		// נשמר את צורת המשושה כפי שהוגדר קודם
//		scene.geometries.add(new Polygon(new Point(1.5, 0, 0.05), // Vertex 1
//				new Point(0.75, 1.3, 0.05), // Vertex 2
//				new Point(-0.75, 1.3, 0.05), // Vertex 3
//				new Point(-1.5, 0, 0.05), // Vertex 4
//				new Point(-0.75, -1.3, 0.05), // Vertex 5
//				new Point(0.75, -1.3, 0.05) // Vertex 6
//		).setEmission(new Color(30, 80, 120)) // מים כחולים
//				.setMaterial(
//						new Material().setKD(0.2).setKS(0.5).setShininess(50).setKT(0.7).setKR(0.8).setRAngle(5.0)));
//
//		// "עצים" (3 מצולעים לדוגמה) - ירוק כהה יותר (לפני שינוי לגזע ועלווה)
//		// נשמור את המיקומים האלה כרגע, ונשנה את הגאומטריה בהמשך
//		scene.geometries.add(new Polygon(new Point(3, 5, 0), new Point(4, 5, 0), new Point(3.5, 5.5, 5))
//				.setEmission(new Color(70, 150, 30)) // ירוק
//				.setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(20)));
//
//		scene.geometries.add(new Polygon(new Point(-4, 4, 0), new Point(-3, 4, 0), new Point(-3.5, 4.5, 4))
//				.setEmission(new Color(80, 140, 40)) // ירוק שונה קלות
//				.setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(20)));
//
//		scene.geometries.add(new Polygon(new Point(2, -4, 0), new Point(3, -4, 0), new Point(2.5, -3.5, 3))
//				.setEmission(new Color(90, 130, 50)) // ירוק נוסף
//				.setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(20)));
//
//		// קירות הביתן (מצולעים מוגדרים)
//		// חזית הביתן (עם שקיפות חלבית) - הגברת שקיפות
//		scene.geometries.add(new Polygon(new Point(-2.5, 2.5, 0), new Point(-0.5, 2.5, 0), new Point(-0.5, 2.5, 2.0),
//				new Point(-2.5, 2.5, 2.0)).setEmission(new Color(225, 225, 225)) // לבן אפרפר
//				.setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(30).setKT(0.95).setTAngle(10.0))); // הגברת
//																													// KT
//																													// ל-0.95
//
//		// קירות צדדיים ואחוריים (אטומים) - חום עץ/אבן
//		scene.geometries.add(new Polygon(new Point(-2.5, 0.5, 0), new Point(-2.5, 2.5, 0), new Point(-2.5, 2.5, 2.0),
//				new Point(-2.5, 0.5, 2.0)).setEmission(new Color(150, 120, 90)) // חום עץ
//				.setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(10)));
//
//		scene.geometries.add(new Polygon(new Point(-0.5, 0.5, 0), new Point(-0.5, 2.5, 0), new Point(-0.5, 2.5, 2.0),
//				new Point(-0.5, 0.5, 2.0)).setEmission(new Color(150, 120, 90)) // חום עץ
//				.setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(10)));
//
//		scene.geometries.add(new Polygon(new Point(-2.5, 0.5, 0), new Point(-0.5, 0.5, 0), new Point(-0.5, 0.5, 2.0),
//				new Point(-2.5, 0.5, 2.0)).setEmission(new Color(150, 120, 90)) // חום עץ
//				.setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(10)));
//
//		// גג הביתן (מצולע) - חום כהה
//		scene.geometries.add(new Polygon(new Point(-2.5, 2.5, 2.0), new Point(-0.5, 2.5, 2.0),
//				new Point(-0.5, 0.5, 2.0), new Point(-2.5, 0.5, 2.0)).setEmission(new Color(80, 50, 30)) // חום כהה
//				.setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(10)));
//
//		// עץ מיניאטורי בתוך הביתן (3 משולשים) - ירוק בהיר
//		scene.geometries
//				.add(new Triangle(new Point(-1.55, 1.55, 0.5), new Point(-1.45, 1.55, 0.5), new Point(-1.5, 1.55, 1.0))
//						.setEmission(new Color(100, 180, 60)) // ירוק בהיר
//						.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));
//
//		scene.geometries
//				.add(new Triangle(new Point(-1.6, 1.5, 1.0), new Point(-1.4, 1.5, 1.0), new Point(-1.5, 1.5, 1.5))
//						.setEmission(new Color(100, 180, 60)) // ירוק בהיר
//						.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));
//
//		scene.geometries
//				.add(new Triangle(new Point(-1.65, 1.45, 1.5), new Point(-1.35, 1.45, 1.5), new Point(-1.5, 1.45, 2.0))
//						.setEmission(new Color(100, 180, 60)) // ירוק בהיר
//						.setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10)));
//
//		// עננים (3 ספרות) - לבן-תכלכל עדין (לפני ארגון מחדש)
//		scene.geometries.add(new Sphere(new Point(-5, 10, 8), 2.0).setEmission(new Color(230, 240, 250)) // לבן-תכלכל
//				.setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(10)));
//
//		scene.geometries.add(new Sphere(new Point(3, 8, 7.5), 1.5).setEmission(new Color(230, 240, 250))
//				.setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(10)));
//
//		scene.geometries.add(new Sphere(new Point(0, 12, 8.5), 2.5).setEmission(new Color(230, 240, 250))
//				.setMaterial(new Material().setKD(0.9).setKS(0.1).setShininess(10)));
//
//		// אלמנט דקורטיבי נוסף (מצולע) - סלע אפור
//		scene.geometries.add(new Polygon(new Point(-0.5, -1.8, 0), new Point(-0.3, -1.9, 0), new Point(-0.4, -1.7, 0.3))
//				.setEmission(new Color(100, 100, 100)) // אפור כהה
//				.setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10)));
//
//		// --- 4. הגדרת מצלמה ורינדור (כיול עדין) ---
//		cameraBuilder.setLocation(new Point(0.5, -12, 6)) // הגדלתי את המרחק והגובה קלות
//				.setDirection(new Point(0, 1.5, 1.0), new Vector(0, 1, 0.3)) // שינוי וקטור כיוון, מכוון קצת יותר למטה
//				.build().renderImage().writeToImage("gardenSceneTest_PlanesForGroundAndSky"); // שם קובץ חדש
//	}
//}
