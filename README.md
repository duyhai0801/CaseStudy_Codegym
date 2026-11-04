# CaseStudy_Codegym — Quản lý trung tâm tiếng Anh

Case study Module 2 (CodeGym). Đây là ứng dụng console viết bằng Java để quản lý một trung tâm tiếng Anh: khóa học, giảng viên, lớp học, học viên, đăng ký học, học phí và điểm danh. Dữ liệu lưu ra file CSV nên tắt chương trình đi mở lại vẫn còn.

Chương trình có **3 vai trò** đăng nhập: **quản trị viên** (toàn quyền), **giảng viên** (quản lý lớp mình dạy, điểm danh) và **học viên** (xem thông tin của mình). Không ai tự đăng ký được: chỉ admin mới cấp tài khoản cho giảng viên và học viên.

Cấu trúc và cách chia tầng lấy theo bài mẫu **QuanLySanPham**: `entity → repository → service → ui`, cộng thêm `validator` và `lib`.

## 1. Cách chạy

**Bằng VS Code** (cần cài extension *Extension Pack for Java*):

1. `File → Open Folder…` rồi chọn **đúng thư mục `CaseStudy_Codegym`**. Đừng mở thư mục `module 2` bên ngoài.
2. Mở `src/main/java/Main.java`, chờ VS Code nạp Java xong (góc dưới hiện *Java: Ready*), rồi bấm **Run** phía trên hàm `main`.
3. Chương trình chạy trong cửa sổ Terminal của VS Code, gõ lựa chọn vào đó.

Nếu VS Code báo lỗi kiểu *"not on the classpath"*, mở Command Palette (`Ctrl+Shift+P`), chạy lệnh **Java: Add Folder to Java Source Path**, rồi chọn thư mục `src/main/java`.

**Không cần IDE:** bấm đúp file `run.bat`. File này tự biên dịch rồi chạy chương trình.

**Tài khoản mẫu:**

| Vai trò | Username | Password | Gắn với |
|---|---|---|---|
| Quản trị viên | `admin` | `admin` | Có sẵn, toàn quyền |
| Giảng viên | `minhanh` | `minhanh123` | Tran Minh Anh: dạy GT-K01 (đang học), GT-K02 |
| Giảng viên | `david` | `david123` | David Nguyen: dạy TOEIC-K01 (đang học) |
| Học viên | `minhtuan` | `tuan12345` | Nguyen Minh Tuan: học GT-K01, IELTS70-K01, còn nợ học phí |
| Học viên | `thanhhuong` | `huong1234` | Bui Thanh Huong: học TOEIC-K01, vắng nhiều |

**Reset dữ liệu về ban đầu:** xóa thư mục `data`, rồi copy thư mục `data_mau` và đổi tên bản copy thành `data`.

> **Lưu ý**
> - Đường dẫn `data/...` là đường dẫn tương đối, nên chương trình phải chạy với thư mục làm việc là thư mục gốc project. Mở đúng thư mục `CaseStudy_Codegym` trong VS Code, hoặc chạy bằng `run.bat`, là được.
> - Nhập tiếng Việt **không dấu**, vì terminal Windows hay làm lỗi ký tự có dấu khi đọc bằng `Scanner`.
> - Khi nhập ngày, dùng dạng `dd/MM/yyyy`, ví dụ `15/11/2026`.
> - Khi **sửa**, nhấn Enter để giữ nguyên giá trị cũ.

## 2. Cấu trúc thư mục

```
CaseStudy_Codegym/
├── src/main/java/
│   ├── Main.java                 điểm khởi chạy, chỉ gọi AuthUI.showMainMenu()
│   ├── entity/                   các lớp chỉ chứa dữ liệu
│   │   ├── Nguoi.java            abstract, lớp cha của GiangVien và HocVien
│   │   ├── GiangVien.java, HocVien.java
│   │   ├── KhoaHoc.java, LopHoc.java, DangKy.java, DiemDanh.java
│   │   ├── User.java             tài khoản: username, password, vaiTro, nguoiId
│   │   └── TrinhDo, TrangThaiLop, TrangThaiHocPhi, VaiTro, TrangThaiDiemDanh   (enum)
│   ├── repository/               đọc/ghi file CSV
│   │   ├── IRepository.java      interface generic: read(), write(List<T>)
│   │   └── KhoaHocRepository, GiangVienRepository, HocVienRepository,
│   │       LopHocRepository, DangKyRepository, DiemDanhRepository, UserRepository
│   ├── service/                  logic nghiệp vụ, Singleton
│   │   ├── IService.java         interface generic: add, delete, update, getAll, findIndexById, findById
│   │   ├── KhoaHocService, GiangVienService, HocVienService, LopHocService
│   │   ├── DangKyService         đăng ký vào lớp, hủy đăng ký, đóng học phí
│   │   ├── DiemDanhService       lưu / cập nhật điểm danh, tính chuyên cần
│   │   ├── ThongKeService        các số liệu thống kê
│   │   └── UserService           login(), cấp / xóa tài khoản, đổi mật khẩu
│   ├── ui/                       menu, nhập/xuất, bắt exception
│   │   ├── AuthUI                đăng nhập, rồi chuyển menu theo vai trò
│   │   ├── MainUI                menu ADMIN → KhoaHocUI, GiangVienUI, LopHocUI, HocVienUI,
│   │   │                         DangKyUI, ThongKeUI, DiemDanhUI, TaiKhoanUI
│   │   ├── GiangVienMenuUI       menu của tài khoản GIẢNG VIÊN
│   │   └── HocVienMenuUI         menu của tài khoản HỌC VIÊN
│   ├── validator/                kiểm tra dữ liệu nhập bằng regex / khoảng giá trị
│   │   └── CommonValidator, NguoiValidator, KhoaHocValidator, LopHocValidator, UserValidator
│   └── lib/
│       ├── InputHelper.java      bọc 1 Scanner duy nhất, nhập sai thì bắt nhập lại
│       ├── FormatHelper.java     định dạng tiền, ngày
│       └── errors/               DataNotFoundException, UserIsExist, NghiepVuException
├── data/                         khoahoc.csv, giangvien.csv, hocvien.csv, lophoc.csv, dangky.csv,
│                                 diemdanh.csv, users.csv
├── data_mau/                     bản sao dữ liệu mẫu ban đầu, dùng để reset
├── README.md, DEMO.md
└── run.bat                       bấm đúp để biên dịch và chạy
```

## 3. Các tầng và luồng gọi

```
UI ──gọi──► Service ──gọi──► Repository ──đọc/ghi──► data/*.csv
│              │
├─ validator   └─ ném lỗi từ lib/errors (UI bắt và in thông báo)
└─ lib/InputHelper
(Entity được tất cả các tầng dùng để chứa dữ liệu)
```

| Tầng | Nhiệm vụ | Không được làm |
|---|---|---|
| **entity** | Mô tả dữ liệu: thuộc tính private, constructor, getter/setter, vài hàm tính toán nhỏ (`tinhSoTienConNo()`, `tinhTuoi()`) | Đọc file, in menu |
| **repository** | Chuyển dòng CSV thành object (`read`) và object thành dòng CSV (`write`) | Logic nghiệp vụ |
| **service** | Giữ danh sách trong bộ nhớ, kiểm tra quy tắc nghiệp vụ, sau mỗi thay đổi gọi `repository.write()` để lưu | In menu, đọc bàn phím |
| **ui** | In menu, nhận lựa chọn, gọi validator để nhập, gọi service, bắt exception rồi in thông báo | Đọc/ghi file trực tiếp |

**Ví dụ luồng "đăng ký học viên vào lớp":**
`DangKyUI.dangKyUI()` chọn học viên và nhập id lớp → gọi `DangKyService.dangKyVaoLop()`. Hàm này kiểm tra 4 điều kiện, tính học phí, rồi gọi `add()` → `DangKyRepository.write()` ghi lại `data/dangky.csv`. Nếu vi phạm điều kiện, service ném `NghiepVuException`, `DangKyUI` bắt lỗi đó và in lý do ra màn hình.

## 4. Entity và quan hệ

```
KhoaHoc 1 ──── n LopHoc n ──── 1 GiangVien
                   1
                   │
                   n
                 DangKy n ──── 1 HocVien

LopHoc 1 ──── n DiemDanh n ──── 1 HocVien     (mỗi buổi học, mỗi học viên 1 dòng)

User ──(vaiTro + nguoiId)──► GiangVien hoặc HocVien   (ADMIN thì nguoiId = 0)
Nguoi (abstract) ◄── GiangVien, HocVien   (kế thừa)
```

- Các entity liên kết với nhau bằng **id**, giống cách `Product` giữ `categoryId` trong bài mẫu. Ví dụ `LopHoc` giữ `khoaHocId` và `giangVienId`.
- `DangKy` là **entity trung gian** cho quan hệ n–n giữa `HocVien` và `LopHoc`: một học viên học được nhiều lớp, một lớp có nhiều học viên.
- **id của mọi entity** (khóa học, giảng viên, học viên, lớp, đăng ký, tài khoản) lấy theo **số giây thời gian thực** (`System.currentTimeMillis() / 1000`), giống bài mẫu. Nếu trùng id đã có thì cộng thêm 1. Dữ liệu mẫu ban đầu giữ id nhỏ (1, 2, 3…) cho dễ demo.
- `User` gắn với người dùng thật qua cặp **`vaiTro` + `nguoiId`**. Ví dụ tài khoản `minhanh` có `vaiTro = GIANG_VIEN`, `nguoiId = 1`, nghĩa là giảng viên id 1. Nhờ vậy khi giảng viên đăng nhập, chương trình biết đó là ai để chỉ hiện lớp của người đó.
- `DangKy.hocPhiPhaiDong` được **chốt lúc đăng ký**, nên sau này khóa học có đổi giá thì các lượt đăng ký cũ cũng không bị ảnh hưởng.
- Trạng thái học phí (chưa đóng / đóng một phần / đã đóng đủ) **không lưu vào file** mà tính từ 2 con số, nên luôn khớp với số tiền thực tế.

## 5. Chức năng theo vai trò

Đăng nhập xong, `AuthUI.loginUI()` xem `user.getVaiTro()` rồi mở đúng menu: `MainUI` (admin), `GiangVienMenuUI` hoặc `HocVienMenuUI`.

### Quản trị viên (ADMIN): toàn quyền

| Menu | Chức năng |
|---|---|
| 1. Khóa học | Thêm, sửa, xóa, xem, tìm theo tên, sắp xếp theo học phí (tăng/giảm) |
| 2. Giảng viên | Thêm, sửa, xóa, xem |
| 3. Lớp học | Mở lớp, sửa, xóa, xem (kèm sĩ số), xem học viên của lớp, lọc lớp còn chỗ, lọc theo khóa học, lọc theo trình độ, sắp xếp theo ngày khai giảng |
| 4. Học viên | Thêm, sửa, xóa, xem, tìm theo tên, tìm theo SĐT, sắp xếp theo tên (kiểu Việt Nam: so chữ cuối), xem các lớp của học viên |
| 5. Đăng ký & học phí | Đăng ký vào lớp, hủy đăng ký, đóng học phí, xem tất cả lượt đăng ký |
| 6. Thống kê | Tổng quan học phí (đã thu, còn nợ, tỉ lệ), học viên còn nợ (nợ nhiều nhất ở trên), sĩ số và tỉ lệ lấp đầy từng lớp, xếp hạng khóa học theo số học viên, số lớp đang dạy của giảng viên |
| 7. Điểm danh & chuyên cần | Điểm danh / cập nhật điểm danh **mọi lớp**, xem một buổi, tổng hợp chuyên cần |
| 8. Quản lý tài khoản | **Cấp tài khoản** cho giảng viên hoặc học viên (chọn vai trò → chọn người → đặt username/password), xem danh sách tài khoản, đặt lại mật khẩu, xóa tài khoản |
| 9. Đổi mật khẩu | |

### Giảng viên: chỉ làm việc với các lớp mình dạy

| Menu | Chức năng |
|---|---|
| 1. Thông tin cá nhân | Họ tên, liên hệ, chuyên môn, số lớp đang phụ trách |
| 2. Các lớp tôi dạy | Danh sách lớp kèm sĩ số, lịch học, trạng thái |
| 3. Học viên và học phí của lớp | Danh sách học viên của 1 lớp kèm **ai đã đóng / còn nợ bao nhiêu**, và số người đã đóng đủ |
| 4. Điểm danh / cập nhật điểm danh | Chọn lớp đang học, chọn ngày (Enter = hôm nay), chọn trạng thái từng học viên: có mặt / vắng có phép / vắng không phép. Buổi đã điểm danh thì hỏi có cập nhật lại không |
| 5. Xem điểm danh một buổi | Chọn lớp, chọn buổi, xem ai có mặt, ai vắng |
| 6. Tổng hợp chuyên cần | Số buổi có mặt / vắng của từng học viên, tỉ lệ chuyên cần, đánh dấu học viên đi học **dưới 80%** để nhắc nhở |
| 7. Đổi mật khẩu | |

### Học viên: chỉ xem dữ liệu của chính mình

| Menu | Chức năng |
|---|---|
| 1. Thông tin cá nhân | Họ tên, liên hệ, ngày sinh, trình độ đầu vào |
| 2. Các lớp tôi đang học | Lớp, khóa học, giảng viên, lịch học, ngày khai giảng, trạng thái |
| 3. Học phí của tôi | Từng lớp: phải đóng, đã đóng, còn nợ, và tổng số tiền còn nợ |
| 4. Chuyên cần của tôi | Số buổi có mặt / vắng và tỉ lệ từng lớp, liệt kê các buổi vắng |
| 5. Các lớp đang mở | Lớp còn chỗ trống (muốn đăng ký thì liên hệ quầy) |
| 6. Đổi mật khẩu | |

## 6. Quy tắc nghiệp vụ

**Đăng ký vào lớp** (`DangKyService.dangKyVaoLop`) kiểm tra lần lượt:
1. Học viên và lớp phải tồn tại.
2. Lớp chưa kết thúc.
3. Lớp chưa đủ sĩ số.
4. Học viên chưa đăng ký lớp này.

Qua hết 4 điều kiện thì tạo lượt đăng ký. Học viên **đã từng đăng ký** ít nhất 1 lớp thì được **giảm 10%** học phí.

**Hủy đăng ký:** chỉ được hủy khi lớp còn *sắp khai giảng*. Nếu học viên đã đóng tiền, chương trình báo số tiền cần hoàn trả.

**Đóng học phí:** số tiền phải lớn hơn 0 và không vượt quá số còn nợ.

**Tài khoản:**
- Tài khoản `admin` / `admin` luôn có sẵn. Nếu file `users.csv` không có admin thì `UserService` tự tạo lại.
- Không ai tự đăng ký được. Chỉ admin cấp tài khoản, và chỉ cấp cho người **đã có** trong danh sách giảng viên / học viên.
- Mỗi người chỉ có 1 tài khoản. Username không được trùng.
- Không xóa được tài khoản admin.
- Đăng nhập không kiểm tra regex, chỉ so khớp username/password (nhờ vậy mật khẩu mặc định `admin` vẫn đăng nhập được). Mật khẩu mới thì phải 6–20 ký tự, có cả chữ và số.

**Điểm danh** (`DiemDanhService`):
- Chỉ điểm danh lớp **đang học**.
- Ngày điểm danh không được ở tương lai, không được trước ngày khai giảng.
- Giảng viên chỉ chọn được lớp mình dạy.
- Điểm danh lại một buổi đã có thì **thay toàn bộ** bản cũ của buổi đó, tức là cập nhật điểm danh.
- Tỉ lệ chuyên cần = số buổi có mặt / số buổi lớp đã điểm danh. Dưới 80% thì bị đánh dấu "cần nhắc nhở".

**Ràng buộc khi xóa:** không cho xóa dữ liệu đang được bản ghi khác dùng tới.
- Khóa học còn lớp thì không xóa được.
- Giảng viên còn phụ trách lớp thì không xóa được.
- Lớp đã có học viên đăng ký thì không xóa được.
- Học viên còn lượt đăng ký thì không xóa được.
- Giảng viên / học viên đang có tài khoản đăng nhập thì phải xóa tài khoản trước.

**Sửa lớp:** sĩ số tối đa không được nhỏ hơn số học viên hiện có. Lớp đã có học viên thì không được đổi sang khóa học khác, vì học phí đã chốt theo khóa cũ.

**Kiểm tra dữ liệu nhập:**
- Họ tên chỉ gồm chữ và khoảng trắng.
- Số điện thoại có 10 số, bắt đầu bằng 0, và không được trùng giữa các học viên.
- Email đúng định dạng.
- Học viên từ 6 tuổi trở lên.
- Không nhận ngày không tồn tại, ví dụ 31/02.
- Sĩ số tối đa từ 5 đến 30.
- Các trường chữ **không được chứa dấu phẩy**, vì file CSV tách cột bằng dấu phẩy.
- Nhập chữ vào ô số thì chương trình báo lỗi và cho nhập lại, không bị crash.

## 7. Điểm cải tiến so với bài mẫu QuanLySanPham

| Bài mẫu | Case study này |
|---|---|
| `case 0` thiếu `break` nên thoát xong vẫn in "Invalid choice" | Mọi `case` đều có `break` |
| Có menu không in và không xử lý lựa chọn `0` | Menu nào cũng có `0` |
| Xóa category không kiểm tra product đang dùng nên `findById` ném exception và crash | Kiểm tra ràng buộc trước khi xóa. Bảng in tên qua hàm `layTen...()` có `try/catch`, nên dữ liệu lỗi cũng không làm crash |
| `editUI` nhập tên bằng `inputString()`, bỏ qua validator | Sửa cũng đi qua validator, nhấn Enter để giữ giá trị cũ |
| Tên có dấu phẩy làm hỏng `split(",")` | Regex của validator chặn dấu phẩy |
| id = `currentTimeMillis()/1000`, thêm 2 bản ghi trong cùng 1 giây sẽ bị trùng | Vẫn lấy id theo số giây thực, nhưng `taoIdMoi()` có vòng `while (daCoId(id)) id++` để chống trùng |
| `UserRepository` có hàm `main()` test, chạy nhầm là ghi đè file | Không để `main()` test trong repository |
| Ai cũng tự đăng ký được, đăng ký xong là toàn quyền; dòng `role` để comment | Có phân quyền 3 vai trò (`VaiTro` enum). Chỉ admin cấp tài khoản, mỗi vai trò thấy menu riêng |
| Sửa trực tiếp object trong list rồi mới gọi `update` | Tạo object mới rồi gọi `update`, service từ chối thì dữ liệu cũ vẫn nguyên |

Ngoài ra project còn thể hiện thêm:
- **Kế thừa và đa hình:** lớp `Nguoi` abstract với hàm `layVaiTro()`.
- **enum**.
- **Exception tự định nghĩa** có message (`NghiepVuException`).
- **`Map`** dùng trong thống kê.
- **`Comparator`** viết bằng lambda để sắp xếp.
- **`LocalDate`** cho các trường ngày tháng.

**Lưu ý về Singleton:** các service gọi nhau qua `XxxService.getObject()` **bên trong hàm**, không gán vào field. Ví dụ `LopHocService` cần `DangKyService`, và ngược lại `DangKyService` cũng cần `LopHocService`. Nếu gán vào field thì lúc khởi tạo, 2 Singleton sẽ gọi vòng tròn lẫn nhau và một bên nhận được `null`.
