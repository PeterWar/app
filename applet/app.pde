import guicomponents.*;
import treemap.*;

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

void setup() {
  
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

void draw() {
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


void drawMap() {
  strokeWeight(0.25f);
  textFont(mapFont);
  map.draw();  
}


//------------------------------------------GUI components and event fields------------------//

// Add an event to the events textfield
void handleSliderEvents(GSlider slider) {
  if (currentMapTime != getSliderTime()) {            //COMPARA TIPUS DE MAPA AMB TEMPS SELECCIONAT
  currentMapTime = getSliderTime();
    updateTreemap();
  }
}

void handleTextFieldEvents(GTextField tfield){
  if (!textBoxContent.equals(txfML1.getText())) {
    textBoxContent = txfML1.getText();
    updateTreemap();
  }
  txfML1.draw();
}

void handleButtonEvents(GButton button) {
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

void mouseMoved() {
 // Check if it's over treemap
     // Highlight a WordItem
     if (state == "treemap") {
       WordItem[] wordItems = (WordItem[]) currentWordMap.getItems();
       for (int i = 0; i < wordItems.length; i++) {
           wordItems[i].isMouseOver();
       }
     }
}

void mouseClicked() {
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

  
boolean isMouseInside(int x, int y, int w, int h) {
  if (mouseX > x && mouseX < x+w) {
     if (mouseY > y && mouseY < y+h) {
       return true;
       }
     }
     return false;
  }
  

//------------------------------------------Calculating functions----------------------------------------

void updateTreemap() {
 currentWordMap = createWordMap(textBoxContent, (WordMap)dataMap.get(currentMapTime));
 map = new Treemap(currentWordMap, treeX, treeY, treeW , treeH );
}

WordMap createWordMap(String words, WordMap wordMap) {
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

int  getSliderTime() {
 return sdr1.getValue()*5 + 1970; 
}

void mousePressed(){
 if (state=="popup"){
 if (mouseX>X0&&mouseX<X0+(X1-X0)/4&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=1;}
 else if (mouseX>X0+(X1-X0)/4&&mouseX<X0+(X1-X0)/2&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=2;;}
 else if (mouseX>X0+(X1-X0)/2&&mouseX<X1-(X1-X0)/4&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=3;}
 else if (mouseX>X1-(X1-X0)/4&&mouseX<X1&&mouseY>Y1-h/10&&mouseY<Y1){move=1;select=4;}
 }
}

void setupSlider(){
  
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
