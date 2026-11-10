# Kịch bản demo (7–8 phút) và câu hỏi phỏng vấn

## Chuẩn bị

- **Reset dữ liệu trước khi demo:** xóa thư mục `data`, copy thư mục `data_mau` rồi đổi tên bản copy thành `data`. Kịch bản dưới đây dùng dữ liệu mẫu ban đầu.
- **id mẫu cần dùng** (id là số 10 chữ số, nên copy từ đây hoặc từ bảng trong Terminal):

| Đối tượng | id |
|---|---|
| Học viên *Ly Mai Phuong* (chưa đăng ký lớp nào, chưa có tài khoản) | `6654192463` |
| Học viên *Nguyen Minh Tuan* | `6175858811` |
| Khóa học *Giao tiep co ban* | `1788863335` |
| Lớp GT-K01 (đang học, của cô Minh Anh) | `8014946297` |
| Lớp GT-K02 (sắp khai giảng, của cô Minh Anh) | `4291129747` |
| Lớp TOEIC-K01 (đang học, của thầy David) | `2980189196` |
| Lớp IELTS55-K01 (đã kết thúc) | `6102505113` |
| Lớp IELTS55-K02 (sắp khai giảng) | `1343100656` |
| Lớp IELTS70-K01 (đã đầy 5/5) | `3949671471` |

- Mã đăng ký mới là số dài 10 chữ số. Khi cần nhập lại, bôi đen mã trong bảng ở Terminal, nhấn `Ctrl+C`, rồi bấm chuột phải vào Terminal để dán, khỏi gõ sai.
- Lớp GT-K01 học thứ 2-4-6. Ở bước 16, nếu hôm demo là T2, T4 hoặc T6 thì chương trình hỏi *Xac nhan bat dau buoi hoc?*. Nếu không phải thì nó cảnh báo ngày không có trong lịch học và hỏi có phải buổi học bù không. Cả hai trường hợp đều nhập `y` để tiếp tục.

## Phần A: Quản trị viên và nghiệp vụ chính

| # | Thao tác (menu → nhập) | Kết quả mong đợi | Nói gì khi demo |
|---|---|---|---|
| 1 | 1 Đăng nhập → `admin` / `admin` | Vào menu QUAN TRI VIEN | Màn hình đầu chỉ có đăng nhập, không ai tự đăng ký được |
| 2 | 6 Thống kê → 1, rồi 3 | Tổng quan học phí. Bảng sĩ số: IELTS70-K01 đầy **5/5** | Cho người xem nắm tổng quan dữ liệu |
| 3 | 0 → 5 Đăng ký → 1 → học viên `6654192463` → lớp `3949671471` | *Lop IELTS70-K01 da du si so (5/5)* | Lớp đầy thì chặn |
| 4 | 1 → học viên `6654192463` → lớp `6102505113` | *Lop IELTS55-K01 da ket thuc* | Lớp đã kết thúc thì chặn |
| 5 | 1 → học viên `6654192463` → lớp `4291129747` | Thành công, 3,600,000. Mã đăng ký là số giây hiện tại | Đăng ký lần đầu, trả giá gốc. id theo thời gian thực |
| 6 | 1 → học viên `6654192463` → lớp `1343100656` | Thành công, **6,480,000** (giảm 10%) | Học viên cũ được giảm 10%, học phí chốt vào `DangKy` |
| 7 | 3 Đóng học phí → học viên `6654192463` → mã đăng ký lớp GT-K02 → `5000000` | *So tien vuot qua so con no* | Không cho đóng quá số đang nợ |
| 8 | 3 → học viên `6654192463` → cùng mã → `1000000` | Còn nợ 2,600,000 (Dong mot phan) | Trạng thái học phí **tính ra** từ số tiền, không lưu vào file |
| 9 | 0 → 1 Khóa học → 3 Xóa → id `1788863335` → y | *Khoa hoc dang co lop hoc, khong the xoa* | Sửa lỗi bài mẫu (xóa category làm crash) |
| 10 | 0 → 4 Học viên → 7 Danh sách học viên theo tên | Anh, Bao, Chau, Duc… | So sánh **chữ cuối** của họ tên |
| 10b | 1 Thêm học viên → nhập họ tên, SĐT → ở ô email gõ `0` | *Da huy thao tac.* rồi hiện lại menu QUAN LY HOC VIEN, danh sách không có người mới | Gõ `0` ở bất kỳ ô nào là hủy, chưa có gì được lưu |

## Phần B: Phân quyền

| # | Thao tác (menu → nhập) | Kết quả mong đợi | Nói gì khi demo |
|---|---|---|---|
| 11 | 0 → 8 Quản lý tài khoản → 2 | Danh sách 5 tài khoản, không hiện mật khẩu | 3 vai trò: quản trị viên, giảng viên, học viên |
| 12 | 1 Cấp tài khoản → 2 (Học viên) → 2 (chọn có sẵn) → học viên `6654192463` → `maiphuong` / `phuong123` | *Da tao tai khoan... Hay gui cho ho...* kèm danh sách quyền hạn | Chỉ admin tạo tài khoản, gắn với học viên qua `nguoiId` |
| 13 | 1 → 2 → 2 → học viên `6175858811` | *Nguoi nay da co tai khoan (username: minhtuan)* | Mỗi người chỉ 1 tài khoản |
| 13b | 5 Xem quyền hạn của từng vai trò | Quyền của quản trị viên, giảng viên, học viên | Phân quyền rõ ràng cho từng vai trò |
| 13c | 0 → 4 Học viên → 1 Thêm: `Vo Minh Khang`, `0933444555`, `khang@gmail.com`, `03/03/2003`, 1 → username `minhkhang` / `khang123` | *Da them hoc vien* rồi *Da tao tai khoan...* | Thêm học viên mới là tạo luôn tài khoản để đưa cho họ |
| 14 | 0 → 0 Đăng xuất → 1 → `minhanh` / `minhanh123` | Vào menu GIANG VIEN: Tran Minh Anh | Đăng nhập xong tự vào menu theo vai trò |
| 15 | 3 Danh sách học viên và học phí theo lớp → lớp `2980189196` | *Chi duoc chon lop trong danh sach* (lớp TOEIC-K01 không phải của cô) → nhập `8014946297` | Giảng viên chỉ thấy lớp mình dạy. Xem được ai đã đóng học phí |
| 16 | 4 Điểm danh → lớp `8014946297` → Enter (hôm nay) → `y` (xác nhận) → Enter, Enter, `3`, Enter | *Da luu diem danh... 3/4 co mat* | Enter = có mặt, 3 = vắng không phép |
| 17 | 6 Tổng hợp chuyên cần → lớp `8014946297` | Le Hoang Bao bị đánh dấu *Vang nhieu - can nhac nho*, có SĐT để gọi nhắc | Tỉ lệ có mặt dưới 80% thì cảnh báo. Tỉ lệ tính trên số buổi của từng học viên |
| 17b | 7 Bắt đầu lớp → lớp `4291129747` | *Chua toi ngay khai giang cua lop GT-K02* | Giảng viên tự mở lớp được, nhưng phải tới ngày khai giảng. Admin cũng làm được ở *Lớp học → 1 Cập nhật lớp → 4*, gọi cùng hàm `batDauLop()` nên cùng luật |
| 18 | 0 Đăng xuất → 1 → `minhtuan` / `tuan12345` | Vào menu HOC VIEN: Nguyen Minh Tuan | |
| 19 | 3 Học phí của tôi, rồi 4 Chuyên cần | Còn nợ 8,820,000. Bảng chuyên cần và các buổi vắng | Học viên chỉ xem được dữ liệu của mình |
| 19b | 5 Đăng ký / hủy đăng ký → 1 → lớp `3949671471` → y | *Lop IELTS70-K01 da du si so* | Học viên tự đăng ký vẫn qua đủ các bước kiểm tra |
| 19c | 1 → lớp `4291129747` → y | Thành công, **3,240,000** (giảm 10%), nhắc đóng tiền trước ngày khai giảng | Dùng chung `dangKyVaoLop()` với admin |
| 19d | 2 → mã vừa tạo (hiện ở bảng *Lop ban da dang ky*) → y, rồi 0 | *Da huy dang ky!* | Chỉ hủy được của mình, lớp còn sắp khai giảng |
| 20 | 0 → 1 → `maiphuong` / `phuong123` → 3 | Thấy 2 lớp vừa đăng ký ở phần A, còn nợ 9,080,000 | Tài khoản vừa được cấp ở bước 12 dùng được ngay |

## Câu hỏi phỏng vấn hay gặp

**Vì sao chia thành các tầng entity / repository / service / ui?**
Mỗi tầng làm một việc riêng. Ví dụ muốn đổi từ file CSV sang database thì chỉ phải sửa repository. Muốn đổi giao diện thì chỉ sửa ui. Còn service giữ logic nghiệp vụ, không phụ thuộc vào chuyện dữ liệu nằm ở đâu hay hiển thị ra sao.

**Phân quyền làm thế nào?**
`User` có thêm `vaiTro` (enum `VaiTro`: ADMIN, GIANG_VIEN, HOC_VIEN) và `nguoiId`. `AuthUI.loginUI()` đăng nhập xong thì `switch (user.getVaiTro())` để mở `MainUI`, `GiangVienMenuUI` hoặc `HocVienMenuUI`. Mỗi menu chỉ có những chức năng của vai trò đó.

**id ở đây là số gì?**
Mọi id là số 10 chữ số. Dữ liệu mẫu dùng id ngẫu nhiên 10 chữ số; bản ghi thêm mới thì lấy số giây thời gian thực (`currentTimeMillis() / 1000`), trùng thì cộng 1. Các bảng liên kết với nhau bằng đúng các id này.

**Thêm giảng viên / học viên thì tài khoản được tạo thế nào? Xóa người thì sao?**
`GiangVienUI.addUI()` và `HocVienUI.addUI()` thêm người xong thì gọi `TaiKhoanUI.taoTaiKhoanChoNguoi(vaiTro, id)`: nhập username (trùng thì bắt nhập lại), nhập password, rồi tạo `User` có `vaiTro` và `nguoiId` là id người vừa thêm. Khi xóa người, `GiangVienService.delete()` / `HocVienService.delete()` gọi `UserService.xoaTaiKhoanCua()` để xóa luôn tài khoản, không để tài khoản "mồ côi".

**Làm sao biết tài khoản giảng viên là giảng viên nào?**
Qua `nguoiId`. Tài khoản `minhanh` có `nguoiId = 1783209919`, nên `GiangVienMenuUI` gọi `giangVienService.findById(1783209919)`, rồi `lopHocService.locTheoGiangVien(1783209919)` để chỉ lấy lớp của giảng viên đó. Khi chọn lớp, `DiemDanhUI.chonLop()` chỉ chấp nhận id nằm trong danh sách lớp của giảng viên.

**Vì sao đăng nhập không kiểm tra regex mật khẩu?**
Regex là luật cho mật khẩu **mới**. Lúc đăng nhập chỉ cần so khớp với dữ liệu đã lưu. Nếu đăng nhập cũng kiểm tra regex thì mật khẩu mặc định `admin` (không có số) sẽ không bao giờ đăng nhập được.

**Ai được điểm danh?**
Chỉ giảng viên dạy lớp đó. `DiemDanhUI.diemDanhUI(giangVienId)` chỉ cho chọn lớp của giảng viên đang đăng nhập. Menu của admin (`DiemDanhUI.showMainMenu()`) chỉ còn *xem lịch sử buổi học* và *tổng hợp chuyên cần*: tách quyền, ai dạy người đó điểm danh, admin theo dõi.

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
Đây là unchecked exception, nên không bắt buộc phải khai báo `throws` ở mọi hàm, giống bài mẫu. Service ném lỗi ra, UI là nơi bắt lỗi và in thông báo. `HuyThaoTacException` cũng vậy: nếu là checked exception thì hàm nào có nhập liệu cũng phải ghi `throws`.

**Vì sao `InputHelper` dùng `nextLine()` rồi mới parse?**
`nextInt()` không đọc ký tự xuống dòng, nên lệnh `nextLine()` ngay sau đó bị "trôi". Đọc cả dòng rồi `parseInt` thì tránh được lỗi này, và bắt được `NumberFormatException` để cho nhập lại.

**Học viên vào lớp muộn thì tỉ lệ chuyên cần tính sao?**
Tỉ lệ = số buổi có mặt / số buổi **của chính học viên đó** (`DiemDanhService.demSoBuoiCuaHocVien()`), không chia cho tổng số buổi của lớp. Học viên mới vào chưa có buổi nào thì hiện "Chua co buoi nao" thay vì 0%. Khi điểm danh (kể cả cập nhật một buổi cũ), `DiemDanhService.layHocVienCanDiemDanh()` chỉ lấy học viên có `ngayDangKy` không sau ngày của buổi đó, nên người vào muộn không bị chấm cho buổi trước khi họ đăng ký.

**Điểm danh sai ngày thì sao?**
`DiemDanhService.laNgayCoLich()` tách chuỗi lịch học (vd `T2-T4-T6 18:00-19:30`) thành các thứ trong tuần rồi so với thứ của ngày điểm danh. `DiemDanhUI.diemDanhUI()` dùng một chuỗi `if / else if` để chọn **đúng một câu xác nhận**:
- buổi đã điểm danh → *Cap nhat lai?*
- ngày tương lai → *Ngay hoc chua den, xac nhan hoc truoc?*
- ngoài lịch → *Day la buoi hoc bu?*
- đúng lịch → *Xac nhan bat dau buoi hoc?*

Trả lời khác `y` thì thoát, không lưu gì. Chỉ ngày trước khai giảng là bị chặn hẳn. Chương trình không so giờ học, để giảng viên điểm danh trễ hoặc điểm danh bù được.

**Admin bắt đầu khai giảng một lớp thế nào? Sao không cho sửa trạng thái trực tiếp?**
*Lớp học → 1 Cập nhật lớp → 4 Bắt đầu lớp* → nhập id. Hàm gọi `LopHocService.batDauLop()`, cũng là hàm giảng viên dùng. Hàm kiểm tra lớp đang *sắp khai giảng* và đã tới ngày khai giảng. Trước đây admin đổi trạng thái được trong *Sửa lớp*, đi qua `update()` không kiểm tra gì, nên admin có thể cho lớp học trước ngày khai giảng hoặc mở lại lớp đã kết thúc. Giờ trạng thái chỉ đổi qua `batDauLop()` / `ketThucLop()`, và `update()` từ chối nếu trạng thái bị đổi. Một việc, một luật, cho mọi vai trò.

**Chọn nhầm chức năng thì thoát ra sao?**
Gõ `0` ở bất kỳ ô nhập nào. `InputHelper.inputString()` thấy `0` thì `throw new HuyThaoTacException()`. Exception này bay ngược lên tới menu gần nhất, vì mỗi menu bọc `switch` trong `try / catch (HuyThaoTacException e)`. Menu in "Da huy thao tac." rồi hiện lại chính nó. Nhờ vậy không phải viết `if (x == 0) return;` ở từng ô: một chỗ ném, mỗi menu một chỗ bắt.

Ô "Nhap lua chon" của menu đọc bằng `inputLuaChon()`, không ném lỗi, vì ở đó `0` là "Quay lai". Gõ `0` không làm mất dữ liệu nửa chừng, vì các hàm chỉ gọi `service.add()` / `update()` ở cuối, sau khi nhập xong. Riêng thêm giảng viên / học viên thì nhập tài khoản trước (`TaiKhoanUI.nhapTaiKhoan`), lưu người, rồi mới lưu tài khoản (`TaiKhoanUI.luuTaiKhoan`).

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
