# Learing-Java-Spring-Boot

```sql
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