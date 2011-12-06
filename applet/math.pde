//THE MATH CLASS WAS UPGRADED ON 05-14-2010 to include calculations of simple linear regression (t-values)

valueTable datamath;    //THIS CLASS DOES ALL THE MATH WORK TO COMPUTE MEAN, SD AND CORRELATIONS AND t-values
    
float[] sd;
class mathClass{                  //THIS CLASS DOES ALL STATISTIC ANALYSIS OP, CALCULATES MEAN, SD AND CORRELATION
  
  
  float mean(int value){          //CALCULATES MEAN OF A SIGNLE VARIABLE
    
   datamath= new valueTable();
   String buffer[]=datamath.get_table_lines();
   
   float XMEAN=0;
   
    for(int i=1;i<buffer.length;i++){
        
        String[] col = split(buffer[i],',');  //ROW
        float X1=float(col[value]);

        XMEAN=X1+XMEAN;
    }
        float t=buffer.length-1;      //TOTAL NUMBER OF ROWS TO DIVIDE MEAN
      
        return(XMEAN/t);  //RETURNS REAL MEAN
    
    }
    
  float SD(int value){            //CALCULATES STANDARD DEVIATION OF A SINGLE VARIABLE
    datamath= new valueTable();
  
    String buffer[]=datamath.get_table_lines();
    float n=buffer.length-1;
    float sd=0;
    float SDT=0;
    m=mean(value);
    for(int i=1;i<buffer.length;i++){  //WARNING!!! THIS IS A DOUBLE NESTED LOOP AND SHOULD BE KEPT LIGHT BECAUSE IT'S SLOW
      String[] col = split(buffer[i],',');  //ROW
      float X1=float(col[value]);
      sd=(X1-m);
      SDT=pow(sd,2)+SDT;
    }
   
    SDT=SDT/(n-1);
    SDT=sqrt(SDT);
   
    return(SDT);
  }
  
  
  float correlate(int value1, int value2){    //VALCULATES CORRELATION OF TWO VARIABLES (X AND Y)
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
    
      float X1=float(col[value1]);
      float Y1=float(col[value2]);
      
      r=(X1-Xm)*(Y1-Ym)/(sdX*sdY)+r;
       
     }
     
     r=r/(n-1);

    return(r);
  
  }


float tvalue(int value1, int value2){
  
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

void all(){
            M1=mean(WORDCOLUMN);                          //THIS IS A HEAVY COMPUTING OPERATION                        
            SD1=SD(WORDCOLUMN);                       //THIS IS A HEAVY COMPUTING OPERATION                                
            V1=pow(SD1,2);                                    //COMPUTES VARIANCE, AS IT'S JUST SQUARE ROOT OF STANDARD DEVIATION                                             
            r=correlate(WORDCOLUMN,drawselect);   //THIS IS A HEAVY COMPUTING OPERATION AND SHOULD NEVER BE DONE IN LOOP   
            t=tvalue(WORDCOLUMN,drawselect);      //THIS OPERATION IS DEPENDENT ON RESULTS OF r and SD1


}
}
