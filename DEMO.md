# Kịch bản demo (7–8 phút) và câu hỏi phỏng vấn

## Chuẩn bị

- **Reset dữ liệu trước khi demo:** xóa thư mục `data`, copy thư mục `data_mau` rồi đổi tên bản copy thành `data`. Kịch bản dưới đây dùng dữ liệu mẫu ban đầu: học viên id 12 là *Ly Mai Phuong*, chưa đăng ký lớp nào và chưa có tài khoản.
- Mã đăng ký mới là số dài 10 chữ số. Khi cần nhập lại, bôi đen mã trong bảng ở Terminal, nhấn `Ctrl+C`, rồi bấm chuột phải vào Terminal để dán, khỏi gõ sai.
- Lớp GT-K01 học thứ 2-4-6. Nếu hôm demo không phải ngày có lịch thì ở bước 13 vẫn nhấn Enter (điểm danh hôm nay) được, chương trình không kiểm tra thứ.

## Phần A: Quản trị viên và nghiệp vụ chính

| # | Thao tác (menu → nhập) | Kết quả mong đợi | Nói gì khi demo |
|---|---|---|---|
| 1 | 1 Đăng nhập → `admin` / `admin` | Vào menu QUAN TRI VIEN | Màn hình đầu chỉ có đăng nhập, không ai tự đăng ký được |
| 2 | 6 Thống kê → 1, rồi 3 | Tổng quan học phí. Bảng sĩ số: IELTS70-K01 đầy **5/5** | Cho người xem nắm tổng quan dữ liệu |
| 3 | 0 → 5 Đăng ký → 1 → học viên **12** → lớp **6** | *Lop IELTS70-K01 da du si so (5/5)* | Lớp đầy thì chặn |
| 4 | 1 → học viên 12 → lớp **4** | *Lop IELTS55-K01 da ket thuc* | Lớp đã kết thúc thì chặn |
| 5 | 1 → học viên 12 → lớp **2** | Thành công, 3,600,000. Mã đăng ký là số giây hiện tại | Đăng ký lần đầu, trả giá gốc. id theo thời gian thực |
| 6 | 1 → học viên 12 → lớp **5** | Thành công, **6,480,000** (giảm 10%) | Học viên cũ được giảm 10%, học phí chốt vào `DangKy` |
| 7 | 3 Đóng học phí → học viên 12 → mã đăng ký lớp GT-K02 → `5000000` | *So tien vuot qua so con no* | Không cho đóng quá số đang nợ |
| 8 | 3 → học viên 12 → cùng mã → `1000000` | Còn nợ 2,600,000 (Dong mot phan) | Trạng thái học phí **tính ra** từ số tiền, không lưu vào file |
| 9 | 0 → 1 Khóa học → 3 Xóa → id **1** → y | *Khoa hoc dang co lop hoc, khong the xoa* | Sửa lỗi bài mẫu (xóa category làm crash) |
| 10 | 0 → 4 Học viên → 7 Sắp xếp theo tên | Anh, Bao, Chau, Duc… | So sánh **chữ cuối** của họ tên |

## Phần B: Phân quyền

| # | Thao tác (menu → nhập) | Kết quả mong đợi | Nói gì khi demo |
|---|---|---|---|
| 11 | 0 → 8 Quản lý tài khoản → 2 | Danh sách 5 tài khoản, không hiện mật khẩu | 3 vai trò: quản trị viên, giảng viên, học viên |
| 12 | 1 Cấp tài khoản → 2 (Học viên) → học viên **12** → `maiphuong` / `phuong123` | *Da cap tai khoan... Hay gui cho ho...* | Chỉ admin cấp tài khoản, gắn với học viên có sẵn qua `nguoiId` |
| 13 | 1 → 2 → học viên **1** | *Nguoi nay da co tai khoan (username: minhtuan)* | Mỗi người chỉ 1 tài khoản |
| 14 | 0 → 0 Đăng xuất → 1 → `minhanh` / `minhanh123` | Vào menu GIANG VIEN: Tran Minh Anh | Đăng nhập xong tự vào menu theo vai trò |
| 15 | 3 Học viên và học phí → lớp **3** | *Chi duoc chon lop trong danh sach* (lớp 3 không phải của cô) → nhập **1** | Giảng viên chỉ thấy lớp mình dạy. Xem được ai đã đóng học phí |
| 16 | 4 Điểm danh → lớp 1 → Enter (hôm nay) → Enter, Enter, `3`, Enter | *Da luu diem danh... 3/4 co mat* | Enter = có mặt, 3 = vắng không phép |
| 17 | 6 Tổng hợp chuyên cần → lớp 1 | Le Hoang Bao bị đánh dấu *Vang nhieu - can nhac nho* | Tỉ lệ có mặt dưới 80% thì cảnh báo |
| 18 | 0 Đăng xuất → 1 → `minhtuan` / `tuan12345` | Vào menu HOC VIEN: Nguyen Minh Tuan | |
| 19 | 3 Học phí của tôi, rồi 4 Chuyên cần | Còn nợ 8,820,000. Bảng chuyên cần và các buổi vắng | Học viên chỉ xem được dữ liệu của mình, không sửa được gì |
| 20 | 0 → 1 → `maiphuong` / `phuong123` → 3 | Thấy 2 lớp vừa đăng ký ở phần A, còn nợ 9,080,000 | Tài khoản vừa được cấp ở bước 12 dùng được ngay |

## Câu hỏi phỏng vấn hay gặp

**Vì sao chia thành các tầng entity / repository / service / ui?**
Mỗi tầng làm một việc riêng. Ví dụ muốn đổi từ file CSV sang database thì chỉ phải sửa repository. Muốn đổi giao diện thì chỉ sửa ui. Còn service giữ logic nghiệp vụ, không phụ thuộc vào chuyện dữ liệu nằm ở đâu hay hiển thị ra sao.

**Phân quyền làm thế nào?**
`User` có thêm `vaiTro` (enum `VaiTro`: ADMIN, GIANG_VIEN, HOC_VIEN) và `nguoiId`. `AuthUI.loginUI()` đăng nhập xong thì `switch (user.getVaiTro())` để mở `MainUI`, `GiangVienMenuUI` hoặc `HocVienMenuUI`. Mỗi menu chỉ có những chức năng của vai trò đó.

**Làm sao biết tài khoản giảng viên là giảng viên nào?**
Qua `nguoiId`. Tài khoản `minhanh` có `nguoiId = 1`, nên `GiangVienMenuUI` gọi `giangVienService.findById(1)`, rồi `lopHocService.locTheoGiangVien(1)` để chỉ lấy lớp của giảng viên đó. Khi chọn lớp, `DiemDanhUI.chonLop()` chỉ chấp nhận id nằm trong danh sách lớp của giảng viên.

**Vì sao đăng nhập không kiểm tra regex mật khẩu?**
Regex là luật cho mật khẩu **mới**. Lúc đăng nhập chỉ cần so khớp với dữ liệu đã lưu. Nếu đăng nhập cũng kiểm tra regex thì mật khẩu mặc định `admin` (không có số) sẽ không bao giờ đăng nhập được.

**Điểm danh lưu thế nào? Cập nhật điểm danh làm sao?**
Mỗi học viên trong mỗi buổi là 1 dòng `DiemDanh` (lớp, học viên, ngày, trạng thái). Khi lưu một buổi, `DiemDanhService.luuDiemDanhBuoi()` xóa hết các dòng cũ của buổi đó (cùng lớp, cùng ngày) rồi thêm các dòng mới. Nhờ vậy "điểm danh" và "cập nhật điểm danh" dùng chung một hàm. Vòng xóa duyệt **ngược** từ cuối list để xóa phần tử mà không bị lệch chỉ số.

**Singleton là gì, vì sao service dùng Singleton?**
Constructor để `private`, object duy nhất được tạo sẵn trong biến `static final`, và chỉ lấy ra qua `getObject()`. Cả chương trình chỉ có **một** danh sách dữ liệu dùng chung. Nếu mỗi UI tự `new` service thì sẽ có nhiều danh sách khác nhau, sửa ở chỗ này mà chỗ khác không thấy.

**Vì sao các service gọi `getObject()` bên trong hàm mà không gán vào field?**
`LopHocService` cần `DangKyService`, và `DangKyService` cũng cần `LopHocService`. Nếu gán vào field `static final` thì lúc khởi tạo, hai class sẽ chờ nhau và một bên nhận được `null`. Gọi trong hàm thì lúc hàm chạy cả hai đã khởi tạo xong.

**`IRepository<T>` và `IService<T>` dùng generic để làm gì?**
Để viết một bộ hàm chung cho mọi kiểu dữ liệu. `KhoaHocService implements IService<KhoaHoc>` thì `T` trở thành `KhoaHoc`. Các service có cùng tên hàm nên dễ đọc, dễ dùng.

**id lấy thế nào? Có bị trùng không?**
`taoIdMoi()` lấy `System.currentTimeMillis() / 1000`, tức số giây tính từ 1/1/1970, giống bài mẫu. Bài mẫu sẽ trùng id nếu thêm 2 bản ghi trong cùng 1 giây. Ở đây có thêm vòng `while (daCoId(id)) id++`: id đã tồn tại thì cộng 1 đến khi không trùng nữa. Lúc lưu điểm danh cả lớp trong 1 giây, các dòng sẽ nhận id liên tiếp nhờ vòng này.

**Vì sao cần entity `DangKy`?**
Học viên và lớp có quan hệ **n–n**, không thể nhét gọn vào một bên. `DangKy` là bảng trung gian, đồng thời là chỗ lưu thông tin riêng của từng lượt đăng ký: ngày đăng ký, học phí, số tiền đã đóng.

**Vì sao lưu `hocPhiPhaiDong` mà không lấy học phí từ khóa học mỗi lần cần?**
Có hai lý do. Thứ nhất, khóa học có thể đổi giá sau này. Thứ hai, học viên cũ được giảm 10%. Nên số tiền phải chốt lại đúng tại thời điểm đăng ký.

**Đa hình nằm ở đâu?**
`Nguoi` là abstract class, có hàm abstract `layVaiTro()`. `GiangVien` và `HocVien` mỗi lớp override hàm này theo cách riêng. Ngoài ra các service đều implement cùng interface `IService`.

**Vì sao exception kế thừa `RuntimeException`?**
Đây là unchecked exception, nên không bắt buộc phải khai báo `throws` ở mọi hàm, giống bài mẫu. Service ném lỗi ra, UI là nơi bắt lỗi và in thông báo.

**Vì sao `InputHelper` dùng `nextLine()` rồi mới parse?**
`nextInt()` không đọc ký tự xuống dòng, nên lệnh `nextLine()` ngay sau đó bị "trôi". Đọc cả dòng rồi `parseInt` thì tránh được lỗi này, và bắt được `NumberFormatException` để cho nhập lại.

**Hạn chế còn lại / hướng phát triển?**
- Mật khẩu đang lưu dạng chữ thường trong `users.csv`. Bản thật nên mã hóa (hash) mật khẩu.
- Phân quyền mới kiểm tra ở tầng UI (mỗi vai trò thấy menu khác nhau). Chặt hơn thì service cũng phải kiểm tra người gọi.
- Chưa so trình độ học viên với trình độ khóa học khi đăng ký.

**Nếu muốn thêm thuộc tính `diaChi` cho học viên thì sửa những đâu?**
Lần lượt từ tầng dưới lên:
1. `HocVien`: thêm field, constructor, getter/setter.
2. `HocVienRepository`: thêm cột trong `read()` và `write()`.
3. `NguoiValidator`: thêm hàm `inputDiaChi()`.
4. `HocVienUI`: sửa `addUI`, `editUI`, `inDanhSach`.
5. `data/hocvien.csv`: thêm cột dữ liệu.
