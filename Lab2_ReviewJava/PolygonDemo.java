package Lab2_ReviewJava;

import java.util.ArrayList;
import java.util.List;
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
        this.vertices = new ArrayList<>(vertices);
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
        // Kiểm tra cùng toạ độ đỉnh hoặc cùng diện tích và chu vi
        return Math.abs(this.area() - other.area()) < 1e-4 &&
               Math.abs(this.perimeter() - other.perimeter()) < 1e-4;
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
        System.out.println("=== DEMO BÀI 7: HỆ THỐNG ĐA GIÁC (POLYGON) ===");

        // 1. Tạo các đa giác theo kích thước
        Polygon eqTriangle = new EquilateralTriangle(6.0);
        Polygon rect = new Rectangle(8.0, 5.0);
        Polygon sq = new Square(4.0);
        Polygon penta = new Pentagon(5.0);
        Polygon hexa = new Hexagon(5.0);
        Polygon octa = new Octagon(5.0);

        System.out.println("1. Diện tích và chu vi theo kích thước:");
        System.out.printf("- Tam giác đều (cạnh 6): Chu vi = %.2f | Diện tích = %.2f%n", eqTriangle.perimeter(), eqTriangle.area());
        System.out.printf("- Hình chữ nhật (8x5):  Chu vi = %.2f | Diện tích = %.2f%n", rect.perimeter(), rect.area());
        System.out.printf("- Hình vuông (cạnh 4):   Chu vi = %.2f | Diện tích = %.2f%n", sq.perimeter(), sq.area());
        System.out.printf("- Ngũ giác đều (cạnh 5): Chu vi = %.2f | Diện tích = %.2f%n", penta.perimeter(), penta.area());
        System.out.printf("- Lục giác đều (cạnh 5): Chu vi = %.2f | Diện tích = %.2f%n", hexa.perimeter(), hexa.area());
        System.out.printf("- Bát giác đều (cạnh 5): Chu vi = %.2f | Diện tích = %.2f%n", octa.perimeter(), octa.area());

        // 2. Nhập và kiểm tra đa giác theo toạ độ đỉnh
        System.out.println("\n2. Nhập đa giác qua toạ độ đỉnh (Ví dụ: Tam giác và Tứ giác):");
        List<Point> vertices1 = List.of(new Point(0, 0), new Point(4, 0), new Point(0, 3));
        CoordinatePolygon poly1 = new CoordinatePolygon(vertices1);
        System.out.printf("Đa giác 1 (Đỉnh: %s): Chu vi = %.2f | Diện tích = %.2f%n",
                poly1.getVertices(), poly1.perimeter(), poly1.area());

        List<Point> vertices2 = List.of(new Point(1, 1), new Point(5, 1), new Point(1, 4));
        CoordinatePolygon poly2 = new CoordinatePolygon(vertices2);
        System.out.printf("Đa giác 2 (Đỉnh: %s): Chu vi = %.2f | Diện tích = %.2f%n",
                poly2.getVertices(), poly2.perimeter(), poly2.area());

        boolean same = poly1.isSameAs(poly2);
        System.out.println("-> Kiểm tra hai đa giác có giống nhau (cùng diện tích và chu vi): " + (same ? "GIỐNG NHAU" : "KHÁC NHAU"));
    }
}
