package Lab1_JavaBasic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class MonHoc {
    private String tenMonHoc;
    private int tinChi;
    private double diem;

    public MonHoc(String tenMonHoc, int tinChi, double diem) {
        this.tenMonHoc = tenMonHoc;
        this.tinChi = tinChi;
        this.diem = diem;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public int getTinChi() {
        return tinChi;
    }

    public double getDiem() {
        return diem;
    }

    @Override
    public String toString() {
        return "MonHoc [ten=" + tenMonHoc + ", tinChi=" + tinChi + ", diem=" + diem + "]";
    }
}

class SinhVien {
    private String mssv;
    private String ten;
    private int tuoi;
    private ArrayList<MonHoc> listMH;

    public SinhVien(String mssv, String ten, int tuoi, ArrayList<MonHoc> listMH) {
        this.mssv = mssv;
        this.ten = ten;
        this.tuoi = tuoi;
        this.listMH = listMH;
    }

    public String getMssv() {
        return mssv;
    }

    public String getTen() {
        return ten;
    }

    public int getTuoi() {
        return tuoi;
    }

    public ArrayList<MonHoc> getListMH() {
        return listMH;
    }

    @Override
    public String toString() {
        return "SinhVien [mssv=" + mssv + ", ten=" + ten + ", tuoi=" + tuoi + ", monHoc=" + listMH + "]";
    }
}

public class BinaryFileDemo {
    public static void saveSV(String src, ArrayList<SinhVien> listSV) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(src)))) {
            dos.writeInt(listSV.size());
            for (SinhVien sv : listSV) {
                dos.writeUTF(sv.getMssv());
                dos.writeUTF(sv.getTen());
                dos.writeInt(sv.getTuoi());
                dos.writeInt(sv.getListMH().size());
                for (MonHoc mh : sv.getListMH()) {
                    dos.writeUTF(mh.getTenMonHoc());
                    dos.writeInt(mh.getTinChi());
                    dos.writeDouble(mh.getDiem());
                }
            }
            dos.flush();
        }
        System.out.println("Ghi file nhị phân thành công vào: " + src);
    }

    public static void loadSV(String src) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(new File(src)))) {
            int size = dis.readInt();
            ArrayList<SinhVien> listSV = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String mssv = dis.readUTF();
                String name = dis.readUTF();
                int age = dis.readInt();
                int sizemh = dis.readInt();
                ArrayList<MonHoc> listMH = new ArrayList<>();
                for (int j = 0; j < sizemh; j++) {
                    String tenMonHoc = dis.readUTF();
                    int tinChi = dis.readInt();
                    double diem = dis.readDouble();
                    listMH.add(new MonHoc(tenMonHoc, tinChi, diem));
                }
                listSV.add(new SinhVien(mssv, name, age, listMH));
            }
            System.out.println("--- Đọc danh sách sinh viên từ file nhị phân ---");
            for (SinhVien sv : listSV) {
                System.out.println(sv);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MonHoc mh = new MonHoc("ltcb", 3, 6.7);
        MonHoc mh1 = new MonHoc("ltw", 3, 7.5);
        MonHoc mh2 = new MonHoc("tkhdt", 3, 8.0);

        ArrayList<MonHoc> listMH = new ArrayList<>();
        listMH.add(mh);
        listMH.add(mh1);
        listMH.add(mh2);

        ArrayList<SinhVien> listSV = new ArrayList<>();
        listSV.add(new SinhVien("11329078", "nguyen van A", 23, listMH));
        listSV.add(new SinhVien("11329079", "nguyen Van B", 22, listMH));

        String filePath = "sinhvien_binary.dat";
        saveSV(filePath, listSV);
        loadSV(filePath);

        new File(filePath).delete();
    }
}
