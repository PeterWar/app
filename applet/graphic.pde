 class graphicClass{          //THIS CLASS DRAWS THE GRAPHIC FOR POPUP SKETCH
 PFont font; //VARIABLE FONT
 valueTable data;              //Creating data table from file to be used by graphic class
 void drawvalues(int type){
 
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
     
     float RositaSalamo0=float(col[0]);
     float m0= map(RositaSalamo0,rangeYear[0],rangeYear[1],X0+8,X1-8);    //Mapping Year range into graph
     
     float RositaSalamo1=float(col[type]);
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


void movegraphdown(){
if (select!=drawselect){
   if (move==1){
      R=R+a;
      a=a+0.0098;
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


void movegraphup(){

   if (move==0){
      R=R-a;
      a=a+0.0098;
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
void cleargraph(){  //CLEARS GRAPH CONTENTS AND REDRAWS INFO  
  
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

void clearJunk(){
  
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
}



