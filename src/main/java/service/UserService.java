package service;

import entity.User;
import entity.VaiTro;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import lib.errors.UserIsExist;
import repository.UserRepository;

import java.util.List;

public class UserService {
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD_MAC_DINH = "admin";

    private UserRepository userRepository = new UserRepository();
    private List<User> list;
    private static final UserService userService = new UserService();

    private UserService() {
        this.list = userRepository.read();
        damBaoCoTaiKhoanAdmin();
    }

    public static UserService getObject() {
        return userService;
    }

    private void damBaoCoTaiKhoanAdmin() {
        for (User item : list) {
            if (item.getVaiTro() == VaiTro.ADMIN) {
                return;
            }
        }
        list.add(new User(taoIdMoi(), ADMIN_USERNAME, ADMIN_PASSWORD_MAC_DINH, VaiTro.ADMIN, 0));
        userRepository.write(list);
    }

    public long taoIdMoi() {
        long id = System.currentTimeMillis() / 1000;
        while (daCoId(id)) {
            id++;
        }
        return id;
    }

    private boolean daCoId(long id) {
        for (User item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public User login(String username, String password) {
        for (User item : this.list) {
            if (item.getUsername().equals(username) && item.getPassword().equals(password)) {
                return item;
            }
        }
        return null;
    }

    public List<User> getAll() {
        return this.list;
    }

    public User findById(long id) {
        for (User item : list) {
            if (item.getId() == id) {
                return item;
            }
        }
        throw new DataNotFoundException();
    }

    public boolean coTaiKhoanCua(VaiTro vaiTro, long nguoiId) {
        return timTaiKhoanCua(vaiTro, nguoiId) != null;
    }

    public User timTaiKhoanCua(VaiTro vaiTro, long nguoiId) {
        for (User item : list) {
            if (item.getVaiTro() == vaiTro && item.getNguoiId() == nguoiId) {
                return item;
            }
        }
        return null;
    }

    public void kiemTraCoTheCapTaiKhoan(VaiTro vaiTro, long nguoiId) {
        if (vaiTro == VaiTro.ADMIN) {
            throw new NghiepVuException("Khong the cap them tai khoan quan tri vien.");
        }
        try {
            if (vaiTro == VaiTro.GIANG_VIEN) {
                GiangVienService.getObject().findById(nguoiId);
            } else {
                HocVienService.getObject().findById(nguoiId);
            }
        } catch (DataNotFoundException e) {
            throw new NghiepVuException(vaiTro.getTenHienThi() + " id " + nguoiId + " khong ton tai.");
        }
        User daCo = timTaiKhoanCua(vaiTro, nguoiId);
        if (daCo != null) {
            throw new NghiepVuException("Nguoi nay da co tai khoan (username: " + daCo.getUsername() + ").");
        }
    }

    public void taoTaiKhoan(User user) {
        kiemTraCoTheCapTaiKhoan(user.getVaiTro(), user.getNguoiId());
        for (User item : this.list) {
            if (item.getUsername().equals(user.getUsername())) {
                throw new UserIsExist();
            }
        }
        this.list.add(user);
        userRepository.write(list);
    }

    public void doiMatKhau(long userId, String matKhauMoi) {
        User user = findById(userId);
        user.setPassword(matKhauMoi);
        userRepository.write(list);
    }

    public boolean daCoUsername(String username) {
        for (User item : list) {
            if (item.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void xoaTaiKhoanCua(VaiTro vaiTro, long nguoiId) {
        User user = timTaiKhoanCua(vaiTro, nguoiId);
        if (user != null) {
            this.list.remove(user);
            userRepository.write(list);
        }
    }

    public void xoaTaiKhoan(long userId) {
        User user = findById(userId);
        if (user.getVaiTro() == VaiTro.ADMIN) {
            throw new NghiepVuException("Khong the xoa tai khoan quan tri vien.");
        }
        this.list.remove(user);
        userRepository.write(list);
    }
}
