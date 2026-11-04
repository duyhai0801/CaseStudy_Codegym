package repository;

import entity.HocVien;
import entity.TrinhDo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Đọc/ghi data/hocvien.csv. Mỗi dòng: id,hoTen,soDienThoai,email,ngaySinh(yyyy-MM-dd),trinhDoDauVao
public class HocVienRepository implements IRepository<HocVien> {
    private static final String FILE_NAME = "data/hocvien.csv";

    @Override
    public void write(List<HocVien> list) {
        String dataStr = "";
        for (HocVien item : list) {
            dataStr += item.getId() + "," + item.getHoTen() + "," + item.getSoDienThoai() + ","
                    + item.getEmail() + "," + item.getNgaySinh() + "," + item.getTrinhDoDauVao().name() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[HocVienRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<HocVien> read() {
        List<HocVien> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                String hoTen = data[1];
                String soDienThoai = data[2];
                String email = data[3];
                LocalDate ngaySinh = LocalDate.parse(data[4]); // "2004-03-15" -> LocalDate
                TrinhDo trinhDoDauVao = TrinhDo.valueOf(data[5]);
                list.add(new HocVien(id, hoTen, soDienThoai, email, ngaySinh, trinhDoDauVao));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[HocVienRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
