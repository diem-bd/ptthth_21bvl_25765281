package Lab1_JavaBasic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFileDemo {
    public boolean copyFile(String source, String dest) throws IOException {
        File sourceFile = new File(source);
        File destFile = new File(dest);

        if (!sourceFile.exists()) {
            System.out.println("file nguồn không tồn tại");
            return false;
        }
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            byte[] arr = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(arr)) != -1) {
                // Chỉ ghi đúng số byte đọc được ở lần cuối.
                fos.write(arr, 0, bytesRead);
            }
        }
        System.out.println("copy thành công");
        return true;
    }

    public static void main(String[] args) throws IOException {
        CopyFileDemo demo = new CopyFileDemo();
        // Tạo file nguồn tạm
        File src = new File("test_src.txt");
        try (FileOutputStream fos = new FileOutputStream(src)) {
            fos.write("Hello Java Copy File!".getBytes());
        }
        demo.copyFile("test_src.txt", "test_dest.txt");
        src.delete();
        new File("test_dest.txt").delete();
    }
}
