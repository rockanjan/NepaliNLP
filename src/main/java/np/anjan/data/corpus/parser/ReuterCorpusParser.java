package np.anjan.data.corpus.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReuterCorpusParser extends DefaultHandler{	
	public InputStream xmlInputStream; 
	public File file;
	StringBuffer stringBuffer;
	List<String> sentenceList = new ArrayList<String>();
	
	String tmpString = ""; //tmpString from the element
	boolean isWordStarted = false;
	public ReuterCorpusParser(InputStream xmlInputStream) {
		this.xmlInputStream = xmlInputStream;
	}
	public ReuterCorpusParser(File file) {
		this.file = file;
	}
	
	@Override
	public void startElement(String uri, String localName, String elementName,
			Attributes attributes) throws SAXException {
		if(elementName.equalsIgnoreCase("s")) {
			stringBuffer = new StringBuffer();
		}
		if(elementName.equalsIgnoreCase("w")) {
			isWordStarted = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String elementName)
			throws SAXException {
		if(elementName.equals("s")) {
			sentenceList.add(stringBuffer.toString());
			isWordStarted = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(isWordStarted) {
			stringBuffer.append(new String(ch, start, length) + " ");
		}
	}
	
	public List<String> parse() {
		 SAXParserFactory factory = SAXParserFactory.newInstance();
	        try {
	        	SAXParser parser = factory.newSAXParser();
	        	Reader reader = new InputStreamReader(xmlInputStream);
	        	InputSource is = new InputSource(reader);
	        	//is.setEncoding("UTF-8");
	        	if(file == null) {
	        		parser.parse(is, this);
	        	} else {
	        		parser.parse(file, this);
	        	}
	        } catch (ParserConfigurationException e) {
	            System.out.println("ParserConfig error");
	            e.printStackTrace();
	        	System.exit(-1);
	        } catch (SAXException e) {
	        	System.out.println("SAXException : xml not well formed");
	        	e.printStackTrace();
	        	System.exit(-1);
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.exit(-1);
	        }
		return sentenceList;
	}
}
