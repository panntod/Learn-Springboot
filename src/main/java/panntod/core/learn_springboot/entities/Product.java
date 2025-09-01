package panntod.core.learn_springboot.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data // Lombok: otomatis bikin getter, setter, toString, equals, dan hashCode
@Entity // Menandakan class ini adalah Entity JPA (dipetakan ke tabel database)
@Table(
        name = "products", // Nama tabel di database
        indexes = {
                @Index(name = "idx_products_name", columnList = "name"),       // Index untuk kolom "name"
                @Index(name = "idx_products_category", columnList = "category") // Index untuk kolom "category"
        }
)
public class Product {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID Identity = autoincrement(), UUID = generateUUID()
    private Long id;

    @Column(nullable = false) // Wajib diisi, tidak boleh null
    private String name;

    // Bisa null, kategori produk
    private String category;

    @Column(length = 2000) // Panjang maksimal 2000 karakter untuk deskripsi
    private String description;

    @Column(nullable = false) // Harga wajib diisi
    private BigDecimal price;

    // Tanggal/waktu produk dibuat, default = sekarang
    private Instant createdAt = Instant.now();
}
