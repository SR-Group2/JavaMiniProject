import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionManager {
	private static Connection con;
	private ConnectionManager(){};
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/dbproduct";
		if(con == null)
			con = DriverManager.getConnection(url, "seyha" , "");
		return con;
	}
}
