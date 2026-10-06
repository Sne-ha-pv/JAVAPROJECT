# 🧪 Vehicle Rental System — Test Cases & Validation Suite

This document outlines the test cases demonstrating functional correctness, edge-case handling, and business rule enforcement.

---

## Summary Matrix

| Test ID | Test Scenario | Module | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- |
| **TC-01** | Add new Car, Bike, and Van | Inventory | Added to fleet with ID & Available status | `PASSED` |
| **TC-02** | Search vehicles by Type | Search | Returns only matching vehicle types | `PASSED` |
| **TC-03** | Search vehicles by Brand | Search | Matches brand/model case-insensitively | `PASSED` |
| **TC-04** | Update vehicle details | Inventory | Updates rate, brand, specs properly | `PASSED` |
| **TC-05** | Remove available vehicle | Inventory | Vehicle removed from fleet | `PASSED` |
| **TC-06** | Remove rented vehicle (Safety check) | Inventory | Operation blocked with warning error | `PASSED` |
| **TC-07** | Standard Booking with Deposit | Booking | Calculates cost = `Rate × Days`, locks vehicle | `PASSED` |
| **TC-08** | On-time Vehicle Return | Returns | Full security deposit refunded, penalty = Rs. 0 | `PASSED` |
| **TC-09** | Late Vehicle Return with Penalty | Returns | Deducts `Late Days × Rs. 200`, refunds balance | `PASSED` |
| **TC-10** | Severe Late Return (Penalty > Deposit) | Returns | Penalty capped, deposit fully absorbed, refund = 0 | `PASSED` |

---

## Detailed Test Specifications

### Test Case 1: Standard Vehicle Booking
- **Input:**
  - Customer ID: `1` (*Aarav Sharma*)
  - Vehicle ID: `1` (*Toyota Camry Hybrid*, Rate: Rs. 3500/day)
  - Start Date: `2026-10-10`
  - End Date: `2026-10-13` (3 Days)
  - Security Deposit: `Rs. 3,000`
- **Calculation:**
  - Duration = `3 Days`
  - Rental Cost = `3 × 3500 = Rs. 10,500`
  - Total Upfront = `10,500 + 3,000 = Rs. 13,500`
- **Expected Output:**
  - Rental created with Status `ACTIVE`.
  - Vehicle ID 1 availability set to `false` (**🔴 On Rent**).
  - Added to Customer #1's rental history.

---

### Test Case 2: On-Time Return (Full Deposit Refund)
- **Input:**
  - Rental ID: `102`
  - Scheduled End Date: `2026-10-13`
  - Actual Return Date: `2026-10-13`
  - Security Deposit: `Rs. 3,000`
- **Calculation:**
  - Late Days = `0`
  - Late Penalty = `0 × 200 = Rs. 0`
  - Refund Amount = `3000 - 0 = Rs. 3,000`
- **Expected Output:**
  - Status becomes `RETURNED`.
  - Late Penalty: `Rs. 0`
  - Refund: `Rs. 3,000`
  - Vehicle status restored to `true` (**🟢 Available**).

---

### Test Case 3: Late Return with Penalty Calculation
- **Input:**
  - Rental ID: `101`
  - Scheduled End Date: `2026-10-08`
  - Actual Return Date: `2026-10-11` (3 Days Late)
  - Security Deposit: `Rs. 5,000`
- **Calculation:**
  - Late Days = `3 Days`
  - Late Penalty = `3 × 200 = Rs. 600`
  - Refund Amount = `5000 - 600 = Rs. 4,400`
- **Expected Output:**
  - Late Penalty: `Rs. 600`
  - Refund Amount to Customer: `Rs. 4,400`
  - Vehicle status restored to `true` (**🟢 Available**).

---

### Test Case 4: Extreme Late Return (Penalty Exceeds Deposit)
- **Input:**
  - Rental ID: `103`
  - Scheduled End Date: `2026-10-01`
  - Actual Return Date: `2026-10-15` (14 Days Late)
  - Security Deposit: `Rs. 2,000`
- **Calculation:**
  - Late Days = `14 Days`
  - Late Penalty = `14 × 200 = Rs. 2,800`
  - Refund Amount = `max(0, 2000 - 2800) = Rs. 0`
- **Expected Output:**
  - Late Penalty: `Rs. 2,800`
  - Refund Amount: `Rs. 0` (Deposit absorbed)
  - Vehicle status restored to `true`.

---

### Test Case 5: Protection Against Deleting Rented Vehicles
- **Input:**
  - Vehicle ID: `2` (Currently Rented)
  - Action: Attempt delete
- **Expected Output:**
  - Action rejected.
  - Server returns error message: `"Vehicle cannot be removed (currently rented)"`.
  - Vehicle remains in the fleet.
