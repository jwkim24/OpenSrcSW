package simpleIR;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class indexer {

	String fileName;	//입력받은 파일 경로 저장
	
	//생성자
	public indexer(String string) throws Exception {
		this.fileName = string;
		makeIndex();
	}

	
	@SuppressWarnings("unchecked")
	private void makeIndex() throws Exception {
		//파일 불러와 읽기
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		DocumentBuilder dBuilder = factory.newDocumentBuilder();
		Document doc = dBuilder.parse(fileName);

		Element root = doc.getDocumentElement();

		NodeList children = root.getChildNodes();
		int DocNum = children.getLength();	//총 doc의 개수 (즉, N)
		
		Integer[] freq = new Integer[DocNum+1];	
		//단어가 몇번 나왔는지 저장(tf_xy) + 마지막 배열엔 문서에 등장한 횟수(df_xy) 저장
		
		for(int k=0; k<freq.length; k++) {
			freq[k]=0;	//배열 초기화
		}
		
		//단어와 빈도수 저장할 hashMap
		HashMap<String, Integer[]> word = new HashMap<>();
		
		for (int i = 0; i < DocNum; i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) { // 해당 노드의 종류 판정(Element일 때)
				Element ele = (Element) node;
				NodeList childeren2 = ele.getChildNodes();
				Node body = childeren2.item(1);	//body 부분만 얻음
				String bodyTxt = body.getTextContent();
				
				String[] bodyWord = bodyTxt.split("#");	//#을 기준으로 분리해 배열에 저장
				for(int k=0; k<bodyWord.length;k++) {
					String temp[] = bodyWord[k].split(":");	//:을 기준으로 분리해 배열에 저장
					if(word.containsKey(temp[0])) {	//이미 key값에 있는 경우
						if(word.get(temp[0])[i]==0) {	//서로 다른 문서에 등장한 횟수
							word.get(temp[0])[DocNum]++;
						}
						//해당되는 문서 번호에 더해줌
						word.get(temp[0])[i] += Integer.parseInt(temp[1]);
						continue;
					}
					freq[i] = Integer.parseInt(temp[1]);	//빈도수 저장
					word.put(temp[0], freq.clone());	//배열 복사 후 저장
					word.get(temp[0])[DocNum]++;	//각 문서에 등장한 횟수
					freq[i]=0;	//초기화
				}
			}
		}
		//가중치를 저장할 hashmap 생성
		HashMap<String, ArrayList> w_xy = new HashMap<>();
		
		for(String key : word.keySet()) {
			ArrayList list = new ArrayList();	
			for (int i = 0; i < DocNum; i++) {
				if (word.get(key)[i] == 0) {
					// i문서에 그 단어가 없는 경우
					continue;
				} else if (word.get(key)[i] != 0) {
					// i문서에 그 단어가 있는 경우 리스트에 문서번호, 가중치 저장
					list.add(i + 1);	//문서 번호 저장
					float temp = (float)(DocNum) / word.get(key)[DocNum];
					float w = (float) (word.get(key)[i] * Math.log(temp));
					//가중치 소수점 3째자리에서 반올림
					w = (float) (Math.round(w*100))/100;
					list.add(w);	//그 단어의 가중치 저장
				}
			}
			w_xy.put(key, list);	//hashmap에 저장
		}
		
		
		//ObjectOutputStream을 이용하여 직렬화된 hashmap 객체를 파일에 저장
		FileOutputStream fileStream = new FileOutputStream("index.post");
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		objectOutputStream.writeObject(w_xy);
		
		objectOutputStream.close();
	}

}
