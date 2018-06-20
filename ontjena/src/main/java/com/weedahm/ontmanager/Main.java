package com.weedahm.ontmanager;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	static final String TAG = "Main";

	public static void main(String[] args) {		

		CSVParser csvParser = new CSVParser("D:\\Ontologies\\180615_test.csv");
		ArrayList<String> parseData = new ArrayList<>();
		try {
			parseData = csvParser.parse();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		OntologyManager ontologyManager = new OntologyManager(parseData);
		ontologyManager.createModel();
		Log.d(TAG, "String writing file");
		ontologyManager.printOnt();
		Log.d(TAG, "All Complete");
	}

}
