//auxiliary class for implementing of XMLRPC-methods

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class Calendar {

    public int addEntry(String s) {
        //System.out.println("Adding entry...Done!");
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
            out.println(s);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return 42;
    }

    public int deleteEntry(String lineToRemove) {

        String currentLine = null;
        ArrayList<String> lines = new ArrayList<String>();
        File file = new File("Calendar.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.equals(lineToRemove))continue;
                lines.add(currentLine);
            }

            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
            for (int i = 0; i < lines.size(); i++) {
                out.println(lines.get(i));
            }
            out.close();
        } catch (IOException e) {
              e.printStackTrace();
        }
        return 42;
    }


    public Vector getList() {
        //read from the file and create one string and send
        Vector result = new Vector();
        File file = new File("Calendar.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
