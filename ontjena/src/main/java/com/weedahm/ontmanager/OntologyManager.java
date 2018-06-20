package com.weedahm.ontmanager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.rdf.model.ModelFactory;

public class OntologyManager {
	
	private final String TAG = this.getClass().getSimpleName();
	private ArrayList<String> lines;
	
	private String weedahmIRI = "http://www.semanticweb.org/weedahm/ontology";
	
	private String demonstraionIRI = "#����";
	private String symptomIRI = "#����";
	
	private String hasSymptomURI = "#hasSymptom";
	private String isSymptomOfURI = "#isSymptomOf";
	
	OntProperty hasSymptomProp, isSymptomOfProp;
	
	private OntModel weedahmOnt = null;
	
	OntologyManager(ArrayList<String> lines) {
		this.lines = lines;
	}
	
	OntModel createModel() {
		weedahmOnt = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		initModel();
		Log.d(TAG, "Model Creation Complete");
		Log.d(TAG, "Start Composing Ontology");
		makeOntology();
		Log.d(TAG,  "Ontology Composition Complete");
		return weedahmOnt;
	}
	
	private void initModel() {
		weedahmOnt.createClass(weedahmIRI+demonstraionIRI);
		weedahmOnt.createClass(weedahmIRI+symptomIRI);
		
		hasSymptomProp = weedahmOnt.createOntProperty(weedahmIRI + hasSymptomURI);
		isSymptomOfProp = weedahmOnt.createOntProperty(weedahmIRI + isSymptomOfURI);
		hasSymptomProp.addInverseOf(isSymptomOfProp);
		hasSymptomProp.setDomain(weedahmOnt.getResource(weedahmIRI + demonstraionIRI));
		hasSymptomProp.setRange(weedahmOnt.getResource(weedahmIRI + symptomIRI));
		isSymptomOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + symptomIRI));
		isSymptomOfProp.setRange(weedahmOnt.getResource(weedahmIRI + demonstraionIRI));
	}
	
	private void makeOntology() {
		String csvSplitBy = ",";
		
		for(String aline: lines) {
			String data[] = aline.split(csvSplitBy);
			oneLineToOntology(data);
		}
	}
	
	// ���� ��з� �߰�
	private void oneLineToOntology(String[] data) {
		int symptomsLength = data.length - 2;
		
		// preparing URI
		String majorDemonstrationUri = "#"+data[0];
		String minorDemonstrationUri = "#"+data[1];
		ArrayList<String> symptomsUris = new ArrayList<String>();
		for(int i = 2; i < 2+symptomsLength; i++) {
			symptomsUris.add("#"+data[i]);
		}
		
		// Create & Add Demonstrations
		OntClass majorDemonstration = weedahmOnt.createClass(weedahmIRI + majorDemonstrationUri);
		OntClass minorDemonstration = weedahmOnt.createClass(weedahmIRI + minorDemonstrationUri);
		weedahmOnt.getOntClass(weedahmIRI + demonstraionIRI).addSubClass(majorDemonstration);
		majorDemonstration.addSubClass(minorDemonstration);
		
		// Create & Add Symptoms
		for(String symptomUri: symptomsUris) {		
			OntClass symptom = weedahmOnt.createClass(weedahmIRI + symptomUri);
			weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(symptom);
			SomeValuesFromRestriction hasSomeSymptom = weedahmOnt.createSomeValuesFromRestriction(null, hasSymptomProp, symptom);
			minorDemonstration.addSubClass(hasSomeSymptom);
		}
	}
	
	void printOnt() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("D:\\Ontologies\\weedahmOnt\\weedahm.owl");
			weedahmOnt.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
