package repository;

import entity.GiangVien;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GiangVienRepository implements IRepository<GiangVien> {
    private static final String FILE_NAME = "data/giangvien.csv";

    @Override
    public void write(List<GiangVien> list) {
        String dataStr = "";
        for (GiangVien item : list) {
            dataStr += item.getId() + "," + item.getHoTen() + "," + item.getSoDienThoai() + ","
                    + item.getEmail() + "," + item.getChuyenMon() + "," + item.getSoNamKinhNghiem() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[GiangVienRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<GiangVien> read() {
        List<GiangVien> list = new ArrayList<>();
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
                String chuyenMon = data[4];
                int soNamKinhNghiem = Integer.parseInt(data[5]);
                list.add(new GiangVien(id, hoTen, soDienThoai, email, chuyenMon, soNamKinhNghiem));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[GiangVienRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
