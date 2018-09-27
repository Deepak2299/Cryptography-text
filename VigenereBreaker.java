import java.util.*;
import edu.duke.*;
import java.io.*;
 
public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        StringBuilder submessage = new StringBuilder();
        for (int i=whichSlice;i<message.length();i+=totalSlices){
            submessage.append(message.charAt(i));
        }
        return submessage.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for(int i = 0;i<klength;i++){
            String str = sliceString(encrypted,i,klength);
            key[i]=cc.getKey(str);
        
        }
        
        return key;
    }

    public void breakVigenere () {
        FileResource fr = new FileResource();
        String s = fr.asString();
        HashMap<String,HashSet<String>> map = new HashMap<String,HashSet<String>>(); 
        DirectoryResource dr = new DirectoryResource();
        for(File file : dr.selectedFiles()){
        FileResource dic = new FileResource(file);
        HashSet<String> dictionary = readDictionary(dic);
        map.put(file.getName(),dictionary);
    }   
        
        breakForAllLangs(s,map);
        //WRITE YOUR CODE HERE 
    }
     
    public HashSet<String> readDictionary(FileResource fr){
    HashSet<String> set = new HashSet<String>();
    for(String word : fr.lines()){
        word = word.toLowerCase();
        set.add(word);
    }
    return set;
    }
    
    public int countWords(String message,HashSet<String> dictionary){
    int count=0;
    for(String word : message.split("\\W")){
    if(dictionary.contains(word.toLowerCase())){
        
        count+=1;   }
    }
    return count;
    }
    
    /*public String breakForLanguage(String encrypted,HashSet<String> dictionary){
    //int count=0;
    int largest = 0;
    int[] wordcount = new int[100];
    String str="";
    int keys=0;
    int i=1;
    while(i<=100){
    int[] key = tryKeyLength(encrypted,i,'e');
    VigenereCipher vc = new VigenereCipher(key);
    String decrypt = vc.decrypt(encrypted);
    //int count1 = countWords(decrypt,dictionary);
    wordcount[i-1]=countWords(decrypt,dictionary);
    /*if(count<count1){
        count = count1;
        str=decrypt;
        keys = key.length;
    }*/
    /*i++;
    }
    for (int k = 0; k < 100; k++) {
            if (wordcount[k] > largest) {
                largest = wordcount[k];
                //index = k;
            }
        }
    //System.out.println("This file contains "+ keys +" valid words out of "+ countWords(str,dictionary) );
    System.out.println(largest);
    return str;
    }*/
     public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        int[] key_list = new int[100];
        int[] wordcount = new int[100];
        char commonChar = mostCommonCharIn(dictionary);
        for (int k = 1; k <= 100; k++) key_list[k-1] = k;
        
        for (int k = 0; k < 100; k++) {
            int[] key = tryKeyLength(encrypted, key_list[k], commonChar);
            VigenereCipher vc = new VigenereCipher(key);
            String result = vc.decrypt(encrypted);
            wordcount[k] = countWords(result, dictionary);
        }
        
        // figure out which key length has the largest word count.
        int largest = 0;
        int index = 0;
        for (int k = 0; k < 100; k++) {
            if (wordcount[k] > largest) {
                largest = wordcount[k];
                index = k;
            }
        }
        
        //System.out.println("The largest count is "+largest);
        
        int truekey = key_list[index];
        int[] key = tryKeyLength(encrypted, truekey, 'e');
        System.out.println("The keys are "+"\t");
        for (int k = 0; k < key.length; k++) {
            //System.out.println(key[k]);
        }
        //System.out.println("The key length is "+key.length);
        VigenereCipher vc = new VigenereCipher(key);
        return vc.decrypt(encrypted);
    }
    
    public char mostCommonCharIn(HashSet<String> Dictionary){
    HashMap<Character,Integer> map = new HashMap<Character,Integer>();
    
        for(String word : Dictionary){
        word = word.toLowerCase();
            for(int i=0;i<word.length();i++){
                char s=word.charAt(i);
                if(!map.keySet().contains(s)){
                    map.put(s,1);
                }
                else{
                    map.put(s,map.get(s)+1);
                }
          }
    }
    char s='a';
    for(char check :map.keySet()){
        if(map.get(s)<map.get(check)){
            s=check;
        }
    }
    return s;
    }
    
    public void breakForAllLangs(String encrypted , HashMap<String,HashSet<String>> Dictionary){
        int maxcount=0;
        String str="";
        String usedLanguage="";
        for(String language : Dictionary.keySet()){
            HashSet<String> dictionary = Dictionary.get(language);
            String decrypt = breakForLanguage(encrypted,dictionary);
            int count = countWords(decrypt,dictionary);
            if(maxcount<count){
                maxcount = count;
                str = decrypt;
                usedLanguage = language;
            }
        
        }
        System.out.println(str.substring(0,100));
        System.out.println(usedLanguage);
    }
}