import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Maintenance {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
//		
//		Connection con = ConnectionManager.getConnection();
//		String sql = "select * into tempProduct from tblproduct";
//		PreparedStatement stmt =  con.prepareStatement(sql);
//		stmt.executeUpdate();
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd_MM_yyyy_h:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		System.out.println(strDate);
	}

}
