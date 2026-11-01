package Lab1_JavaBasic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Gọi trực tiếp hai phương thức đọc/ghi ảnh của Class_Images để chụp minh chứng. */
public class DemoImages {
    public static void main(String[] args) throws IOException {
        File source = new File("Lab3_JavaStream/data/source.png");
        if (!source.exists()) {
            System.out.println("Không tìm thấy ảnh thử: " + source.getAbsolutePath());
            return;
        }
        byte[] bytes = Class_Images.readFile(source);
        System.out.println("Ảnh nguồn: " + source.getPath());
        System.out.println("Số byte đã đọc: " + bytes.length);

        File target = new File("lab01-anh-ra.png");
        Class_Images.saveFile(target, "png", bytes);

        BufferedImage before = ImageIO.read(new ByteArrayInputStream(bytes));
        BufferedImage after = ImageIO.read(new ByteArrayInputStream(read(target)));
        System.out.println("Kích thước: " + after.getWidth() + " x " + after.getHeight());
        boolean equal = before.getWidth() == after.getWidth()
                && before.getHeight() == after.getHeight()
                && java.util.Arrays.equals(
                        before.getRGB(0, 0, before.getWidth(), before.getHeight(), null, 0, before.getWidth()),
                        after.getRGB(0, 0, after.getWidth(), after.getHeight(), null, 0, after.getWidth()));
        System.out.println("Điểm ảnh giống nhau: " + equal);
        System.out.println("Đã dọn tệp thử: " + target.delete());
    }

    private static byte[] read(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            return out.toByteArray();
        }
    }
}
