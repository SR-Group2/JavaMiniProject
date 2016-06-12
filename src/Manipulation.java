import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

public class Manipulation extends Thread{
	static Connection con;
	static ArrayList<Stock> tempData = new ArrayList<Stock>();
	static int limit = 5;
	static int id;
	Scanner scan = new Scanner(System.in);
	ResultSet rs;
	
	public int checkInt(String str){
		 Scanner sc = new Scanner(System.in);
		 int number;
		    do {
		        System.out.print(str);
		        while (!sc.hasNextInt()) {
		            System.out.println("That's not a number!");
		            System.out.print(str);
		            sc.next(); // this is important!
		        }
		        number = sc.nextInt();
		    } while (number <= 0);
		   return number;
	}
	
	public double checkDouble(String str){
		 Scanner sc = new Scanner(System.in);
		    double number;
		    do {
		        System.out.print(str);
		        while (!sc.hasNextDouble()) {
		            System.out.println("That's not a number!");
		            System.out.print(str);
		            sc.next(); // this is important!
		        }
		        number = sc.nextDouble();
		    } while (number <= 0);
		   return number;
	}
	
	public String checkString(String info){
		String str;
		boolean b = true, isString = false;
		while (b) {
			System.out.print(info);
			scan = new Scanner(System.in);
			str = scan.next();
			isString = str.matches("\\d+");
			if (isString == true) {
				System.out.println("Please input only string value!");
				b = true;
			}
			else{
				b = false;
				return str;
			}
		}
		
		return "";
	}
	
	public static void smartSave(String info) throws IOException, ClassNotFoundException, SQLException{
		Scanner scan = new Scanner(System.in);
		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream("file/stock.txt")))) {

			ArrayList<Stock> readData = (ArrayList<Stock>) ois.readObject();
			if (!readData.isEmpty()) {
				System.out.print("Do you want to " + info + "  ? Y | N > ");
				String confirm = scan.next().toLowerCase();
				if (confirm.equals("y")) {
					Manipulation.tempData = readData;
					new Manipulation().saveToData(confirm);
				} else {
					File file = new File("file/stock.txt");
					file.delete();
				}
			}

		} catch (FileNotFoundException e) {

		}
	}
	
	public Manipulation() throws ClassNotFoundException, SQLException {
		con = ConnectionManager.getConnection();
	
	}
	
	public int backup() throws SQLException{
		
//		SimpleDateFormat sdfDate = new SimpleDateFormat("dd_MM_yyyy_h:mm:ss");
//		Date now = new Date();
//		String strDate = sdfDate.format(now);
		
//		int results = stmtd.executeUpdate();
//		PreparedStatement stmt =  con.prepareStatement("SELECT * INTO temp FROM tblproduct");
//		int result = 0;
//		result = stmt.executeUpdate();
//		return result;
		
		dropTable("DROP TABLE temp");
		cloneTable("SELECT * INTO temp FROM tblproduct");
		
		return 1;
		
	}
	
	public void dropTable(String sql) throws SQLException{

		PreparedStatement stmtd =  con.prepareStatement(sql);
		stmtd.executeUpdate();
	}
	
	public void cloneTable(String sql) throws SQLException{

		PreparedStatement stmtd =  con.prepareStatement(sql);
		stmtd.executeUpdate();
	}
	public int restore() throws SQLException{
		dropTable("DROP TABLE tblproduct");
		cloneTable("SELECT * INTO tblproduct FROM temp");
		return 1;
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
			//System.out.println("Product ID: " + id);
		} else {
			id = getLastID() + 1;
			//System.out.println("Product ID: " + id);
		}
//		System.out.print("Input Product Name > ");
//		String name = scan.next();
		String name = this.checkString("Input Product Name > " );
		//System.out.print("Input Price > ");
		double price = this.checkDouble("Input Price > ");
		//System.out.print("Input Product Quantity > ");
		int qty = this.checkInt("Input Product Quantity > ");
		tempData.add(new Stock(id, name, price, qty, strDate));
		
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream("file/stock.txt")));
	    oos.writeObject(tempData);
	    oos.close();

	}

	public void saveToData(String confirm) throws SQLException {
		
		
		if (confirm.equals("y")) {
			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO tblproduct(p_id,p_name,p_unitprice,p_qty,p_date) VALUES(?,?,?,?,?)");
			for(Stock product : Manipulation.tempData){
				stmt.setInt(1, product.getID());
				stmt.setString(2, product.getPname());
				stmt.setDouble(3, product.getpUnitPrice());
				stmt.setInt(4, product.getpQty());
				stmt.setString(5, product.getpDate());
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
