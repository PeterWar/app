// Code from Visualizing Data, First Edition, Copyright 2008 Ben Fry.
class WordMap extends SimpleMapModel {    
  HashMap words;
  int scanSampled;
  int sampleYear;
    
  WordMap(int year) {
    words = new HashMap();
    sampleYear = year;
  }
  
  void addWord(String word) {
    WordItem item = (WordItem) words.get(word);
    if (item == null) {
      item = new WordItem(word, sampleYear);
      words.put(word, item);
    }
    item.incrementSize();
  }
  
  void setscanSampled(int s) {
   scanSampled = s; 
  }
  
  int getscanSampled() {
   return this.scanSampled; 
  }
    
  void finishAdd() {
    items = new WordItem[words.size()];
    words.values().toArray(items);
  }
  
  void putWord (String word, WordItem wordItem) {
   words.put(word, wordItem); 
    
  }
  
  void inputWord(String word, int frequency) {
   WordItem w = new WordItem(word, sampleYear);
   w.setSize(frequency);
   this.putWord(word, w);
  }
  
  WordItem getWordItem(String word) {
    return (WordItem) words.get(word);
  }
  
  boolean containsWord(String word) {
   return words.containsKey(word);
    
  }
  
  boolean isEmpty() {
   return words.isEmpty();
  }
  
  

}
