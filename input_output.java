package patentmatching;
import java.io.*;

/**
 *
 * @author aemad
 */
public class input_output {

public BufferedReader csv_reader(String path)
    {
    BufferedReader k  = null;
    try {
    k = new BufferedReader(new FileReader(path));
      }
    catch(Exception e) {System.out.println("DFSDF");}
   return k;

}
}
