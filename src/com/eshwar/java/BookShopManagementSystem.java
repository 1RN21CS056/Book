package com.eshwar.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookShopManagementSystem 
{

    private static final String DB_URL = "jdbc:mysql://localhost/bookshopmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@1eshWAR";

    private static final String ADD_BOOK_QUERY = "INSERT INTO bookshop (Book_title, Book_Author, Book_price) VALUES (?, ?, ?)";
    private static final String SEARCH_BOOK_QUERY = "SELECT * FROM bookshop WHERE Book_title = ?";
    private static final String UPDATE_BOOK_QUERY = "UPDATE bookshop SET Book_Author = ?, Book_price = ? WHERE Book_title = ?";
    private static final String DELETE_BOOK_QUERY = "DELETE FROM bookshop WHERE Book_title = ?";
    private static final String GET_ALL_BOOKS_QUERY = "SELECT * FROM bookshop";


    public static void main(String[] args) 
    {

        Scanner input = new Scanner(System.in);

        while (true)
        {
        	System.out.println("******* WELCOME TO BOOK SHOP MANAGEMENT SYSTEM *******");
        	System.out.println("Enter your choice:");
        	System.out.println("1. Add book");
        	System.out.println("2. Search book");
        	System.out.println("3. Update book");
        	System.out.println("4. Delete book");
        	System.out.println("5. Generate report");
        	System.out.println("6. Exit");
        	int choice = input.nextInt();

        	switch (choice) {
        	    case 1:
        	        addBook(input);
        	        break;
        	    case 2:
        	        searchBook(input);
        	        break;
        	    case 3:
        	        updateBook(input);
        	        break;
        	    case 4:
        	        deleteBook(input);
        	        break;
        	    case 5:
        	        generateReport();
        	        break;
        	    case 6:
        	        System.out.println("------Thank you for using the Book Shop Management System!------");
        	        System.exit(0);
        	        break;
        	    default:
        	        System.out.println("Invalid choice");
        	}

        }
    }

    public static void addBook(Scanner input) 
    {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(ADD_BOOK_QUERY)) 
        {
            System.out.println("Enter book title:");
            String title = input.next();
            System.out.println("Enter book author:");
            String author = input.next();
            System.out.println("Enter book price:");
            double price = input.nextDouble();

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setDouble(3, price);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) 
            {
                System.out.println("+++++++++ BOOK ADDED SUCCESSFULLY +++++++++");
            } else 
            {
                System.out.println("OOPS!!!!!.....Failed to add book");
            }
        } catch (SQLException e) 
        {
            System.out.println("OOPS!!!!!.....Failed to add book: " + e.getMessage());
        }
    }

    public static void searchBook(Scanner input) 
    {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(SEARCH_BOOK_QUERY)) 
        {
            System.out.println("Enter book title:");
            String title = input.next();

            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String author = rs.getString("Book_Author");
                double price = rs.getDouble("Book_price");
                Book book = new Book(title, author, price);
                System.out.println(book);
            } else 
            {
                System.out.println("Book not found in the database");
            }
        } catch (SQLException e) 
        {
            System.out.println("OOPS!!!!!.....Failed to search book:"+ e.getMessage());
        }
    }
    

    public static void updateBook(Scanner input) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_BOOK_QUERY)) {
            System.out.println("Enter book title:");
            String title = input.next();
            System.out.println("Enter new author:");
            String author = input.next();
            System.out.println("Enter new price:");
            double price = input.nextDouble();

            stmt.setString(1, author);
            stmt.setDouble(2, price);
            stmt.setString(3, title);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("+++++++++ BOOK UPDATED SUCCESSFULLY +++++++++");
            } else {
                System.out.println("Book not found in the database");
            }
        } catch (SQLException e) {
            System.out.println("OOPS!!!!!.....Failed to update book: " + e.getMessage());
        }
    }

    public static void deleteBook(Scanner input)
    {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(DELETE_BOOK_QUERY)) 
        {
            System.out.println("Enter book title:");
            String title = input.next();

            stmt.setString(1, title);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) 
            {
                System.out.println("+++++++++ BOOK DELETED SUCCESSFULLY +++++++++");
            } 
            else 
            {
                System.out.println("Book not found in the database");
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("OOPS!!!!!.....Failed to delete book: " + e.getMessage());
        }
    }

    public static void generateReport() 
    {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(GET_ALL_BOOKS_QUERY)) 
        {

            ResultSet rs = stmt.executeQuery();

            ArrayList<Book> books = new ArrayList<>();

            while (rs.next()) 
            {
                String title = rs.getString("Book_title");
                String author = rs.getString("Book_Author");
                double price = rs.getDouble("Book_price");
                Book book = new Book(title, author, price);
                books.add(book);
            }

            System.out.println("******* BOOKS IN THE DATABASE *******");
            for (Book book : books) 
            {
                System.out.println(book);
            }

        } 
        catch (SQLException e) 
        {
            System.out.println("OOPS!!!!!.....Failed to generate report: " + e.getMessage());
        }
    }
}
