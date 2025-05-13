// File: OnlineLibraryManagementSystem.java
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// ==== Main Class (Public) ====
public class OnlineLibraryManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().display());
    }
}

// ==== Model Classes ====
class Book {
    String id, title, author, genre;
    boolean available;

    Book(String id, String title, String author, String genre, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = available;
    }

    static Book fromString(String line) {
        String[] p = line.split("\\|");
        return new Book(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[4]));
    }

    public String toString() {
        return id + "|" + title + "|" + author + "|" + genre + "|" + available;
    }
}

// ==== Utility Class ====
class FileUtil {
    static List<String> readFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) lines.add(line);
        br.close();
        return lines;
    }

    static void writeFile(String path, List<String> lines, boolean append) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path, append));
        for (String line : lines) bw.write(line + "\n");
        bw.close();
    }
}

// ==== Manager Classes ====
class BookManager {
    private static final String BOOKS_FILE = "books.txt";

    public List<Book> getBooks() throws IOException {
        List<Book> list = new ArrayList<>();
        for (String line : FileUtil.readFile(BOOKS_FILE)) list.add(Book.fromString(line));
        return list;
    }

    public void saveBooks(List<Book> books) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Book b : books) lines.add(b.toString());
        FileUtil.writeFile(BOOKS_FILE, lines, false);
    }

    public boolean borrowBook(String id) throws IOException {
        List<Book> books = getBooks();
        for (Book b : books) {
            if (b.id.equals(id) && b.available) {
                b.available = false;
                saveBooks(books);
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(String id) throws IOException {
        List<Book> books = getBooks();
        for (Book b : books) {
            if (b.id.equals(id) && !b.available) {
                b.available = true;
                saveBooks(books);
                return true;
            }
        }
        return false;
    }

    public List<Book> searchBooks(String field, String query) throws IOException {
        List<Book> result = new ArrayList<>();
        for (Book b : getBooks()) {
            switch (field.toLowerCase()) {
                case "title":
                    if (b.title.toLowerCase().contains(query.toLowerCase()))
                        result.add(b);
                    break;
                case "author":
                    if (b.author.toLowerCase().contains(query.toLowerCase()))
                        result.add(b);
                    break;
                case "genre":
                    if (b.genre.toLowerCase().contains(query.toLowerCase()))
                        result.add(b);
                    break;
            }
        }
        return result;
    }
}

class UserManager {
    private static final String USERS_FILE = "users.txt";

    public boolean registerUser(String username, String password) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true));
            bw.write(username + "|" + password + "\n");
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 2 && p[0].equals(username) && p[1].equals(password))
                    return true;
            }
        } catch (IOException e) {}
        return false;
    }
}

class TransactionManager {
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public void logTransaction(String record) {
        try {
            FileUtil.writeFile(TRANSACTIONS_FILE, Arrays.asList(record), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving transaction.");
        }
    }
}

// ==== UI Classes ====
class MainMenu {
    public void display() {
        JFrame frame = new JFrame("Library System");
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(null);

        JButton regBtn = new JButton("Register");
        JButton logBtn = new JButton("Login");
        JButton adminBtn = new JButton("Admin Panel");
        JButton exitBtn = new JButton("Exit");

        regBtn.setBounds(90, 30, 120, 25);
        logBtn.setBounds(90, 70, 120, 25);
        adminBtn.setBounds(90, 110, 120, 25);
        exitBtn.setBounds(90, 150, 120, 25);

        panel.add(regBtn);
        panel.add(logBtn);
        panel.add(adminBtn);
        panel.add(exitBtn);

        regBtn.addActionListener(e -> {
            frame.dispose();
            new UserRegister().display();
        });

        logBtn.addActionListener(e -> {
            frame.dispose();
            new UserLogin().display();
        });

        adminBtn.addActionListener(e -> {
            frame.dispose();
            new AdminLogin().display();
        });

        exitBtn.addActionListener(e -> System.exit(0));

        frame.add(panel);
        frame.setVisible(true);
    }
}

class UserRegister {
    public void display() {
        JTextField userField = new JTextField();
        JTextField passField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            boolean success = new UserManager().registerUser(userField.getText(), passField.getText());
            if (success)
                JOptionPane.showMessageDialog(null, "Registration successful.");
            else
                JOptionPane.showMessageDialog(null, "Error registering user.");
        }
        new MainMenu().display();
    }
}

class UserLogin {
    public void display() {
        JTextField userField = new JTextField();
        JTextField passField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText();
            String password = passField.getText();
            boolean valid = new UserManager().validateUser(username, password);
            if (valid) {
                JOptionPane.showMessageDialog(null, "Login successful.");
                new UserPanel(username).display();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials.");
                new MainMenu().display();
            }
        } else {
            new MainMenu().display();
        }
    }
}

class UserPanel {
    private final String username;
    UserPanel(String username) { this.username = username; }

    public void display() {
        JFrame frame = new JFrame("User Options");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton viewBtn = new JButton("View Books");
        JButton searchBtn = new JButton("Search Books");
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton logoutBtn = new JButton("Logout");

        viewBtn.addActionListener(e -> {
            try {
                List<Book> books = new BookManager().getBooks();
                StringBuilder sb = new StringBuilder();
                for (Book b : books) sb.append(b.toString()).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading books.");
            }
        });

        searchBtn.addActionListener(e -> {
            String[] fields = {"Title", "Author", "Genre"};
            String field = (String) JOptionPane.showInputDialog(null, "Search by:", "Search", JOptionPane.QUESTION_MESSAGE, null, fields, fields[0]);
            if (field != null) {
                String query = JOptionPane.showInputDialog("Enter " + field + ":");
                if (query != null && !query.trim().isEmpty()) {
                    try {
                        List<Book> results = new BookManager().searchBooks(field.toLowerCase(), query.trim());
                        if (results.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No matching books found.");
                        } else {
                            StringBuilder sb = new StringBuilder("Search Results:\n");
                            for (Book b : results) sb.append(b.toString()).append("\n");
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error searching books.");
                    }
                }
            }
        });

        borrowBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Book ID to borrow:");
            if (id != null && !id.trim().isEmpty()) {
                try {
                    boolean ok = new BookManager().borrowBook(id.trim());
                    if (ok) {
                        new TransactionManager().logTransaction(username + " borrowed " + id);
                        JOptionPane.showMessageDialog(null, "Book borrowed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Book unavailable or ID not found.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error borrowing book.");
                }
            }
        });

        returnBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Book ID to return:");
            if (id != null && !id.trim().isEmpty()) {
                try {
                    boolean ok = new BookManager().returnBook(id.trim());
                    if (ok) {
                        new TransactionManager().logTransaction(username + " returned " + id);
                        JOptionPane.showMessageDialog(null, "Book returned successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Book ID or already returned.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error returning book.");
                }
            }
        });

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new MainMenu().display();
        });

        panel.add(viewBtn);
        panel.add(searchBtn);
        panel.add(borrowBtn);
        panel.add(returnBtn);
        panel.add(logoutBtn);

        frame.add(panel);
        frame.setVisible(true);
    }
}

class AdminLogin {
    private static final String ADMIN_PASSWORD = "EWU123";

    public void display() {
        JTextField passField = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Admin Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (passField.getText().equals(ADMIN_PASSWORD)) {
                new AdminPanel().display();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password.");
                new MainMenu().display();
            }
        } else {
            new MainMenu().display();
        }
    }
}

class AdminPanel {
    public void display() {
        JFrame frame = new JFrame("Admin Panel");
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton addBookBtn = new JButton("Add Book");
        JButton searchBtn = new JButton("Search Books");
        JButton viewUsersBtn = new JButton("View Users");
        JButton viewTransBtn = new JButton("View Transactions");
        JButton backBtn = new JButton("Back");

        addBookBtn.addActionListener(e -> {
            JTextField id = new JTextField();
            JTextField title = new JTextField();
            JTextField author = new JTextField();
            JTextField genre = new JTextField();

            JPanel form = new JPanel(new GridLayout(4, 2));
            form.add(new JLabel("ID:")); form.add(id);
            form.add(new JLabel("Title:")); form.add(title);
            form.add(new JLabel("Author:")); form.add(author);
            form.add(new JLabel("Genre:")); form.add(genre);

            int result = JOptionPane.showConfirmDialog(null, form, "Add Book", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Book b = new Book(id.getText(), title.getText(), author.getText(), genre.getText(), true);
                try {
                    FileUtil.writeFile("books.txt", Arrays.asList(b.toString()), true);
                    JOptionPane.showMessageDialog(null, "Book added.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving book.");
                }
            }
        });

        searchBtn.addActionListener(e -> {
            String[] fields = {"Title", "Author", "Genre"};
            String field = (String) JOptionPane.showInputDialog(null, "Search by:", "Search", JOptionPane.QUESTION_MESSAGE, null, fields, fields[0]);
            if (field != null) {
                String query = JOptionPane.showInputDialog("Enter " + field + ":");
                if (query != null && !query.trim().isEmpty()) {
                    try {
                        List<Book> results = new BookManager().searchBooks(field.toLowerCase(), query.trim());
                        if (results.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No matching books found.");
                        } else {
                            StringBuilder sb = new StringBuilder("Search Results:\n");
                            for (Book b : results) sb.append(b.toString()).append("\n");
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error searching books.");
                    }
                }
            }
        });

        viewUsersBtn.addActionListener(e -> viewFile("users.txt", "Users"));
        viewTransBtn.addActionListener(e -> viewFile("transactions.txt", "Transactions"));
        backBtn.addActionListener(e -> {
            frame.dispose();
            new MainMenu().display();
        });

        panel.add(addBookBtn);
        panel.add(searchBtn);
        panel.add(viewUsersBtn);
        panel.add(viewTransBtn);
        panel.add(backBtn);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void viewFile(String path, String title) {
        try {
            StringBuilder sb = new StringBuilder(title + ":\n");
            for (String line : FileUtil.readFile(path)) sb.append(line).append("\n");
            JOptionPane.showMessageDialog(null, sb.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file.");
        }
    }
}
