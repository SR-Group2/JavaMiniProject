import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Manipulation {
	static Connection con;
	static ArrayList<Stock> tempData = new ArrayList<Stock>();
	static int limit = 5;
	static int id;
	Scanner scan = new Scanner(System.in);
	ResultSet rs;
	
	public Manipulation() throws ClassNotFoundException, SQLException {
		con = ConnectionManager.getConnection();
	
	}
	
	public int backup() throws SQLException{
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd_MM_yyyy_h:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String sql = "SELECT * INTO "+ strDate+" FROM tblproduct";
		PreparedStatement stmt =  con.prepareStatement(sql);
		int result = stmt.executeUpdate();
		return result;
	}
	public void restore(){
		
	}
	public static ResultSet getAllProducts(int remain) throws SQLException{
		ResultSet rs;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM tblproduct ORDER BY p_id LIMIT " + remain);
		return rs;
	}
	public ResultSet getProductPerPage() throws ClassNotFoundException, SQLException {
		ResultSet rs;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM tblproduct ORDER BY p_id DESC LIMIT " + limit);
		return rs;
	}

	public int getNumRows() throws SQLException {
		ResultSet rs;
		int rows = 0;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT COUNT(*) AS numRow FROM tblproduct");
		while (rs.next()) {
			rows = rs.getInt("numRow");
		}
		return rows;
	}

	public static int getLastID() throws SQLException {
		ResultSet rs;
		int rows = 0;
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT Max(p_id) AS lastID FROM tblproduct");
		while (rs.next()) {
			rows = rs.getInt("lastID");
		}
		return rows;
	}

	public void saveToCollection() throws SQLException, IOException {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
		Date now = new Date();
		String strDate = sdfDate.format(now);

		System.out.println();
		// Checking product id
		if (id > getLastID()) {
			++id;
			System.out.println("Product ID: " + id);
		} else {
			id = getLastID() + 1;
			System.out.println("Product ID: " + id);
		}
		System.out.print("Input Product Name > ");
		String name = scan.next();
		System.out.print("Input Price > ");
		double price = scan.nextDouble();
		System.out.print("Input Product Quantity > ");
		int qty = scan.nextInt();
		tempData.add(new Stock(id, name, price, qty, strDate));
		
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream("file/stock.txt")));
	    oos.writeObject(tempData);
	    oos.close();

	}

	public void saveToData(String confirm) throws SQLException {
		
		
		if (confirm.equals("y")) {
			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO tblproduct(p_name,p_unitprice,p_qty,p_date) VALUES(?,?,?,?)");
			for(Stock product : Manipulation.tempData){
				stmt.setString(1, product.getPname());
				stmt.setDouble(2, product.getpUnitPrice());
				stmt.setInt(3, product.getpQty());
				stmt.setString(4, product.getpDate());
				stmt.addBatch();
			}
			stmt.executeBatch();
			stmt.close();
			Manipulation.tempData.clear();
		} else {
			System.out.println("your products are not saved!");
		}
		
		File file = new File("file/stock.txt");
		file.delete();
	}
	
	public boolean readData() throws SQLException{
		System.out.print("Enter your Product ID > ");
		int id = scan.nextInt();
		if(!Manipulation.tempData.isEmpty()){
			for (Stock read : Manipulation.tempData) {
				if (id == read.getID()) {
					System.out.println("================== Display Specific Data ================== ");
					System.out.println("Product iD > " + read.getID());
					System.out.println("Product Name > " + read.getPname());
					System.out.println("Qauntity > " + read.getpQty());
					System.out.println("Price > " + read.getpUnitPrice());
					System.out.println("Imported Date > " + read.getpDate());
					System.out.println("=========================================================== ");
					return true;
				}
			}
		}
		Statement stmt = con.createStatement();
		ResultSet search = stmt.executeQuery("SELECT COUNT(p_id) AS productid FROM tblproduct WHERE p_id = " + id);
		int result =0;
		while(search.next()){
			result = search.getInt("productid");
		}
		if(result > 0){
			
			Statement get = con.createStatement();
			rs = get.executeQuery("SELECT * FROM tblproduct WHERE p_id =" + id);
			while(rs.next()){
				System.out.println("================== Display Specific Data ================== ");
				System.out.println("Product iD > " + rs.getInt(1));
				System.out.println("Product Name > " + rs.getString(2));
				System.out.println("Qauntity > " + rs.getString(3));
				System.out.println("Price > " + rs.getString(4));
				System.out.println("Imported Date > " +rs.getString(5));
				System.out.println("=========================================================== ");
			}
			
			return true;
		}
		return false;
	}
	
	public ResultSet search() throws SQLException{
		System.out.print("Input Product Name > ");
		String find = scan.next().toLowerCase();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM tblproduct WHERE LOWER(p_name) LIKE '%" + find + "%'" + "  ORDER BY p_id DESC LIMIT " + Manipulation.limit);
		return rs;
	}
	
	public boolean delete(int id) throws SQLException{
		
		PreparedStatement stmt = con.prepareStatement("DELETE FROM tblproduct WHERE p_id = " + id);
		if( stmt.executeUpdate() > 0)
			return true;
		return false;
	}
	
	public boolean update(ArrayList<Stock> rs) throws SQLException{
		String sql="";
		for(Stock st: rs){
			sql = "UPDATE tblproduct set p_name ='" + st.getPname()  +"', p_unitprice ='"+ st.getpUnitPrice() + 
					"',p_qty ='" + st.getpQty() +
					"',p_date='"+ st.getpDate() +"' WHERE p_id = '" + st.getID() + "'";
		}
		
		PreparedStatement stmt = con.prepareStatement(sql);
		if( stmt.executeUpdate() > 0)
			return true;
		return false;
	}
	
	public ResultSet searchID(int id) throws SQLException{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM tblproduct WHERE p_id = " + id);
		if(rs != null)
			return rs;
		return null;
	}

}
