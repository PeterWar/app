import processing.core.*; 
import processing.xml.*; 

import guicomponents.*; 
import treemap.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class app extends PApplet {




graphicClass graphic;        //Initializing drawing class to draw graphic
mathClass compute;          //Initializing math class to compute MEAN,SD and Correlations
POPUPClass popup;
//writerClass write;        //Writer to output to a file an interesting correlation, NOT USED


int WORDCOLUMN=5;    //MANTAINED FOR LEGACY PURPOSES (ALWAYS WORDCOLUMN=5)
PImage Image1;
int[] seleccio={1,2,3,4,5,6,7,8,9};        //TO BE DISCONTINUED
String[] popuptime = new String[] { "Q1 10", "Q2 10", "Q3 10", "Q4 10", "Q1 11", "Q2 11", "Q3 11", "Q4 11","Actual"};  //NEW TIME RANGE FOR POPUP
String[] indicatorNames = {"", "", "", ""};  // 4 INDICATORS OF POPUP CLASS
int[] COLOR={0,140,0};
int[] COLORDATA={80,80,200};

//----------------POPUP COLOR SCHEME------------------------

int sColor=30;              //POPUP Fill color of scatterplot
int backColor=220;          // Background Color
int graphbackColor=250;    //POPUP Graph Background Color
//-----------------------------------------------------------


//---------------POPUP CONFIG DATA----------------------------

int select=1;             //POPUP Screen selector changed from top buttons
int drawselect=1;        //POPUP Select wich variables to draw
int ST=10;
int w=950;              //POPUP Total Width of sketch
int h=600;             //POPUP Total Height of sketch

float m;
float M1,SD1,V1,r,t;      //GLOBAL VARIABLES FOR MATH

float a=0;  //acceleration parameter for animation movement
float R=1;  //Aspect Ratio of top height of graph, used for animation
int move=2; //flag that indicates start(1)/stop(0)/static(2) of transition animation

//Aspect Atributes
float X0=180;      
float X1=630;       
float Y0=340;      
float Y1=140;

//---------------------------------------------------------------


// Data map (year -> WordMap)
HashMap dataMap;

// Current state of system: treemap, popup
String state;

// Indicator list
HashMap indicators;
Indicator lastIndicator;

// Empty wording 
String emptyWord = "(No se encuentran marcas)";

// TreeMap attributes
Treemap map;
WordMap currentWordMap;
WordItem chosenWordItem;
int currentMapTime;
int treeX = 100;
int treeY = 30;
int treeW = 590;
int treeH = 400;


Timeline timeline;
int deltaX = -10;
int deltaY = -20;
int xButtonDelta = 0;
int yButtonDelta = 0;

// Text attributes
PFont font;
PFont mapFont;
PFont popupFont;
PFont popup2Font;
PFont popupTitleFont;
PFont popupSmallFont;

// Inputfield
GTextField txfML1;
String textBoxContent;
int textX = 720;
int textY = 30;
int textW = 200;
int textH = 300;

// Updatebutton
GButton btnUpdate;
int buttonUpdateX = 650;
int buttonUpdateY = 180;
int buttonUpdateW = 100;
int buttonUpdateH = 50;

// Suggested word array
int arrayX = 722;
int arrayY = 340;
int arrayW = 95;
int arrayH = 25;
int spacing = 2;

// Suggested words buttons
GButton[][] suggestedArray; // 8 buttons
String[] suggestedWords = {"Vista general,danone,hacendado,abc,avia,aquilea",
                          "Dietetica,hacendado,almendrina,alven,monsoy,candy,montosa,mela,millac,montesano,osadas,laselva",
                          "Infantil,palmil,promavel,montesano,libbys,calvo,eroski,aprolis",
                          "Fitness,hacendado,danone,avia,mela",
                          "Celiacos,provamel,libbys,laselva,aviSerra,mela",
                          "Gourmet,mela,millac,montesano,alven",
                          "Marca Propia,alven,mela,hacendado,mrolli,milord,abc",
                          "Gluten Free,almendrina,aprolis,plamil,monsoy,avia"};
                          
// Slider
GWSlider sdr1;

public void setup() {
  
  // Setting up the environment
  size(950, 600);          //Defining size of popup screen
  background(color(0)); 
  frameRate(30);
  smooth();
  // Load data into WordMaps
  dataMap = new HashMap();
  for (int i = 1; i < 9; i = i+1) {
    float range = map (i,1,9,1970,2010);
    int parse=floor(range);
    
    WordMap u = new WordMap(parse);  
     
    //String[] lines = loadStrings("ExcelTSV"+i+".txt");      //LOAD DE FITXERS!!!
   
    String[] lines = loadStrings("ExcelTSV" + i +".txt");
    //print("ExcelTSV" + i +".txt"+"\n");
    String lineone[] = lines[0].split("\\W");
    
 //scanSampled DISCONTINUED (always 1) as we don't have a sample size N, every foodLinkerobservation is our entire universe)
    int scanSampled = Integer.parseInt(lineone[0]);  
    u.setscanSampled(scanSampled);
 //------------------------------
 
    for (int k = 1; k < lines.length; k++) {
      String[] line = lines[k].split("\\t");
      
      u.inputWord(line[0], Integer.parseInt(line[1]));              //NOM I DIMENSIO DE WORD PER TREEMAP!
      //print(line[0]+TAB+line[1]+"\n");
    }
    u.finishAdd();
    //print(parse+"\n");
    dataMap.put(parse, u);
    
  }

  // Load data into indicator list (INDICADORS POPUP)
  indicators = new HashMap();
  String[] indicatorLines = loadStrings("indicators.csv");
  String[] columns = split(indicatorLines[0],',');
  
  for (int k = 1; k < indicatorLines.length; k++) {
    //print(indicatorLines.length);
    String values[] = split(indicatorLines[k], ',');
    indicatorNames[k-1]=values[0];                        //MODIFIES GLOBAL STRING TO ALLOW TO CHANGE INDICATORS ON EXCEL DIRECTLY
    Indicator i = new Indicator(values[0]);
    
    for (int u = 1; u < values.length; u++) {
      int dataYear = Integer.parseInt(columns[u]);
      float f = new Float(values[u]);
      i.addDataPoint(dataYear, f);
    }  
    indicators.put(values[0], i);
    // Sets the default indicator
    if (k == 1) lastIndicator = i;
  }
  graphic=new graphicClass();
  compute=new mathClass();
  popup=new POPUPClass();
  
  background(backColor);
 

  
  // Variables initiated
  textBoxContent = "Introducir marcas";
   currentMapTime = 1985;

  // System wide fonts
  font = createFont("Helvetica", 24);
  mapFont = createFont("Serif", 13);
  popupFont = createFont("Arial", 20); //Creating a FONT
  popup2Font = createFont("Arial", 11); //Creating a FONT
  popupTitleFont = createFont("Arial", 10); //Creating a FONT
  popupSmallFont = createFont("Arial", 9); //Creating a FONT
  
  // Set default indicator
  // different choices for the layout method
  //MapLayout algorithm = new SliceLayout();
  //MapLayout algorithm = new StripTreemap();
  //MapLayout algorithm = new PivotBySplitSize();
  //MapLayout algorithm = new SquarifiedLayout();
  
  setupSlider();
  
  // Setting up Text Box component
  GComponent.globalColor = GCScheme.getColor(this,  GCScheme.GREEN_SCHEME);
  GComponent.globalFont = GFont.getFont(this, "Arial", 13);
  txfML1 = new GTextField(this, textBoxContent, textX, textY, textW, textH, true);
  txfML1.tag = "Words input";
  
  /*
  // Setting up update button
  btnUpdate = new GButton(this, "UPDATE BBDD >", buttonUpdateX,buttonUpdateY,buttonUpdateW,buttonUpdateH);
  btnUpdate.setBorder(5);
  btnUpdate.fireAllEvents(true);
  */
  
  // Setting up suggested words buttons
  suggestedArray = new GButton[2][4];
  int counter = 0;
  for (int dx = 0; dx < suggestedArray.length; dx++){
    for (int dy = 0; dy < suggestedArray[0].length; dy++){
       GButton u = new GButton(this, suggestedWords[counter].split(",")[0], arrayX+dx*(arrayW+spacing), arrayY+dy*(arrayH+spacing),arrayW,arrayH);
       u.setBorder(0);
       suggestedArray[dx][dy] = u;
       u.fireAllEvents(false);
       counter++;
  }
  }
  
  
  // Setting the stage
  state = "treemap";
  updateTreemap();
  mouseMoved();
}

public void draw() {
  background(color(155)); 
  //drawLegend(25, 300);

  Image1= loadImage("foodlinkerlogo.png");
  image(Image1,10,530);
  
  if (state == "treemap") {
    drawMap();
  }
  
   if (state == "popup") {
     
    drawMap();
    
    fill(backColor);
    rect(X0-72,35,570,385);            //DRAWS HEADER, CAPSALERA OF POPUP
    
    graphic.cleargraph();
    stroke(COLORDATA[0],COLORDATA[1],COLORDATA[2]);
    popup.getValue(WORDCOLUMN);
    fill(255);
    graphic.drawvalues(WORDCOLUMN);    //Draws the value type into the graph
  
    stroke(COLOR[0],COLOR[1],COLOR[2]);
    noFill();
    graphic.drawvalues(drawselect);

    graphic.movegraphdown();
    graphic.movegraphup();
    
   
    stroke(COLORDATA[0],COLORDATA[1],COLORDATA[2]);
    popup.getValue(WORDCOLUMN);
    popup.drawscatterplot();  //Draws Scatterplot
  }

}


public void drawMap() {
  strokeWeight(0.25f);
  textFont(mapFont);
  map.draw();  
}


//------------------------------------------GUI components and event fields------------------//

// Add an event to the events textfield
public void handleSliderEvents(GSlider slider) {
  if (currentMapTime != getSliderTime()) {            //COMPARA TIPUS DE MAPA AMB TEMPS SELECCIONAT
  currentMapTime = getSliderTime();
    updateTreemap();
  }
}

public void handleTextFieldEvents(GTextField tfield){
  if (!textBoxContent.equals(txfML1.getText())) {
    textBoxContent = txfML1.getText();
    updateTreemap();
  }
  txfML1.draw();
}

public void handleButtonEvents(GButton button) {
int counter = 0;
for (int dx = 0; dx < suggestedArray.length; dx++){
    for (int dy = 0; dy < suggestedArray[0].length; dy++){
     if(button == suggestedArray[dx][dy] && button.getEventType() == GButton.CLICKED) {
       txfML1.setText(suggestedWords[counter].split(",",2)[1].replace(",", "\n"));
         
      
     }
     counter++;
  }
  }
 
}	

public void mouseMoved() {
 // Check if it's over treemap
     // Highlight a WordItem
     if (state == "treemap") {
       WordItem[] wordItems = (WordItem[]) currentWordMap.getItems();
       for (int i = 0; i < wordItems.length; i++) {
           wordItems[i].isMouseOver();
       }
     }
}

public void mouseClicked() {
    WordItem[] wordItems = (WordItem[]) currentWordMap.getItems();
     for (int i = 0; i < wordItems.length; i++) {
       WordItem m = wordItems[i];
       if (state == "popup" && timeline.isMouseOverCloseButton()) {
          chosenWordItem = null;
          //timeline.removeButtons();
          timeline = null;
          state = "treemap";
          updateTreemap();
      }
      else {
      if (state == "treemap" && m.isMouseOver() && !m.getWord().equals(emptyWord)) {
         
          chosenWordItem = m;
          timeline = new Timeline(this,lastIndicator, m, dataMap, treeX-deltaX, treeY-deltaY, treeW+2*deltaX, treeH+2*deltaY); 
     
          compute.all();   //COMPUTES MEAN, SD, CORRELATION AND REGRESSION, STORED ON GLOBAL VARIABLES
            
          state = "popup";
       }
      }
    }
}

  
public boolean isMouseInside(int x, int y, int w, int h) {
  if (mouseX > x && mouseX < x+w) {
     if (mouseY > y && mouseY < y+h) {
       return true;
       }
     }
     return false;
  }
  

//------------------------------------------Calculating functions----------------------------------------

public void updateTreemap() {
 currentWordMap = createWordMap(textBoxContent, (WordMap)dataMap.get(currentMapTime));
 map = new Treemap(currentWordMap, treeX, treeY, treeW , treeH );
}

public WordMap createWordMap(String words, WordMap wordMap) {
  WordMap returnMap = new WordMap(currentMapTime);
  words = words.toLowerCase();                                      //FUNCIONS DE PARSEJAT!!!!!!!!!!!!!
  words = words.replaceAll(" ", "");
  String[] wordArray = words.split("\\n");
  returnMap.setscanSampled(wordMap.getscanSampled());
  for (int i = 0 ; i < wordArray.length; i++) {
    String word = wordArray[i];
                                                                        //AQUESTES SON LES PARAULES!!!!!!!!
    if (wordMap.containsWord(word)) {
      returnMap.putWord(word, wordMap.getWordItem(word));
    }
  }
  if (returnMap.isEmpty()) {
    returnMap.addWord(emptyWord); 
    returnMap.setscanSampled(0);
  }
  
  returnMap.finishAdd();
  
  return returnMap;
  
}

public int  getSliderTime() {
 return sdr1.getValue()*5 + 1970; 
}

public void mousePressed(){
 if (state=="popup"){
 if (mouseX>X0&&mouseX<X0+(X1-X0)/4&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=1;}
 else if (mouseX>X0+(X1-X0)/4&&mouseX<X0+(X1-X0)/2&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=2;;}
 else if (mouseX>X0+(X1-X0)/2&&mouseX<X1-(X1-X0)/4&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=3;}
 else if (mouseX>X1-(X1-X0)/4&&mouseX<X1&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=4;}
 }
}

public void setupSlider(){
  
  // Setting up slider
  String[] sdr1TickLabels = new String[] { "Q1-Q2", "Q2-Q3", "Q3-Q4", "Q4-Q1", "Q1-Q2", "Q2-Q3", "Q3-Q4", "Q4-Q1"};
  sdr1 = new GWSlider(this, treeX,treeY+treeH+30,treeW);
  sdr1.setTickLabels(sdr1TickLabels);
  sdr1.setLimits(0, 0, 7);
  sdr1.setRenderValueLabel(false); 
  sdr1.setStickToTicks(true);
  sdr1.setValue(2.345f);
  sdr1.setFontColour(55, 15, 255);        //COLOR TEXT SLIDER
}
class Indicator {
  String label;
  HashMap valueMap;
  
  Indicator(String indicatorLabel) {
   valueMap = new HashMap();
   label = indicatorLabel;
  }
  
  public void addDataPoint(int year, float value) {
    valueMap.put(year, value); 
    
  }
  
  public float[] getDataPoints() {
   float[] dataPoints = new float[40];
   int u = 0;
   for (int i = 1970; i < 2010; i++) {
      if (valueMap.containsKey(i)) {
        dataPoints[u] = (Float) valueMap.get(i);
      }
      else {
       dataPoints[u] = 0; 
      }
      u++;
   }

    return dataPoints;   
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public float getDataValue(int dataYear) {
      if (valueMap.containsKey(dataYear)) return (Float) valueMap.get(dataYear);
      return 0;
    
  }
  
}
class POPUPClass{
int[] COLOR={0,140,0};
int[] COLORDATA={80,80,200};
valueTable data;              //Creating data table from file to be used by POPUP
PFont font; //VARIABLE FONT

public void drawscatterplot(){

  textFont(popupTitleFont);
 
  data= new valueTable();
  String buffer[]=data.get_table_lines();
  String[] col = split(buffer[0],',');                    //PRINTING THE TITLES OF REAL WORLD VALUES OF POPUP 
  if (select == 1){
  fill(COLOR[0],COLOR[1],COLOR[2]);
  text(col[1],X0+55,Y1-h/20);
  //textFont(popupSmallFont);
  //text("Casos por 100,000",X0+55,Y1-h/30);
  textFont(popupTitleFont);
  fill(sColor);
  text(col[2],X0+50+(X1-X0)/4,Y1-h/30);
  text(col[3],X0+45+(X1-X0)/2,Y1-h/30);
  text(col[4],X1+40-(X1-X0)/4,Y1-h/30);
  }
    if (select == 2){
  fill(sColor);
  text(col[1],X0+55,Y1-h/30);
  text(col[3],X0+50+(X1-X0)/2,Y1-h/30);
  text(col[4],X1+40-(X1-X0)/4,Y1-h/30); 
  fill(COLOR[0],COLOR[1],COLOR[2]);
  //textFont(popupSmallFont);
  //text("US$ Inflation adjusted",X0+50+(X1-X0)/4,Y1-h/30);
  textFont(popupTitleFont);
  text(col[2],X0+50+(X1-X0)/4,Y1-h/20);

  }
    if (select == 3){
  fill(COLOR[0],COLOR[1],COLOR[2]);
  text(col[3],X0+50+(X1-X0)/2,Y1-h/20);
  textFont(popupSmallFont);
  //text("per 1000 women",X0+50+(X1-X0)/2,Y1-h/30);
  //text("aged 15-19",X0+50+(X1-X0)/2,Y1-h/50);
  textFont(popupTitleFont);
 
  fill(sColor);
  text(col[1],X0+55,Y1-h/30);
  text(col[2],X0+50+(X1-X0)/4,Y1-h/30);
  text(col[4],X1+40-(X1-X0)/4,Y1-h/30);
  }
    if (select == 4){
  fill(COLOR[0],COLOR[1],COLOR[2]);
  text(col[4],X1+40-(X1-X0)/4,Y1-h/20);
  //textFont(popupSmallFont);
  //text("people 15-49 seleccio (%)",X1+50-(X1-X0)/4,Y1-h/30);
  textFont(popupTitleFont);
  fill(sColor);
  text(col[2],X0+50+(X1-X0)/4,Y1-h/30);
  text(col[3],X0+50+(X1-X0)/2,Y1-h/30);
  text(col[1],X0+55,Y1-h/30);

  }
   fill(graphbackColor);
    
  fill(sColor);
  text("Media ",190,50); 
  text(M1,190,65);
  textFont(mapFont);
  text("\u03c3",240,50);
  textFont(popupTitleFont);
  text(SD1,240,65);
  text("Varianza",290,50);
  text(V1,290,65);
  
  //text(col[drawselect],580,55);
  if (r>0.7f||r<-0.7f){
    fill(205,30,30);
    text("Correlaci\u00f3n",355,50);
    textFont(mapFont);
    text(r,355,65);
    textFont(popupTitleFont);
  }
  else{
    text("Correlaci\u00f3n",355,50);
    text(r,355,65);
  }
  
  float rangeSample[]=findMaxMin(buffer,0);     
  text("Regresion (valor t)",438,50);
  text(t,433,65);
  textFont(popupSmallFont);
  int dfreedom=floor(rangeSample[1]-rangeSample[0]-1);
  text("df:"+dfreedom,465,65);
  textFont(popupTitleFont);
  
  
  
  fill(sColor);                                                    //DRAWING TIME RANGE ON POPUP X AXIS
  for (int i=0;i<popuptime.length;i++){       
    float Xaxis=map(seleccio[i],seleccio[0],seleccio[8],X0,X1-25);
    text(popuptime[i],Xaxis+10,Y0+30);
    if(i==popuptime.length-1){
    }
    
  } 
}

public float[] findMaxMin(String[] buffer,int type){
  
  float maxvalue=MIN_FLOAT;        //max value of our data type
  float minvalue=MAX_FLOAT;        //minimum value of our data type
  
  for(int i=1;i<buffer.length;i++){ 
    String[] col = split(buffer[i],',');
    float f0=PApplet.parseFloat(col[type]);
    if (f0>maxvalue){
      maxvalue=f0;
    }
    if (f0<minvalue){
      minvalue=f0;
    }
  }

  float[] ret = {minvalue, maxvalue};
  return(ret);
}

public void getValue(int type){        //SHOW WICH WORD ARE WE VISUALIZING

  data= new valueTable();
  String buffer[]=data.get_table_lines();
  
  
  textFont(popupFont);
  if (type>=5){
    fill(108,35,570,385);
    
    
    rect(X0-15,40,X0+300,40);
    
    String[] colName = split(buffer[0],',');
    if(backColor>91){
    fill(backColor,230);
    }
    else{
    fill(backColor,230);
    
    }
    rect(X0-15,40,X0+300,40);
    fill(COLORDATA[0],COLORDATA[1],COLORDATA[2]);
    text(colName[type],570,55);
    textFont(popupSmallFont);
    text("Estadistica de consulta",575,72);
    textFont(popupTitleFont);
    
    //fill(255, 0, 0);
    rect(X0-59, 41, 22, 22);              //DRAWING CLOSE BUTTON
    strokeWeight(2);
    stroke(255);
    line(X0-43,47,X0-53,57);
    line(X0-43,57,X0-53,47);
    strokeWeight(1);
    stroke(COLORDATA[0],COLORDATA[1],COLORDATA[2]);
    
  }

}
}


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
  
  
  public boolean isMouseOverCloseButton() {
   
   if (isMouseInside(120, 40, 25, 25)) return true;
   return false;
   
   
  }

}
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

  public void draw() {
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
   
    
  public double getAverageUsage() {
    WordMap m = (WordMap)dataMap.get(sampleYear);
     return (this.getSize()/m.getscanSampled());
    
  }

  
  public void setMouseOver(boolean isMouseOver) {
   mouseOver = isMouseOver; 
  }
  
  public boolean isMouseOver() {
         int wordX = (int) getBounds().x;
         int wordY = (int) getBounds().y;
         int wordW = (int) getBounds().w;
         int wordH = (int) getBounds().h;
         boolean isMouseOver = isMouseInside(wordX, wordY, wordW, wordH);
         mouseOver = isMouseOver;
         return mouseOver;
          
  }
  
  public String getWord() {
   return word; 
  }
  
  public float[] getValueRange() {
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
           valueRange[u+k] = 0.0f;
         }
          
        }
      u = u+5;
    }
    
    return valueRange;
    
    
  }
 
}
// Code from Visualizing Data, First Edition, Copyright 2008 Ben Fry.
class WordMap extends SimpleMapModel {    
  HashMap words;
  int scanSampled;
  int sampleYear;
    
  WordMap(int year) {
    words = new HashMap();
    sampleYear = year;
  }
  
  public void addWord(String word) {
    WordItem item = (WordItem) words.get(word);
    if (item == null) {
      item = new WordItem(word, sampleYear);
      words.put(word, item);
    }
    item.incrementSize();
  }
  
  public void setscanSampled(int s) {
   scanSampled = s; 
  }
  
  public int getscanSampled() {
   return this.scanSampled; 
  }
    
  public void finishAdd() {
    items = new WordItem[words.size()];
    words.values().toArray(items);
  }
  
  public void putWord (String word, WordItem wordItem) {
   words.put(word, wordItem); 
    
  }
  
  public void inputWord(String word, int frequency) {
   WordItem w = new WordItem(word, sampleYear);
   w.setSize(frequency);
   this.putWord(word, w);
  }
  
  public WordItem getWordItem(String word) {
    return (WordItem) words.get(word);
  }
  
  public boolean containsWord(String word) {
   return words.containsKey(word);
    
  }
  
  public boolean isEmpty() {
   return words.isEmpty();
  }
  
  

}
 class graphicClass{          //THIS CLASS DRAWS THE GRAPHIC FOR POPUP SKETCH
 PFont font; //VARIABLE FONT
 valueTable data;              //Creating data table from file to be used by graphic class
 public void drawvalues(int type){
 
  smooth();
  
  data= new valueTable();
  String buffer[]=data.get_table_lines();
  
  float rangeData[]=findMaxMin(buffer,type);    //Returns minvalue(0) and maxvalue(1) of Year
  float rangeYear[]=findMaxMin(buffer,0);    //Returns minvalue(0) and maxvalue(1) of Data


  textFont(popup2Font);
  
  beginShape();      //Begins Shape of polygon
  vertex(X0,Y0);
  for(int i=1;i<buffer.length;i++){            
     String[] col = split(buffer[i],',');
     
     float RositaSalamo0=PApplet.parseFloat(col[0]);
     float m0= map(RositaSalamo0,rangeYear[0],rangeYear[1],X0+8,X1-8);    //Mapping Year range into graph
     
     float RositaSalamo1=PApplet.parseFloat(col[type]);
     float m1= map(RositaSalamo1,rangeData[0],rangeData[1],Y0-1,Y1*R+10);    //Mapping Data range into graph
     
     vertex(m0,m1); 
     
  }
   //curveVertex(X0,Y0);
  
  vertex(X1-1,Y0);
  vertex(X1-1,Y0);
  endShape(CLOSE);
  
  fill(backColor);
  noStroke();
  beginShape();                 //CLEARING LEFT PART OF GRAPH (Y AXIS)
  vertex(X0-70,Y0+h/14);   
  vertex(X0,Y0+h/14);
  vertex(X0,Y1-h/14);
  vertex(X0-70,Y1-h/14);
  endShape(CLOSE);
   

  DecimalFormat df = new DecimalFormat("#.##");
  if (type<5){      //IF VALUE IS AN INDICATOR               
   //DRAWING VALUES ON Y AXIS (LEFT)
   fill(COLOR[0],COLOR[1],COLOR[2]);
   float RD0=(rangeData[0]);
   float RD1=(rangeData[1]);
  
   String percentage = df.format(RD0);
   String percentage2 = df.format(RD1); 
   String percentage3 = df.format(RD0+(RD1-RD0)/2); 
   
   text(percentage,X0-23,Y0);  
   text(percentage2,X0-23,Y1);  
   text(percentage3,X0-23,Y0+(Y1-Y0)/2);
   
     clearJunk();  //CLEARS BORDERS OF GRAPH AREA FOR STETIC PURPOSES
   }
   else{               //IF VALUE IS A WORD
    fill(COLORDATA[0],COLORDATA[1],COLORDATA[2]);
   
    float RD0=(rangeData[0]);
    float RD1=(rangeData[1]);
    
    String percentage4 = df.format(RD0);
    String percentage5 = df.format(RD1);
    String percentage6 = df.format(RD0+(RD1-RD0)/2);
  
    
    text(percentage4,X1+15,Y0);   
    text(percentage5,X1+15,Y1);  
  
    text(percentage6,X1+15,Y0+(Y1-Y0)/2);
    clearJunk();
   }
}


public void movegraphdown(){
if (select!=drawselect){
   if (move==1){
      R=R+a;
      a=a+0.0098f;
      fill(25);
      rect(0,height,-X0,Y0);
      if ((Y1*R)>=Y0){      //Implement animation effect
        move=0;
        a=0;
        clearJunk();      //just in case...
        drawselect=select;
        
         M1=compute.mean(WORDCOLUMN);                          //THIS IS A HEAVY COMPUTING OPERATION                        
         SD1=compute.SD(WORDCOLUMN);                       //THIS IS A HEAVY COMPUTING OPERATION                                
         V1=pow(SD1,2);                                                 
         r=compute.correlate(WORDCOLUMN,drawselect);   //THIS IS A HEAVY COMPUTING OPERATION AND SHOULD NEVER BE DONE IN LOOP    
         t=compute.tvalue(WORDCOLUMN,drawselect);      //THIS OPERATION IS DEPENDENT ON RESULTS OF r and SD1         
     }
   }
}
}


public void movegraphup(){

   if (move==0){
      R=R-a;
      a=a+0.0098f;
      fill(25);
      rect(0,height,-X0,Y0);
       if (Y1*R<=Y1){      //Implement animation effect
        move=2;
        R=1;
        a=0;
        clearJunk();      //just in case...
     }
   }
}
public void cleargraph(){  //CLEARS GRAPH CONTENTS AND REDRAWS INFO  
  
  //CREATING EMPTY RECTANGLE THAT OVERWRITES ENTIRE GRAPH
  fill(graphbackColor);
  noStroke();
  beginShape();
  vertex(X0+ST,Y0);
  vertex(X1-ST,Y0);
  vertex(X1-ST,Y1-height/13);  //added -height/10 to clear the "jump" when falling
  vertex(X0+ST,Y1-height/13);
  endShape();
  
  //CREATING RECTANGLE THAT OVERWRITES TOP PARAMETERS AREA
  beginShape();
  fill(backColor);
  vertex(X0+ST,Y1);
  vertex(X1-ST,Y1);
  vertex(X1-ST,Y1-height/13);  //added -height/10 to clear the "jump" when falling
  vertex(X0+ST,Y1-height/13);
  endShape();
  
  
  //CREATING RECTANGLE THAT OVERWRITES BOTTOM PARAMETERS AREA
  /*beginShape();
  fill(backColor);
  vertex(X0,Y0);
  vertex(X1,Y0);
  vertex(X1,Y0+height/10);  //added -height/10 to clear the "jump" when falling
  vertex(X0,Y0+height/10);
  endShape();
  */
  
  /*
  fill(backColor);
  beginShape();       //CLEARING LEFT PART OF GRAPH (Y AXIS)
  vertex(X0,Y0+h/10);   
  vertex(300,Y0+h/10);
  vertex(300,Y1-h/10);
  vertex(X0,Y1-h/10);
  endShape(CLOSE);
  
  
  fill(backColor);
  beginShape();       //CLEARING RIGHT PART OF GRAPH (Y AXIS)
  vertex(X1+40,Y0+h/10);   
  vertex(X1,Y0+h/10);
  vertex(X1,Y1-h/10);
  vertex(X1+40,Y1-h/10);
  endShape(CLOSE);
  */
  
 
  
  if(select==1){
    beginShape();
     fill(graphbackColor);
    vertex(X0+ST,Y1);
    vertex(X0+ST,Y1-height/13);  //added -height/10 to clear the "jump" when falling
    vertex(X0+(X1-X0)/4,Y1-height/13);
    vertex(X0+(X1-X0)/4,Y1);
    endShape();
  }
  
  else if(select==2){
    beginShape();
    fill(graphbackColor);
    vertex(X0+(X1-X0)/4,Y1);
    vertex(X0+(X1-X0)/4,Y1-height/13);  //added -height/10 to clear the "jump" when falling
    vertex(X0+(X1-X0)/2,Y1-height/13);
    vertex(X0+(X1-X0)/2,Y1);
    endShape();
  }
  
  
    else if(select==3){
    beginShape();
    fill(graphbackColor);
    vertex(X1-(X1-X0)/2,Y1);
    vertex(X1-(X1-X0)/2,Y1-height/13);  //added -height/10 to clear the "jump" when falling
    vertex(X1-(X1-X0)/4,Y1-height/13);
    vertex(X1-(X1-X0)/4,Y1);
    endShape();
  }
  
    else if(select==4){
    beginShape();
    fill(graphbackColor);
    vertex(X1-(X1-X0)/4,Y1);
    vertex(X1-(X1-X0)/4,Y1-height/13);  //added -height/10 to clear the "jump" when falling
    vertex(X1-ST,Y1-height/13);
    vertex(X1-ST,Y1);
    endShape();
  }

  stroke(sColor,12);  
  strokeWeight(3);
  
  //DRAWING BACKGROUND LINES
  line(X0+ST,Y0+(Y1-Y0)/2,X1-ST,Y0+(Y1-Y0)/2);
  line(X0+(X1-X0)/4,Y0,X0+(X1-X0)/4,Y1);
  line(X0+(X1-X0)/2,Y0,X0+(X1-X0)/2,Y1);
  
  line(X1-(X1-X0)/4,Y0,X1-(X1-X0)/4,Y1);
  strokeWeight(1);
}

public void clearJunk(){
  
  noStroke();
  fill(backColor);
  
  beginShape();    //CLEARING BOTTOM PART (YEAR VALUES)
  vertex(X0,Y0+1);
  vertex(X1,Y0+1);
  vertex(X1,Y0+80);
  vertex(X0,Y0+80);
  endShape(CLOSE);
  

  fill(backColor);
  
  beginShape();      //CLEARING BOTTOM PART OF GRAPH
  vertex(X0,Y0);
  vertex(X1,Y0);
  vertex(X1,Y0+3);
  vertex(X0,Y0+3);
  endShape(CLOSE);
  
  beginShape();
  vertex(X1-ST,Y0);    //CLEARING RIGHT PART OF GRAPH
  vertex(X1,Y0);
  vertex(X1,Y1-height/13);
  vertex(X1-ST,Y1-height/13);
  endShape(CLOSE);
  
  beginShape();
  vertex(X0+ST,Y0);    //CLEARING LEFT PART OF GRAPH
  vertex(X0,Y0);
  vertex(X0,Y1-height/13);
  vertex(X0+ST,Y1-height/13);
  endShape(CLOSE);
 
}
public float[] findMaxMin(String[] buffer,int type){
  
  float maxvalue=MIN_FLOAT;        //max value of our data type
  float minvalue=MAX_FLOAT;        //minimum value of our data type
  
  for(int i=1;i<buffer.length;i++){ 
    String[] col = split(buffer[i],',');
    float f0=PApplet.parseFloat(col[type]);
    if (f0>maxvalue){
      maxvalue=f0;
    }
    if (f0<minvalue){
      minvalue=f0;
    }
  }

  float[] ret = {minvalue, maxvalue};
 
  return(ret);
  
}
}



//THE MATH CLASS WAS UPGRADED ON 05-14-2010 to include calculations of simple linear regression (t-values)

valueTable datamath;    //THIS CLASS DOES ALL THE MATH WORK TO COMPUTE MEAN, SD AND CORRELATIONS AND t-values
    
float[] sd;
class mathClass{                  //THIS CLASS DOES ALL STATISTIC ANALYSIS OP, CALCULATES MEAN, SD AND CORRELATION
  
  
  public float mean(int value){          //CALCULATES MEAN OF A SIGNLE VARIABLE
    
   datamath= new valueTable();
   String buffer[]=datamath.get_table_lines();
   
   float XMEAN=0;
   
    for(int i=1;i<buffer.length;i++){
        
        String[] col = split(buffer[i],',');  //ROW
        float X1=PApplet.parseFloat(col[value]);

        XMEAN=X1+XMEAN;
    }
        float t=buffer.length-1;      //TOTAL NUMBER OF ROWS TO DIVIDE MEAN
      
        return(XMEAN/t);  //RETURNS REAL MEAN
    
    }
    
  public float SD(int value){            //CALCULATES STANDARD DEVIATION OF A SINGLE VARIABLE
    datamath= new valueTable();
  
    String buffer[]=datamath.get_table_lines();
    float n=buffer.length-1;
    float sd=0;
    float SDT=0;
    m=mean(value);
    for(int i=1;i<buffer.length;i++){  //WARNING!!! THIS IS A DOUBLE NESTED LOOP AND SHOULD BE KEPT LIGHT BECAUSE IT'S SLOW
      String[] col = split(buffer[i],',');  //ROW
      float X1=PApplet.parseFloat(col[value]);
      sd=(X1-m);
      SDT=pow(sd,2)+SDT;
    }
   
    SDT=SDT/(n-1);
    SDT=sqrt(SDT);
   
    return(SDT);
  }
  
  
  public float correlate(int value1, int value2){    //VALCULATES CORRELATION OF TWO VARIABLES (X AND Y)
     datamath= new valueTable();
     String buffer[]=datamath.get_table_lines();
     float r=0;
     float n=buffer.length-1;
     float Xm=mean(value1);
     float Ym=mean(value2);
     float sdX=SD(value1);
     float sdY=SD(value2);
     
     String[] colT = split(buffer[0],',');  //ROW

     for(int i=1;i<buffer.length;i++){
      String[] col = split(buffer[i],',');  //ROW
    
      float X1=PApplet.parseFloat(col[value1]);
      float Y1=PApplet.parseFloat(col[value2]);
      
      r=(X1-Xm)*(Y1-Ym)/(sdX*sdY)+r;
       
     }
     
     r=r/(n-1);

    return(r);
  
  }


public float tvalue(int value1, int value2){
  
  datamath= new valueTable();
  String buffer[]=datamath.get_table_lines();
  float n=buffer.length-1;

  float sdY=SD(value2);
  //float sdX=SD(value1);                  //VALUE ALREADY COMPUTED STORED IN GLOBAL FLOAT SD1
  float sdX=SD1;
  //float co=correlate(value1,value2);    //CORRELATION ALREADY COMPUTED STORED IN GLOBAL FLOAT r
  float co=r;
  
  float b1=co*(sdY/sdX);
  float iR=(1-(co*co))/(n-2);
  float root=sqrt(iR);

  float DENOM=(sdY/sdX)*root;      
  float t=b1/DENOM;      //t-test result
  
  return(t);    
  
}

public void all(){
            M1=mean(WORDCOLUMN);                          //THIS IS A HEAVY COMPUTING OPERATION                        
            SD1=SD(WORDCOLUMN);                       //THIS IS A HEAVY COMPUTING OPERATION                                
            V1=pow(SD1,2);                                    //COMPUTES VARIANCE, AS IT'S JUST SQUARE ROOT OF STANDARD DEVIATION                                             
            r=correlate(WORDCOLUMN,drawselect);   //THIS IS A HEAVY COMPUTING OPERATION AND SHOULD NEVER BE DONE IN LOOP   
            t=tvalue(WORDCOLUMN,drawselect);      //THIS OPERATION IS DEPENDENT ON RESULTS OF r and SD1


}
}
class valueTable{    //THIS CLASS READS INPUT DATA FOR POPUP SKETCH AND RETURNS A STRING
                    //transforms valueTable into a format readable for POPUP CLASS
  public String[] get_table_lines(){
  
      String[] rows = new String[41]; 
   
      if (chosenWordItem == null) chosenWordItem = new WordItem("NullPointer", 1985);
      //GETS INFO FROM GLOBAL IndicatorNames STRING TO ALLOW TO CHANGE INDICATORS ON EXCEL DIRECTLY
      rows[0] = "MUESTRA," + indicatorNames[0] + "," + indicatorNames[1] + "," + indicatorNames[2] + "," + indicatorNames[3] + "," + chosenWordItem.getWord();
      //System.out.println(rows[0]);   
      int counter = 0;
      float[] wordValueRange = chosenWordItem.getValueRange();
      for (int i = 0; i < 40; i++) {
        String row = ""+i;
       
         //System.out.println("ABANS:"+row+"\n");
          for (int u = 0; u < indicatorNames.length; u++) row = row + "," + ((Indicator)indicators.get(indicatorNames[u])).getDataValue(i);
            //System.out.println(row);   
            row = row + "," + wordValueRange[counter];   
            
            rows[counter+1] = row;
            counter++;
      }
      //exit();
     return (rows);
   }
  }
  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "app" });
  }
}
