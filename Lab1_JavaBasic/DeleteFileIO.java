package Lab1_JavaBasic;

import java.io.File;

public class DeleteFileIO {
    public void deleteFile(String source) {
        File file = new File(source);
        if (file.exists()) {
            System.out.println("file ton tai");
            if (file.delete()) {
                System.out.println("xoa file thanh cong");
            } else {
                System.out.println("khong the xoa file");
            }
        } else {
            System.out.println("file khong ton tai");
        }
    }

    public static void main(String[] args) {
        DeleteFileIO deleteFileIO = new DeleteFileIO();
        // Demo với file tạm
        String path = "test_delete.txt";
        try {
            File f = new File(path);
            f.createNewFile();
            System.out.println("Đã tạo file demo: " + f.getAbsolutePath());
            deleteFileIO.deleteFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
