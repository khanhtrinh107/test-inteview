# Tài Liệu Ứng Dụng Contact Selection

## 1. Tổng Quan Dự Án

**Tên Dự Án:** Contact Selection  
**Phiên Bản:** 0.0.1-SNAPSHOT  
**Ngôn Ngữ:** Java  
**Framework:** Spring Boot 3.5.11  
**Phiên Bản Java:** 17  
**Cơ Sở Dữ Liệu:** MySQL

### Mục Đích
Ứng dụng Contact Selection là một dịch vụ REST API được xây dựng bằng Spring Boot, cung cấp chức năng tìm kiếm và lọc thông tin liên hệ (Contact) dựa trên các tiêu chí như tên, kana (phiên âm tiếng Nhật), và vùng địa lý. Ứng dụng hỗ trợ phân trang để xử lý dữ liệu lớn hiệu quả.

---

## 2. Kiến Trúc Ứng Dụng

### 2.1 Cấu Trúc Thư Mục
```
src/main/
├── java/com/upload/contactselection/
│   ├── ContactSelectionApplication.java    # Entry point của ứng dụng
│   ├── controller/
│   │   └── RefSelectRestController.java    # REST API endpoints
│   ├── service/
│   │   ├── ContactService.java             # Interface dịch vụ
│   │   └── impl/
│   │       └── ContactServiceImpl.java      # Triển khai dịch vụ
│   ├── repository/
│   │   ├── ContactRepository.java          # JPA Repository cho Contact
│   │   └── RegionRepository.java           # JPA Repository cho Region
│   ├── entity/
│   │   ├── Contact.java                    # Entity Contact
│   │   └── Region.java                     # Entity Region
│   ├── dto/
│   │   ├── ContactDto.java                 # DTO cho Contact
│   │   ├── RefSelectSearchRequest.java     # DTO yêu cầu tìm kiếm
│   │   ├── RefSelectSearchResponse.java    # DTO phản hồi tìm kiếm
│   │   └── RegionDto.java                  # DTO cho Region
│   └── utils/
│       └── CommonUtils.java                # Tiện ích chung
└── resources/
    ├── application.yml                     # Cấu hình ứng dụng
    ├── static/                             # Tệp tĩnh
    └── templates/                          # Mẫu HTML
```

### 2.2 Mô Hình Kiến Trúc
```
REST API Request
        ↓
Controller (RefSelectRestController)
        ↓
Service (ContactServiceImpl)
        ↓
Repository (ContactRepository)
        ↓
Database (MySQL)
        ↓
Response (RefSelectSearchResponse)
```

---

## 3. Các Thành Phần Chính

### 3.1 Các Entity

#### Contact (Liên Hệ)
Đại diện cho thông tin liên hệ của một người/tổ chức.

| Trường | Loại | Mô Tả |
|--------|------|-------|
| contactCode | String | Mã liên hệ (khóa chính) |
| contactName | String | Tên liên hệ |
| contactKana | String | Phiên âm tiếng Nhật của tên |
| prefecture | String | Tỉnh/Thành phố |
| region | Region | Mối quan hệ với bảng Region |
| deleteFlag | Integer | Cờ xóa (0: hoạt động, 1: xóa) |

**Bảng:** `contact_m`

#### Region (Vùng)
Đại diện cho các vùng địa lý.

| Trường | Loại | Mô Tả |
|--------|------|-------|
| regionCode | String | Mã vùng (khóa chính) |
| regionName | String | Tên vùng |
| displayOrder | Integer | Thứ tự hiển thị |
| deleteFlag | Integer | Cờ xóa (0: hoạt động, 1: xóa) |

**Bảng:** `region_m`

### 3.2 Các DTO (Data Transfer Object)

#### RefSelectSearchRequest
Yêu cầu tìm kiếm liên hệ từ client.

```java
{
  "contactName": "string",      // Tên liên hệ (tùy chọn)
  "contactKana": "string",      // Phiên âm (tùy chọn)
  "regionCodes": ["string"],    // Danh sách mã vùng (tùy chọn)
  "page": 1,                    // Trang (mặc định: 1)
  "size": 10                    // Số lượng bản ghi trên trang (mặc định: 10)
}
```

#### RefSelectSearchResponse
Phản hồi kết quả tìm kiếm.

```java
{
  "total": 100,                 // Tổng số liên hệ khớp
  "contacts": [
    {
      "contactCode": "C001",
      "contactName": "Nguyễn Văn A",
      "contactKana": "グエン ヴァン A",
      "prefecture": "TP Hồ Chí Minh",
      "regionName": "Miền Nam"
    }
  ]
}
```

#### ContactDto
Thông tin liên hệ được trả về cho client.

---

## 4. REST API Endpoints

### 4.1 Tìm Kiếm Liên Hệ

**Endpoint:** `POST /purchase/event_entry/event_info/ajax/ref_select_load`

**Mô Tả:** Tìm kiếm liên hệ theo các tiêu chí và hỗ trợ phân trang.

**Request Body:**
```json
{
  "contactName": "Nguyễn",
  "contactKana": "グエン",
  "regionCodes": ["R001", "R002"],
  "page": 1,
  "size": 20
}
```

**Response (200 OK):**
```json
{
  "total": 45,
  "contacts": [
    {
      "contactCode": "C001",
      "contactName": "Nguyễn Văn A",
      "contactKana": "グエン ヴァン A",
      "prefecture": "TP Hồ Chí Minh",
      "regionName": "Miền Nam"
    },
    {
      "contactCode": "C002",
      "contactName": "Nguyễn Văn B",
      "contactKana": "グエン ヴァン B",
      "prefecture": "Hà Nội",
      "regionName": "Miền Bắc"
    }
  ]
}
```

**Tiêu Chí Tìm Kiếm:**
- Chỉ hiển thị các liên hệ chưa bị xóa (deleteFlag = 0)
- Tên liên hệ: tìm kiếm LIKE (không phân biệt hoa-thường)
- Phiên âm: tìm kiếm LIKE
- Vùng: nếu có danh sách mã vùng, lọc theo danh sách đó
- Phân trang: mặc định trang 1, 10 bản ghi trên trang

---

## 5. Lớp Dịch Vụ (Service Layer)

### 5.1 ContactServiceImpl

**Trách Nhiệm:**
- Xử lý logic tìm kiếm liên hệ
- Chuyển đổi Entity sang DTO
- Quản lý phân trang

**Phương Thức Chính:**
```java
RefSelectSearchResponse search(RefSelectSearchRequest request)
```

**Logic Chi Tiết:**
1. Xử lý tham số phân trang (chuyển từ 1-based sang 0-based)
2. Nếu size không được cung cấp, mặc định là 10
3. Xử lý regionCodes: nếu rỗng hoặc null, đặt thành null để truy vấn không lọc
4. Gọi Repository để tìm kiếm
5. Chuyển đổi danh sách Contact thành ContactDto
6. Trả về kết quả cùng với tổng số bản ghi

---

## 6. Lớp Repository (Data Access Layer)

### 6.1 ContactRepository

**Truy Vấn Tùy Chỉnh:**
```sql
SELECT c FROM Contact c
WHERE c.deleteFlag = 0
AND (:contactName IS NULL OR c.contactName LIKE %:contactName%)
AND (:contactKana IS NULL OR c.contactKana LIKE %:contactKana%)
AND (:regionCodes IS NULL OR c.region.regionCode IN :regionCodes)
```

**Đặc Điểm:**
- Tìm kiếm động: tham số NULL sẽ bị bỏ qua
- Hỗ trợ phân trang tự động
- Sử dụng LAZY loading cho quan hệ Region

### 6.2 RegionRepository

**Phương Thức:**
```java
List<Region> findByDeleteFlagOrderByDisplayOrder(Integer deleteFlag)
```

Tìm các vùng theo cờ xóa và sắp xếp theo thứ tự hiển thị.

---

## 7. Tiện Ích (Utils)

### 7.1 CommonUtils

**Phương Thức:** `toDto(Contact contact) -> ContactDto`

Chuyển đổi Entity Contact sang DTO:
- Trích xuất tên vùng từ quan hệ Region
- Loại bỏ các trường không cần thiết cho client

---

## 8. Cấu Hình

### 8.1 application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/contact_selection_db
    username: root
    password: root
  
  jpa:
    hibernate:
      ddl-auto: update           # Tự động cập nhật schema
    show-sql: true              # In SQL ra log

server:
  port: 8080                    # Cổng ứng dụng
```

---

## 9. Công Nghệ Sử Dụng

| Công Nghệ | Phiên Bản | Mục Đích |
|-----------|----------|---------|
| Spring Boot | 3.5.11 | Framework chính |
| Spring Data JPA | - | ORM & Database access |
| Spring Web | - | REST API |
| Lombok | - | Giảm boilerplate code |
| MySQL Connector | - | Driver MySQL |
| Jakarta Persistence | - | JPA annotations |

---

## 10. Luồng Xử Lý

### 10.1 Luồng Tìm Kiếm Liên Hệ

```
1. Client gửi POST request đến /purchase/event_entry/event_info/ajax/ref_select_load
   ↓
2. RefSelectRestController nhận request và gọi contactService.search()
   ↓
3. ContactServiceImpl xử lý:
   - Chuẩn bị tham số phân trang
   - Chuẩn bị filter regionCodes
   ↓
4. ContactRepository thực hiện truy vấn JPA:
   - Lọc theo deleteFlag = 0
   - Lọc theo contactName (nếu có)
   - Lọc theo contactKana (nếu có)
   - Lọc theo regionCodes (nếu có)
   - Áp dụng phân trang
   ↓
5. Database trả về Contact entities
   ↓
6. CommonUtils chuyển đổi Contact → ContactDto
   - Lazyload region.regionName
   ↓
7. Service trả về RefSelectSearchResponse
   ↓
8. Controller trả về JSON response cho client
```

---

## 11. Các Tính Năng

### ✅ Đã Triển Khai
- ✅ Tìm kiếm liên hệ theo tên
- ✅ Tìm kiếm theo phiên âm tiếng Nhật
- ✅ Lọc theo vùng địa lý
- ✅ Phân trang kết quả
- ✅ Lấy thông tin Region liên kết
- ✅ Loại bỏ bản ghi đã xóa

### 🔄 Có Thể Cải Tiến
- 🔄 Thêm sắp xếp kết quả (ORDER BY)
- 🔄 Thêm cache cho dữ liệu Region
- 🔄 Thêm validation input
- 🔄 Thêm error handling & custom exceptions
- 🔄 Thêm logging
- 🔄 Thêm unit tests
- 🔄 Thêm authentication/authorization
- 🔄 Thêm API documentation (Swagger)

---

## 12. Yêu Cầu Database

### 12.1 Bảng contact_m
```sql
CREATE TABLE contact_m (
    contact_code VARCHAR(50) PRIMARY KEY,
    contact_name VARCHAR(255),
    contact_kana VARCHAR(255),
    prefecture VARCHAR(100),
    region_code VARCHAR(50),
    delete_flag INT DEFAULT 0,
    FOREIGN KEY (region_code) REFERENCES region_m(region_code)
);
```

### 12.2 Bảng region_m
```sql
CREATE TABLE region_m (
    region_code VARCHAR(50) PRIMARY KEY,
    region_name VARCHAR(255),
    display_order INT,
    delete_flag INT DEFAULT 0
);
```

---

## 13. Cách Khởi Động Ứng Dụng

### 13.1 Yêu Cầu
- Java 17+
- MySQL 5.7+
- Maven 3.6+

### 13.2 Các Bước
1. Sao chép source code
2. Cấu hình cơ sở dữ liệu trong `application.yml`
3. Tạo database và bảng (hoặc để Hibernate tự tạo)
4. Chạy lệnh:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Ứng dụng sẽ chạy tại `http://localhost:8080`

---

## 14. Thông Tin Liên Hệ & Hỗ Trợ

**Tên Dự Án:** contact-selection  
**Nhóm:** com.upload  
**Artifact ID:** contact-selection  
**Phiên Bản:** 0.0.1-SNAPSHOT

---

*Tài liệu này được tạo từ phân tích source code của dự án Contact Selection*
