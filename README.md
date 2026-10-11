# Báo Cáo Thực Hành Lập Trình Phân Tán / Java Nâng Cao (PT HTTH)

Mã lớp/sinh viên: **ptthth_21bvl_25765281**  
Link GitHub kho lưu trữ: **https://github.com/diem-bd/ptthth_21bvl_25765281**

---

### 📂 Cấu trúc các bài thực hành trong dự án:

#### 1. `Lab1_JavaBasic/` (Lab 1: JAVA Basic)
* **`BaiTapCoBan.java`**: Hello World, nhập tên chào hỏi, tính tổng 2 số, kiểm tra chẵn/lẻ, in tháng tiếng Anh bằng switch/case.
* **`ZooManager.java`**: Quản lý động vật vườn thú (Animal, Lion, Snake, Monkey).
* **Thao tác File/Folder**:
  * `DeleteFileIO.java`: Xoá tệp.
  * `DeleteFolderDemo.java`: Xoá thư mục rỗng, thư mục chứa file, xoá đệ quy toàn bộ thư mục.
  * `FindFileDemo.java`: Tìm kiếm file theo đuôi mở rộng.
  * `CopyFileDemo.java`: Sao chép file bằng byte stream.
  * `BinaryFileDemo.java`: Ghi và đọc danh sách đối tượng SinhVien, MonHoc nhị phân.
  * `Class_Images.java`: Đọc và ghi tệp hình ảnh.

#### 2. `Lab2_ReviewJava/` (Thực hành Lab 01 – Review Java)
* **Bài 1**: `HCN.java`, `HVuong.java`, `HTG.java`, `DemoHinh.java` (Chu vi & diện tích hình học).
* **Bài 2**: `IntArray.java`, `DemoArray.java` (Mảng n phần tử, Radix Sort, tìm kiếm nhị phân).
* **Bài 3, 4, 5**: `Person.java`, `Student.java`, `StudentDemo.java` (Quản lý sinh viên, kiểm tra định dạng email, xét học bổng).
* **Bài 6**: `ClassHierarchyDemo.java` (Field hiding & gán giá trị biến x của lớp A từ C).
* **Bài 7**: `PolygonDemo.java` (Interface Polygon, các loại đa giác, tính diện tích/chu vi theo kích thước và toạ độ đỉnh theo công thức Shoelace).

#### 3. `Lab3_OOPJava/` (OOP trong Java)
* **Bài 1**: `SanPham.java`, `MainBai1.java` (Quản lý sản phẩm - Tính đóng gói, nhập hàng, kiểm soát bán hàng và tồn kho).
* **Bài 2**: `Nguoi.java`, `SinhVien.java`, `GiangVien.java`, `MainBai2.java` (Quản lý nhân sự trường đại học - Tính kế thừa, xếp loại sinh viên, tính lương giảng viên).

#### 4. `Lab4_JavaIOStream/` (Lập trình Socket TCP và UDP trong Java)
* **`src/vidu41/HostInspector.java`**: Khảo sát địa chỉ mạng bằng `InetAddress`, phân loại IPv4/IPv6, Loopback và Site Local, xử lý host không tồn tại.
* **`src/vidu42/`**: `TcpCommandServer.java` & `TcpCommandClient.java` (Giao thức dòng UTF-8 qua TCP, hỗ trợ PING, TIME, UPPER, QUIT).
* **`src/vidu43/MultiClientTcpServer.java`**: TCP server phục vụ đồng thời nhiều client bằng ExecutorService (Fixed Thread Pool).
* **`src/vidu44/`**: `UdpEchoServer.java` & `UdpEchoClient.java` (UDP request-response, buffer 4096 bytes, `setSoTimeout(3000)`).
* **`src/baitap1/HostAndUriInspector.java`**: Khảo sát hostname và phân tích chi tiết các thành phần URI (scheme, host, port, path, query, fragment), minh chứng đầy đủ 3 ca hợp lệ và 3 ca lỗi.
* **`src/baitap2/`**: `DigitToWordServer.java` & `DigitToWordClient.java` (TCP đổi chữ số 0-9 thành tiếng Việt, xử lý các ca biên bắt buộc: 0, 9, chuỗi rỗng, 10, ký tự 'a', khoảng trắng).
* **`src/baitap3/`**: `DateTimeTcpServer.java`, `DateTimeTcpClient.java`, `DateTimeUdpServer.java`, `DateTimeUdpClient.java` (Dịch vụ DATE, TIME, DATETIME trên cả TCP và UDP; so sánh hành vi xử lý khi Server dừng đột ngột với cơ chế timeout 3 giây của UDP).
* **`src/baitap4/`**: `CalculatorServer.java` & `CalculatorClient.java` (Máy tính từ xa TCP: `CALC <op> <a b>`, kiểm soát lỗi chia cho 0, mã lỗi `ERR DIVIDE_BY_ZERO`, `ERR INVALID_NUMBER`, `ERR INVALID_FORMAT`).
* **`src/baitap5/`**: `ChatServer.java` & `ChatClient.java` (Chat TCP nhiều client: Quản lý nickname duy nhất, thread-safe bằng `ConcurrentHashMap`, broadcast tin nhắn, danh sách online USERS, ngắt kết nối an toàn).
* **`src/baitap6/`**: `BenchmarkServer.java` & `BenchmarkClient.java` (Thực nghiệm so sánh hiệu năng TCP và UDP: Gửi 1.000 gói tin 256 bytes qua 5 lượt, đo đạc độ trễ latency và throughput).
* **`src/baitap7/`**: `LogServer.java` & `LogClient.java` (Server lưu nhật ký tin nhắn: Bắt buộc HELLO clientId hợp lệ theo Regex `^[a-zA-Z0-9_-]+$`, ghi log đồng bộ đa luồng vào file `data/logs/{clientId}.txt`).
* **`src/baitap8/`**: `FileTransferServer.java` & `FileTransferClient.java` (Truyền file TCP an toàn: Gửi metadata, chống Path Traversal `../`, đọc chính xác số byte, kiểm tra toàn vẹn băm SHA-256).
* **`src/baitap9/`**: `MulticastReceiver.java` & `MulticastSender.java` (Multicast thông báo: Nhóm quản trị cục bộ `239.255.0.1:5019`, cấu hình `NetworkInterface`, giải pháp tương thích phòng lab/loopback khi mạng chặn multicast).
* **`src/baitap10/`**: `ServiceDiscoveryServer.java` & `ServiceDiscoveryClient.java` (Khám phá dịch vụ 2 tầng: Tầng 1 UDP broadcast discovery $\rightarrow$ Tầng 2 tự động kết nối TCP service tương ứng và gọi lệnh).
* **`screenshots/`**: Bộ 12 ảnh chụp toàn màn hình VS Code + kết quả chạy chi tiết của từng ví dụ và bài tập đề xuất (từ Ví dụ 4.1 đến Bài tập 10).
* **`BAO_CAO_LAB4.md`**: Báo cáo tổng hợp chi tiết bài làm có gắn kèm link GitHub và toàn bộ ảnh chụp minh chứng.
