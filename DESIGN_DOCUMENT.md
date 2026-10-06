# 📄 Vehicle Rental System — OOP Design Document

## 1. System Overview
The **Vehicle Rental System** is a pure Object-Oriented Java application equipped with a server-side rendered (SSR) web interface built using **Java, HTML5, and CSS3** (100% pure Java backend; **Zero JavaScript / no other programming language used**). It manages vehicle fleet inventory, customer registrations, booking transactions, rental rate calculations, security deposit escrows, and late-return penalty settlements.

---

## 2. Object-Oriented Programming (OOP) Principles

| OOP Principle | Implementation in System |
| :--- | :--- |
| **Interface Implementation** | `Rentable` interface defines contract methods (`calculateRentalCost()`, `isAvailable()`, `setAvailable()`) implemented by `Vehicle`. |
| **Inheritance** | `Car`, `Bike`, and `Van` inherit common properties and behaviors from `Vehicle`. |
| **Polymorphism** | Runtime polymorphic handling of vehicles via `Rentable` references and specialized vehicle subtypes with distinct attributes. |
| **Encapsulation** | All entity fields are `private`, accessed and mutated via validated getter and setter methods. |
| **Composition & Aggregation** | `Rental` aggregates a `Customer` and a `Vehicle`. `RentalAdmin` manages collections of `Vehicle`, `Customer`, and `Rental` objects. |
| **Server-Side Rendering (SSR)** | `HtmlRenderer` dynamically renders HTML views using Java string builders styled with CSS3. |

---

## 3. Class Design & Architecture

```mermaid
classDiagram
    class Rentable {
        <<interface>>
        +calculateRentalCost(int days) double
        +isAvailable() boolean
        +setAvailable(boolean available) void
    }

    class Vehicle {
        -int vehicleId
        -String type
        -String brand
        -String model
        -double rentalRate
        -boolean available
        -String imageUrl
        -String features
        +calculateRentalCost(int days) double
        +isAvailable() boolean
        +setAvailable(boolean available) void
        +toJson() String
    }

    class Car {
        -int seatingCapacity
        -String fuelType
        +getSeatingCapacity() int
        +getFuelType() String
    }

    class Bike {
        -int engineCc
        -String bikeCategory
        +getEngineCc() int
        +getBikeCategory() String
    }

    class Van {
        -int cargoCapacityKg
        -int passengerCapacity
        +getCargoCapacityKg() int
        +getPassengerCapacity() int
    }

    class Customer {
        -int customerId
        -String name
        -String contactDetails
        -String email
        -String licenseNumber
        -ArrayList~Rental~ rentalHistory
        +addRental(Rental rental) void
        +getRentalHistory() ArrayList~Rental~
    }

    class Rental {
        -int rentalId
        -Customer customer
        -Vehicle vehicle
        -LocalDate startDate
        -LocalDate endDate
        -LocalDate actualReturnDate
        -double rentalCost
        -double securityDeposit
        -double latePenalty
        -String status
        +calculateLatePenalty(LocalDate returnDate) void
        +getRefundAmount() double
    }

    class RentalAdmin {
        -ArrayList~Vehicle~ vehicles
        -ArrayList~Customer~ customers
        -ArrayList~Rental~ rentals
        -int nextRentalId
        -int nextVehicleId
        -int nextCustomerId
        +addVehicle(Vehicle v) void
        +updateVehicle(int id, String type, String brand, String model, double rate) boolean
        +removeVehicle(int id) boolean
        +searchVehicles(String type, String brand, Boolean onlyAvailable) List~Vehicle~
        +addCustomer(Customer c) void
        +bookVehicle(int customerId, int vehicleId, LocalDate start, LocalDate end, double deposit) Rental
        +returnVehicle(int rentalId, LocalDate returnDate) Rental
    }

    class HtmlRenderer {
        +renderLayout(String title, String activeTab, String content, String msg, String type) String
        +renderFleetPage(RentalAdmin admin, String type, String brand, boolean avail) String
        +renderRentalsPage(RentalAdmin admin) String
        +renderBookingPage(RentalAdmin admin, int preselectedVehicleId) String
        +renderReturnPage(RentalAdmin admin, int rentalId) String
        +renderAddOrEditVehiclePage(Vehicle editVehicle) String
        +renderCustomersPage(RentalAdmin admin, Integer viewCustomerId) String
        +renderDocsPage() String
    }

    class WebServer {
        -int port
        -RentalAdmin admin
        +start() void
        +stop() void
    }

    Rentable <|.. Vehicle
    Vehicle <|-- Car
    Vehicle <|-- Bike
    Vehicle <|-- Van
    Rental --> Customer : aggregates
    Rental --> Vehicle : aggregates
    Customer o-- Rental : tracks history
    RentalAdmin *-- Vehicle : manages
    RentalAdmin *-- Customer : manages
    RentalAdmin *-- Rental : manages
    WebServer --> RentalAdmin : delegates actions
    WebServer --> HtmlRenderer : generates HTML
```

---

## 4. Sequence Diagrams

### 4.1 Vehicle Booking Flow (Pure Java & HTML Form)
```mermaid
sequenceDiagram
    autonumber
    actor User as User / Admin
    participant Browser as Web Browser (HTML/CSS)
    participant Server as Java WebServer (port 8080)
    participant Admin as RentalAdmin
    participant Vehicle as Vehicle (Rentable)
    participant Rental as Rental
    participant Renderer as HtmlRenderer

    User->>Browser: Opens /book?vehicleId=1
    Browser->>Server: GET /book?vehicleId=1
    Server->>Renderer: renderBookingPage(admin, 1)
    Renderer-->>Server: HTML Booking Form
    Server-->>Browser: 200 OK (HTML)
    User->>Browser: Fills dates & deposit, clicks "Confirm & Book"
    Browser->>Server: POST /book (customerId, vehicleId, startDate, endDate, deposit)
    Server->>Admin: bookVehicle(customerId, vehicleId, startDate, endDate, deposit)
    Admin->>Vehicle: isAvailable()
    Vehicle-->>Admin: true
    Admin->>Vehicle: calculateRentalCost(days)
    Vehicle-->>Admin: rentalCost
    Admin->>Rental: new Rental(...)
    Admin->>Vehicle: setAvailable(false)
    Admin->>Admin: Add to active rentals & customer history
    Server-->>Browser: 302 Redirect to /rentals?msg=Success
    Browser->>Server: GET /rentals
    Server->>Renderer: renderRentalsPage(admin)
    Renderer-->>Server: HTML Ledger with updated status
    Server-->>Browser: 200 OK (Rendered HTML with Success Banner)
```

### 4.2 Vehicle Return & Penalty Settlement Flow
```mermaid
sequenceDiagram
    autonumber
    actor Admin as Admin
    participant Browser as Web Browser
    participant Server as Java WebServer
    participant AdminModel as RentalAdmin
    participant Rental as Rental
    participant Vehicle as Vehicle
    participant Renderer as HtmlRenderer

    Admin->>Browser: Clicks "Process Return" on Rental #101
    Browser->>Server: GET /return?rentalId=101
    Server->>Renderer: renderReturnPage(admin, 101)
    Renderer-->>Server: HTML Return Form
    Server-->>Browser: 200 OK (HTML Form)
    Admin->>Browser: Selects actual return date, clicks "Complete Return"
    Browser->>Server: POST /return (rentalId=101, returnDate=2026-10-15)
    Server->>AdminModel: returnVehicle(101, 2026-10-15)
    AdminModel->>Rental: calculateLatePenalty(returnDate)
    Note over Rental: Late Days = Days between End and Return<br/>Late Penalty = Late Days * Rs. 200<br/>Refund = max(0, Deposit - Penalty)
    AdminModel->>Vehicle: setAvailable(true)
    Server-->>Browser: 302 Redirect to /rentals?msg=Returned+Penalty+Refund
    Browser->>Server: GET /rentals
    Server-->>Browser: 200 OK (Rendered Ledger showing Completed & Refund)
```

---

## 5. Mathematical & Business Formulas
1. **Rental Duration:**
   $$\text{Duration (days)} = \max(1, \text{ChronoUnit.DAYS.between}(\text{StartDate}, \text{EndDate}))$$
2. **Rental Cost:**
   $$\text{Rental Cost} = \text{Daily Rate} \times \text{Duration (days)}$$
3. **Late Days:**
   $$\text{Late Days} = \begin{cases} 
   \text{ChronoUnit.DAYS.between}(\text{EndDate}, \text{ReturnDate}) & \text{if } \text{ReturnDate} > \text{EndDate} \\ 
   0 & \text{otherwise} 
   \end{cases}$$
4. **Late Return Penalty:**
   $$\text{Late Penalty} = \text{Late Days} \times \text{Rs. } 200$$
5. **Security Deposit Refund:**
   $$\text{Refund Amount} = \max(0, \text{Security Deposit} - \text{Late Penalty})$$
