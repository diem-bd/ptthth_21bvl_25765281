# Lab 03 — Java I/O Streams

Sáu file Java gồm đọc bàn phím, ghi/đọc UTF-8, sao chép byte, mô hình Product,
đọc CSV và InventoryManager. Thư mục `Lab3_OOPJava` cũ chứa **Lab 02** theo đề;
thư mục này chứa **Lab 03 — Java I/O Streams**.

Từ thư mục gốc repo:

```bash
javac -encoding UTF-8 --release 21 -d bin/lab123 Lab1_JavaBasic/*.java Lab2_ReviewJava/*.java Lab3_OOPJava/*.java Lab3_JavaStream/*.java
cd Lab3_JavaStream
java -cp ../bin/lab123 Lab3_JavaStream.ConsoleReaderDemo
java -cp ../bin/lab123 Lab3_JavaStream.TextFileDemo
java -cp ../bin/lab123 Lab3_JavaStream.BinaryFileCopy data/source.png data/copy-new.png
java -cp ../bin/lab123 Lab3_JavaStream.ProductCsvApp
java -cp ../bin/lab123 Lab3_JavaStream.InventoryManager
```

Menu InventoryManager: 1 nhập/lưu, 2 đọc CSV, 3 xem danh sách/tổng/cao nhất,
4 ghi báo cáo, 0 thoát. Khi chọn 2, Enter dùng `data/inventory.csv`;
nhập `data/inventory-errors.csv` để xem xử lý dòng lỗi.

Hai tệp nộp kèm: `data/inventory.csv`, `data/inventory-report.txt`.
CSV đơn giản không hỗ trợ mã/tên chứa dấu phẩy hoặc xuống dòng.
Byte copy dùng `CREATE_NEW`: chọn tên đích chưa tồn tại để chạy lại.

Kiểm thử tích hợp, từ thư mục gốc repo:

```bash
python3 Lab3_JavaStream/verify_lab123.py
```

Kết quả đã kiểm chứng: 26/26 kiểm tra đạt. `bin/lab123` là đầu ra riêng của
lệnh `javac --release 21`, tránh trộn class với Java Extension trong IDE.
