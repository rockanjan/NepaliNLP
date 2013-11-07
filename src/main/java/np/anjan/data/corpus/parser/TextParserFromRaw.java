package np.anjan.data.corpus.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import np.anjan.data.corpus.Paragraph;
import np.anjan.data.corpus.Text;

public class TextParserFromRaw {
	@SuppressWarnings({ "unchecked", "null" })
	public List<Text> readConfig(String configFile) {
		List<Text> textList = new ArrayList<Text>();
		Text text = null;
		String tmpString;
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(configFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in,"UTF-8");
			// read the XML document
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					// If we have an item element, we create a new item
					if (startElement.getName().getLocalPart()
							.equalsIgnoreCase("text")) {
						text = new Text();
					}

					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart()
								.equalsIgnoreCase("body")) {
							event = eventReader.nextEvent();
							Paragraph p = new Paragraph();
							tmpString = event.asCharacters().getData();
							// System.out.println(tmpString);
							p.setRawText(tmpString);
							p.mergeMultiline();
							text.paragraphs.add(p);
							tmpString = "";
							continue;
						}
					}
				}
				// If we reach the end of an item element, we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart()
							.equalsIgnoreCase("text")) {
						textList.add(text);
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return textList;
	}
}
