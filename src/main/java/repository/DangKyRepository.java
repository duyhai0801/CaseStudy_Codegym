package repository;

import entity.DangKy;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Đọc/ghi data/dangky.csv.
// Mỗi dòng: id,hocVienId,lopHocId,ngayDangKy(yyyy-MM-dd),hocPhiPhaiDong,soTienDaDong
public class DangKyRepository implements IRepository<DangKy> {
    private static final String FILE_NAME = "data/dangky.csv";

    @Override
    public void write(List<DangKy> list) {
        String dataStr = "";
        for (DangKy item : list) {
            dataStr += item.getId() + "," + item.getHocVienId() + "," + item.getLopHocId() + ","
                    + item.getNgayDangKy() + "," + item.getHocPhiPhaiDong() + "," + item.getSoTienDaDong() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[DangKyRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<DangKy> read() {
        List<DangKy> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                long hocVienId = Long.parseLong(data[1]);
                long lopHocId = Long.parseLong(data[2]);
                LocalDate ngayDangKy = LocalDate.parse(data[3]);
                long hocPhiPhaiDong = Long.parseLong(data[4]);
                long soTienDaDong = Long.parseLong(data[5]);
                list.add(new DangKy(id, hocVienId, lopHocId, ngayDangKy, hocPhiPhaiDong, soTienDaDong));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[DangKyRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
