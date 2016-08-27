package generator.stringtable.dev.StringTableHUMaven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StringTableGenerator {
		static BufferedWriter output;
		public static void setupoutfile(String filename) throws IOException{
			File outfile=new File(filename);
			output =new BufferedWriter(new FileWriter(outfile));
		}
		
		public static void outfile(String s) throws IOException{
			//output.newLine();
			
			output.write(s);
			output.newLine();
			output.flush();
		}
}
