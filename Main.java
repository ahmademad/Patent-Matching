package patentmatching;
import patentmatching.patent_matching;
/**
 *
 * @author aemad
 *
 */
public class Main {

    /**
     * @param args the command line arguments
     */
  public static void main(String[]args) throws Exception {
      patent_matching o = new patent_matching();
        o.matching_function("USDA_researchers.csv", "USPTO_database.csv");
    }
}
