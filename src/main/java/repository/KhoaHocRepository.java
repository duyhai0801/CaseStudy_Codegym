package repository;

import entity.KhoaHoc;
import entity.TrinhDo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocRepository implements IRepository<KhoaHoc> {
    private static final String FILE_NAME = "data/khoahoc.csv";

    @Override
    public void write(List<KhoaHoc> list) {
        String dataStr = "";
        for (KhoaHoc item : list) {
            dataStr += item.getId() + "," + item.getTenKhoaHoc() + "," + item.getTrinhDo().name() + ","
                    + item.getSoBuoi() + "," + item.getHocPhi() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[KhoaHocRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<KhoaHoc> read() {
        List<KhoaHoc> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                String tenKhoaHoc = data[1];
                TrinhDo trinhDo = TrinhDo.valueOf(data[2]);
                int soBuoi = Integer.parseInt(data[3]);
                long hocPhi = Long.parseLong(data[4]);
                list.add(new KhoaHoc(id, tenKhoaHoc, trinhDo, soBuoi, hocPhi));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[KhoaHocRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
