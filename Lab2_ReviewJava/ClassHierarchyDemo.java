package Lab2_ReviewJava;

class ClassA {
    public int x = 10;
}

class ClassB extends ClassA {
    public int x = 20;
}

class ClassC extends ClassB {
    public int x = 30;

    // Phương thức trong C truy cập và gán giá trị cho trường x của A
    // bằng cách ép kiểu (A) this:
    public void setXOfA(int val) {
        ((ClassA) this).x = val;
    }

    public void displayAllX() {
        System.out.println("x trong ClassC (this.x)          = " + this.x);
        System.out.println("x trong ClassB (super.x)         = " + super.x);
        System.out.println("x trong ClassA (((ClassA)this).x)= " + ((ClassA) this).x);
    }
}

public class ClassHierarchyDemo {
    public static void main(String[] args) {
        System.out.println("=== DEMO BÀI 6: FIELD HIDING & TRUY CẬP TRƯỜNG CỦA LỚP TỔ TIÊN A ===");
        ClassC obj = new ClassC();
        System.out.println("--- Trước khi gán x của A ---");
        obj.displayAllX();

        System.out.println("\nGọi obj.setXOfA(999)...");
        obj.setXOfA(999);

        System.out.println("--- Sau khi gán x của A ---");
        obj.displayAllX();
    }
}
