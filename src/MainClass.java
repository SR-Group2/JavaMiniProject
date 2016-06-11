import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
		Date now = new Date();
		String strDate = sdfDate.format(now);

		Connection con = ConnectionManager.getConnection();
		// Insert Record Test
		// PreparedStatement stmt = con
		// .prepareStatement("INSERT INTO tblproduct(p_name,
		// p_unitprice,p_qty,p_date) values(?,?,?,?)");
		// for (int i = 0; i < 1000; i++) {
		// stmt.setString(1, "MacBook");
		// stmt.setDouble(2, 1400);
		// stmt.setInt(3, 10);
		// stmt.setString(4, strDate);
		// stmt.addBatch();
		// }
		// stmt.executeBatch();
		
		Scanner scan = new Scanner(System.in);
		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream("file/stock.txt")))) {

			ArrayList<Stock> readData = (ArrayList<Stock>) ois.readObject();
			if (!readData.isEmpty()) {
				System.out.print("Do you want to recovery ? Y | N > ");
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
		new Display().getAllProducts();
		Display.showMenu();
		Scanner input;
		String menu;
		boolean b = true;
		while (b) {
			input = new Scanner(System.in);
			menu = input.next();
			if (menu == "e") {
				b = false;
			}
			Display.selectMenu(menu);// options selected men

		}
		con.close();
	}

}
