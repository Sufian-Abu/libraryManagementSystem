
/**
 * This will host as the runtime for the new app.
 *
 * @author mr-mou
 * user & admin can list all books in the library  using method listBooks()
 * user can check out books using method chekoutBook()
 * user can return a book using method returnBook()
 * admin can register new user using method register()
 * admin can add new user books method addNewBook()
 * admin can get user info with method printUserInfo()
 * admin can print a report of checked out books by using method printReport()
 *
 */
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LibraryApp {
	private static final int CHECKING = 10;
	private static final int REGISTER = 0;
	private static String command;
	private static Scanner scanner;
	private static String uUserName;
	private static String uPassword;
	private static String uTypeOfUse;
	private static int uUserIndex;
	private static boolean usernameIsAvailable=true;
	private static ArrayList<User> users;
	private static ArrayList<Book> checkedOutBooks;
	private static ArrayList<Book> libraryBooks;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		System.out.println("LibraryApp started...\n type 'help' to show list of commands");
		JButton btn = new JButton();
		btn.setSize(20,20);
		libraryBooks = new ArrayList<Book>(0);
		users = new ArrayList<User>(0);
		checkedOutBooks = new ArrayList<Book>(0);

		for (int i=0; i<11; i++){
			Book book = new Book("book:"+i,null, null);
			libraryBooks.add(book);
		}

		register("user1" ,"userpass1", "user", REGISTER);
		register("user2" ,"userpass2", "user", REGISTER);
		register("admin1" ,"adminpass1", "admin", REGISTER);
       	login();
	}
	private static void adminStartScanning() {
		if (uTypeOfUse.equals("admin")) {
			command = scanner.nextLine();
			switch (command) {
				case "listbooks":

					listBooks();
					adminStartScanning();

					break;
				case "addnewbook":

					String name;
					System.out.println("What is the book mBookName?");
					name = scanner.nextLine();
					addNewBook(name);
					adminStartScanning();

					break;
				case "help":

					System.out.println(
							"listbooks     to list all books which remaining in the library "
									+ "\n" + "addnewbook    to add new book to the library"
									+ "\n" + "printreport   to print report of checked out books"
									+ "\n" + "register      to register new user"
									+ "\n" + "getuserinfo   to get info of user by username"
									+ "\n" + "logout        -"
					);
					adminStartScanning();

					break;
				case "printreport":

					printReport();
					adminStartScanning();

					break;
				case "register": {

					String username;
					String password = "";
					String typeofuse = "";
					System.out.println("register: enter new username");
					username = scanner.nextLine();
					register(username, password, typeofuse, CHECKING);
					if (usernameIsAvailable) {
						System.out.println("Register: enter new password");
						password = scanner.nextLine();

						System.out.println("Register: enter type of  user");
						typeofuse = scanner.nextLine();
						register(username, password, typeofuse, REGISTER);
					}
					adminStartScanning();

					break;
				}
				case "getuserinfo": {

					String username = scanner.nextLine();
					printUserInfo(username);
					adminStartScanning();


					break;
				}
				case "logout":

					System.out.println("logout succeed! ");
					System.out.println("\n");
					login();
					break;
				default:
					//if unknown command
					System.out.println(command + " is unknown command!");
					adminStartScanning();
					break;
			}
		}

	}

	private static void printUserInfo(String username) {
		int index = 0;
		for (int i =0; i<users.size(); i++) {
			if (users.get(i).getUsername().equals(username)) index=i;
		}
		ArrayList<Book> backpack = users.get(index).getBackpack();
		try {
			System.out.println(username+" checked out these books=");
			for (int i=0; i<backpack.size(); i++) {
                System.out.println((i + 1) + ") " + backpack.get(i).getBookName());
                i++;
            }
		} catch (NullPointerException e) {
			System.out.println(username+"  haven't checked out any books!");
		}
			System.out.println("Password: "+ users.get(index).getPassword() +"\n Type of user: "+ users.get(index).getRole());
	}

	public static void userStartScanning() {

		command = scanner.nextLine();
		switch (command) {
			case "listbooks":

				listBooks();
				userStartScanning();
				break;
			case "checkoutbook": {

				System.out.println("Please, enter the book name.");
				String theBook = scanner.nextLine();
				checkOutBook(theBook);
				userStartScanning();
				break;
			}
			case "returnbook": {

				System.out.println("Please, enter the book name.");
				String bookName=scanner.nextLine();
				returnBook(bookName);
				userStartScanning();
				break;
			}
			case "myinfo":

				ArrayList<Book> backpack = users.get(uUserIndex).getBackpack();
				try {
					if(!backpack.get(0).getBookName().isEmpty())System.out.println("You checked out those books=");
					else System.out.println("You haven't checked out any books");
					for (int i=0; i!=backpack.size();i++) {
						Book book =backpack.get(i);
						System.out.println((i + 1) + ") " + book.getBookName()+" at:"+book.getCheckedOutDate());
					}
				} catch (NullPointerException | IndexOutOfBoundsException e) {
					System.out.println("You haven't checked out any books");
				}

				userStartScanning();


				break;
			case "logout":

				System.out.println("logout succeed! ");
				System.out.println("\n");
				login();
				break;
			case "help":
				System.out.println(
						"listbooks     to list all books in the library " + "\n" + "checkoutbook  to checkout a book from library"
								+ "\n" + "returnbook  to return checked out book to library" + "\n" + "myinfo  to get info about checked out books"
				);
				userStartScanning();

				break;
			default:
				//if unknown command
				System.out.println(command + " is unknown command!");
				userStartScanning();
				break;
		}

	}

	public static void listBooks() {
		int counter = 0;
		while (counter != libraryBooks.size()) {
			System.out.println((counter + 1) + ") " + libraryBooks.get(counter).getBookName());
			counter++;
		}
	}

	private static void checkOutBook(String bookName) {
			Integer bookIndex=null;
		for(int i=0; i<libraryBooks.size(); i++){
			if(libraryBooks.get(i).getBookName().equals(bookName))bookIndex=i;
		}

		try {
			Book book = libraryBooks.get(bookIndex);
			System.out.println("found the Book!");
			libraryBooks.remove(book);
			book.setBorrowerName(uUserName);
			book.setCheckedOutDate(currentTime());
			ArrayList<Book> backpack = users.get(uUserIndex).getBackpack();
			User user =new User(uUserName, uPassword, uTypeOfUse, backpack);
			try {
				backpack.add(book);
				user.setBackpack(backpack);
			} catch (NullPointerException e) {
				backpack=new ArrayList<Book>(0);
				backpack.add(book);
				user.setBackpack(backpack);
			}
			users.set(uUserIndex, user);
			checkedOutBooks.add(book);
			System.out.println("process of check out completed!");
			System.out.println("You checked out those books=");
			int counter=0;
			while (counter != backpack.size()) {
				System.out.println((counter + 1) + ") " + backpack.get(counter).getBookName());
				counter++;
			}
			System.out.println("library remaining books:");
			listBooks();
		} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
			System.out.println("book not found!");
		}
	}

	private static void addNewBook(String bookName) {
		Book book = new Book(bookName, null, null);
		if (libraryBooks.contains(book)){
			System.out.print("Book already exists!");
		}else {
			libraryBooks.add(book);
			System.out.println(book.getBookName() + " Successfully added to the library!");
		}
	}

	private static void returnBook(String bookName) {

		ArrayList<Book> backpack = users.get(uUserIndex).getBackpack();
		Integer bookIndex=null;
		for(int i=0; i!=backpack.size(); i++){
			if(backpack.get(i).getBookName().equals(bookName))bookIndex=i;
		}
		try {
			Book book = backpack.get(bookIndex);
			backpack.remove(book);
			users.get(uUserIndex).setBackpack(backpack);
			book.setBorrowerName(null);
			book.setCheckedOutDate(null);
			libraryBooks.add(book);
			//get the object by index then remove
			int counter = 0;
			while (counter < checkedOutBooks.size()) {
				if (bookName.equals(checkedOutBooks.get(counter).getBookName())) {
					checkedOutBooks.remove(counter);
				}
				counter++;
			}
		} catch (NullPointerException e) {
			System.out.println("You haven't check out this book!");
		}

	}

	private static void login() {
		System.out.println("Please enter your Username..");
		uUserName = scanner.nextLine();
		System.out.println("Please enter your Password..");
		uPassword = scanner.nextLine();
		int counter = 0;
		while (counter < users.size()) {
			if (users.get(counter).getUsername().equals(uUserName)) {
				System.out.println("found uUserName at index " + counter);
				uUserIndex = counter;
			}
			counter++;
		}
		if (uUserName.equals(users.get(uUserIndex).getUsername()) && uPassword.equals(users.get(uUserIndex).getPassword())) {
			uTypeOfUse = users.get(uUserIndex).getRole();
			System.out.println("login success! welcome " + uTypeOfUse + " " + uUserName);
			if (uTypeOfUse.equals("user")) {
				userStartScanning();
			} else adminStartScanning();
		} else {
			System.out.println("Username or password is incorrect! please try again");
			login();
		}
	}

	private static void printReport(){
		System.out.println("Today is: "+currentTime());
		for (Book book : checkedOutBooks) {

			String details= "BookName:"+book.getBookName()+" User:"+book.getBorrowerName()+" CheckedOutOn:"+book.getCheckedOutDate();
			System.out.println(details);
		}
	}

	private static void register(String username, String password, String typeofuse, int mode){
		if (mode == CHECKING || mode == REGISTER ) {
			int c=0;
			while(( c < users.size()&& usernameIsAvailable)) {
				if (users.get(c).getUsername().equals(username)) {
					usernameIsAvailable = false;
					System.out.println("username is already exist!");
				}
				c++;
			}
		}
		if (mode == REGISTER ) {
			if (usernameIsAvailable) {
				User o =new User(username, password,typeofuse ,null);
				o.setUsername(username);
				o.setPassword(password);
				o.setRole(typeofuse);
				users.add(o);
				System.out.println(username+" Registered successfully!");
			}
		}
	}

	private static String currentTime(){

		long timeStamp =System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd h:m a");
		return format1.format(calendar.getTime());
	}
}
