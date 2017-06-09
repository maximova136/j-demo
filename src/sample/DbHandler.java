package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
            stmt.execute("CREATE TABLE if not exists 'IMAGE' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'urlid' text, 'url' text);");
        } catch (Exception e){
            System.err.println( e.getClass() .getName() + ": " + e.getMessage() );
            System.exit(0) ;
        }
        System.out.println("Table exists or created successfully") ;
    }

    public void writeDB(String urlid, String url)  {
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
            conn.setAutoCommit(false) ;
            System.out.println("Opened database successfully") ;

            stmt = conn.createStatement() ;
            String sql = "INSERT INTO IMAGE (URLID, URL) " +
                    "VALUES ('" + urlid + "', '"+ url +"');";
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

    public void showDB(){
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
                String  name = rs.getString("urlid") ;
                String age  = rs.getString("url") ;
                System.out.println( "ID = " + id ) ;
                System.out.println( "urlid = " + name ) ;
                System.out.println( "url = " + age ) ;
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
    }

    public void removeDB(int number){
        try {
            Class.forName("org.sqlite.JDBC") ;
            conn = DriverManager.getConnection("jdbc:sqlite:DataBase.db") ;
            conn.setAutoCommit(false) ;
            stmt = conn.createStatement() ;
            String sql = "DELETE from IMAGE where ID="+ number + ";";
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
