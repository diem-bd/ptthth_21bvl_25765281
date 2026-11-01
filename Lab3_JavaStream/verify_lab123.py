"""Kiểm thử tích hợp: chạy Java thật, xác nhận đầu ra và tệp, không giả lập kết quả."""

import hashlib
import json
from pathlib import Path
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
LAB = ROOT / "Lab3_JavaStream"
EVIDENCE = ROOT.parent / "screenshots_lab123"


def run(class_name, text="", args=(), cwd=LAB):
    result = subprocess.run(
        ["java", "-Dfile.encoding=UTF-8", "-cp", str(ROOT / "bin/lab123"), class_name, *args],
        input=text, text=True, encoding="utf-8", stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT, cwd=cwd, timeout=20,
    )
    if result.returncode:
        raise AssertionError(result.stdout)
    return result.stdout


def main():
    records = []
    def check(name, condition):
        if not condition:
            raise AssertionError(name)
        print("PASS:", name)
        records.append({"case": name, "result": "PASS"})

    with tempfile.TemporaryDirectory(prefix="lab123-", dir="/private/var/folders/q8/9t2txpjx5fl6pjvn99v735gw0000gn/T/opencode") as temporary:
        work = Path(temporary)
        (work / "data").mkdir()
        out = run("Lab3_JavaStream.ConsoleReaderDemo", "Xin chào Java\nDữ liệu tiếng Việt\nq\nkhông được đọc\n", cwd=work)
        check("Console: UTF-8, dừng ở q, đếm 2 dòng", "Tổng số dòng đã nhập: 2" in out and "không được đọc" not in out)
        check("Console: EOF khi chưa có dữ liệu", "Tổng số dòng đã nhập: 0" in run("Lab3_JavaStream.ConsoleReaderDemo", cwd=work))
        run("Lab3_JavaStream.TextFileDemo", cwd=work)
        file = work / "data/ghi_chu.txt"
        check("Text: ghi mới 3 dòng UTF-8", len(file.read_text().splitlines()) == 3 and "tiếng Việt" in file.read_text())
        run("Lab3_JavaStream.TextFileDemo", args=("--append",), cwd=work)
        check("Text: append tăng lên 6 dòng", len(file.read_text().splitlines()) == 6)
        source = work / "data/source.bin"
        source.write_bytes(bytes(range(256)) * 37 + b"last-120-bytes" * 9)
        out = run("Lab3_JavaStream.BinaryFileCopy", args=("data/source.bin", "data/copy.bin"), cwd=work)
        target = work / "data/copy.bin"
        check("Binary: lần đọc cuối ngắn hơn 8192, SHA-256 giống nhau", hashlib.sha256(source.read_bytes()).digest() == hashlib.sha256(target.read_bytes()).digest())
        before = source.read_bytes()
        out = run("Lab3_JavaStream.BinaryFileCopy", args=("data/source.bin", "data/source.bin"), cwd=work)
        check("Binary: từ chối nguồn trùng đích, giữ nguyên nguồn", "hai tệp khác nhau" in out and source.read_bytes() == before)
        out = run("Lab3_JavaStream.BinaryFileCopy", args=("data/missing.bin", "data/not-created.bin"), cwd=work)
        check("Binary: báo thiếu nguồn, không tạo tệp đích", "Sao chép thất bại" in out and not (work / "data/not-created.bin").exists())
        run("Lab3_JavaStream.BinaryFileCopy", args=("data/source.bin", "data/copy.bin"), cwd=work)
        check("Binary: không ghi đè tệp có sẵn", target.read_bytes() == before)
        (work / "data/products.csv").write_bytes((LAB / "data/products.csv").read_bytes())
        out = run("Lab3_JavaStream.ProductCsvApp", cwd=work)
        check("CSV thực hành 4: 3 sản phẩm, tổng 6,550,000 VND", "6,550,000" in out and "Số sản phẩm: 3" in (work / "data/report.txt").read_text())

        valid = "1\n3\nSP01\nBàn phím cơ\n350000\n10\nSP02\nChuột không dây\n250000\n5\nSP03\nTai nghe\n600000\n3\n2\n\n3\n4\n0\n"
        out = run("Lab3_JavaStream.InventoryManager", valid, cwd=work)
        check("Inventory: nhập, lưu, đọc lại 3 sản phẩm; tổng đúng", "Đã đọc 3 sản phẩm" in out and "6,550,000" in out)
        saved = (work / "data/inventory.csv").read_text(encoding="utf-8")
        check("Inventory: giữ đúng dấu tiếng Việt sau vòng lưu/đọc", "Bàn phím cơ" in saved and "Chuột không dây" in out)
        report = (work / "data/inventory-report.txt").read_text()
        check("Inventory: báo cáo tổng hợp và sản phẩm cao nhất SP01", "Số sản phẩm: 3" in report and "cao nhất: SP01" in report)
        invalid = "1\n1\n\nBàn phím\n350000\n1\nSP01\nBàn phím\n-1000\n1\nSP01\nBàn phím\nabc\n1\nSP01\nBàn phím\n350000\n1\n3\n0\n"
        out = run("Lab3_JavaStream.InventoryManager", invalid, cwd=work)
        check("Inventory: mã rỗng, giá âm, nhập chữ rồi nhập lại thành công", out.count("Từ chối:") == 3 and "Số sản phẩm: 1" in out)
        errors = LAB / "data/inventory-errors.csv"
        out = run("Lab3_JavaStream.InventoryManager", f"2\n{errors}\n3\n0\n", cwd=work)
        check("Inventory: bỏ qua 6 dòng CSV lỗi, chỉ rõ số dòng/tệp", out.count("Bỏ qua dòng.") == 6 and "dòng 3" in out and "dòng 9" in out and "Số sản phẩm: 2" in out)
        out = run("Lab3_JavaStream.InventoryManager", "2\ndata/does-not-exist.csv\n3\n0\n", cwd=work)
        check("Inventory: thiếu tệp, tiếp tục menu và danh sách rỗng", "Không tìm thấy tệp: data/does-not-exist.csv" in out and "Danh sách rỗng" in out)
        out = run("Lab3_JavaStream.InventoryManager", "1\n1\n", cwd=work)
        check("Inventory: EOF giữa lúc nhập không lưu dữ liệu dở dang", "EOF" in out and "Đã lưu" not in out)

    polygon = "Lab2_ReviewJava.PolygonDemo"
    out = run(polygon, "4\n8\n5\n3\n6\n6\n5\n7\n5\n8\n5\n0\n", cwd=ROOT)
    check("Polygon: menu nhập hình, chữ nhật 8x5, đa giác đều", "Chu vi = 26.00 | Diện tích = 40.00" in out and "Octagon" in out)
    out = run(polygon, "1\n1\n2\n3\n4\n-1\nabc\n8\n5\n0\n", cwd=ROOT)
    check("Polygon: tam giác sai, số âm, chữ; tiếp tục chạy", "không tạo thành tam giác" in out and "phải lớn hơn 0" in out and "Diện tích = 40.00" in out)
    out = run(polygon, "10\n3\n0\n0\n4\n0\n0\n3\n3\n1\n1\n5\n1\n1\n4\n0\n", cwd=ROOT)
    check("Polygon: hai tam giác tịnh tiến bằng nhau", "GIỐNG NHAU" in out)
    out = run(polygon, "10\n3\n0\n0\n4\n0\n0\n3\n3\n1\n4\n5\n1\n1\n1\n0\n", cwd=ROOT)
    check("Polygon: đảo chiều và đổi đỉnh bắt đầu vẫn bằng nhau", "GIỐNG NHAU" in out)
    out = run(polygon, "10\n3\n0\n0\n4\n0\n0\n3\n3\n0\n0\n8\n0\n0\n6\n0\n", cwd=ROOT)
    check("Polygon: khác kích thước không bằng nhau", "KHÁC NHAU" in out)
    out = run(polygon, "9\n4\n0\n0\n4\n4\n0\n4\n4\n0\n0\n", cwd=ROOT)
    check("Polygon: từ chối đa giác tự cắt", "tự cắt nhau" in out)
    out = run("Lab1_JavaBasic.BaiTapCoBan", "Bùi Dương Diễm\n12\n8\n7\n1\n12\n0\n", cwd=ROOT)
    check("Lab01: tổng, lẻ, tháng và tên", all(x in out for x in ["Bùi Dương Diễm", "12 + 8 = 20", "số lẻ", "January", "December"]))
    out = run("Lab2_ReviewJava.DemoArray", cwd=ROOT)
    check("Lab01b: Radix và tìm kiếm đúng dữ liệu mẫu", "2 24 45 66 75 90 111 170 802" in out and "Vị trí tìm thấy: 8" in out)
    out = run("Lab3_OOPJava.MainBai1", cwd=ROOT)
    check("Lab02: nhập kho 15, bán còn 2, bán quá kho thất bại", "Tồn kho mới: 15" in out and "còn lại: 2" in out and "Kết quả bán: false" in out)
    out = run("Lab3_OOPJava.MainBai2", cwd=ROOT)
    check("Lab02: xếp loại và lương", all(x in out for x in ["Giỏi", "Trung bình", "35,000,000", "23,800,000"]))

    EVIDENCE.mkdir(exist_ok=True)
    (EVIDENCE / "verification.json").write_text(json.dumps(records, ensure_ascii=False, indent=2))
    print(f"\nĐã đạt {len(records)}/{len(records)} kiểm tra tích hợp.")


if __name__ == "__main__":
    main()
