package Lab2_ReviewJava;

public class HVuong extends HCN {
    public HVuong() {
        super(0, 0);
    }

    public HVuong(double canh) {
        super(canh, canh);
    }

    public double getCanh() {
        return getDai();
    }

    public void setCanh(double canh) {
        setDai(canh);
        setRong(canh);
    }

    @Override
    public void xuatThongTin() {
        System.out.printf("Hình Vuông: Cạnh = %.2f | Chu vi = %.2f | Diện tích = %.2f%n",
                getDai(), tinhChuVi(), tinhDienTich());
    }
}
