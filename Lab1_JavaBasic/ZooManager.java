package Lab1_JavaBasic;

class Animal {
    private String name;
    private double weight;

    public Animal(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

class Lion extends Animal {
    private double eat; // Số cân thịt ăn mỗi ngày

    public Lion(String name, double weight, double eat) {
        super(name, weight);
        this.eat = eat;
    }

    public double getEat() {
        return eat;
    }

    public void setEat(double eat) {
        this.eat = eat;
    }

    public void hienThi() {
        System.out.printf("– Sư tử %s nặng %.0f cân và ăn %.0f cân thịt mỗi ngày.%n",
                getName(), getWeight(), eat);
    }
}

class Snake extends Animal {
    private double length; // Chiều dài (mét)

    public Snake(String name, double weight, double length) {
        super(name, weight);
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void hienThi() {
        System.out.printf("– Con rắn %s nặng %.0f cân và dài %.0f mét.%n",
                getName(), getWeight(), length);
    }
}

class Monkey extends Animal {
    private String favoriteFood; // Thức ăn yêu thích

    public Monkey(String name, double weight, String favoriteFood) {
        super(name, weight);
        this.favoriteFood = favoriteFood;
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public void hienThi() {
        System.out.printf("– Con khỉ %s nặng %.0f cân và thích ăn %s%n",
                getName(), getWeight(), favoriteFood);
    }
}

public class ZooManager {
    public static void main(String[] args) {
        Lion lion = new Lion("Leo", 300, 5);
        Snake snake = new Snake("Boa", 50, 5);
        Monkey monkey = new Monkey("George", 150, "chuối");

        System.out.println("=== Quản lý động vật trong vườn thú ===");
        lion.hienThi();
        snake.hienThi();
        monkey.hienThi();
    }
}
