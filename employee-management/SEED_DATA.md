# Seed Data Documentation

## Overview
Dự án này tự động tạo và nạp dữ liệu sample vào database khi ứng dụng khởi động lần đầu tiên. Điều này được thực hiện qua class `com.example.employeemanagement.db.seed`.

## Dữ Liệu Được Seed

### Departments (4 phòng ban)
| ID | Tên Phòng |
|----|-----------|
| 1  | IT        |
| 2  | HR        |
| 3  | Finance   |
| 4  | Sales     |

### Employees (7 nhân viên)
| ID | Tên               | Email                      | Phòng Ban |
|----|------------------|----------------------------|-----------|
| 1  | John Doe         | john.doe@example.com       | IT        |
| 2  | Jane Smith       | jane.smith@example.com     | HR        |
| 3  | Bob Johnson      | bob.johnson@example.com    | Finance   |
| 4  | Alice Williams   | alice.williams@example.com | Sales     |
| 5  | Charlie Brown    | charlie.brown@example.com  | IT        |
| 6  | Diana Prince     | diana.prince@example.com   | Sales     |
| 7  | Eve Davis        | eve.davis@example.com      | HR        |

## Cách Thức Hoạt Động

1. Khi ứng dụng Spring Boot khởi động:
   - Flyway migration chạy đầu tiên và tạo database schema
   - Class `seed` (implement `CommandLineRunner`) được thực thi tự động

2. Logic seed:
   ```java
   - Kiểm tra xem bảng Department có dữ liệu không
   - Nếu trống → insert 4 departments
   - Kiểm tra xem bảng Employee có dữ liệu không
   - Nếu trống → insert 7 employees (liên kết với departments tương ứng)
   ```

3. Output log:
   ```
   Seeding departments table....
   ✓ Seeded 4 departments
   Seeding employees table....
   ✓ Seeded 7 employees
   ```

## Không Seed Lại Nếu Đã Có Dữ Liệu

Seed chỉ chạy **lần đầu tiên** khi database rỗng. Nếu bạn muốn seed lại:

1. **Xóa tất cả dữ liệu:**
   ```sql
   DELETE FROM employee;
   DELETE FROM department;
   -- Reset auto_increment (tuỳ MySQL config)
   ALTER TABLE department AUTO_INCREMENT = 1;
   ALTER TABLE employee AUTO_INCREMENT = 1;
   ```

2. **Hoặc xóa toàn bộ database và tạo lại:**
   ```sql
   DROP DATABASE employees_db;
   CREATE DATABASE employees_db;
   ```

3. Restart ứng dụng Spring Boot

## Location của Seed Class
- **File:** `src/main/java/com/example/employeemanagement/db/seed.java`
- Nhớ rằng class này sử dụng Flyway migration để tạo bảng trước khi seed
- Migration files nằm ở: `src/main/resources/db/migration/`

## Yêu Cầu Configuration

Đảm bảo `application.properties` hoặc biến môi trường được cấu hình đúng:
- `DB_URL_MINI_PROJECT`: JDBC connection string (ví dụ: `jdbc:mysql://localhost:3306/employees_db`)
- `DB_USER`: Username database
- `DB_PASSWORD`: Password database

## Extension Seed Data

Để thêm dữ liệu mới, chỉnh sửa class `seed.java`:

1. Thêm dữ liệu vào mảng trong các method `seedDepartments()` hoặc `seedEmployees()`
2. Gọi `entityManager.persist(entity)` để lưu từng entity
3. Test bằng cách xóa database và restart ứng dụng

