import edu.duke.*;
/**
 * Write a description of tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class tester {
    public void tested(){
        
        CaesarCracker cc = new CaesarCracker('a');
        FileResource fr = new FileResource();
        String str = fr.asString();
        System.out.println(cc.decrypt(str));
    
    }
    public void test1(){
        int[] rome={17,14,12,4};
        FileResource fr = new FileResource();
        String str = fr.asString();
        VigenereCipher vc = new VigenereCipher(rome);
        System.out.println(vc.encrypt(str));
        //System.out.println(vc.decrypt(str));
    
    }
}
