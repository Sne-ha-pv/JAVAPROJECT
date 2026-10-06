\# Vehicle Rental System - Test Cases



\## 1. Introduction



The following test cases were used to verify the functionality of the Vehicle Rental System.



\## 2. Test Cases



| Test Case | Function | Input | Expected Result | Status |

|---|---|---|---|---|

| TC01 | Add Vehicle | Vehicle ID 101, Car, Toyota, Innova, Rs.2000 | Vehicle added successfully | Pass |

| TC02 | Add Vehicle | Vehicle ID 102, Bike, Honda, Activa, Rs.500 | Vehicle added successfully | Pass |

| TC03 | Display Vehicles | Option 2 | All vehicles are displayed | Pass |

| TC04 | Search by Type | Type: Car | Available cars are displayed | Pass |

| TC05 | Search by Brand | Brand: Toyota | Available Toyota vehicles are displayed | Pass |

| TC06 | Add Customer | ID 1, Sneha, contact details | Customer added successfully | Pass |

| TC07 | Book Vehicle | Customer 1, Vehicle 101, 3 days, Rs.5000 deposit | Vehicle booked successfully | Pass |

| TC08 | Rental Cost | Rs.2000/day × 3 days | Rental cost = Rs.6000 | Pass |

| TC09 | Book Rented Vehicle | Vehicle 101 again | Vehicle is already rented | Pass |

| TC10 | Return Vehicle | Rental ID 1, 0 late days | Vehicle returned, penalty = Rs.0 | Pass |

| TC11 | Security Deposit Refund | Rs.5000 deposit, Rs.0 penalty | Refund = Rs.5000 | Pass |

| TC12 | Late Return | Rental ID 2, 2 late days | Late penalty = Rs.400 | Pass |

| TC13 | Late Return Refund | Rs.5000 deposit, Rs.400 penalty | Refund = Rs.4600 | Pass |

| TC14 | Update Vehicle | Vehicle 102, Yamaha, R15, Rs.800 | Vehicle updated successfully | Pass |

| TC15 | Remove Vehicle | Vehicle 102 | Vehicle removed successfully | Pass |

| TC16 | Invalid Vehicle | Non-existing Vehicle ID | Vehicle not found | Pass |



\## 3. Important Test Results



\### Rental Cost



For a Toyota Innova with a rental rate of Rs.2000 per day:



```text

Rental Cost = 2000 × 3

&#x20;           = Rs.6000

