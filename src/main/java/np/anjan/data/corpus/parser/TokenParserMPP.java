package np.anjan.data.corpus.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class TokenParserMPP {
	@SuppressWarnings({ "unchecked", "null" })
	public List<String> readConfig(String configFile) {
		List<String> sentenceList = new ArrayList<String>();
		StringBuffer sentenceBuffer = new StringBuffer();
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(configFile);
			//XMLEventReader eventReader = inputFactory.createXMLEventReader(in,"UTF-8");
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// read the XML document
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					// If we have an item element, we create a new item
					if (startElement.getName().getLocalPart()
							.equalsIgnoreCase("s")) {
						sentenceBuffer = new StringBuffer();
					}

					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase("w")) {
							event = eventReader.nextEvent();
							sentenceBuffer.append(event.asCharacters().getData() + " ");
							if(ExtractTokensMPP.includePosLabel) {
								Iterator<Attribute> attributes = startElement.getAttributes();
								while (attributes.hasNext()) {
									Attribute attribute = attributes.next();
									if (attribute.getName().toString().equals("ctag")) {
										sentenceBuffer.append(attribute.getValue());
										sentenceBuffer.append("\n");
									}
								}
							}
							continue;
						}
					}
				}
				// If we reach the end of an item element, we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart().equalsIgnoreCase("s")) {
						sentenceList.add(sentenceBuffer.toString());						
						
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return sentenceList;
	}
}