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
        if (!folder.exists()) {
            System.out.println("folder không tồn tại");
            return false;
        }
        File[] listFile = folder.listFiles();
        if (listFile != null) {
            for (File f : listFile) {
                if (f.isFile() && !f.delete()) {
                    System.out.println("Không thể xóa file: " + f.getAbsolutePath());
                    return false;
                }
            }
        }
        // Chỉ báo thành công khi thư mục thực sự bị xóa.
        if (folder.delete()) {
            System.out.println("Delete folder thành công!");
            return true;
        }
        System.out.println("Không thể xóa folder (có thể vẫn còn nội dung)");
        return false;
    }

    // TH3: Delete thư mục chứa danh sách thư mục con và file con (đệ quy)
    public boolean deleteRecursive(String source) throws IOException {
        File folder = new File(source);
        if (!folder.exists()) {
            System.out.println("folder không tồn tại: " + source);
            return false;
        }
        File[] listFile = folder.listFiles();
        if (listFile != null) {
            for (File f : listFile) {
                if (f.isFile()) {
                    if (!f.delete()) {
                        System.out.println("Không thể xóa file: " + f.getAbsolutePath());
                        return false;
                    }
                } else if (f.isDirectory() && !deleteRecursive(f.getAbsolutePath())) {
                    return false;
                }
            }
        }
        if (folder.delete()) {
            System.out.println("Delete folder thành công: " + source);
            return true;
        }
        System.out.println("Không thể xóa folder: " + source);
        return false;
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
