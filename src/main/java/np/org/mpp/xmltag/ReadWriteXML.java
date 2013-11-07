package np.org.mpp.xmltag;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
/**
 * Manipulate the XML string
 */
public class ReadWriteXML{
	static Document document;
	static NodeList nodeList,a;
	Node n;
	
	/**
	 * Constructor which reads a xml tagged string and puts it in a Document 
	 * and then into the nodeList.
	 *
	 */
	public ReadWriteXML(String str){
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			document = parser.parse(new InputSource(new StringReader(str)));
			nodeList = document.getElementsByTagName("token");
			
		
			
		}catch(Exception e){
			System.out.println(e.toString());
			
		}

	}
/**
 * Get the document as a string
 * @return xmlString
 */
public String getDocumentString(){
	
	/**
	 * To write the changed document to a string xmlString
	 */
	StringWriter sw=new StringWriter();
	TransformerFactory tFactory =
        TransformerFactory.newInstance();
    try{
	Transformer transformer = tFactory.newTransformer();
    DOMSource source = new DOMSource(document);
    StreamResult result = new StreamResult(sw);
    transformer.transform(source, result);
    
    }catch(Exception e){
    	System.out.println(e.toString());
    }
    String xmlString=sw.toString();
    return xmlString;
  
}
/**
 * Get the length of the node list
 * @return int length
 */	
public int getNodeListLength() {
	
		return (nodeList.getLength());
	}

/**
 * Get the text content of the xml tag
 * @param item number
 * @return text content
 */
public String getTextCont(int i) {

	if(nodeList.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("w")){
				
		String content = nodeList.item(i).getTextContent(); 
		
		return content;
		
	}//to avoid parsing other tokens
	else return "nocontent";
}
/**
 * Create a node attribute and add the morphology(.....) in the xml tag <.....>
 */
public void setMorphContent(int i,String str){
	Node n = nodeList.item(i);
	Element e = (Element)n;
	//e.appendChild(document.createAttribute("pre"));
	e.setAttribute("morph",str);
	
	
	
}
public void setPosContent(int i,String str){
	Node n = nodeList.item(i);
	Element e = (Element)n;
	//e.appendChild(document.createAttribute("pre"));
	e.setAttribute("pos",str);
	
	
	
}	
}