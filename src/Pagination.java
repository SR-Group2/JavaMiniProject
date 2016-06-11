import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pagination {
	Connection con;
	static int offset = 0;
//	public static void main(String[] args) throws ClassNotFoundException, SQLException{
//		if(new Pagination().nextPage() != null){
//			ResultSet rs = new Pagination().nextPage();
//			while(rs.next()){
//				System.out.println(rs.getString("p_name"));
//			}
//		}
//	}
	
	
	public Pagination() throws ClassNotFoundException, SQLException{
		 con = ConnectionManager.getConnection();
	}
	public ResultSet nextPage() throws SQLException{
	
		ResultSet rs;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM tblproduct ORDER BY p_id DESC LIMIT " + Manipulation.limit + " OFFSET " + offset);
		if(rs != null)
			return rs;
		return null;
	}
	public void previusPage(){
		
	}
	public void firstPage(){
		
	}
	public ResultSet lastPage(int lastPage) throws SQLException{
		int last = lastPage - Manipulation.limit;
		ResultSet rs;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM tblproduct ORDER BY p_id DESC LIMIT " + Manipulation.limit + " OFFSET " + last);
		if(rs != null)
			return rs;
		return null;
	}
	public ResultSet goToPage(int page) throws SQLException{
		int goPage = (Manipulation.limit * page) - Manipulation.limit;
		ResultSet rs;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM tblproduct ORDER BY p_id DESC LIMIT " + Manipulation.limit + " OFFSET " + goPage);
		if(rs != null)
			return rs;
		return null;
	}
}
