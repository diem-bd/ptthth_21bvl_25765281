package Lab3_OOPJava;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private double donGia;
    private int soLuongTonKho;

    // Constructor đầy đủ tham số
    public SanPham(String maSanPham, String tenSanPham, double donGia, int soLuongTonKho) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donGia = donGia;
        this.soLuongTonKho = soLuongTonKho;
    }

    public String getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public double getDonGia() { return donGia; }
    public int getSoLuongTonKho() { return soLuongTonKho; }

    public double tinhThanhTien() {
        return donGia * soLuongTonKho;
    }

    public void nhapHang(int soLuongNhap) {
        if (soLuongNhap > 0) {
            this.soLuongTonKho += soLuongNhap;
            System.out.printf("-> Nhập thêm %d sản phẩm '%s' thành công. Tồn kho mới: %d%n",
                    soLuongNhap, tenSanPham, this.soLuongTonKho);
        } else {
            System.out.println("-> Lỗi: Số lượng nhập phải lớn hơn 0!");
        }
    }

    public boolean banHang(int soLuongBan) {
        if (soLuongBan <= 0) {
            System.out.println("-> Lỗi: Số lượng bán phải lớn hơn 0!");
            return false;
        }
        if (soLuongBan > this.soLuongTonKho) {
            System.out.printf("-> Thất bại: Không đủ hàng trong kho (yêu cầu: %d, tồn kho: %d).%n",
                    soLuongBan, this.soLuongTonKho);
            return false;
        }
        this.soLuongTonKho -= soLuongBan;
        System.out.printf("-> Bán thành công %d sản phẩm '%s'. Tồn kho còn lại: %d%n",
                soLuongBan, tenSanPham, this.soLuongTonKho);
        return true;
    }

    public void hienThiThongTin() {
        System.out.printf("Mã: %-8s | Tên: %-18s | Đơn giá: %,10.0f VNĐ | Tồn kho: %3d | Thành tiền: %,12.0f VNĐ%n",
                maSanPham, tenSanPham, donGia, soLuongTonKho, tinhThanhTien());
    }
}
