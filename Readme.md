# Online Library Management System (Java GUI)

## 📚 Overview

The **Online Library Management System** is a desktop-based Java Swing application that allows users to:
- Register and log in
- View a list of books
- Borrow and return books

Admins can:
- Log into a separate admin panel
- Add or update books
- View registered users
- View borrowing/return transaction logs

All data is stored in plain text files (`books.txt`, `users.txt`, `transactions.txt`).

---

## 🔧 How It Works

This system uses Java Swing for the GUI and file I/O to manage data. Here's a breakdown of the components:

- **books.txt** – Stores book information in the format:  
  `BookID|Title|Author|Genre|Availability`  
  (Availability is either `true` for available or `false` for borrowed)

- **users.txt** – Stores user credentials:  
  `Username|Password`

- **transactions.txt** – Logs borrowing and returning transactions:  
  `Username borrowed BookID` or `Username returned BookID`

- Admin password is hardcoded as: `EWU123`

---

## 🖥️ How to Run

1. **Prerequisites**
   - Java installed (JDK 8 or later)
   - Any Java IDE or a terminal to run the `.java` file

2. **Compile the Program**
   ```bash
   javac OnlineLibraryManagementSystem.java
   ``` 
   
3. ** Run the Program ** 

	``` bash
	java OnlineLibraryManagementSystem
	```
---

## 👤 User Guide

### 🏠 Main Menu Options
- **Register**: Create a new user account. Your credentials are saved in `users.txt`.
- **Login**: Login with an existing username and password.
- **Admin Panel**: Requires a password (`EWU123`).
- **Exit**: Close the application.

---

## 📕 User Functionalities

After logging in, users can:

- **View Books List**  
  Displays all books and their availability status.

- **Borrow Book**  
  Input a Book ID to borrow if it's available.

- **Return Book**  
  Input a Book ID to return the borrowed book.

- **Log Out**  
  Returns the user to the main menu.

---

## 🛠 Admin Functionalities

After entering the admin password:

- **Add Book**  
  Input book details (ID, Title, Author, Genre). Book is marked as available (`true`).

- **Update Book**  
  Enter the Book ID and update its details.

- **View Users**  
  Displays the content of `users.txt`.

- **View Transactions**  
  Displays the borrowing and return logs from `transactions.txt`.

- **Back to Main Menu**  
  Returns to the main menu.

---