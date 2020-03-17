package com.juego.learning.pojos;

public class PatternPOJO {

	String patternGroupId;
	PatternCoreData patternCoreData;

	public PatternPOJO() {
	}

	public PatternPOJO(String patternGroupId, PatternCoreData data) {
		this.patternGroupId = patternGroupId;
		this.patternCoreData = data;
	}

	public String getPatternGroupId() {
		return patternGroupId;
	}

	public void setPatternGroupId(String patternGroupId) {
		this.patternGroupId = patternGroupId;
	}

	public PatternCoreData getPatternData() {
		return patternCoreData;
	}

	public void setPatternData(PatternCoreData patternData) {
		this.patternCoreData = patternData;
	}

}
