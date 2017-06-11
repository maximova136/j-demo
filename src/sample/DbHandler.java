package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbHandler {

    Connection conn;
    Statement stmt;

    //создание самого файла базы данных
    public void onCreateDB(){
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
        } catch ( Exception e ) {
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Opened database successfully") ;
    }

    //создание таблицы в базе данных
    public void createTable(){
        try {
            stmt = conn.createStatement();
//            stmt.execute("CREATE TABLE if not exists 'IMAGE' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'urlid' text, 'url' text);");
            stmt.execute("CREATE TABLE if not exists 'IMAGE' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'urlid' text);");
        } catch (Exception e){
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Table exists or created successfully") ;
    }

//    public void writeDB(String urlid, String url)  {
        public void writeDB(String urlid)  {
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
            conn.setAutoCommit(false) ;
            System.out.println("Opened database successfully") ;

            stmt = conn.createStatement() ;
//            String sql = "INSERT INTO IMAGE (URLID, URL) " +
//                    "VALUES ('" + urlid + "', '"+ url +"');";
            String sql = "INSERT INTO IMAGE (URLID) " +
                    "VALUES ('" + urlid + "');";
            stmt.executeUpdate(sql);
            stmt.close() ;
            conn.commit() ;
            conn.close() ;
        } catch ( Exception e ) {
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Records created successfully") ;
    }

    public ArrayList showDB(){
        ArrayList<String> list = new ArrayList();
        conn = null;
        stmt = null;
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
            conn.setAutoCommit(false) ;
            stmt = conn.createStatement() ;
            ResultSet rs = stmt.executeQuery( "SELECT * FROM IMAGE;" ) ;
            while ( rs.next() ) {
                int id = rs.getInt("id") ;
                String  urlid = rs.getString("urlid") ;
//                String url  = rs.getString("url") ;
                System.out.println( "ID = " + id ) ;
                System.out.println( "urlid = " + urlid ) ;
                list.add(urlid);
//                System.out.println( "url = " + url ) ;
                System.out.println() ;
            }
            rs.close() ;
            stmt.close() ;
            conn.close() ;
        } catch ( Exception e ) {
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Operation done successfully") ;
        return list;
    }


    public int getSizeDB(){
        int counter = 0;
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
            conn.setAutoCommit(false) ;
            stmt = conn.createStatement() ;
            ResultSet rs = stmt.executeQuery( "SELECT * FROM IMAGE;" ) ;
            while ( rs.next() ) {
                counter++;
            }
            rs.close() ;
            stmt.close() ;
            conn.close() ;
        } catch ( Exception e ) {
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Operation done successfully") ;
        return counter;
    }

    public void removeDB(String publicId){
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
            conn.setAutoCommit(false) ;
            stmt = conn.createStatement() ;
            String sql = "DELETE from IMAGE where URLID='"+ publicId + "';";
            stmt.executeUpdate(sql) ;
            conn.commit() ;
            stmt.close() ;
            conn.close() ;

        } catch ( Exception e ) {
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Operation done successfully") ;

        showDB();
    }
}
