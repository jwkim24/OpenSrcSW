package simpleIR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

//파일 읽어들여서 맞는 갯수 해쉬맵에 넣고 출력하려고 했습니다.
public class genSnippet {

	String fileName, query;
	
	public genSnippet(String string, String string2) throws Exception {
		// TODO Auto-generated constructor stub
		this.fileName = string;
		this.query = string2;
		search();
	}

	public void search() throws Exception {
		// TODO Auto-generated method stub
		
		File file = new File(fileName);	//파일객체 생성
		FileReader filereader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(filereader);
		String[] line = new String[5];
		int i=0;
		
		HashMap<Integer, Integer> h = new HashMap<>();
		while ((line[i] = bufReader.readLine()) != null) {
			String[] word = new String[5];
			word = line[i].split(" ");
			String[] q = new String[5];
			q=query.split(query);
			int correct=0;
			for(int k=0; k<q.length;k++) {
				for(int j=0; j<word.length;j++) {
					if(q[k].equals(word[j])) {
						correct++;
					}
				}
			}
			h.put(i, correct);
		}

		// sim를 내림차순으로 정렬
		List<Entry<Integer, Integer>> sort = new ArrayList<Entry<Integer, Integer>>(h.entrySet());
		Collections.sort(sort, new Comparator<Entry<Integer, Integer>>() {
			public int compare(Entry<Integer, Integer> obj1, Entry<Integer, Integer> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		System.out.println(line[sort.get(0).getKey()]);
	}
	
	

}
