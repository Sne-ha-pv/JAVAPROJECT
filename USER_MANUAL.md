# 📖 Vehicle Rental System — User Manual (Java, HTML & CSS)

## 1. System Overview
The **Vehicle Rental System** is a pure **Java** web application (with **HTML5 & CSS3**, **Zero JavaScript**) designed to manage vehicle fleets (Cars, Bikes, Vans), customer accounts, booking transactions, security deposits, and vehicle return operations with automatic late penalty calculation.

---

## 2. Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher (JDK 17 or 21 recommended).
- **Web Browser:** Google Chrome, Microsoft Edge, Mozilla Firefox, or Safari.
- **Zero Third-Party Libraries or JavaScript needed.**

---

## 3. How to Compile and Run

### Step 1: Open Terminal / Command Prompt
Navigate to the root directory of the project:
```powershell
cd c:\Users\hp\OneDrive\Desktop\vehicle-rental-system
```

### Step 2: Compile All Java Source Code
Compile all `.java` files from `src/` into a `bin/` directory:
```powershell
javac -d bin src/*.java
```

### Step 3: Launch the Application
Start the Java web application:
```powershell
java -cp bin Main
```

The terminal will confirm:
```text
=================================================
  VEHICLE RENTAL SYSTEM WEB APPLICATION STARTED  
  (100% Java & Server-Side Rendered — No JS)     
  URL: http://localhost:8080
=================================================
🌐 Access the Web Application at: http://localhost:8080
```

### Step 4: Open in Web Browser
Open your browser and navigate to:
```
http://localhost:8080
```

---

## 4. Web Application Features & Usage

### 4.1 Fleet Explorer & Live Availability
- **Live Fleet View:** Displays all registered vehicles with high-resolution imagery, rental rates, and real-time status badges (**🟢 Available** or **🔴 On Rent**).
- **Search & Filtering:** Use the search form to filter by brand/model name, select vehicle types (**All**, **Cars**, **Bikes**, **Vans**), and toggle **Available Only**.
- **Action Buttons:** Click **"Rent Now"** on any available vehicle to jump straight into the booking form with that vehicle preselected.

### 4.2 Booking a Vehicle
1. Click **"Rent Now"** on any available vehicle or click **"Book Now"** in the top navigation.
2. Select a customer from the registered customers dropdown.
3. Select an available vehicle.
4. Set the **Start Date** and **End Date**.
5. Specify the **Security Deposit Amount (Rs.)**.
6. Click **"Confirm & Book Vehicle"**.
7. The system creates the rental transaction, immediately marks the vehicle as **🔴 On Rent**, and redirects to the **Active Rentals** ledger with a confirmation banner.

### 4.3 Returning a Vehicle & Late Penalty Calculation
1. Navigate to the **"Active Rentals"** tab.
2. Click **"Process Return"** next to the active rental.
3. In the Return form, enter the **Actual Vehicle Return Date**.
4. The system calculates:
   - **Late Days:** Days past the scheduled end date.
   - **Late Penalty Fee:** `Late Days × Rs. 200`.
   - **Refund Amount:** `max(0, Security Deposit - Late Penalty)`.
5. Click **"Complete Return & Calculate Refund"**.
6. The vehicle is immediately returned and restored to **🟢 Available** status in the fleet.

### 4.4 Managing Fleet (Add, Edit, Remove)
- **Add Vehicle:** Click **"+ Add Vehicle"**, select the type (*Car*, *Bike*, *Van*), brand, model, daily rate, and specifications.
- **Edit Vehicle:** Click the edit icon on any vehicle card to update pricing or specs.
- **Delete Vehicle:** Click the trash icon. *(Note: The system blocks removal of any vehicle that is currently rented).*

### 4.5 Customer Directory & Rental History
- **Register Customer:** Click **"+ Add Customer"** to register a new customer with their name, phone, email, and driver's license number.
- **View History:** In the **"Customers"** tab, click **"View History"** to display all past and active rentals for that customer.

---

## 5. Interactive Console CLI (Optional)
If you wish to test via the command-line interface, pass the `--cli` argument:
```powershell
java -cp bin Main --cli
```
