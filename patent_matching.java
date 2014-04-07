package patentmatching;

import java.io.*;
import java.util.*;
import com.csvreader.CsvWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.lucene.search.spell.JaroWinklerDistance;
/**
 *
 * @author aemad
 */
public class patent_matching {

    public void matching_function(String f1, String f2) throws Exception {
        input_output l = new input_output();
        BufferedReader k1 = l.csv_reader(f1);
        CsvWriter write = new CsvWriter(new FileWriter("matched.csv", true), ',');
        String line = "";
        String line2 = "";
        HashSet<String> name1 = new HashSet();
        ArrayList<String> initial1 = new ArrayList();

        HashSet<String> name2 = new HashSet();
        ArrayList<String> initial2 = new ArrayList();
        int count = 0;
        while((line = k1.readLine())!=null) {



            System.out.println(line);
            count++;
            if(count==10) break;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            
            ArrayList<String> row1 = new ArrayList(Arrays.asList(line.split(",")));

            String id_sm = row1[0];
            String vendor_sm = row1[9];
            duns_sm = row1[5];
            HashMap hm = get_names(row1,0);
            name1 = (HashSet)hm.get("name");
            initial1 = (ArrayList<String>)hm.get("initial");
            //System.out.println(name1);
            BufferedReader k2 = l.csv_reader(f2);
            double ratio1 = 0;
            while((line2 = k2.readLine()) != null) {
                //System.out.print("\r"+count++);
                ArrayList<String> row2 = new ArrayList(Arrays.asList(line2.split(",")));
                
                HashMap hm1 = get_names(row2,1);
                name2 = (HashSet)hm1.get("name");
                initial2 = (ArrayList<String>)hm1.get("initial");
                ArrayList<String> b = initial1;
                initial1.retainAll(initial2);
                int initial_count = initial1.size();
                
                if(initial_count<2) continue;
                System.out.println(initial1);
                ratio1 = match_ratio(name1, b,name2,initial2,initial_count);

                if(ratio1 >= 0.95) {
                    row1.add(row2.get(0));
                    row1.add(row2.get(1));
                    row1.add(row2.get(2));
                    row1.add(Double.toString(ratio1));
                    row1.add("\n");
                }

            }



            for(int n=0;n<row1.size();++n)
            {
                ArrayList<String> pfile = new ArrayList();
                String item = row1.get(n);
                while(!item.equals("\n"))
                {
                    
                    pfile.add(item);
                    if(n+1>=row1.size()) break;
                        item = row1.get(++n);


                }
                if(n>=11){
                pfile.add(0, row1.get(0));
                pfile.add(1, row1.get(1));
                pfile.add(2, row1.get(2));
                pfile.add(3, row1.get(3));
                pfile.add(4, row1.get(4));
                pfile.add(5, row1.get(5));
                //pfile.add(6, row1.get(6));
                //pfile.add(7, row1.get(7));
                }
                System.out.println(pfile);
                write.writeRecord(pfile.toArray(new String[pfile.size()]));
                write.flush();
            }
            }
            write.close();
        }
    public int intersection(HashSet<String> a, HashSet<String> b){
        int count = 0;
        for(String e : a) {
            if(b.contains(e)) count++;
        }
        return count;
    }

    public double match_ratio(HashSet<String> name1,ArrayList<String> initial1,HashSet<String> name2,ArrayList<String> initial2,int initial_count) {
        int name_count = intersection(name1, name2);
        float big1=0;
        float big2 = 0;
        if(name_count >=2)
        {

            if(initial_count>=3 || initial1.size()==2 || initial2.size()==2)
                return 1;
            else
                return 0.99;
        }
        JaroWinklerDistance p = new JaroWinklerDistance();
        for(String item1 : name1) {
            for(String item2 : name2) {
                float m = p.getDistance(item1, item2);
                if(m>big1) big1 = m;
                else if(m>big2) big2 = m;
            }
        }
        float ratio = (big1+big2)/2;
        return ratio;
    }

    public HashMap get_names(ArrayList<String> row, int j) {
        ArrayList<String> initial1 = new ArrayList();
        HashMap hm = new HashMap();
        HashSet<String> name1 = new HashSet();
        for(int i=j;i<3;++i) {

            String  [] fname = row.get(i).toUpperCase().split(" ");
            
            for(int k=0; k<fname.length; ++k) {
                String item = fname[k];
                
                if(item.length()>1 && item.charAt(1)!='.') {
                    
                    item = item.replaceAll("\\P{L}+","");
                    try {
                        String c = String.valueOf(item.charAt(0));
                        initial1.add(c);
                        name1.add(item);
                }
                    catch(Exception e) {}
            }
                else try {initial1.add(String.valueOf(item.charAt(0)));} catch(Exception e) {}
        }
        }
        hm.put("initial",initial1);
        hm.put("name", name1);
        return hm;
    }
  
}
