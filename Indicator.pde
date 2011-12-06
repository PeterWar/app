class Indicator {
  String label;
  HashMap valueMap;
  
  Indicator(String indicatorLabel) {
   valueMap = new HashMap();
   label = indicatorLabel;//WOW
  }
  
  void addDataPoint(int year, float value) {
    valueMap.put(year, value); 
    
  }
  
  float[] getDataPoints() {
   float[] dataPoints = new float[40];
   int u = 0;
   for (int i = 1970; i < 2010; i++) {
      if (valueMap.containsKey(i)) {
        dataPoints[u] = (Float) valueMap.get(i);
      }
      else {
       dataPoints[u] = 0; 
      }
      u++;
   }

    return dataPoints;   
  }
  
  String getLabel() {
    return this.label;
  }
  
  float getDataValue(int dataYear) {
      if (valueMap.containsKey(dataYear)) return (Float) valueMap.get(dataYear);
      return 0;
    
  }
  
}
