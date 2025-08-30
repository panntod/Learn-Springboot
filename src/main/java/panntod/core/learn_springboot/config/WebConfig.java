package panntod.core.learn_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ✅ Atur CORS agar bisa diakses dari FE (misal React/Next)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // semua endpoint
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true);
    }
}
