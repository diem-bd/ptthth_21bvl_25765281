package Lab1_JavaBasic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Class_Images {
    private static final Logger LOGGER = Logger.getLogger(Class_Images.class.getName());

    public static void saveFile(File path, String tfile, byte[] bfile) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bfile));
            ImageIO.write(img, tfile, path);
            System.out.println("Ghi ảnh thành công vào: " + path.getAbsolutePath());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] readFile(File path) {
        try (FileInputStream fis = new FileInputStream(path);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int readNum;
            while ((readNum = fis.read(buf)) != -1) {
                bos.write(buf, 0, readNum);
            }
            return bos.toByteArray();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Class_Images sẵn sàng để đọc/ghi file ảnh.");
    }
}
