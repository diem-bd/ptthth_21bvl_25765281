# HƯỚNG DẪN CHẠY CHƯƠNG TRÌNH CHAT NHÓM QUA TCP SOCKET
**Môn học:** Phát triển hệ thống tích hợp (PTHTTH)  
**Sinh viên:** Bùi Dương Diễm - **MSSV:** 25765281  
**GitHub:** https://github.com/diem-bd/ptthth_21bvl_25765281  

---

## 1. Cấu trúc mã nguồn (5 file độc lập chuẩn OOP)
- `ChatServer.java`: Khởi động ServerSocket(cổng 6789), lắng nghe kết nối từ Client và cấp phát luồng phục vụ.
- `ClientHandler.java`: Tách riêng lớp xử lý luồng (implements Runnable) phục vụ riêng từng Client trên Server.
- `ChatRoom.java`: Lớp quản lý 1 phòng chat, quản lý danh sách Client trong phòng và gửi tin nhắn (broadcast) nội bộ phòng.
- `ChatClient.java`: Chương trình Client kết nối Socket tới Server, luồng chính đọc phím người dùng nhập từ bàn phím.
- `ServerListener.java`: Tách riêng lớp xử lý luồng nhận tin nhắn (implements Runnable) chạy ngầm trên Client để in tin nhắn ra màn hình.

---

## 2. Hướng dẫn biên dịch và chạy chương trình

### Bước 1: Biên dịch toàn bộ 5 file Java
Mở Terminal tại thư mục gốc của dự án và chạy:
```bash
javac -d bin BaiKiemTra_ChatTCP/*.java
```

### Bước 2: Khởi động Server
Tại Terminal 1, chạy lệnh:
```bash
java -cp bin BaiKiemTra_ChatTCP.ChatServer
```
*Màn hình hiển thị: Server đã chạy trên cổng 6789 và sẵn sàng 3 phòng: Study, Game, TraiNghiem.*

### Bước 3: Mở các Client để chat
Mở thêm các tab Terminal khác để kết nối các Client:
```bash
java -cp bin BaiKiemTra_ChatTCP.ChatClient
```
- **Nhập tên:** Nhập tên hiển thị của bạn (vd: `Nam`, `Hoa`, `Binh`).
- **Chọn phòng:** Nhập số thứ tự (`1`: Study, `2`: Game, `3`: TraiNghiem) hoặc nhập tên phòng.
- **Chat:** Gõ tin nhắn và Enter để trò chuyện cùng những người trong phòng.
- **Lệnh /quit:** Rời phòng hiện tại và quay lại danh sách chọn phòng mới.
- **Lệnh /exit:** Ngắt kết nối an toàn và đóng ứng dụng.
