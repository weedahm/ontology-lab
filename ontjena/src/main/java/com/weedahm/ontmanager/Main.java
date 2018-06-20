package com.weedahm.ontmanager;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	static final String TAG = "Main";

	public static void main(String[] args) {
		CSVParser csvParser = new CSVParser("C:\\Users\\������\\Desktop\\Excel\\Data Source\\180615_test.csv");
		ArrayList<String> parseData = new ArrayList<String>();
		try {
			parseData = csvParser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		OntologyManager ontologyManager = new OntologyManager(parseData);
		ontologyManager.createModel();
		Log.d(TAG, "String writing file");
		ontologyManager.printOnt();
		Log.d(TAG, "All Complete");
	}

}
