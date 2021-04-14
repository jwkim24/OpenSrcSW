package simpleIR;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class searcher {
	public searcher(String fileName, String string) {
		// TODO Auto-generated constructor stub
		try {
			InnerProduct(fileName,string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CalcSim2() {
		
	}

	@SuppressWarnings("rawtypes")
	private void InnerProduct(String fileName, String string) throws Exception {
		// TODO Auto-generated method stub
		String query = string;
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);

		// 키워드 추출 후 wordList에 저장
		ArrayList<String> wordList = new ArrayList<>();
		for (int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			wordList.add(kwrd.getString());
		}

		// index.post파일 불러온 후 hashMap에 저장
		File indexF = new File(fileName);
		FileInputStream fis;
		fis = new FileInputStream(indexF);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object object = ois.readObject();
		ois.close();
		HashMap hashMap = (HashMap) object;

		// index.post파일 경로를 바탕으로 collection.xml 불러오기
		File collectionF = new File(indexF.getParentFile().toString().concat("\\collection.xml"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		DocumentBuilder dBuilder = factory.newDocumentBuilder();
		Document doc = dBuilder.parse(collectionF);
		Element root = doc.getDocumentElement();
		NodeList children = root.getChildNodes();
		
		// Q⋅id 저장할 hashmap 생성
		HashMap<Integer, Float> Q_id = new HashMap<>();
		for (int i = 0; i < children.getLength(); i++) {
			for (int k = 0; k < wordList.size(); k++) {
				ArrayList list = (ArrayList) hashMap.get(wordList.get(k));
				if (list.contains(i)) {
					if (!Q_id.containsKey(i)) {
						Q_id.put(i, (Float) list.get(list.indexOf(i) + 1)); // 문서 번호가 key, 가중치가 값
					} else {
						float new_val = Q_id.get(i) + (Float) list.get(list.indexOf(i) + 1); // 저장된 가중치에 추가로 더해줌
						Q_id.put(i,(float) (Math.round(new_val*100))/100);
					}
				}else
					Q_id.put(i,(float) 0.0);
			}
		}
		
		// Q⋅id를 내림차순으로 정렬
		List<Entry<Integer, Float>> sort = new ArrayList<Entry<Integer, Float>>(Q_id.entrySet());
		Collections.sort(sort, new Comparator<Entry<Integer, Float>>() {
			public int compare(Entry<Integer, Float> obj1, Entry<Integer, Float> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		

		
		//관련있는 문서 타이틀 순서대로 출력 (3개)
		System.out.println("유사도 상위 3위까지의 문서");
		for (int i = 0; i < 3; i++) {
			Node node = children.item(sort.get(i).getKey());
			Element ele = (Element) node;
			NodeList childeren2 = ele.getChildNodes();
			Node title = childeren2.item(0);
			System.out.println((i+1)+". "+title.getTextContent().trim());
		}
		
		//문서 유사도 출력
		System.out.println("모든 문서의 유사도");
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(sort.get(i).getKey());
			Element ele = (Element) node;
			NodeList childeren2 = ele.getChildNodes();
			Node title = childeren2.item(0);
			System.out.println(title.getTextContent().trim() + "		유사도 :" + sort.get(i).getValue());
		}

	}
}
