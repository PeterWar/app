class Timeline {
  PFont font;
  Indicator indicator;
  GButton[] indicatorArray;

  HashMap dataMap;
  WordItem wordItem;
  float[] yearRange;
  float[] wordValues;
  float[] indicatorValueRange;
  int x;
  int y;
  int w;
  int h;

  int backColor=220;  
  int graphColor=0;
  int[] valueColor = {204, 102, 0};
  int[] indicatorColor = {0, 0, 0};
  
Timeline(app a, Indicator i, WordItem word,  HashMap data, int x, int y, int w, int h) {    
 
  indicator = i;
   //print (a+"\n");
   dataMap = data;
   wordItem = word;
   wordValues = wordItem.getValueRange();
   indicatorValueRange =indicator.getDataPoints();
   this.x = x;
   this.y = y;
   this.w = w;
   this.h = h;
   
  }
  
  
  boolean isMouseOverCloseButton() {
   
   if (isMouseInside(120, 40, 25, 25)) return true;
   return false;
   
   
  }

}
