class valueTable{    //THIS CLASS READS INPUT DATA FOR POPUP SKETCH AND RETURNS A STRING
                    //transforms valueTable into a format readable for POPUP CLASS
  String[] get_table_lines(){
  
      String[] rows = new String[41]; 
   
      if (chosenWordItem == null) chosenWordItem = new WordItem("NullPointer", 1985);
      //GETS INFO FROM GLOBAL IndicatorNames STRING TO ALLOW TO CHANGE INDICATORS ON EXCEL DIRECTLY
      rows[0] = "MUESTRA," + indicatorNames[0] + "," + indicatorNames[1] + "," + indicatorNames[2] + "," + indicatorNames[3] + "," + chosenWordItem.getWord();
      //System.out.println(rows[0]);   
      int counter = 0;
      float[] wordValueRange = chosenWordItem.getValueRange();
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
