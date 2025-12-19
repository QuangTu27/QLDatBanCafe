## ⚙️ Hướng dẫn cài đặt (Cho thành viên mới)

### Bước 1: Chuẩn bị Database
1. Mở SQL Server Management Studio.
2. Mở file script tại: `database/script_quanlycafe.sql`.
3. Chạy script để tạo CSDL `QLDatBanCafe`.

### Bước 2: Cấu hình kết nối
1. Mở file `src/connect/DBConnection.java`.
2. Sửa lại `user` và `password` của SQL Server máy bạn:
   ```java
   String user = "sa";
   String password = "123"; // Sửa thành pass máy bạn
