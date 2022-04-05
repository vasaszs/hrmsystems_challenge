package hrm.challenge.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import hrm.challenge.data.Person;

public class PersonExtractor {

	public static ConcurrentHashMap<String, Person> extractPersons(String rawXMLinBody)
			throws SAXException, IOException, ParserConfigurationException {
		InputStream rawXMLStream = new ByteArrayInputStream(rawXMLinBody.getBytes(StandardCharsets.UTF_8));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.parse(rawXMLStream);
		// System.out.print("Raw XML:" + rawXMLinBody);

		NodeList entries = doc.getElementsByTagName("person");

		String personName;
		String personAge;
		String personGender;
		int age;

		ConcurrentHashMap<String, Person> personHashMap = new ConcurrentHashMap<String, Person>();

		for (int i = 0; i < entries.getLength(); i++) {
			Element element = (Element) entries.item(i);
			personName = getValueFromPersonElement(element, "name");
			personAge = getValueFromPersonElement(element, "age");
			personGender = getValueFromPersonElement(element, "gender");

			if (personName != null && personAge != null && personGender != null) {
				try {
					age = Integer.parseInt(personAge);
					Person newPerson = new Person(personName, personGender, age);
					personHashMap.put(personName, newPerson);
				} catch (NumberFormatException e) {
					// TODO Error handling: invalid person element (age is not an integer)
				}
			}

		}

		return personHashMap;

	}

	private static String getValueFromPersonElement(Element element, String tagName) {
		String value;

		NodeList tagSpecificEntries = element.getElementsByTagName(tagName);
		if (tagSpecificEntries.getLength() == 1) {
			value = tagSpecificEntries.item(0).getTextContent();
		} else {
			value = null;
		}

		return value;
	}

}
