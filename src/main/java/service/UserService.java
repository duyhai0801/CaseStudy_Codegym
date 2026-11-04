package service;

import entity.User;
import entity.VaiTro;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import lib.errors.UserIsExist;
import repository.UserRepository;

import java.util.List;

// Quản lý tài khoản: đăng nhập, admin cấp tài khoản cho giảng viên / học viên, đổi và đặt lại mật khẩu.
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

    // Tài khoản admin luôn có sẵn: nếu file chưa có admin (file mới / bị xóa) thì tự tạo admin/admin
    private void damBaoCoTaiKhoanAdmin() {
        for (User item : list) {
            if (item.getVaiTro() == VaiTro.ADMIN) {
                return;
            }
        }
        list.add(new User(taoIdMoi(), ADMIN_USERNAME, ADMIN_PASSWORD_MAC_DINH, VaiTro.ADMIN, 0));
        userRepository.write(list);
    }

    // id = số giây hiện tại (giống bài mẫu: currentTimeMillis() / 1000).
    // Bài mẫu bị trùng id nếu thêm 2 bản ghi trong cùng 1 giây, nên ở đây gặp id đã có thì cộng thêm 1.
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

    // Trả về tài khoản nếu đúng username + password, sai thì trả về null
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

    /*
    Kiểm tra trước khi cấp tài khoản:
    - Chỉ cấp cho GIANG_VIEN hoặc HOC_VIEN (admin chỉ có 1, có sẵn)
    - Người được cấp phải tồn tại
    - Mỗi người chỉ có 1 tài khoản
     */
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

    // Chỉ admin gọi (qua TaiKhoanUI). Trùng username thì ném UserIsExist giống bài mẫu.
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

    public void xoaTaiKhoan(long userId) {
        User user = findById(userId);
        if (user.getVaiTro() == VaiTro.ADMIN) {
            throw new NghiepVuException("Khong the xoa tai khoan quan tri vien.");
        }
        this.list.remove(user);
        userRepository.write(list);
    }
}
