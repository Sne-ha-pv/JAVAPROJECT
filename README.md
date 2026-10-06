# 🚗 AutoRent Pro — Vehicle Rental System (Java, HTML & CSS Only)

A complete **Vehicle Rental System** designed and implemented using **Object-Oriented Programming (OOP) in Java** with **HTML5 and CSS3** (Server-Side Rendered in pure Java; **Zero JavaScript / No other language used**).

---

## 🌟 Key Highlights
- **100% Pure Java Backend & SSR:** Powered entirely by standard Java (`com.sun.net.httpserver.HttpServer`) without external dependencies or JavaScript.
- **Zero JavaScript:** All views, live navigation, search queries, booking operations, and return settlements use server-side rendered HTML5 and CSS3.
- **Real-Time Fleet Availability:** Instant status tracking (**🟢 Available** vs **🔴 On Rent**) that reflects across all fleet pages and booking dropdowns.
- **Automated Cost & Penalty Engine:** Calculates rental duration, daily rates, security deposits, late-return penalty fees (`Rs. 200/day`), and deposit refund balances.
- **Full Fleet & Customer Management:** Add, update, delete (with safety checks), and search vehicles (*Cars, Bikes, Vans*) + customer account directory with rental history tracking.
- **Design:** Modern glassmorphic dark theme, responsive layout, custom badges, and high-contrast typography.

---

## 🏗️ Project Structure

```
vehicle-rental-system/
├── src/
│   ├── Rentable.java        # Interface defining core rental contract
│   ├── Vehicle.java         # Base entity implementing Rentable
│   ├── Car.java             # Subclass for Cars (Inheritance & Polymorphism)
│   ├── Bike.java            # Subclass for Bikes
│   ├── Van.java             # Subclass for Vans
│   ├── Customer.java        # Customer entity & rental history tracker
│   ├── Rental.java          # Rental transaction & late penalty logic
│   ├── RentalAdmin.java     # Fleet controller & inventory manager
│   ├── HtmlRenderer.java    # Server-Side HTML5 generator (Zero JS)
│   ├── WebServer.java       # Pure Java HTTP server
│   └── Main.java            # Main entry point (Web Server + CLI fallback)
├── web/
│   ├── styles.css           # Glassmorphism dark theme CSS styling
│   └── index.html           # Pure HTML redirect
├── DESIGN_DOCUMENT.md       # Class diagrams, sequence flows & OOP documentation
├── USER_MANUAL.md           # Setup, execution, and operating guide
├── TEST_CASES.md            # Comprehensive test cases & expected results
└── README.md                # Project documentation overview
```

---

## 🚀 How to Run

### 1. Compile Java Code
```powershell
javac -d bin src/*.java
```

### 2. Start the Server
```powershell
java -cp bin Main
```

### 3. Open in Browser
Visit **[http://localhost:8080](http://localhost:8080)** in your browser.

---

## 📚 Deliverables
1. [DESIGN_DOCUMENT.md](file:///c:/Users/hp/OneDrive/Desktop/vehicle-rental-system/DESIGN_DOCUMENT.md) — Comprehensive Class Design, UML Diagrams, and OOP Architecture.
2. [USER_MANUAL.md](file:///c:/Users/hp/OneDrive/Desktop/vehicle-rental-system/USER_MANUAL.md) — Step-by-step Setup and User Guide.
3. [TEST_CASES.md](file:///c:/Users/hp/OneDrive/Desktop/vehicle-rental-system/TEST_CASES.md) — Sample inputs, outputs, and business logic validation.
4. **Source Code** — Complete Java codebase in `src/` and CSS in `web/`.
