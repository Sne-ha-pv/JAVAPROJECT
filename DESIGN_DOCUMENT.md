\# Vehicle Rental System - Design Document



\## 1. Introduction



The Vehicle Rental System is a Java-based console application designed to manage vehicle rentals efficiently.



The system uses Object-Oriented Programming concepts such as classes, objects, encapsulation, interfaces, and ArrayList.



\## 2. Objectives



The main objectives of the system are:



\- To maintain vehicle details.

\- To manage customer information.

\- To allow customers to book available vehicles.

\- To calculate rental costs based on the number of days.

\- To maintain real-time vehicle availability.

\- To manage security deposits.

\- To calculate late-return penalties.

\- To process vehicle returns.

\- To maintain customer rental history.



\## 3. System Components



The system consists of the following components:



\### Rentable Interface



The `Rentable` interface defines the common operations required for a rentable vehicle.



Methods:



\- `calculateRentalCost(int days)`

\- `isAvailable()`

\- `setAvailable(boolean available)`



\### Vehicle Class



The `Vehicle` class stores:



\- Vehicle ID

\- Vehicle type

\- Brand

\- Model

\- Rental rate

\- Availability status



The class implements the `Rentable` interface.



\### Customer Class



The `Customer` class stores:



\- Customer ID

\- Customer name

\- Contact details

\- Rental history



\### Rental Class



The `Rental` class stores:



\- Rental ID

\- Customer

\- Vehicle

\- Start date

\- End date

\- Rental cost

\- Security deposit

\- Late-return penalty



It also calculates the refundable security deposit.



\### RentalAdmin Class



The `RentalAdmin` class manages:



\- Vehicle inventory

\- Customer records

\- Rental records

\- Vehicle booking

\- Vehicle return

\- Vehicle searching

\- Vehicle updating

\- Vehicle removal



\### Main Class



The `Main` class provides the menu-driven user interface and accepts input using the `Scanner` class.



\## 4. OOP Concepts Used



\### Encapsulation



The data members of the classes are declared as private and accessed through methods.



\### Interface



The `Rentable` interface provides common rental-related methods for the `Vehicle` class.



\### Abstraction



The interface hides the implementation details of rental operations.



\### Object-Oriented Design



The system is divided into separate classes based on their responsibilities.



\## 5. Data Structures Used



`ArrayList` is used to store:



\- Vehicles

\- Customers

\- Rental records



`ArrayList` allows the system to dynamically add and remove records.



\## 6. Booking Process



The booking process works as follows:



1\. Customer ID is entered.

2\. Vehicle ID is entered.

3\. The system checks whether the customer exists.

4\. The system checks whether the vehicle exists.

5\. The system checks vehicle availability.

6\. Rental duration is entered.

7\. Rental cost is calculated.

8\. Security deposit is recorded.

9\. A rental record is created.

10\. Vehicle availability is changed to unavailable.



\## 7. Return Process



The return process works as follows:



1\. Rental ID is entered.

2\. Late days are entered.

3\. The system calculates the late-return penalty.

4\. The vehicle availability is changed to available.

5\. The refundable security deposit is calculated.

6\. The return details are displayed.



\## 8. Rental Cost Calculation



Rental cost is calculated using:



```text

Rental Cost = Rental Rate per Day × Number of Days

