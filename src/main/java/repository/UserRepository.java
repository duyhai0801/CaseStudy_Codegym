package repository;

import entity.User;
import entity.VaiTro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {
    private static final String FILE_NAME = "data/users.csv";

    @Override
    public void write(List<User> list) {
        String dataStr = "";
        for (User item : list) {
            dataStr += item.getId() + "," + item.getUsername() + "," + item.getPassword() + ","
                    + item.getVaiTro().name() + "," + item.getNguoiId() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("[UserRepository.write.error]: " + e.getMessage());
        }
    }

    @Override
    public List<User> read() {
        List<User> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 5) continue;
                long id = Long.parseLong(data[0]);
                String username = data[1];
                String password = data[2];
                VaiTro vaiTro = VaiTro.valueOf(data[3]);
                long nguoiId = Long.parseLong(data[4]);
                list.add(new User(id, username, password, vaiTro, nguoiId));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[UserRepository.read.error]: " + e.getMessage());
        }
        return list;
    }
}
