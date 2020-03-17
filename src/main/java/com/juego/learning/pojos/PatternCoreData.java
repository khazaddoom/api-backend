package com.juego.learning.pojos;

import java.util.ArrayList;

public class PatternCoreData {

	String rowIndex;
	ArrayList<ArrayList<String>> data;

	public PatternCoreData() {
	}

	public PatternCoreData(String l, ArrayList<ArrayList<String>> data) {
		this.rowIndex = l;
		this.data = data;
	}

	public String getKey() {
		return rowIndex;
	}

	public void setKey(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public ArrayList<ArrayList<String>> getData() {
		return data;
	}

	public void setData(ArrayList<ArrayList<String>> data) {
		this.data = data;
	}

}
