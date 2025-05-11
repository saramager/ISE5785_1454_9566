
package scene;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.LightSource;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

/**
 * LoaderXml is responsible for parsing an XML file and constructing a Scene
 * object.
 */
public class LoaderXml {

	/**
	 * Loads a Scene object from an XML file.
	 *
	 * @param filePath the path to the XML file
	 * @return a Scene object constructed from the XML file
	 * @throws Exception if there is an error during file parsing
	 */
	public static Scene loadFromXml(String filePath) throws Exception {
		// Create a Document object for parsing the XML file
		File file = new File(filePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);

		// Read the scene element
		Element sceneElement = document.getDocumentElement();

		Scene scene = new Scene(filePath);

		// Parse the background color
		String backgroundColor = sceneElement.getAttribute("background-color");
		Double3 backgroundRgb = parseDouble3(backgroundColor);
		scene.setBackground(new Color(backgroundRgb.d1(), backgroundRgb.d2(), backgroundRgb.d3()));

		// Parse the ambient light
		Element ambientLightElement = (Element) document.getElementsByTagName("ambient-light").item(0);
		String ambientLightColor = ambientLightElement.getAttribute("color");
		Double3 ambientRgb = parseDouble3(ambientLightColor);
		scene.setAmbientLight(new AmbientLight(new Color(ambientRgb.d1(), ambientRgb.d2(), ambientRgb.d3())));

		// parse the geometries
		Element geometriesElement = (Element) document.getElementsByTagName("geometries").item(0);
		NodeList geometriesNodes = geometriesElement.getChildNodes();

		Geometries geometries = new Geometries();

		for (int i = 0; i < geometriesNodes.getLength(); i++) {
			Node node = geometriesNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				switch (element.getTagName()) {
				case "sphere":
					geometries.add(parseSphere(element));
					break;
				case "triangle":
					geometries.add(parseTriangle(element));
					break;
				case "plane":
					geometries.add(parsePlane(element));
					break;
				}
			}
		}

		scene.setGeometries(geometries);

		Element lightsElement = (Element) document.getElementsByTagName("lights").item(0);
		NodeList lightsNodes = geometriesElement.getChildNodes();

		List<LightSource> lights = new LinkedList<>();

		for (int i = 0; i < lightsNodes.getLength(); i++) {
			Node node = lightsNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				switch (element.getTagName()) {
				case "directionalLight":
					lights.add(parseDirectionalLight(element));
					break;
				case "pointLight":
					lights.add(parsePointLight(element));
					break;
				case "spotLight":
					lights.add(parseSpotLight(element));
					break;
				}
			}
		}

		scene.setLights(lights);

		return scene;

	}

	/**
	 * Parses a spot light element from the XML and creates a SpotLight object.
	 *
	 * @param element the XML element representing the spot light
	 * @return a SpotLight object
	 */
	private static SpotLight parseSpotLight(Element element) {
		String direction = element.getAttribute("direction");
		Double3 directionVector = parseDouble3(direction);
		String color = element.getAttribute("intensity");
		Double3 intensity = parseDouble3(color);
		String position = element.getAttribute("position");
		Double3 positionVector = parseDouble3(position);
		String kc = element.getAttribute("kc");
		String kl = element.getAttribute("kl");
		String kq = element.getAttribute("kq");

		Point point = new Point(positionVector);
		SpotLight spotlight = new SpotLight(new Color(intensity.d1(), intensity.d2(), intensity.d3()), point,
				new Vector(directionVector));

		if (!kc.isEmpty()) {
			spotlight.setKc(Double.parseDouble(kc));
		}
		if (!kl.isEmpty()) {
			spotlight.setKl(Double.parseDouble(kl));
		}
		if (!kq.isEmpty()) {
			spotlight.setKq(Double.parseDouble(kq));
		}

		return spotlight;
	}

	/**
	 * Parses a point light element from the XML and creates a PointLight object.
	 *
	 * @param element the XML element representing the point light
	 * @return a PointLight object
	 */
	private static PointLight parsePointLight(Element element) {
		String color = element.getAttribute("intensity");
		Double3 intensity = parseDouble3(color);
		String position = element.getAttribute("position");
		Double3 positionVector = parseDouble3(position);
		String kc = element.getAttribute("kc");
		String kl = element.getAttribute("kl");
		String kq = element.getAttribute("kq");

		Point point = new Point(positionVector);
		PointLight pointLight = new PointLight(new Color(intensity.d1(), intensity.d2(), intensity.d3()), point);

		if (!kc.isEmpty()) {
			pointLight.setKc(Double.parseDouble(kc));
		}
		if (!kl.isEmpty()) {
			pointLight.setKl(Double.parseDouble(kl));
		}
		if (!kq.isEmpty()) {
			pointLight.setKq(Double.parseDouble(kq));
		}

		return pointLight;
	}

	/**
	 * Parses a directional light element from the XML and creates a
	 * DirectionalLight object.
	 *
	 * @param element the XML element representing the directional light
	 * @return a DirectionalLight object
	 */
	private static DirectionalLight parseDirectionalLight(Element element) {
		String color = element.getAttribute("intensity");
		Double3 intensity = parseDouble3(color);
		String direction = element.getAttribute("direction");
		Double3 directionVector = parseDouble3(direction);

		return new DirectionalLight(new Color(intensity.d1(), intensity.d2(), intensity.d3()),
				new Vector(directionVector));
	}

	/**
	 * Parses a sphere element from the XML and creates a Sphere object.
	 *
	 * @param element the XML element representing the sphere
	 * @return a Sphere object
	 */
	private static Sphere parseSphere(Element element) {
		// Parse the center coordinates
		String centerCoords = element.getAttribute("center");
		Point center = new Point(parseDouble3(centerCoords));

		// Parse the radius
		double radius = Double.parseDouble(element.getAttribute("radius"));

		// Create and return the Sphere object
		Sphere sphere = new Sphere(center, radius);
		praseGeomtryColor(element, sphere);
		return sphere;

	}

	/**
	 * Parses a triangle element from the XML and creates a Triangle object.
	 *
	 * @param element the XML element representing the triangle
	 * @return a Triangle object
	 */
	private static Triangle parseTriangle(Element element) {
		// Parse the coordinates of the three points
		String p1Coords = element.getAttribute("p0");
		String p2Coords = element.getAttribute("p1");
		String p3Coords = element.getAttribute("p2");
		Point p1 = new Point(parseDouble3(p1Coords));
		Point p2 = new Point(parseDouble3(p2Coords));
		Point p3 = new Point(parseDouble3(p3Coords));

		// Create and return the Triangle object
		Triangle triangle = new Triangle(p1, p2, p3);
		praseGeomtryColor(element, triangle);
		return triangle;
	}

	/**
	 * Parses a plane element from the XML and creates a Plane object.
	 *
	 * @param element the XML element representing the plane
	 * @return a Plane object
	 */
	private static Intersectable parsePlane(Element element) {
		// Option 1: Parse using three points
		if (element.hasAttribute("p0") && element.hasAttribute("p1") && element.hasAttribute("p2")) {
			Point p0 = new Point(parseDouble3(element.getAttribute("p0")));
			Point p1 = new Point(parseDouble3(element.getAttribute("p1")));
			Point p2 = new Point(parseDouble3(element.getAttribute("p2")));
			Plane plane = new Plane(p0, p1, p2);
			praseGeomtryColor(element, plane);
			return plane;
		}

		// Option 2: Parse using a point and a normal vector
		if (element.hasAttribute("p") && element.hasAttribute("normal")) {
			Point p0 = new Point(parseDouble3(element.getAttribute("p")));
			Vector normal = new Vector(parseDouble3(element.getAttribute("normal")));
			Plane plane = new Plane(p0, normal);
			praseGeomtryColor(element, plane);
			return plane;
		}

		// If neither format is valid, throw an exception
		throw new IllegalArgumentException(
				"Invalid plane definition: must provide either (p0, p1, p2) or (p0, normal)");
	}

	/**
	 * Parses the geometry color attributes from the XML element and sets them in
	 * the Geometry object.
	 *
	 * @param element  the XML element representing the geometry
	 * @param geometry the Geometry object to set the color attributes for
	 */
	public static void praseGeomtryColor(Element element, Geometry geometry) {
		String emissionColor = element.getAttribute("emission");
		if (!emissionColor.isEmpty()) {
			Double3 emission = parseDouble3(emissionColor);
			geometry.setEmission(new Color(emission.d1(), emission.d2(), emission.d3()));
		}
		String material = element.getAttribute("material");
		if (!material.isEmpty()) {
			String KA = element.getAttribute("kA");
			if (!KA.isEmpty()) {
				Double3 kA = parseDouble3(KA);
				geometry.setMaterial(geometry.getMaterial().setKA(kA));
			}
			String KD = element.getAttribute("kD");
			if (!KD.isEmpty()) {
				Double3 kD = parseDouble3(KD);
				geometry.setMaterial(geometry.getMaterial().setKD(kD));
			}
			String KS = element.getAttribute("kS");
			if (!KS.isEmpty()) {
				Double3 kS = parseDouble3(KS);
				geometry.setMaterial(geometry.getMaterial().setKS(kS));
			}
			String shininess = element.getAttribute("shininess");
			if (!shininess.isEmpty()) {
				int shininessValue = Integer.parseInt(shininess);
				geometry.setMaterial(geometry.getMaterial().setShininess(shininessValue));
			}

		}
	}

	/**
	 * Parses a color string in the format "rgb(r,g,b)" and returns a Double3
	 * representing the color.
	 *
	 * @param str the color string
	 * @return a Double3 object representing the color
	 */
	private static Double3 parseDouble3(String str) {
		Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?");
		Matcher m = p.matcher(str);
		double[] numbers = new double[3];
		int i = 0;
		while (m.find() && i < 3) {
			numbers[i++] = Double.parseDouble(m.group());
		}
		return new Double3(numbers[0], numbers[1], numbers[2]);
	}

}
