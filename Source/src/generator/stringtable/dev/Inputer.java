package generator.stringtable.dev.StringTableHUMaven;

import java.io.IOException;
import java.sql.SQLException;

public class Inputer {
	public static void main(String[] args) throws IOException, SQLException{
		//1.java -jar StringTable.jar//For local tests.
		//2.java -jar StringTable.jar<hu_type>
		//3.java -jar StringTable.jar<hu_type><region><language>
		//4.java -jar StringTable.jar<app_name><hu_type><region><language>

		    int generateall=args.length;
			if(generateall==4)
			{
				String app_name=args[0];
				String hu_type=args[1];
				String region=args[2];
				String language=args[3];
				
				GetConnection.generateall(hu_type,region,language,app_name);
				
			}
			else if(generateall==1)
			{
				String hu_type=args[0];
				GetConnection.generateall(hu_type);
				
			}
			else if(generateall==3)
			{
				
				String hu_type=args[0];
				String region=args[1];
				String language=args[2];

				GetConnection.generateall(hu_type,region,language);
				
			}
			else
			{
				System.out.println("wrong input arguments");
			}
		 
	}
}
