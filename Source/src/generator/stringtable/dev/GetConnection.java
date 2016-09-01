package generator.stringtable.dev.StringTableHUMaven;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
public class GetConnection {
	static Connection conn;
	static String driverName;
	static String url;
	static String username;
	static String passcode;
	public static void readProperties() throws IOException{
		  Properties pro=new Properties();
		  FileInputStream in = new FileInputStream("parameters.properties");
		  pro.load(in);
		  //getting values from property file
		  driverName=pro.getProperty("driverName");
		  url=pro.getProperty("url");
		  //System.out.println(url);
		  username=pro.getProperty("username");
		  passcode=pro.getProperty("passcode");  
	}
	public static void checkConnection() throws IOException
	{
		try{
			readProperties();
            //Class.forName("com.mysql.jdbc.Driver");
			Class.forName(driverName);
            System.out.println("Successfully add the sql Driver！");
            
        }catch(ClassNotFoundException e1){
            System.out.println("Can not find sql Driver!");
            e1.printStackTrace();
          
        }
	}
	public static void createConnection() throws SQLException, IOException{
		readProperties();
		//String url="jdbc:mysql://localhost:3306/StringTable";//JDBC's URL   
		//conn = DriverManager.getConnection(url,    "root","");
		conn = DriverManager.getConnection(url,    username,passcode);
		
		System.out.println("Connection Success");
	}
	public static String createStm(String query, String keyword) throws SQLException{
		 Statement stmt = conn.createStatement(); 
		 ResultSet id=stmt.executeQuery(query);
		 String app_id=null;   
         //4. Process the result set
           while(id.next())
           {
           	app_id=id.getString(keyword);
           //	System.out.println(keyword);
           	
           }
           stmt.close();
           return app_id;
	}

    public static void write(String path_id) throws SQLException, IOException{
    	Statement stmt = conn.createStatement(); 
    	String tablequery="select STR_ID,Value from Template where PATH_ID='"+path_id+"'";
		ResultSet tableset=stmt.executeQuery(tablequery);
		String str_id=null;
        String value=null;
		while(tableset.next())
		{
			str_id=tableset.getString("STR_ID");
			value=tableset.getString("Value");
			
			String find_str_id="select ST from STR_ID where STR_ID='"+str_id+"'";
    		String result_str_id=createStm(find_str_id,"ST");
    		
    		String find_str_value="select VALUE_ST from VALUE where VALUE='"+value+"'";
    		String result_str_value=createStm(find_str_value,"VALUE_ST");
			
    		if(str_id!=null)
         	{
         		String outstring="     <entry id=\"$"+result_str_id+"\" "+"str="+result_str_value+"/>" ;
             	System.out.println(outstring);
             	StringTableGenerator.outfile(outstring);
         	}
			if(str_id==null&&value!=null)
			{
				System.out.println("     "+result_str_value);
				StringTableGenerator.outfile("       "+result_str_value);
			}
		}
		stmt.close();
    }
    public static void select(String entry) throws IOException{
    	//checkConnection();
    	try{
        	//createConnection();
        	
        	Statement stmt=conn.createStatement();
        	ResultSet entryset=stmt.executeQuery(entry);
        	String path_id=null;
        	String link_id=null;
        	String prelink="";
        	while(entryset.next())
        	{
        		path_id=entryset.getString("PATH_ID");
        		link_id=entryset.getString("LINK_ID");
        		String linkquery="select Link from LINK where LINK_ID='"+link_id+"'";
        		String link=createStm(linkquery,"Link");
        		if(prelink!=""&&!link.equals(prelink))
        		{
        			StringTableGenerator.outfile("</strings>");
                	System.out.println("</strings>");
        		}
        		
        		if(!link.equals(prelink)){
        			StringTableGenerator.setupoutfile(link);
            		StringTableGenerator.outfile("<strings>");
            		System.out.println("<strings>");
        		}
        		write(path_id);
        		prelink=link;
        	}
        	StringTableGenerator.outfile("</strings>");
    		System.out.println("</strings>");
        	stmt.close();
   
             //-----finish querying ---------
             conn.close();
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    		
    	}
    	
    }
   
    public static void generateall(String type) throws IOException, SQLException{
    	/**
         *  //1. Get a connnection to database
        	 //2.Create a statement
        	 //3. Execute SQL query
        	 //4. Process the result set
         */
 
    	checkConnection();
    	createConnection();
    	
    	String hu="select TYPE_ID from HU where TYPE='"+type+"'";
    	String hu_id=createStm(hu,"TYPE_ID");
    	
    	String entry="select PATH_ID,LINK_ID from PATH where HU_ID='"+hu_id+"'ORDER BY LINK_ID ASC";
    	select(entry);

    }
    public static void generateall(String hu_type, String region, String language) throws IOException, SQLException{
    	/**
         *  //1. Get a connnection to database
        	 //2.Create a statement
        	 //3. Execute SQL query
        	 //4. Process the result set
         */
    	checkConnection();
    	createConnection();
    	
    	String hu="select TYPE_ID from HU where TYPE='"+hu_type+"'";
    	String hu_id=createStm(hu,"TYPE_ID");
    	
        String rl="select RL_ID from RL where REGION='"+region+"' and LANGUAGE='"+language+"' ";
        String rl_id=createStm(rl,"RL_ID");

    	String entry="select PATH_ID,LINK_ID from PATH where HU_ID='"+hu_id+"'and RL_ID='"+rl_id+"' ORDER BY LINK_ID ASC";
    	select(entry);
    	
    }
    public static void generateall(String hu_type,String region,String language,String app_name) throws IOException, SQLException{
        /**
         *  //1. Get a connnection to database
        	 //2.Create a statement
        	 //3. Execute SQL query
        	 //4. Process the result set
         */
    	checkConnection();
        	createConnection(); 	
        	String app="select APP_ID from APP where APPNAME= '"+app_name+"'";
        	String app_id=createStm(app,"APP_ID");
    
            String hu="select TYPE_ID from HU where Type= '"+hu_type+"'";
            String hu_id=createStm(hu,"Type_ID");

            String rl="select RL_ID from RL where REGION='"+region+"' and LANGUAGE='"+language+"' ";
            String rl_id=createStm(rl,"RL_ID");
            
            String entry="select PATH_ID,LINK_ID from PATH where APP_ID='"+app_id+"' and HU_ID='"+hu_id+"'and RL_ID='"+rl_id+"' ORDER BY LINK_ID ASC";
            select(entry);

    }
   
}