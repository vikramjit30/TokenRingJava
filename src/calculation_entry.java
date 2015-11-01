import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class calculation_entry {
	     private int result=0;
		 private int b;
		
	     public calculation_entry(int result,int b) {
	       this.result = result;
	       this.b = b;
	       
	    }

	     public void writeToFile_addition() {
	         //1. Write new line to file as date/time/../comment
	    	 result = result+b;
	         String newEvent = (Integer.toString(result));
	         PrintWriter out = null;
	         try {
	             out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
	             out.println(newEvent);
	         } catch (IOException e) {
	             System.err.println(e);
	         } finally {
	             if (out != null) {
	                 out.close();
	             }
	         }
	     }
	     public void writeToFile_subtraction() {
	         //1. Write new line to file as date/time/../comment
	    	 result = result-b;
	         String newEvent = (Integer.toString(result));
	         PrintWriter out = null;
	         try {
	             out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
	             out.println(newEvent);
	         } catch (IOException e) {
	             System.err.println(e);
	         } finally {
	             if (out != null) {
	                 out.close();
	             }
	         }
	     }
	     
	     public void writeToFile_multiplication() {
	         //1. Write new line to file as date/time/../comment
	    	 result = result*b;
	         String newEvent = (Integer.toString(result));
	         PrintWriter out = null;
	         try {
	             out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
	             out.println(newEvent);
	         } catch (IOException e) {
	             System.err.println(e);
	         } finally {
	             if (out != null) {
	                 out.close();
	             }
	         }
	     }
	     
	     public void writeToFile_division() {
	         //1. Write new line to file as date/time/../comment
	    	 result = result/b;
	         String newEvent = (Integer.toString(result));
	         PrintWriter out = null;
	         try {
	             out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
	             out.println(newEvent);
	         } catch (IOException e) {
	             System.err.println(e);
	         } finally {
	             if (out != null) {
	                 out.close();
	             }
	         }
	     }
	     
	     
	     public String makeString() {
	         return (Integer.toString(result));
	     }

	     
}
