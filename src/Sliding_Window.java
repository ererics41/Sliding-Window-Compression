import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.util.ArrayList;

/**
 * @author Eric Shen 
 */
public class Sliding_Window
{   
    public static void main(String[] args) {
        SlidingWindow window = new SlidingWindow(30);
        //window.compress();
        window.decompress();
    }
}

class SlidingWindow{
    FileReader inFile = null;
    FileWriter outFile = null;
    int windowSize;
    ArrayList<Character> input;
    ArrayList<Character> window;
    
    public SlidingWindow(int windowSize) {
    	this.windowSize = windowSize;
        input = new ArrayList<Character>();
        window = new ArrayList<Character>();
    }
    
    public void compress(){
        getInput();
        window.clear();
        FileWriter outFile = null;
        try{
            outFile = new FileWriter("Compression Output.txt");
            for(int i = 0; i < windowSize; i++){
                window.add(input.get(i));
                outFile.write(window.get(i));
            }
            for(int i = windowSize; i < input.size();i++){
                int[] match = maxMatch(i);
                if(match[0] > 3){
                    outFile.write((char)7);
                    outFile.write(match[1]);
                    outFile.write(match[0]);
                    for(int j = 0; j < match[0]; j++){
                        window.remove(0);
                        window.add(input.get(i+j));
                    }
                    i+= match[0]-1;
                }else{
                    outFile.write(input.get(i));
                    window.remove(0);
                    window.add(input.get(i));
                }
            }
            outFile.close();
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void decompress(){
        ArrayList<Character> output = getOutput();
        try{
            outFile = new FileWriter("Decompression Output.txt");
            for(int i = 0; i < output.size(); i++){
                outFile.write(output.get(i));
            }
            outFile.close();
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void setWindowSize(int size){
        windowSize = size;
    }
    
    public int getWindowSize(){
        return windowSize;
    }
    
    private ArrayList<Character> getOutput(){
        ArrayList<Character> output = new ArrayList<Character>();
        try{
            inFile = new FileReader(getFile());
            int count = 0;
            while(inFile.ready()) {
                char c = (char)inFile.read();
                if(count < windowSize){
                    output.add(c);
                    window.add(c);
                    count++;
                }else{
                    if(c == (char)7){
                        int pos = inFile.read();
                        int numElems = inFile.read();
                        for(int i = 0; i < numElems;i++){
                            output.add(window.get(pos+i));
                        }
                        for(int i = 0; i < numElems;i++){
                            window.add(window.get(pos));
                            window.remove(0);
                        }
                    }else{
                        output.add(c);
                        window.add(c);
                        window.remove(0);
                    }
                }
            }
            
        }
        catch (IOException e) {
                System.out.println(e.getMessage());
        }
        return output;
    }
    
    //returns a "pair" consisting of an int array where element 0 is the size of the match and element 1
    //is the position of the match
    private int[] maxMatch(int pos){
        int[] rtn = {0, 0};
        int max = 0;
        for(int i = 0; i < windowSize; i++){
            while(rtn[0]+i<windowSize && pos+rtn[0]<input.size() && window.get(i+rtn[0]).equals(input.get(pos+rtn[0]))){
                rtn[0]++;
            }
            if(rtn[0]>max){
                max = rtn[0];
                rtn[1] = i;
            }
            rtn[0] = 0;
        }
        rtn[0] = max;
        return rtn;
    }
    
    private void getInput(){
        input.clear();
        try{
            inFile = new FileReader(getFile());
            
            while(inFile.ready()) {
                // Reads a single character as a byte
                char c = (char)inFile.read();
                input.add(c);
            }
        }
        catch (IOException e) {
                System.out.println(e.getMessage());
        }
    }
    
    private static File getFile(){
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(null,"Select");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setVisible(true);
        File file = fileChooser.getSelectedFile();
        return file;
    }

}
