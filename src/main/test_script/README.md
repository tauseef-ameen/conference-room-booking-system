# Test Script for Conference Room Booking system microservice

This is a very simple Bash script that automates testing of the Booking Conference Room microservice API by sending a series of HTTP requests to:

1. **Check room availability**
2. **Make a reservation**
3. **Re-check availability** after booking

---

## Script Location

The name of the script is reserve_room.sh. This script is located at `/src/main/test_script`

---

## Prerequisites

- Make sure the application is **running and accessible** (e.g., via Docker on port `8081`).
- Ensure you have `curl` installed.

---

## Usage

```bash
./reserve_room.sh <BASE_URL> <START_TIME> <END_TIME> <ROOM_ID>
```

### Example:

```bash
./reserve_room.sh http://localhost:8081 2025-04-10T10:00 2025-04-11T11:00 22
```

---

## What It Does

1. **GET** `/available/{startTime}/{endTime}`  
   Checks available rooms for the provided time range.

2. **POST** `/reserve`  
   Reserves the specified room.

3. **GET** `/available/{startTime}/{endTime}` (again)  
   Verifies that the reserved room is no longer available.

---

## 📤 Sample Output

```bash
$ ./reserve_room.sh http://localhost:8081 2025-04-10T10:00 2025-04-10T11:00 22
GET Request: http://localhost:8081/available/2025-04-10T10:00/2025-04-10T11:00
HTTP Status: 200
Response Body: [{"buildingName":"gebouw-1","roomId":1,"floorNo":1,"wing":"A"},{"buildingName":"gebouw-2","roomId":11,"floorNo":3,"wing":"D"},{"buildingName":"gebouw-3","roomId":22,"floorNo":5,"wing":"C"},{"buildingName":"gebouw-4","roomId":33,"floorNo":7,"wing":"B"},{"buildingName":"gebouw-5","roomId":44,"floorNo":9,"wing":"E"}]
---------------------------
POST Request: http://localhost:8081/reserve
HTTP Status: 200
Response Body: {"roomId":22,"bookingId":2,"startTime":"2025-04-10T10:00:00","endTime":"2025-04-10T11:00:00"}
---------------------------
GET Request: http://localhost:8081/available/2025-04-10T10:00/2025-04-10T11:00
HTTP Status: 200
Response Body: [{"buildingName":"gebouw-1","roomId":1,"floorNo":1,"wing":"A"},{"buildingName":"gebouw-2","roomId":11,"floorNo":3,"wing":"D"},{"buildingName":"gebouw-4","roomId":33,"floorNo":7,"wing":"B"},{"buildingName":"gebouw-5","roomId":44,"floorNo":9,"wing":"E"}]
---------------------------

```

---