package simpleIR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MakeCollection{

	public MakeCollection(String string) throws Exception {
		makeXml(string);
	}
	
	private void makeXml(String string) throws Exception {
		Integer idNum= 0;	// doc의 id 숫자
		String path = string;	//변환하고자 하는 html자료의 위치
		File dir = new File(path);	//파일객체 생성
		File[] fileList = dir.listFiles();	//파일객체 배열
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document docum = docBuilder.newDocument();
		
		Element docs = docum.createElement("docs");
		docum.appendChild(docs);
		
		for(File file : fileList) {
			if(file.isFile()) {
				//docs 밑에 doc
				Element doc = docum.createElement("doc");
				docs.appendChild(doc);
				
				//doc에 id 부여
				doc.setAttribute("id", idNum.toString());
				idNum++;
				//파일 읽어들이기
				FileReader filereader = new FileReader(file);
				BufferedReader bufReader = new BufferedReader(filereader);
				String line = "";
				
				while ((line = bufReader.readLine()) != null) {
					//타이틀 정보 확인
					if (line.contains("<title>")) {
						line = line.replaceAll("\\<(/?[^\\>]+)\\>", "");
						Element title = docum.createElement("title");
						title.appendChild(docum.createTextNode(line));
						doc.appendChild(title);
					}else if(line.contains("<body>")){
						//본문 정보가 시작되는 지점 확인 후 저장
						Element body = docum.createElement("body");
						while((line = bufReader.readLine()) != null){
							line = line.replaceAll("\\<(/?[^\\>]+)\\>", "");
							body.appendChild(docum.createTextNode(line));
						}
						doc.appendChild(body);	
						
					}
				}
				bufReader.close();
			}
		}
		
		//xml파일로 변환
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(docum);
		StreamResult result = new StreamResult(new FileOutputStream(new File("collection.xml")));
		
		transformer.transform(source, result);
		

	}	
			
}



