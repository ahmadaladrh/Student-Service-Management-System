# SwingRealApp

A real Java Swing desktop application using SQLite.

## Features

- Register user with real validation
- Password hashing using PBKDF2WithHmacSHA256
- Login from database
- Customer Management:
  - Add
  - Update
  - Delete
  - Search
  - JTable display
- Service / Order Management:
  - Add
  - Update
  - Delete
  - Search
  - JTable display
  - Connected to customer by Customer ID
- SQLite database created automatically

## How to open in NetBeans

1. Extract the zip file.
2. Open NetBeans.
3. File -> Open Project.
4. Select `SwingRealApp`.
5. Wait for Maven dependencies to download.
6. Run the project.

## Database

The app creates this file automatically when you run it:

`project_app.db`

You do not need to install MySQL or SQL Server.

## Test steps

1. Open the app.
2. Click "Create Account".
3. Register using:
   - Name: Asri
   - Email: asri@gmail.com
   - Phone: 0781234567
   - Date of Birth: 18/05/2003
   - Password: 123456
   - Confirm Password: 123456
   - Address: Jordan
   - Security Answer: blue
4. Login with the email and password.
5. Open Customer Management.
6. Add a customer.
7. Open Service Management.
8. Add a service using the customer ID from the customer table.

## Screenshots for Phase 3 document

Take screenshots of:

1. Login screen
2. Register screen with validation error
3. Register success
4. Dashboard screen
5. Customer Management with real customer data in table
6. Service Management with real service/order data in table
7. Delete confirmation popup
