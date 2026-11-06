package Lab2_ReviewJava;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

interface Polygon {
    double area();
    double perimeter();
}

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(%.2f, %.2f)".formatted(x, y);
    }
}

// Đa giác xác định bởi danh sách toạ độ đỉnh
class CoordinatePolygon implements Polygon {
    protected List<Point> vertices;

    public CoordinatePolygon(List<Point> vertices) {
        if (vertices == null || vertices.size() < 3) {
            throw new IllegalArgumentException("Đa giác cần ít nhất 3 đỉnh.");
        }
        this.vertices = new ArrayList<>(vertices);
    }

    @Override
    public double perimeter() {
        double p = 0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            p += vertices.get(i).distance(vertices.get((i + 1) % n));
        }
        return p;
    }

    // Công thức diện tích hình đa giác Shoelace (Dây giày Gauss)
    @Override
    public double area() {
        double sum = 0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Point current = vertices.get(i);
            Point next = vertices.get((i + 1) % n);
            sum += (current.x * next.y) - (next.x * current.y);
        }
        return Math.abs(sum) / 2.0;
    }

    public boolean isSameAs(CoordinatePolygon other) {
        if (other == null || this.vertices.size() != other.vertices.size()) return false;
        int n = vertices.size();
        // Hai đa giác "giống nhau" (bằng nhau về hình dạng và kích thước) nếu tồn tại phép
        // tịnh tiến + quay + lật biến đa giác này thành đa giác kia. Chỉ so sánh độ dài cạnh
        // là chưa đủ: hình vuông và hình thoi có thể có bốn cạnh bằng nhau nhưng khác hình.
        // Cách làm: đưa mỗi đa giác về hệ trục riêng (gốc là đỉnh đầu, trục x theo cạnh đầu),
        // sau đó so sánh toạ độ từng đỉnh. Thử mọi đỉnh bắt đầu và cả hai chiều duyệt.
        double[][] goc = chuanHoa(0, 1);
        if (goc == null) return false;
        double canhDau = vertices.get(0).distance(vertices.get(1));
        for (int start = 0; start < n; start++) {
            for (int direction = 1; direction >= -1; direction -= 2) {
                double canhDauKhac = other.vertices.get(start)
                        .distance(other.vertices.get(Math.floorMod(start + direction, n)));
                if (Math.abs(canhDau - canhDauKhac) > 1e-6) continue;
                double[][] khac = other.chuanHoa(start, direction);
                if (khac == null) continue;
                boolean khop = true;
                for (int i = 0; i < n && khop; i++) {
                    if (Math.abs(goc[i][0] - khac[i][0]) > 1e-6
                            || Math.abs(goc[i][1] - khac[i][1]) > 1e-6) {
                        khop = false;
                    }
                }
                if (khop) return true;
            }
        }
        return false;
    }

    /** Toạ độ các đỉnh sau khi dời gốc về đỉnh start và quay cạnh đầu về trục x. */
    private double[][] chuanHoa(int start, int direction) {
        int n = vertices.size();
        Point p0 = vertices.get(start);
        Point p1 = vertices.get(Math.floorMod(start + direction, n));
        double ux = p1.x - p0.x;
        double uy = p1.y - p0.y;
        double mau = ux * ux + uy * uy;
        if (mau < 1e-18) return null;
        double[][] ketQua = new double[n][2];
        for (int i = 0; i < n; i++) {
            Point p = vertices.get(Math.floorMod(start + direction * i, n));
            double dx = p.x - p0.x;
            double dy = p.y - p0.y;
            ketQua[i][0] = (dx * ux + dy * uy) / mau;
            ketQua[i][1] = (dy * ux - dx * uy) / mau;
        }
        return ketQua;
    }
}

class Triangle implements Polygon {
    protected double a, b, c;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    @Override
    public double area() {
        double p = perimeter() / 2.0;
        return Math.sqrt(Math.max(0, p * (p - a) * (p - b) * (p - c)));
    }
}

class IsoscelesTriangle extends Triangle {
    public IsoscelesTriangle(double side, double base) {
        super(side, side, base);
    }
}

class EquilateralTriangle extends IsoscelesTriangle {
    public EquilateralTriangle(double side) {
        super(side, side);
    }
}

class Quadrilateral implements Polygon {
    protected double a, b, c, d;

    public Quadrilateral(double a, double b, double c, double d) {
        this.a = a; this.b = b; this.c = c; this.d = d;
    }

    @Override
    public double perimeter() {
        return a + b + c + d;
    }

    @Override
    public double area() {
        // Với một tứ giác chỉ biết bốn cạnh thì diện tích KHÔNG xác định duy nhất
        // (cùng bốn cạnh có thể có nhiều hình khác nhau). Công thức Brahmagupta chỉ đúng
        // cho tứ giác nội tiếp và đó cũng là diện tích lớn nhất có thể.
        // Lớp con cụ thể (Rectangle, Square...) luôn ghi đè area() bằng công thức chính xác,
        // nên giá trị dưới đây chỉ là trường hợp tứ giác nội tiếp - đã ghi rõ khi in ra.
        double p = perimeter() / 2.0;
        return Math.sqrt(Math.max(0, (p - a) * (p - b) * (p - c) * (p - d)));
    }
}

class Rectangle extends Quadrilateral {
    protected double width, height;

    public Rectangle(double width, double height) {
        super(width, height, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}

class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }
}

// Ngũ giác đều (Pentagon)
class Pentagon implements Polygon {
    private double side;
    public Pentagon(double side) { this.side = side; }
    @Override public double perimeter() { return 5 * side; }
    @Override public double area() { return 0.25 * Math.sqrt(5 * (5 + 2 * Math.sqrt(5))) * side * side; }
}

// Lục giác đều (Hexagon)
class Hexagon implements Polygon {
    private double side;
    public Hexagon(double side) { this.side = side; }
    @Override public double perimeter() { return 6 * side; }
    @Override public double area() { return (3 * Math.sqrt(3) / 2) * side * side; }
}

// Bát giác đều (Octagon)
class Octagon implements Polygon {
    private double side;
    public Octagon(double side) { this.side = side; }
    @Override public double perimeter() { return 8 * side; }
    @Override public double area() { return 2 * (1 + Math.sqrt(2)) * side * side; }
}

public class PolygonDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== BÀI 7: HỆ THỐNG ĐA GIÁC – NHẬP TỪ BÀN PHÍM ===");
        try {
            while (true) {
                System.out.println("\n1. Tam giác  2. Tam giác cân  3. Tam giác đều  4. Chữ nhật");
                System.out.println("5. Vuông  6. Ngũ giác đều  7. Lục giác đều  8. Bát giác đều");
                System.out.println("9. Đa giác theo tọa độ  10. So sánh hai đa giác  0. Thoát");
                int choice = readInt(scanner, "Chọn: ", 0, 10);
                if (choice == 0) {
                    System.out.println("Kết thúc chương trình.");
                    return;
                }
                try {
                    Polygon polygon;
                    switch (choice) {
                        case 1:
                            double a = readPositive(scanner, "Cạnh a: ");
                            double b = readPositive(scanner, "Cạnh b: ");
                            double c = readPositive(scanner, "Cạnh c: ");
                            validateTriangle(a, b, c);
                            polygon = new Triangle(a, b, c);
                            break;
                        case 2:
                            double side = readPositive(scanner, "Cạnh bên: ");
                            double base = readPositive(scanner, "Cạnh đáy: ");
                            validateTriangle(side, side, base);
                            polygon = new IsoscelesTriangle(side, base);
                            break;
                        case 3:
                            polygon = new EquilateralTriangle(readPositive(scanner, "Cạnh: "));
                            break;
                        case 4:
                            polygon = new Rectangle(readPositive(scanner, "Chiều dài: "),
                                    readPositive(scanner, "Chiều rộng: "));
                            break;
                        case 5:
                            polygon = new Square(readPositive(scanner, "Cạnh: "));
                            break;
                        case 6:
                            polygon = new Pentagon(readPositive(scanner, "Cạnh: "));
                            break;
                        case 7:
                            polygon = new Hexagon(readPositive(scanner, "Cạnh: "));
                            break;
                        case 8:
                            polygon = new Octagon(readPositive(scanner, "Cạnh: "));
                            break;
                        case 9:
                            polygon = readPolygon(scanner);
                            break;
                        case 10:
                            System.out.println("Đa giác thứ nhất:");
                            CoordinatePolygon first = readPolygon(scanner);
                            display(first);
                            System.out.println("Đa giác thứ hai:");
                            CoordinatePolygon second = readPolygon(scanner);
                            display(second);
                            System.out.println("Cùng hình dạng và kích thước: "
                                    + (first.isSameAs(second) ? "GIỐNG NHAU" : "KHÁC NHAU"));
                            continue;
                        default:
                            throw new IllegalArgumentException("Lựa chọn không hợp lệ.");
                    }
                    display(polygon);
                } catch (IllegalArgumentException e) {
                    System.out.println("Dữ liệu không hợp lệ: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Đã hết dữ liệu bàn phím (EOF). Kết thúc chương trình.");
        }
    }

    private static double readNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số, dùng dấu chấm cho phần thập phân.");
            }
        }
    }

    private static double readPositive(Scanner scanner, String prompt) {
        while (true) {
            double value = readNumber(scanner, prompt);
            if (value > 0) return value;
            System.out.println("Kích thước phải lớn hơn 0.");
        }
    }

    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException e) {
                // rơi xuống dưới để in lại hướng dẫn
            }
            System.out.printf("Nhập số nguyên từ %d đến %d.%n", min, max);
        }
    }

    private static void validateTriangle(double a, double b, double c) {
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Ba cạnh không tạo thành tam giác.");
        }
    }

    private static CoordinatePolygon readPolygon(Scanner scanner) {
        int count = readInt(scanner, "Số đỉnh (3–100, nhập theo thứ tự quanh biên): ", 3, 100);
        List<Point> vertices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double x = readNumber(scanner, "Đỉnh " + (i + 1) + " – x: ");
            double y = readNumber(scanner, "Đỉnh " + (i + 1) + " – y: ");
            vertices.add(new Point(x, y));
        }
        return new CoordinatePolygon(vertices);
    }

    private static void display(Polygon polygon) {
        double area = polygon.area();
        double perimeter = polygon.perimeter();
        if (!Double.isFinite(area) || !Double.isFinite(perimeter)) {
            throw new IllegalArgumentException("Kích thước quá lớn để tính bằng double.");
        }
        System.out.printf("%s: Chu vi = %.2f | Diện tích = %.2f%n",
                polygon.getClass().getSimpleName(), perimeter, area);
    }
}
