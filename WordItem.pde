// Code from Visualizing Data, First Edition, Copyright 2008 Ben Fry.

class WordItem extends SimpleMapItem {
  String word;
  int frequency;
  boolean mouseOver;
  int sampleYear;
  float brightness;

  WordItem(String word, int y) {
    this.word = word;
    mouseOver = false;
    sampleYear = y;
    
  }

  void draw() {
    if (mouseOver) {
      fill(255);
      textAlign(LEFT, LEFT);
      text(word, treeX, treeY-10);
      DecimalFormat df = new DecimalFormat("#.##");
      String percentage = df.format((w*h)/(treeW*treeH)*100);
      text(percentage + "% del area", treeX+treeW/3-textWidth("Size: ")/2, treeY-10);
      String freq = "Media de consulta: "+ df.format(getAverageUsage());
      text(freq, treeX+treeW-textWidth(freq), treeY-10);
      fill(255, 0, 10,170); //Change treemap highlited color
      
    }
    else { 
      //brightness=random(20,80);
      //brightness=10;
      float RedColor=map(w,0,180,X0,X1-25);
      fill(h,w,100);  //CANVI DE COLOR GENERAL DE TREEMAP

    }
  
    noStroke();
    rect(x, y, w, h);
 
    if (w > textWidth(word) + 6  && h > textAscent() + 6) {
        if (mouseOver) fill(255);    //CANVIAR COLOR FONT CUAN EL MOUSE ESTA A SOBRE
        else fill(0);
        textAlign(CENTER, CENTER);
        text(word, x + w/2, y + h/2);
        }
        
     
     }
   
    
  double getAverageUsage() {
    WordMap m = (WordMap)dataMap.get(sampleYear);
     return (this.getSize()/m.getscanSampled());
    
  }

  
  void setMouseOver(boolean isMouseOver) {
   mouseOver = isMouseOver; 
  }
  
  boolean isMouseOver() {
         int wordX = (int) getBounds().x;
         int wordY = (int) getBounds().y;
         int wordW = (int) getBounds().w;
         int wordH = (int) getBounds().h;
         boolean isMouseOver = isMouseInside(wordX, wordY, wordW, wordH);
         mouseOver = isMouseOver;
         return mouseOver;
          
  }
  
  String getWord() {
   return word; 
  }
  
  float[] getValueRange() {
    float[] valueRange = new float[40];
    int u = 0;
    for (int i = 1970; i < 2010; i = i+5) {
        WordMap w = (WordMap) dataMap.get(i);
        if (w.containsWord(this.word)) {
           float value = (float)w.getWordItem(this.word).getAverageUsage();
           for (int k = 0; k < 5; k++) {
           valueRange[u+k] = value;
         }
        }
        
        else {
          for (int k = 0; k < 5; k++) {
           valueRange[u+k] = 0.0;
         }
          
        }
      u = u+5;
    }
    
    return valueRange;
    
    
  }
 
}
