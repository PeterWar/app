class POPUPClass{
int[] COLOR={0,140,0};
int[] COLORDATA={80,80,200};
valueTable data;              //Creating data table from file to be used by POPUP
PFont font; //VARIABLE FONT

void drawscatterplot(){

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
  text("σ",240,50);
  textFont(popupTitleFont);
  text(SD1,240,65);
  text("Varianza",290,50);
  text(V1,290,65);
  
  //text(col[drawselect],580,55);
  if (r>0.7||r<-0.7){
    fill(205,30,30);
    text("Correlación",355,50);
    textFont(mapFont);
    text(r,355,65);
    textFont(popupTitleFont);
  }
  else{
    text("Correlación",355,50);
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

float[] findMaxMin(String[] buffer,int type){
  
  float maxvalue=MIN_FLOAT;        //max value of our data type
  float minvalue=MAX_FLOAT;        //minimum value of our data type
  
  for(int i=1;i<buffer.length;i++){ 
    String[] col = split(buffer[i],',');
    float f0=float(col[type]);
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

void getValue(int type){        //SHOW WICH WORD ARE WE VISUALIZING

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


