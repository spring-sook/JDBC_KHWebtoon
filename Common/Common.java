package WebtoonConsole.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Common {
    // DB 연결 정보
    final static String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    final static String ORACLE_ID = "KHWEBTOON";
    final static String ORACLE_PW = "1234";
    final static String ORACLE_DRV = "oracle.jdbc.driver.OracleDriver";

    // DB 연결
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(ORACLE_DRV);
            conn = DriverManager.getConnection(ORACLE_URL, ORACLE_ID, ORACLE_PW);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Connection 끊기
    public static void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    // Statement 끊기
    public static void close(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
    // ResultSet 끊기
    public static void close(ResultSet rset) {
        try {
            if (rset != null && !rset.isClosed()) {
                rset.close();
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
