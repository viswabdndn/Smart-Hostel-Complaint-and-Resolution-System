# Smart Hostel — Spring Boot Rewrite

A conversion of the original console-based **Smart Hostel Complaint
Management System** into a modern web application.

Original stack → New stack:

| Original            | Rewritten with                 |
|---------------------|--------------------------------|
| Plain Java / javac  | **Maven** (`pom.xml`)          |
| `main()` + Scanner  | **Spring Boot** REST API       |
| `HashMap` / `LinkedHashMap` in memory | **SQLite** + **Spring Data JPA** |
| Console menu        | **HTML / CSS / JS** frontend   |
| `try/catch` in Main | **`@RestControllerAdvice`** exception handler |

> **All original business logic is preserved.** Method names, parameters,
> "Pending / In Progress / Resolved" status flow, `InvalidLoginException`,
> `AutoSaveThread` with its 20-second cycle, the `complaints.txt` CSV
> format and the `generateReport` `StringBuilder` are all unchanged — only
> the storage (Map → SQLite via JPA) and the UI (console → HTML) moved.

---

## Project layout

```
SmartHostel/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/smarthostel/
    │   ├── SmartHostelApplication.java       # boot + starts AutoSaveThread
    │   ├── model/
    │   │   ├── Student.java                  # @Entity
    │   │   ├── Admin.java                    # hardcoded admin/123
    │   │   └── Complaint.java                # @Entity
    │   ├── repository/
    │   │   ├── StudentRepository.java
    │   │   └── ComplaintRepository.java
    │   ├── service/
    │   │   ├── StudentService.java           # register / login / logout
    │   │   ├── ComplaintService.java         # add / getAll / getById / sortByStudentId
    │   │   ├── AdminService.java             # login / assign / resolve / report
    │   │   ├── AutoSaveThread.java           # 20 s daemon (unchanged)
    │   │   └── FileService.java              # writes complaints.txt (unchanged)
    │   ├── controller/
    │   │   ├── StudentController.java
    │   │   ├── ComplaintController.java
    │   │   └── AdminController.java
    │   ├── exception/
    │   │   ├── InvalidLoginException.java    # kept verbatim
    │   │   └── GlobalExceptionHandler.java   # @RestControllerAdvice
    │   └── student/
    │       └── Student.java                  # stray grade class preserved
    └── resources/
        ├── application.properties            # SQLite URL + dialect
        └── static/
            ├── index.html                    # landing
            ├── student.html                  # register / login / file
            ├── admin.html                    # view / assign / resolve / report
            └── style.css                     # warm editorial theme
```

---

## How to run

Prerequisites: **Java 17+** and **Maven 3.8+**.

```bash
cd SmartHostel
mvn spring-boot:run
```

Then open <http://localhost:8080> in your browser.

On first startup, Hibernate auto-creates `smarthostel.db` (SQLite file)
in the project root with `students` and `complaints` tables.

### Build a jar

```bash
mvn clean package
java -jar target/SmartHostel-1.0.0.jar
```

---

## REST API

Mirrors every menu option in the original `Main.java`.

### Student

| Method | URL                          | Body / Params                                   | Original `Main.java` action        |
|--------|------------------------------|-------------------------------------------------|------------------------------------|
| POST   | `/api/students/register`     | `{name, room, email, phone, password}`          | "1.Student → 1.Register"           |
| POST   | `/api/students/login`        | `{id, password}`                                | "1.Student → 2.Login"              |
| POST   | `/api/students/logout`       | —                                               | `studentService.logout()`          |
| GET    | `/api/students/current`      | —                                               | `getLoggedInStudent()`             |

`POST /api/students/login` throws `InvalidLoginException` → handled by
`GlobalExceptionHandler` → returns **401** with
`{"error":"InvalidLoginException","message":"..."}`.

### Complaint

| Method | URL                          | Body / Params                                   | Original action                    |
|--------|------------------------------|-------------------------------------------------|------------------------------------|
| POST   | `/api/complaints`            | `{studentId, type, description}`                | "addComplaint(...)"                |
| GET    | `/api/complaints`            | —                                               | "1. View All Complaints"           |
| GET    | `/api/complaints/{id}`       | —                                               | `getComplaintById(id)`             |
| GET    | `/api/complaints/sorted`     | —                                               | `sortByStudentId()`                |

### Admin

| Method | URL                      | Body / Params                       | Original action          |
|--------|--------------------------|-------------------------------------|--------------------------|
| POST   | `/api/admin/login`       | `{username, password}`              | Admin login              |
| POST   | `/api/admin/assign`      | `{complaintId, staff}`              | "2. Assign Complaint"    |
| POST   | `/api/admin/resolve`     | `{complaintId, resolution}`         | "3. Resolve Complaint"   |
| GET    | `/api/admin/report`      | —                                   | "4. Generate Report"     |

---

## Preserved behaviors (verification checklist)

- [x] Student register prints `"Registered! ID: N"` to server console
- [x] `InvalidLoginException` thrown with messages `"Student not found."` and `"Wrong password."`
- [x] Complaint defaults: `status="Pending"`, `resolution=""`, `assignedStaff=""`, `date=LocalDate.now()`
- [x] `assignStaff` sets status `"In Progress"`
- [x] `resolveComplaint` sets status `"Resolved"`
- [x] `generateReport` builds `"--- REPORT ---\nID: x Status: y"` via `StringBuilder` and prints to `System.out`
- [x] Admin credentials are hardcoded as `admin` / `123` in `Admin.java`
- [x] `AutoSaveThread` is a daemon, sleeps 20 s, silently swallows `Exception`
- [x] `FileService` writes `complaints.txt` in the same CSV format: `id,studentId,type,description,status`
- [x] `sortByStudentId` uses the same anonymous `Comparator<Complaint>` pattern
- [x] `synchronized` on `addComplaint` retained
- [x] Stray root-level `Student.java` (grade-calculation class) preserved in `com.smarthostel.student`

---

## Notes

- The old in-memory `idCounter` fields are replaced by JPA
  `@GeneratedValue(strategy = IDENTITY)` — SQLite handles auto-increment.
- `AdminService.generateReport` was `void`; it still `System.out.println`s
  the same report and now **also** returns the string so the `/report`
  endpoint can ship it back to the HTML frontend.
- `AdminService.assignStaff` and `AdminService.resolveComplaint` are
  unchanged — only the controllers add a `complaintRepository.save(c)`
  afterwards so the mutation persists to SQLite.
- Admin authentication is stateless at the REST layer: the HTML just
  reveals the admin menu after a successful `/api/admin/login`.
