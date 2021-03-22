package simpleIR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MakeKeyword {
	
	String fileName;
	
	public MakeKeyword(String string) throws IOException, Exception {
		this.fileName = string;
		makeKey();
	}

	private void makeKey() throws Exception, IOException {
		// TODO Auto-generated method stub
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		DocumentBuilder dBuilder = factory.newDocumentBuilder();
		Document doc = dBuilder.parse(fileName);

		Element root = doc.getDocumentElement();

		NodeList children = root.getChildNodes();
		
		
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) { // 해당 노드의 종류 판정(Element일 때)
				Element ele = (Element) node;
				NodeList childeren2 = ele.getChildNodes();
				Node body = childeren2.item(1);
				String bodyTxt = body.getTextContent();
				
				KeywordExtractor ke = new KeywordExtractor();
				KeywordList kl = ke.extractKeyword(bodyTxt, true);
				
				String[] tF = new String[children.getLength()];
				tF[i]="";
				
				for(int k=0; k<kl.size();k++) {
					Keyword kwrd = kl.get(k);
					tF[i] += kwrd.getString()+":"+kwrd.getCnt() + "#";
				}
				body.setTextContent(tF[i]);
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("index.xml")));

		transformer.transform(source, result);
		
		
	}
	
	

}
