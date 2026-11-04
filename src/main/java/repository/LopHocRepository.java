package repository;

import entity.LopHoc;
import entity.TrangThaiLop;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Đọc/ghi data/lophoc.csv.
// Mỗi dòng: id,tenLop,khoaHocId,giangVienId,lichHoc,ngayKhaiGiang(yyyy-MM-dd),siSoToiDa,trangThai
public class LopHocRepository implements IRepository<LopHoc> {
    private static final String FILE_NAME = "data/lophoc.csv";

    @Override
    public void write(List<LopHoc> list) {
        String dataStr = "";
        for (LopHoc item : list) {
            dataStr += item.getId() + "," + item.getTenLop() + "," + item.getKhoaHocId() + ","
                    + item.getGiangVienId() + "," + item.getLichHoc() + "," + item.getNgayKhaiGiang() + ","
                    + item.getSiSoToiDa() + "," + item.getTrangThai().name() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[LopHocRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<LopHoc> read() {
        List<LopHoc> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                String tenLop = data[1];
                long khoaHocId = Long.parseLong(data[2]);
                long giangVienId = Long.parseLong(data[3]);
                String lichHoc = data[4];
                LocalDate ngayKhaiGiang = LocalDate.parse(data[5]);
                int siSoToiDa = Integer.parseInt(data[6]);
                TrangThaiLop trangThai = TrangThaiLop.valueOf(data[7]);
                list.add(new LopHoc(id, tenLop, khoaHocId, giangVienId, lichHoc,
                        ngayKhaiGiang, siSoToiDa, trangThai));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[LopHocRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
