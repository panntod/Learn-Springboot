# Learing-Java-Spring-Boot

```sql
-- buat table untuk belajar springboot
CREATE DATABASE learn_springboot;
CREATE USER learn_user WITH PASSWORD 'learn_password';
GRANT ALL PRIVILEGES ON DATABASE learn_springboot TO learn_user;
```

```sql
-- pastikan user punya akses ke schema public
GRANT ALL ON SCHEMA public TO learn_user;

-- kasih hak untuk create object di schema public
GRANT CREATE ON SCHEMA public TO learn_user;

-- kalau masih error soal ownership
ALTER SCHEMA public OWNER TO learn_user;
```

## Membuat sebuah backend sederhana

Yang perlu disiapkan:
- Java 21
- IDE (Intellij or NetBeans or VsCode)

Melakukan inisialisasi project melalui [`https://start.spring.io/`](https://start.spring.io/)
- Project: Maven
- Spring Boot: 3.5.5
- Project Metadata
  - Group: Nama group/ Folder
  - Artifact: Sesuaikan
  - Name: Sesuaikan 
  - Description: Sesuaikan
  - Package Name: Sesuaikan
  - Packaging: Jar
  - Java: 21
- Dependencies:
  - Lombok
  - Spring Web
  - Springboot devtools
  - Postgresql
  - Mapstruct

Generate dan download zip -> Ekstrak dan buka di IDE

## Inisialisasi backend

Setelah semua setup dilakukan, kita perlu mengkonfigurasi aplikasi yang akan kita gunakan
- Membuat file /resources/appliaction.yml
```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/learn_springboot
    username: learn_user # username untuk akses database
    password: learn_password # password dari username
    hikari:
      maximum-pool-size: 10 # menggunakan hikari untuk maximal pool
  jpa:
    hibernate:
      ddl-auto: update    # dev: update ; prod: validate or none + migrations
    properties:
      hibernate:
        format_sql: true
    show-sql: false
    open-in-view: false

server:
  port: 8080 # port yang akan digunakan untuk aplikasi

# logging:
#   level:
#     root: INFO
```
- Setelah itu membuat config untuk web di config/WebConfig.java
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // semua endpoint
                .allowedOrigins("http://localhost:3000") // contoh aplikasi react.js
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true);
    }
} 
```
- Function/ Aplikasi utama berada di root group berisi
```java
@SpringBootApplication
public class LearnSpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootApplication.class, args);
    }
} 
```

## Membuat aplikasi backend di Spring Boot

### Urutan pembuatan aplikasi

Struktur rekomendasi untuk membuat aplikasi spring boot
```text
src/
└─ main/
   ├─ java/
   │  └─ com.example.myapp/
   │     ├─ MyAppApplication.java
   │     ├─ config/
   │     │  └─ WebConfig.java
   │     ├─ controller/
   │     │  └─ ProductController.java
   │     ├─ dto/
   │     │  ├─ ProductDto.java
   │     │  ├─ ProductCreateDto.java
   │     │  ├─ ProductSearchRequest.java
   │     │  └─ PageResponse.java
   │     ├─ entity/
   │     │  └─ Product.java
   │     ├─ exception/
   │     │  ├─ ApiError.java
   │     │  └─ GlobalExceptionHandler.java
   │     ├─ mapper/
   │     │  └─ ProductMapper.java
   │     ├─ repository/
   │     │  └─ ProductRepository.java
   │     ├─ service/
   │     │  └─ ProductService.java
   │     └─ spec/
   │        └─ ProductSpecification.java
   └─ resources/
      ├─ application.yml
      └─ db/migration/ (optional for Flyway)
```
- Entity (entity/)
  - Mulai dari Product.java sebagai representasi tabel database.
  - Tambahkan anotasi @Entity, @Table, field dengan @Column, dan @Id.
- Repository (repository/)
  - Buat ProductRepository.java extend JpaRepository<Product, Long>.
  - Bisa tambah custom query jika perlu (@Query atau pakai JpaSpecificationExecutor).
- DTO (dto/)
  - ProductDto, ProductCreateDto, ProductSearchRequest, PageResponse.
  - Gunakan untuk komunikasi antara controller ↔ service ↔ client (jangan expose entity langsung).
- Mapper (mapper/)
  - Buat ProductMapper untuk konversi Entity <-> DTO.
  - Bisa pakai MapStruct, atau manual.
- Specification (spec/) (opsional, tapi bagus kalau perlu search/filtering)
  - ProductSpecification untuk dynamic query (misalnya search dengan name, category, dll).
- Service (service/)
  - ProductService.java berisi business logic (CRUD, search, pagination).
  - Jangan letakkan logika berat di controller.
- Controller (controller/)
  - ProductController.java expose REST API endpoint (/api/products).
  - Panggil ProductService untuk eksekusi.
- Exception Handling (exception/)
  - GlobalExceptionHandler.java dengan @ControllerAdvice.
  - Return ApiError.java biar response error konsisten.
- Database Migration (resources/db/migration/) (opsional, tapi sangat recommended)
  - Kalau pakai Flyway, taruh file SQL di V1__init.sql, V2__add_field_price.sql, dll.
  - Bisa otomatis jalan pas aplikasi start.