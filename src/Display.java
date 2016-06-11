
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class Display {
	static Manipulation mp;
	Pagination pg;
	ResultSet rs;
	static int perPage = 1;
	static int rmPage = 0;
	Scanner scan = new Scanner(System.in);
	
	Table t = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

	public Display() throws ClassNotFoundException, SQLException {
		mp = new Manipulation();
		pg = new Pagination();
	}

	// Cannot static data type
	public void getAllProducts() throws SQLException, ClassNotFoundException, IOException {
		Display.perPage = 1;
		
		CellStyle numberStyle = new CellStyle(HorizontalAlign.center);
		t.setColumnWidth(0, 15, 30);
		t.setColumnWidth(1, 15, 30);
		t.setColumnWidth(2, 15, 30);
		t.setColumnWidth(3, 15, 30);
		t.setColumnWidth(4, 15, 30);

		t.addCell("Product ID", numberStyle);
		t.addCell("Product name", numberStyle);
		t.addCell("Unit Price", numberStyle);
		t.addCell("Quantity", numberStyle);
		t.addCell("Imported Date", numberStyle);
		// Fetch Data from Arraylist if they have
		if (!(Manipulation.tempData.isEmpty())) {
			Collections.reverse(Manipulation.tempData);
			for (Stock product : Manipulation.tempData) {
				t.addCell("" + product.getID(), numberStyle);
				t.addCell("" + product.getPname(), numberStyle);
				t.addCell("" + product.getpUnitPrice(), numberStyle);
				t.addCell("" + product.getpQty(), numberStyle);
				t.addCell("" + product.getpDate(), numberStyle);
			}
		}
		// Fetch Data from database
		rs = mp.getProductPerPage();
		while (rs.next()) {
			t.addCell("" + rs.getInt("p_id"), numberStyle);
			t.addCell("" + rs.getString("p_name"), numberStyle);
			t.addCell("" + rs.getDouble("p_unitprice"), numberStyle);
			t.addCell("" + rs.getInt("p_qty"), numberStyle);
			t.addCell("" + rs.getString("p_date"), numberStyle);
		}
		t.addCell("Page: " + Display.perPage + "/" + mp.getNumRows() / Manipulation.limit, numberStyle, 3);
		t.addCell("Total Records: " + mp.getNumRows(), numberStyle, 2);
		System.out.println(t.render());
	}

	// Display data from result search
	public void getSearchData() throws SQLException, ClassNotFoundException {
		Display.perPage = 1;
		Table t = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
		CellStyle numberStyle = new CellStyle(HorizontalAlign.center);

		t.setColumnWidth(0, 15, 30);
		t.setColumnWidth(1, 15, 30);
		t.setColumnWidth(2, 15, 30);
		t.setColumnWidth(3, 15, 30);
		t.setColumnWidth(4, 15, 30);

		t.addCell("Product ID", numberStyle);
		t.addCell("Product name", numberStyle);
		t.addCell("Unit Price", numberStyle);
		t.addCell("Quantity", numberStyle);
		t.addCell("Imported Date", numberStyle);
		// Fetch Data from database
		rs = mp.search();

		while (rs.next()) {
			t.addCell("" + rs.getInt("p_id"), numberStyle);
			t.addCell("" + rs.getString("p_name"), numberStyle);
			t.addCell("" + rs.getDouble("p_unitprice"), numberStyle);
			t.addCell("" + rs.getInt("p_qty"), numberStyle);
			t.addCell("" + rs.getString("p_date"), numberStyle);

		}
		t.addCell("Page: " + Display.perPage + "/" + mp.getNumRows() / Manipulation.limit, numberStyle, 3);
		t.addCell("Total Records: " + mp.getNumRows(), numberStyle, 2);
		System.out.println(t.render());
	}

	public void getNextPage() throws SQLException {

		Table t = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
		CellStyle numberStyle = new CellStyle(HorizontalAlign.center);

		t.setColumnWidth(0, 15, 30);
		t.setColumnWidth(1, 15, 30);
		t.setColumnWidth(2, 15, 30);
		t.setColumnWidth(3, 15, 30);
		t.setColumnWidth(4, 15, 30);

		t.addCell("Product ID", numberStyle);
		t.addCell("Product name", numberStyle);
		t.addCell("Unit Price", numberStyle);
		t.addCell("Quantity", numberStyle);
		t.addCell("Imported Date", numberStyle);

		int remainPage = mp.getNumRows() % Manipulation.limit;
		rmPage = remainPage;

		// Fetch Data from database
		rs = pg.nextPage();
		while (rs.next()) {
			t.addCell("" + rs.getInt("p_id"), numberStyle);
			t.addCell("" + rs.getString("p_name"), numberStyle);
			t.addCell("" + rs.getDouble("p_unitprice"), numberStyle);
			t.addCell("" + rs.getInt("p_qty"), numberStyle);
			t.addCell("" + rs.getString("p_date"), numberStyle);
		}

		t.addCell("Page: " + perPage + "/" + mp.getNumRows() / Manipulation.limit, numberStyle, 3);
		t.addCell("Total Records: " + mp.getNumRows(), numberStyle, 2);
		System.out.println(t.render());
	}

	public void getLastPage() throws SQLException {
		Table t = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
		CellStyle numberStyle = new CellStyle(HorizontalAlign.center);

		t.setColumnWidth(0, 15, 30);
		t.setColumnWidth(1, 15, 30);
		t.setColumnWidth(2, 15, 30);
		t.setColumnWidth(3, 15, 30);
		t.setColumnWidth(4, 15, 30);

		t.addCell("Product ID", numberStyle);
		t.addCell("Product name", numberStyle);
		t.addCell("Unit Price", numberStyle);
		t.addCell("Quantity", numberStyle);
		t.addCell("Imported Date", numberStyle);
		// Fetch Data from database
		int remainPage = mp.getNumRows() % Manipulation.limit;

		rs = pg.lastPage(mp.getNumRows() - remainPage);
		while (rs.next()) {
			t.addCell("" + rs.getInt("p_id"), numberStyle);
			t.addCell("" + rs.getString("p_name"), numberStyle);
			t.addCell("" + rs.getDouble("p_unitprice"), numberStyle);
			t.addCell("" + rs.getInt("p_qty"), numberStyle);
			t.addCell("" + rs.getString("p_date"), numberStyle);
		}

		if (remainPage > 0) {
			rs = Manipulation.getAllProducts(remainPage);
			ArrayList<Stock> arr = new ArrayList<Stock>();
			while (rs.next()) {
				arr.add(new Stock(rs.getInt("p_id"), rs.getString("p_name"), rs.getDouble("p_unitprice"),
						rs.getInt("p_qty"), rs.getString("p_date")));
			}
			Collections.reverse(arr);
			for (Stock p : arr) {
				t.addCell("" + p.getID(), numberStyle);
				t.addCell("" + p.getPname(), numberStyle);
				t.addCell("" + p.getpUnitPrice(), numberStyle);
				t.addCell("" + p.getpQty(), numberStyle);
				t.addCell("" + p.getpDate(), numberStyle);
			}
		}

		t.addCell("Page: " + perPage + "/" + mp.getNumRows() / Manipulation.limit, numberStyle, 3);
		t.addCell("Total Records: " + mp.getNumRows(), numberStyle, 2);
		System.out.println(t.render());
	}

	// Deleting data
	public void doDelete() throws SQLException, ClassNotFoundException, IOException {
		System.out.print("Input Product ID > ");
		Scanner scan = new Scanner(System.in);
		int delete = scan.nextInt();
		System.out.print("Do you really want to delete? Y | N ");
		String confirm = scan.next().toLowerCase();
		if (confirm.equals("y")) {
			if (mp.delete(delete)) {
				System.out.println("Deleted Successfully!");
			}
		}

		new Display().getAllProducts();
	}

	// Updating Data
	public void doUpdate() throws SQLException, ClassNotFoundException, IOException {

		ArrayList<Stock> arr = new ArrayList<Stock>();
		int id;
		System.out.print("Input Product ID > ");
		id = scan.nextInt();
		rs = mp.searchID(id);
		while (rs.next()) {
			arr.add(new Stock(rs.getInt("p_id"), rs.getString("p_name"), rs.getDouble("p_unitprice"),
					rs.getInt("p_qty"), rs.getString("p_date")));
		}
		System.out.println("How many fields do you want to update");
		System.out.println();
		System.out.println("A/ All" + "\t" + "B/ Product Name" + "\t" + "C/ Unit Price" + "\t" + "D/ Qauntiy" + "\t" + "E/ Exit");
		System.out.println();
		
		// ====== store value ==============
		String choose, pname;
		double pprice;
		int qty;

		System.out.print("Choose Your Options > ");
		choose = scan.next().toLowerCase();
		if (choose.equals("a")) {
			System.out.print("Product Name > ");
			pname = scan.next();
			System.out.print("Product Price > ");
			pprice = scan.nextDouble();
			System.out.print("Quantity > ");
			qty = scan.nextInt();
			for(Stock update : arr){
				update.setPname(pname);
				update.setpUnitPrice(pprice);
				update.setpQty(qty);
			}
			
		} else if (choose.equals("b")) {
			System.out.print("Product Name > ");
			pname = scan.next();
			for(Stock update : arr){
				update.setPname(pname);
			}
			
		} else if(choose.equals("c")){
			System.out.print("Product Price > ");
			pprice = scan.nextDouble();
			for(Stock update : arr){
				update.setpUnitPrice(pprice);
			}
		} else if(choose.equals("d")){
			System.out.print("Quantity > ");
			qty = scan.nextInt();
			for(Stock update : arr){
				update.setpQty(qty);
			}
		}else if(choose.equals("e")){
			new Display().getAllProducts();
			return ;
		}else{
			System.out.println("Incorrect Syntax!");
		}
		
		System.out.print("Do you really want to delete? Y | N ");
		String confirm = scan.next().toLowerCase();
		if (confirm.equals("y")) {
			if (mp.update(arr)) {
				System.out.println("Updated Successfully!");
			}
		}
		new Display().getAllProducts();
	}
	
	public void doBackUp() throws SQLException{
		System.out.print("Do you really want to delete? Y | N ");
		String confirm = scan.next().toLowerCase();
		if (confirm.equals("y")) {
			if (mp.backup() > 0) {
				System.out.println("Updated Successfully!");
			}
		}
	}
	public void getGoToPage() throws SQLException {

		int goPage = scan.nextInt();
		int total = mp.getNumRows() / Manipulation.limit;
		int remainPage = mp.getNumRows() % Manipulation.limit;
		if (goPage > total || goPage < 1) {
			System.out.println("Your Page not existing!");
		} else {

			rs = pg.goToPage(goPage);
			Table t = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
			CellStyle numberStyle = new CellStyle(HorizontalAlign.center);

			t.setColumnWidth(0, 15, 30);
			t.setColumnWidth(1, 15, 30);
			t.setColumnWidth(2, 15, 30);
			t.setColumnWidth(3, 15, 30);
			t.setColumnWidth(4, 15, 30);

			t.addCell("Product ID", numberStyle);
			t.addCell("Product name", numberStyle);
			t.addCell("Unit Price", numberStyle);
			t.addCell("Quantity", numberStyle);
			t.addCell("Imported Date", numberStyle);
			while (rs.next()) {
				t.addCell("" + rs.getInt("p_id"), numberStyle);
				t.addCell("" + rs.getString("p_name"), numberStyle);
				t.addCell("" + rs.getDouble("p_unitprice"), numberStyle);
				t.addCell("" + rs.getInt("p_qty"), numberStyle);
				t.addCell("" + rs.getString("p_date"), numberStyle);
			}
			// Check remain page or not
			if (goPage == total) {
				if (remainPage > 0) {
					rs = Manipulation.getAllProducts(remainPage);
					ArrayList<Stock> arr = new ArrayList<Stock>();
					while (rs.next()) {
						arr.add(new Stock(rs.getInt("p_id"), rs.getString("p_name"), rs.getDouble("p_unitprice"),
								rs.getInt("p_qty"), rs.getString("p_date")));
					}
					Collections.reverse(arr);
					for (Stock p : arr) {
						t.addCell("" + p.getID(), numberStyle);
						t.addCell("" + p.getPname(), numberStyle);
						t.addCell("" + p.getpUnitPrice(), numberStyle);
						t.addCell("" + p.getpQty(), numberStyle);
						t.addCell("" + p.getpDate(), numberStyle);
					}
				}
			}
			Display.perPage = goPage;
			t.addCell("Page: " + Display.perPage + "/" + mp.getNumRows() / Manipulation.limit, numberStyle, 3);
			t.addCell("Total Records: " + mp.getNumRows(), numberStyle, 2);
			System.out.println(t.render());
		}
	}

	public static void showMenu() {

		Table t = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.SURROUND);
		CellStyle numberStyle = new CellStyle(HorizontalAlign.center);

		t.setColumnWidth(0, 80, 120);

		t.addCell(" *)Display | W)rite | R)ead | U)pdate | D)elete | F)irst | N)ext | P)revious | " + "L)ast ",
				numberStyle);
		t.addCell(" ");
		t.addCell("S)earch | G)o to | Se)t row | Sa)ve | B)ack up | Re)store | H)elp |" + " E)xit", numberStyle);

		System.out.println(t.render());
		System.out.print("options > ");
	}

	public static void selectMenu(String input) throws ClassNotFoundException, SQLException, IOException {
		switch (input.toLowerCase()) {
		case "*":
			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "w":
			mp.saveToCollection();
			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "r":
			if (mp.readData() == false) {
				System.out.println("Not Found");
			}
			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "u":
			new Display().doUpdate();
			Display.showMenu();
			break;
		case "d":
			new Display().doDelete();
			Display.showMenu();
			break;
		case "f":
			Pagination.offset = 0;
			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "n":
			if (Pagination.offset >= mp.getNumRows() - Manipulation.limit) {
				Pagination.offset = 0;
				Display.perPage = 1;
			} else {
				Display.perPage += 1;
				Pagination.offset = Manipulation.limit + Pagination.offset;
			}
			new Display().getNextPage();
			Display.showMenu();
			break;
		case "p":

			if (Pagination.offset > mp.getNumRows() - Manipulation.limit) {
				Pagination.offset = 0;
				Display.perPage = 1;
			} else if (Pagination.offset > 0) {
				System.out.println(Pagination.offset);
				Display.perPage -= 1;
				Pagination.offset = Pagination.offset - Manipulation.limit;
			} else if (Pagination.offset == 0) {
				Pagination.offset = mp.getNumRows() - Manipulation.limit;
				Display.perPage = mp.getNumRows() / Manipulation.limit;
			}

			new Display().getNextPage();
			Display.showMenu();
			break;
		case "l":
			Display.perPage = mp.getNumRows() / Manipulation.limit;
			new Display().getLastPage();
			Display.showMenu();
			Pagination.offset = mp.getNumRows() - Manipulation.limit;
			break;
		case "s":
			new Display().getSearchData();
			Display.showMenu();
			break;
		case "g":
			System.out.print("Go To Page > ");
			new Display().getGoToPage();
			Display.showMenu();
			break;
		case "se":
			System.out.print("Set Row > ");
			Scanner enter = new Scanner(System.in);
			int row = enter.nextInt();
			Manipulation.limit = row;

			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "b":
			new Display().doBackUp();
			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "re":
			System.out.println("restore");
			break;
		case "h":
			System.out.println("help");
			break;
		case "sa":
			Scanner scan = new Scanner(System.in);
			System.out.print("Do you want to save? y | n ? > ");
			String confirm = scan.next();
			mp.saveToData(confirm);
			new Display().getAllProducts();
			Display.showMenu();
			break;
		case "e":
			System.out.println("Thank for your using my app....");
			System.exit(0);
			break;
		default:
			System.out.println("Incorrect Syntax!");
		}
	}
}
