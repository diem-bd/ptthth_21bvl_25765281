package Lab1_JavaBasic;

import java.io.File;

public class FindFileDemo {
    public void finFile(String source, String key) {
        File file = new File(source);
        if (file.exists()) {
            if (file.isFile()) {
                if (file.getName().endsWith(key)) {
                    System.out.println(file.getAbsolutePath());
                }
            } else if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (File f : listFile) {
                        finFile(f.getAbsolutePath(), key);
                    }
                }
            }
        } else {
            System.out.println("source không tồn tại: " + source);
        }
    }

    public static void main(String[] args) {
        FindFileDemo finder = new FindFileDemo();
        System.out.println("Tìm kiếm các file kết thúc bằng .java trong thư mục hiện tại:");
        finder.finFile(".", ".java");
    }
}
