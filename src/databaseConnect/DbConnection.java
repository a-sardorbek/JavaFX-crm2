
package databaseConnect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnection {
    
    static Connection con;
    
    public static Connection getConnection(boolean connect){
     
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            if(connect==true){
            con = DriverManager.getConnection("jdbc:derby:data;create=true");
            }else{
                con = DriverManager.getConnection("jdbc:derby:data;shutdown=true");
                System.out.println("stopped derby");
            }
            return con;
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Error in connection");
            return null;
        }
        
    }
    
    
   public static void setUser(){
        String table_name = "lorentusers";
        Statement st;
        try{
             st =con.createStatement();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet rs = dbm.getTables(null, null, table_name.toUpperCase(), null);
            if(rs.next()){
                System.out.println("Table users already exists");
            }else{
                st.execute("CREATE TABLE lorentusers (\n" +
                            "  id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                            "  username VARCHAR(45) NOT NULL,\n" +
                            "  password VARCHAR(45) NOT NULL)");

                   
       
           }
        }catch(SQLException e){
            System.out.println("Error in create table patient");
        }finally{
            
        }
    }
   
   public static void setPatientTable(){
        String table_name = "patients1";
        Statement st;
        try{
             st =con.createStatement();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet rs = dbm.getTables(null, null, table_name.toUpperCase(), null);
            if(rs.next()){
                System.out.println("Table patients1 already exists");
            }else{

                   st.execute("CREATE TABLE patients1 (\n" +
                            "  id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                            "  name VARCHAR(45),\n" +
                            "  surname VARCHAR(45),\n" +
                            "  borthDate VARCHAR(45),\n" +
                            "  phone VARCHAR(45),\n" +
                            "  tolov INTEGER,\n" +
                            "  regDate varchar(45),\n"+
                            "  deleted boolean default false)");
       
           }
        }catch(SQLException e){
            System.out.println("Error in create table patient");
        }finally{
            
        }
    }
    
}
