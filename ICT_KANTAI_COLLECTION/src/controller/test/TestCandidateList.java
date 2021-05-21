

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class TestCandidateList {
	public static void main(String[] args) {
		List<Pair<Integer, Integer>> candidateCellList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				candidateCellList.add(new Pair<Integer, Integer>(i, j));
			}
		}
		System.out.println("size: " + candidateCellList.size());
		for (Pair<Integer, Integer> pair : candidateCellList) {
			System.out.println(pair.getKey() + "---" + pair.getValue());
		}
		System.out.println();
		candidateCellList.remove(new Pair<Integer, Integer>(2, 0));
		candidateCellList.remove(new Pair<Integer, Integer>(2, 2));
		candidateCellList.remove(new Pair<Integer, Integer>(1, 2));
		System.out.println("size: " + candidateCellList.size());
		for (Pair<Integer, Integer> pair : candidateCellList) {
			System.out.println(pair.getKey() + "---" + pair.getValue());
		}
		System.out.println();
		
	}
}
