package repository;

import entity.DiemDanh;
import entity.TrangThaiDiemDanh;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Đọc/ghi data/diemdanh.csv. Mỗi dòng: id,lopHocId,hocVienId,ngay(yyyy-MM-dd),trangThai
public class DiemDanhRepository implements IRepository<DiemDanh> {
    private static final String FILE_NAME = "data/diemdanh.csv";

    @Override
    public void write(List<DiemDanh> list) {
        String dataStr = "";
        for (DiemDanh item : list) {
            dataStr += item.getId() + "," + item.getLopHocId() + "," + item.getHocVienId() + ","
                    + item.getNgay() + "," + item.getTrangThai().name() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[DiemDanhRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<DiemDanh> read() {
        List<DiemDanh> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                long lopHocId = Long.parseLong(data[1]);
                long hocVienId = Long.parseLong(data[2]);
                LocalDate ngay = LocalDate.parse(data[3]);
                TrangThaiDiemDanh trangThai = TrangThaiDiemDanh.valueOf(data[4]);
                list.add(new DiemDanh(id, lopHocId, hocVienId, ngay, trangThai));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[DiemDanhRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
