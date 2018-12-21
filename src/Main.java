import java.util.Scanner; 

public class Main {
	public static void main(String[] args) {
        Sliding_Window window = new Sliding_Window(30);
        System.out.println("Enter C to compress a text file or D to decompress a compressed text file.");
        
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while(!(input.toLowerCase().equals("d") || input.toLowerCase().equals("c"))){
        	System.out.println("Invalid input. Enter either C or D.");
        	input = sc.nextLine();
        }
        if(input.toLowerCase().equals("c")) 
        	window.compress();
        else
        	window.decompress();
    }
}
