package Lab2_ReviewJava;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return Math.abs(this.x - p.x) < 1e-6 && Math.abs(this.y - p.y) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
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
        for (int i = 0; i < vertices.size(); i++) {
            Point point = vertices.get(i);
            if (point == null || !Double.isFinite(point.x) || !Double.isFinite(point.y)) {
                throw new IllegalArgumentException("Tọa độ phải là số hữu hạn.");
            }
            for (int j = 0; j < i; j++) {
                if (point.distance(vertices.get(j)) < 1e-8) {
                    throw new IllegalArgumentException("Các đỉnh không được trùng nhau.");
                }
            }
        }
        this.vertices = new ArrayList<>(vertices);
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Hai cạnh kề có chung đỉnh nên không phải giao cắt bên trong.
                if (j == i + 1 || (i == 0 && j == n - 1)) continue;
                if (java.awt.geom.Line2D.linesIntersect(
                        vertices.get(i).x, vertices.get(i).y,
                        vertices.get((i + 1) % n).x, vertices.get((i + 1) % n).y,
                        vertices.get(j).x, vertices.get(j).y,
                        vertices.get((j + 1) % n).x, vertices.get((j + 1) % n).y)) {
                    throw new IllegalArgumentException("Các cạnh không được tự cắt nhau.");
                }
            }
        }
        if (area() <= 1e-8 || !Double.isFinite(area()) || !Double.isFinite(perimeter())) {
            throw new IllegalArgumentException("Đa giác suy biến hoặc tọa độ quá lớn.");
        }
    }

    public List<Point> getVertices() {
        return vertices;
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
        // Bằng nhau về hình dạng/kích thước (congruent), cho phép tịnh tiến,
        // xoay, đối xứng, đổi đỉnh bắt đầu và đảo chiều liệt kê đỉnh.
        // So sánh mọi khoảng cách cặp đỉnh, không chỉ diện tích/chu vi.
        int n = vertices.size();
        for (int start = 0; start < n; start++) {
            for (int direction : new int[] {1, -1}) {
                boolean matches = true;
                for (int i = 0; i < n && matches; i++) {
                    for (int j = i + 1; j < n; j++) {
                        int mappedI = Math.floorMod(start + direction * i, n);
                        int mappedJ = Math.floorMod(start + direction * j, n);
                        double a = vertices.get(i).distance(vertices.get(j));
                        double b = other.vertices.get(mappedI).distance(other.vertices.get(mappedJ));
                        if (Math.abs(a - b) > 1e-6 * Math.max(1, Math.max(a, b))) {
                            matches = false;
                            break;
                        }
                    }
                }
                if (matches) return true;
            }
        }
        return false;
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
        // Diện tích xấp xỉ theo công thức Brahmagupta nếu là tứ giác nội tiếp
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
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (!Double.isFinite(value)) throw new NumberFormatException();
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hữu hạn, dùng dấu chấm cho phần thập phân.");
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
            double value = readNumber(scanner, prompt);
            if (value >= min && value <= max && value == Math.rint(value)) return (int) value;
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
