package Lab1_JavaBasic;

import java.io.File;
import java.io.IOException;

public class DeleteFolderDemo {
    // TH1: Delete thư mục rỗng
    public boolean deleteEmptyFolder(String source) {
        File folder = new File(source);
        if (folder.exists()) {
            if (folder.delete()) {
                System.out.println("folder ton tai\n xoa folder thanh cong");
                return true;
            } else {
                System.out.println("Khong the xoa folder (co the khong rong)");
                return false;
            }
        } else {
            System.out.println("folder khong ton tai");
            return false;
        }
    }

    // TH2: Delete thư mục chứa Files (không chứa thư mục con)
    public boolean deleteListFileInfolder(String source) {
        File folder = new File(source);
        if (folder.exists()) {
            File[] listFile = folder.listFiles();
            if (listFile != null && listFile.length != 0) {
                for (File f : listFile) {
                    if (f.isFile()) {
                        f.delete();
                    }
                }
            }
            folder.delete();
            System.out.println("Delete folder thành công!");
            return true;
        } else {
            System.out.println("folder không tồn tại");
            return false;
        }
    }

    // TH3: Delete thư mục chứa danh sách thư mục con và file con (đệ quy)
    public boolean deleteRecursive(String source) throws IOException {
        File folder = new File(source);
        if (folder.exists()) {
            File[] listFile = folder.listFiles();
            if (listFile != null && listFile.length != 0) {
                for (File f : listFile) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteRecursive(f.getAbsolutePath());
                    }
                }
            }
            folder.delete();
            System.out.println("Delete folder thành công: " + source);
            return true;
        } else {
            System.out.println("folder không tồn tại: " + source);
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        DeleteFolderDemo demo = new DeleteFolderDemo();
        File testDir = new File("test_dir/sub_dir");
        testDir.mkdirs();
        File testFile = new File(testDir, "sample.txt");
        testFile.createNewFile();
        System.out.println("Tạo cấu trúc thư mục thử nghiệm: " + testFile.getAbsolutePath());

        // Xóa đệ quy
        demo.deleteRecursive("test_dir");
    }
}
