package Lab2_ReviewJava;

public class HTG {
    private double a;
    private double b;
    private double c;

    public HTG() {
        this.a = 0;
        this.b = 0;
        this.c = 0;
    }

    public HTG(double a, double b, double c) {
        if (a + b > c && a + c > b && b + c > a) {
            this.a = a;
            this.b = b;
            this.c = c;
        } else {
            System.out.println("Cảnh báo: 3 cạnh không tạo thành tam giác hợp lệ! Gán mặc định bằng 0.");
            this.a = 0;
            this.b = 0;
            this.c = 0;
        }
    }

    public double getA() { return a; }
    public void setA(double a) { this.a = a; }

    public double getB() { return b; }
    public void setB(double b) { this.b = b; }

    public double getC() { return c; }
    public void setC(double c) { this.c = c; }

    public double tinhChuVi() {
        return a + b + c;
    }

    public double tinhDienTich() {
        double p = tinhChuVi() / 2.0;
        double s2 = p * (p - a) * (p - b) * (p - c);
        return s2 > 0 ? Math.sqrt(s2) : 0;
    }

    public void xuatThongTin() {
        System.out.printf("Hình Tam Giác: a = %.2f, b = %.2f, c = %.2f | Chu vi = %.2f | Diện tích = %.2f%n",
                a, b, c, tinhChuVi(), tinhDienTich());
    }
}
