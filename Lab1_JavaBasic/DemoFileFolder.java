package Lab1_JavaBasic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** Chạy gọn 6 thao tác File/Folder của Lab 01 để chụp minh chứng trong một lượt. */
public class DemoFileFolder {
    public static void main(String[] args) throws IOException {
        File base = new File("lab01-thu-nghiem");
        base.mkdirs();

        System.out.println("=== 1. XÓA TỆP ===");
        File file = new File(base, "demo.txt");
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write("noi dung thu".getBytes());
        }
        System.out.println("Đã tạo: " + file.getPath());
        DeleteFileIO deleteFile = new DeleteFileIO();
        deleteFile.deleteFile(file.getPath());
        deleteFile.deleteFile(file.getPath());

        System.out.println("\n=== 2. XÓA THƯ MỤC ===");
        DeleteFolderDemo folderDemo = new DeleteFolderDemo();
        File empty = new File(base, "rong");
        empty.mkdir();
        System.out.println("TH1 thư mục rỗng: " + folderDemo.deleteEmptyFolder(empty.getPath()));

        File withFiles = new File(base, "co-tap");
        withFiles.mkdir();
        new File(withFiles, "a.txt").createNewFile();
        new File(withFiles, "b.txt").createNewFile();
        System.out.println("TH2 thư mục chứa tệp: " + folderDemo.deleteListFileInfolder(withFiles.getPath()));

        File tree = new File(base, "cay/con");
        tree.mkdirs();
        new File(tree, "c.txt").createNewFile();
        System.out.println("TH3 thư mục lồng nhau: " + folderDemo.deleteRecursive(base.getPath()));

        System.out.println("\n=== 3. TÌM TỆP THEO ĐUÔI ===");
        File search = new File("lab01-tim-kiem");
        new File(search, "con").mkdirs();
        new File(search, "ghi-chu.txt").createNewFile();
        new File(search, "con/du-lieu.txt").createNewFile();
        new File(search, "ChuongTrinh.java").createNewFile();
        FindFileDemo finder = new FindFileDemo();
        finder.finFile(search.getPath(), ".txt");
        System.out.println("Đã xóa thư mục tìm kiếm: " + folderDemo.deleteRecursive(search.getPath()));

        System.out.println("\n=== 4. SAO CHÉP TỆP ===");
        File source = new File("lab01-nguon.txt");
        try (FileOutputStream out = new FileOutputStream(source)) {
            out.write("Hello Java Copy File!".getBytes());
        }
        CopyFileDemo copyDemo = new CopyFileDemo();
        copyDemo.copyFile(source.getPath(), "lab01-dich.txt");
        System.out.println("Nguồn: " + source.length() + " byte");
        System.out.println("Đích : " + new File("lab01-dich.txt").length() + " byte");
        System.out.println("Đã dọn tệp thử: "
                + (source.delete() & new File("lab01-dich.txt").delete()));
    }
}
