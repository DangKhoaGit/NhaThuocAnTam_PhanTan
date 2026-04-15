-- ===============================
-- ADD CONSTRAINTS
-- ===============================

-- KhuyenMai
ALTER TABLE KhuyenMai
ADD CONSTRAINT CK_KhuyenMai_MaKM CHECK (MaKM REGEXP '^KM[0-9]{3}$'),
ADD CONSTRAINT CK_KhuyenMai_Ngay CHECK (NgayKetThuc > NgayBatDau),
ADD CONSTRAINT CK_KhuyenMai_So CHECK (So >= 0),
ADD CONSTRAINT CK_KhuyenMai_SoLuong CHECK (SoLuongToiDa >= 0),
ADD CONSTRAINT FK_KhuyenMai_LKM FOREIGN KEY (LoaiKhuyenMai) REFERENCES LoaiKhuyenMai(MaLKM);

-- NhanVien
ALTER TABLE NhanVien
ADD CONSTRAINT CK_NhanVien_MaNV CHECK (MaNV REGEXP '^NV[0-9]{5}$'),
ADD CONSTRAINT CK_NhanVien_Luong CHECK (LuongCoBan > 0);

-- KhachHang
ALTER TABLE KhachHang
ADD CONSTRAINT CK_KH_MaKH CHECK (MaKH REGEXP '^KH[0-9]{9}$');

-- KeThuoc
ALTER TABLE KeThuoc
ADD CONSTRAINT CK_KeThuoc_Ma CHECK (MaKe REGEXP '^KE[0-9]{4}$');

-- Thuoc
ALTER TABLE Thuoc
ADD CONSTRAINT CK_Thuoc_Ma CHECK (MaThuoc REGEXP '^VN-[0-9]{5}-[0-9]{2}$'),
ADD CONSTRAINT CK_Thuoc_GiaBan CHECK (GiaBan > 0),
ADD CONSTRAINT CK_Thuoc_GiaGoc CHECK (GiaGoc > 0),
ADD CONSTRAINT CK_Thuoc_Thue CHECK (Thue >= 0),
ADD CONSTRAINT FK_Thuoc_Ke FOREIGN KEY (MaKe) REFERENCES KeThuoc(MaKe),
ADD CONSTRAINT FK_Thuoc_DDC FOREIGN KEY (DangDieuChe) REFERENCES DangDieuChe(MaDDC),
ADD CONSTRAINT FK_Thuoc_DVT FOREIGN KEY (MaDVTCoso) REFERENCES DonViTinh(MaDVT);

-- PhieuNhap
ALTER TABLE PhieuNhap
ADD CONSTRAINT CK_PN_Ma CHECK (MaPhieuNhap REGEXP '^PN[0-9]{6}$'),
ADD CONSTRAINT FK_PN_NV FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV);

-- ChiTietPhieuNhap
ALTER TABLE ChiTietPhieuNhap
ADD CONSTRAINT CK_CTPN_SL CHECK (SoLuong > 0),
ADD CONSTRAINT CK_CTPN_Gia CHECK (GiaNhap > 0),
ADD CONSTRAINT CK_CTPN_Tien CHECK (ThanhTien > 0);

-- ChiTietThuoc
ALTER TABLE LoThuoc
ADD CONSTRAINT CK_CTT_Ton CHECK (TonKho >= 0);

-- HoaDon
ALTER TABLE HoaDon
ADD CONSTRAINT CK_HD_Ma CHECK (MaHD REGEXP '^HD[0-9]{3}$'),
ADD CONSTRAINT FK_HD_KH FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
ADD CONSTRAINT FK_HD_NV FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
ADD CONSTRAINT FK_HD_KM FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM);

-- ChiTietHoaDon
ALTER TABLE ChiTietHoaDon
ADD CONSTRAINT CK_CTHD_SL CHECK (SoLuong > 0),
ADD CONSTRAINT CK_CTHD_Tien CHECK (ThanhTien > 0);

-- PhieuDatThuoc
ALTER TABLE PhieuDatThuoc
ADD CONSTRAINT CK_PDT_Ma CHECK (MaPDT REGEXP '^PDT[0-9]{3}$'),
ADD CONSTRAINT FK_PDT_KH FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
ADD CONSTRAINT FK_PDT_NV FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
ADD CONSTRAINT FK_PDT_KM FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM);

-- ChiTietPhieuDatThuoc
ALTER TABLE ChiTietPhieuDatThuoc
ADD CONSTRAINT CK_CTPDT_SL CHECK (SoLuong > 0),
ADD CONSTRAINT CK_CTPDT_Tien CHECK (ThanhTien > 0);

-- =========================
-- Dữ liệu mẫu hoàn chỉnh
-- Chạy sau khi đã CREATE TABLE (theo schema bạn gửi)
-- =========================

# -- Disable foreign key checks to allow truncating tables with references
# SET FOREIGN_KEY_CHECKS = 0;
#
# -- Clear existing data to avoid duplicate key errors
# TRUNCATE TABLE ChiTietHoaDon;
# TRUNCATE TABLE ChiTietPhieuDatThuoc;
# TRUNCATE TABLE ChiTietPhieuNhap;
# TRUNCATE TABLE HoaDon;
# TRUNCATE TABLE LoThuoc;
# TRUNCATE TABLE PhieuDatThuoc;
# TRUNCATE TABLE PhieuNhap;
# TRUNCATE TABLE Thuoc;
# TRUNCATE TABLE KhuyenMai;
# TRUNCATE TABLE KhachHang;
# TRUNCATE TABLE NhanVien;
# TRUNCATE TABLE LoaiKhuyenMai;
# TRUNCATE TABLE DangDieuChe;
# TRUNCATE TABLE KeThuoc;
# TRUNCATE TABLE DonViTinh;
#
# -- Re-enable foreign key checks
# SET FOREIGN_KEY_CHECKS = 1;

-- 1) DonViTinh (các đơn vị cơ sở)
INSERT INTO DonViTinh (TenDVT) VALUES
('Viên'), ('Vỉ'), ('Hộp'), ('Chai'), ('Lọ'), ('Ống'), ('Tuýp'), ('Gói'), ('Bịch'), ('Hũ');

-- 2) KeThuoc (tạo 5 kệ mẫu)
INSERT INTO KeThuoc (MaKe, TenKe, LoaiKe) VALUES
('KE0001', 'Kệ 1 - Tổng hợp', 'Tổng hợp'),
('KE0002', 'Kệ 2 - Giảm đau', 'Thuốc giảm đau hạ sốt'),
('KE0003', 'Kệ 3 - Kháng sinh', 'Kháng sinh'),
('KE0004', 'Kệ 4 - Tim mạch', 'Tim mạch'),
('KE0005', 'Kệ 5 - Vitamin', 'Vitamin & Khoáng chất');

-- 3) DangDieuChe (đặt thứ tự rõ ràng để tham chiếu bằng ID)
INSERT INTO DangDieuChe (TenDDC) VALUES
('Viên nén'),
('Viên nang cứng'),
('Viên nang mềm'),
('Viên sủi'),
('Viên ngậm'),
('Viên đặt'),
('Viên nén bao phim'),
('Viên nén kéo dài'),
('Siro'),
('Hỗn dịch uống'),
('Dung dịch uống'),
('Dung dịch nhỏ mắt'),
('Dung dịch nhỏ mũi'),
('Dung dịch nhỏ tai'),
('Thuốc tiêm bột'),
('Thuốc tiêm dung dịch'),
('Thuốc mỡ'),
('Kem bôi'),
('Gel bôi'),
('Xịt mũi họng');

-- 4) LoaiKhuyenMai
INSERT INTO LoaiKhuyenMai (TenLKM) VALUES
('Giảm theo phần trăm'), ('Giảm theo số tiền'), ('Giảm giá cố định'), ('Tặng sản phẩm'), ('Miễn phí vận chuyển'), ('Giảm giá theo hạng'), ('Ưu đãi thành viên'), ('Khuyến mãi đặc biệt'), ('Giảm giá flash'), ('Quà tặng');

-- 5) KhuyenMai (10)
INSERT INTO KhuyenMai (MaKM, TenKM, NgayBatDau, NgayKetThuc, LoaiKhuyenMai, So, SoLuongToiDa)
VALUES
('KM001', 'Giảm giá 10%', '2025-01-01', '2025-12-31', 1, 10, 100),
('KM002', 'Tặng Vitamin C', '2025-02-01', '2025-06-30', 2, 10000, 200),
('KM003', 'Mua 1 tặng 1', '2025-03-01', '2025-04-30', 3, 15, 50),
('KM004', 'Tích điểm x2', '2025-01-15', '2025-12-15', 4, 2000, 500),
('KM005', 'Giảm 20k cho HĐ >200k', '2025-02-10', '2025-05-10', 5, 20000, 300),
('KM006', 'Flash sale cuối tuần', '2025-03-05', '2025-12-31', 6, 50, 100),
('KM007', 'Combo 3 hộp giảm 15%', '2025-04-01', '2025-09-30', 7, 15, 100),
('KM008', 'Voucher 50k', '2025-01-20', '2025-07-20', 8, 5000, 100),
('KM009', 'Ưu đãi thành viên VIP', '2025-02-25', '2025-12-25', 9, 5, 500),
('KM010', 'Quà sinh nhật khách', '2025-01-01', '2025-12-31', 10, 10, 1000);

-- 6) NhanVien (10)
INSERT INTO NhanVien (MaNV, HoTen, SoDienThoai, Email, DiaChi, LuongCoBan, TaiKhoan, MatKhau, IsQuanLi)
VALUES
('NV00001', 'Nguyễn Văn A', '0901111111', 'a@demo.com', N'Hà Nội', 8000000, 'Admin001', '$2a$10$X/xFV0CNENZygt45r6Mv4uZvpzasse8vZP4JILoCIflAJe/iV1Wo.', 1),
('NV00002', 'Trần Thị B', '0902222222', 'b@demo.com', N'Hồ Chí Minh', 7000000, 'Admin002', '$2a$10$kkpueKFVcVU8G7xt18LjFeLmA9aDVIBQkj7x09r.1/ZgynqagApRS', 0),
('NV00003', 'Lê Văn C', '0903333333', 'c@demo.com', N'Đà Nẵng', 7500000, 'Admin003', '$2a$10$Rab1dh6FYroUgva3Uns2XO0XmTwV6GR6.hu3TntMdsdWDL5ImMyEe', 0),
('NV00004', 'Phạm Thị D', '0904444444', 'd@demo.com', N'Hải Phòng', 6000000, 'Admin004', '$2a$10$tKCsBoJdAMnD0U7lDi3I8.wO5aCyVdgOaGWE5IbOGjCvQESLOaMU.', 0),
('NV00005', 'Đỗ Văn E', '0905555555', 'e@demo.com', N'Cần Thơ', 9000000, 'Admin005', '$2a$10$EoEt3vVfQyjuPifUshnBp.9d2yZXD16W4hgqC7t1n8M7qNlKQYd4.', 1),
('NV00006', 'Hoàng Văn F', '0906666666', 'f@demo.com', N'Nghệ An', 6500000, 'Admin006', '$2a$10$aSuLOsWiB8yKQtUiXq8Hb.YheuIeg.Cl5u/A7Tm4uOt7kclZ8fuSe', 0),
('NV00007', 'Nguyễn Thị G', '0907777777', 'g@demo.com', N'Quảng Ninh', 7200000, 'Admin007', '$2a$10$E0GiCfQc1eMA1fSxxNPlhOyPyPGqjb8L.wFfMOKe3T4hMa7tqcyy.', 0),
('NV00008', 'Trần Văn H', '0908888888', 'h@demo.com', N'Hồ Chí Minh', 8100000, 'Admin008', '$2a$10$yKREYSAWILFTrvWZqKAMaeOknKXPuAzFMNE8GLKzbsVzmfh4PxAPW', 0),
('NV00009', 'Vũ Văn I', '0909999999', 'i@demo.com', N'Hà Nội', 5600000, 'Admin009', '$2a$10$pmvOTNGy5T9XbGx15ZaNLOu65Z204ef7PGCQIEvNgMbiGQS0DIQMi', 0),
('NV00010', 'Lê Thị K', '0910000000', 'k@demo.com', N'Đồng Nai', 7700000, 'Admin010', '$2a$10$NBgxliiMLOwW2noPBBhUOuQkuDCx6.XXg8.5n78SZCHyPS7.ri.fK', 0);

INSERT INTO NhanVien (MaNV, HoTen, SoDienThoai, Email, DiaChi, LuongCoBan, TaiKhoan, MatKhau, IsQuanLi)
VALUES
('NV00011', 'Nguyễn Thái Dương', '0901111111', 'a@demo.com', N'Hà Nội', 8000000, 'Duong2008', '@Qlinh2702', 0);

-- 7) KhachHang (10)
INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai)
VALUES
('KH000000001', 'Nguyễn Văn Nam', '0911111111'),
('KH000000002', 'Trần Thị Lan', '0912222222'),
('KH000000003', 'Lê Văn Minh', '0913333333'),
('KH000000004', 'Phạm Thị Hoa', '0914444444'),
('KH000000005', 'Đỗ Văn An', '0915555555'),
('KH000000006', 'Hoàng Văn Bình', '0916666666'),
('KH000000007', 'Nguyễn Thị Cúc', '0917777777'),
('KH000000008', 'Trần Văn Dũng', '0918888888'),
('KH000000009', 'Vũ Văn Khải', '0919999999'),
('KH000000010', 'Lê Thị Mai', '0920000000');

-- 8) Thuốc (50 dòng)
INSERT INTO Thuoc (MaThuoc, TenThuoc, DangDieuChe, HamLuong, GiaBan, GiaGoc, Thue, MaDVTCoso, MaKe)
VALUES
('VN-10001-01', 'Paracetamol 500mg', 1, '500mg', 2000, 1500, 0.05, 1, 'KE0001'),
('VN-10002-01', 'Ibuprofen 400mg', 1, '400mg', 3500, 2800, 0.05, 1, 'KE0002'),
('VN-10003-01', 'Amoxicillin 500mg', 3, '500mg', 5000, 4000, 0.1, 1, 'KE0003'),
('VN-10004-01', 'Vitamin C 1000mg', 9, '1000mg', 2500, 2000, 0.05, 1, 'KE0004'),
('VN-10005-01', 'Cefuroxime 250mg', 3, '250mg', 7000, 5800, 0.1, 1, 'KE0005'),
('VN-10006-01', 'Azithromycin 250mg', 3, '250mg', 8500, 7000, 0.1, 1, 'KE0001'),
('VN-10007-01', 'Ciprofloxacin 500mg', 3, '500mg', 6500, 5200, 0.1, 1, 'KE0002'),
('VN-10008-01', 'Loratadine 10mg', 1, '10mg', 2500, 2000, 0.05, 1, 'KE0003'),
('VN-10009-01', 'Cetirizine 10mg', 1, '10mg', 2200, 1800, 0.05, 1, 'KE0004'),
('VN-10010-01', 'Metformin 500mg', 1, '500mg', 3000, 2400, 0.05, 1, 'KE0005'),
('VN-10011-01', 'Glimepiride 2mg', 1, '2mg', 3500, 2700, 0.05, 1, 'KE0001'),
('VN-10012-01', 'Losartan 50mg', 1, '50mg', 4000, 3200, 0.05, 1, 'KE0002'),
('VN-10013-01', 'Amlodipine 5mg', 1, '5mg', 3800, 3000, 0.05, 1, 'KE0003'),
('VN-10014-01', 'Omeprazole 20mg', 1, '20mg', 2800, 2200, 0.05, 1, 'KE0004'),
('VN-10015-01', 'Pantoprazole 40mg', 1, '40mg', 3200, 2500, 0.05, 1, 'KE0005'),
('VN-10016-01', 'Domperidone 10mg', 1, '10mg', 2600, 2100, 0.05, 1, 'KE0001'),
('VN-10017-01', 'Ranitidine 150mg', 1, '150mg', 2400, 1900, 0.05, 1, 'KE0002'),
('VN-10018-01', 'Simvastatin 20mg', 1, '20mg', 3000, 2400, 0.05, 1, 'KE0003'),
('VN-10019-01', 'Atorvastatin 10mg', 1, '10mg', 3400, 2700, 0.05, 1, 'KE0004'),
('VN-10020-01', 'Clopidogrel 75mg', 1, '75mg', 5000, 4200, 0.1, 1, 'KE0005'),
('VN-10021-01', 'Aspirin 81mg', 1, '81mg', 1500, 1200, 0.05, 1, 'KE0001'),
('VN-10022-01', 'Metronidazole 250mg', 3, '250mg', 2700, 2100, 0.05, 1, 'KE0002'),
('VN-10023-01', 'Doxycycline 100mg', 3, '100mg', 3200, 2500, 0.05, 1, 'KE0003'),
('VN-10024-01', 'Clarithromycin 500mg', 3, '500mg', 8000, 6500, 0.1, 1, 'KE0004'),
('VN-10025-01', 'Levofloxacin 500mg', 3, '500mg', 9000, 7500, 0.1, 1, 'KE0005'),
('VN-10026-01', 'Bromhexine 8mg', 1, '8mg', 2200, 1800, 0.05, 1, 'KE0001'),
('VN-10027-01', 'Ambroxol 30mg', 1, '30mg', 2300, 1900, 0.05, 1, 'KE0002'),
('VN-10028-01', 'Guaifenesin 100mg', 1, '100mg', 2000, 1600, 0.05, 1, 'KE0003'),
('VN-10029-01', 'Salbutamol 2mg', 1, '2mg', 2600, 2100, 0.05, 1, 'KE0004'),
('VN-10030-01', 'Montelukast 10mg', 1, '10mg', 5000, 4200, 0.1, 1, 'KE0005'),
('VN-10031-01', 'Cetrimonium Chloride 5%', 9, '5%', 25000, 20000, 0.05, 1, 'KE0001'),
('VN-10032-01', 'Sodium Chloride 0.9%', 4, '0.9%', 10000, 8000, 0.05, 1, 'KE0002'),
('VN-10033-01', 'Glucose 5%', 4, '5%', 9500, 7500, 0.05, 1, 'KE0003'),
('VN-10034-01', 'Ringer Lactate', 4, '—', 12000, 9500, 0.05, 1, 'KE0004'),
('VN-10035-01', 'Vitamin B1 100mg', 9, '100mg', 2000, 1500, 0.05, 1, 'KE0005'),
('VN-10036-01', 'Vitamin B6 50mg', 9, '50mg', 1800, 1400, 0.05, 4, 'KE0001'),
('VN-10037-01', 'Vitamin B12 500mcg', 9, '500mcg', 2500, 1900, 0.05, 1, 'KE0002'),
('VN-10038-01', 'Calcium Carbonate 500mg', 1, '500mg', 3000, 2500, 0.05, 1, 'KE0003'),
('VN-10039-01', 'Ferrous Sulfate 325mg', 1, '325mg', 2800, 2300, 0.05, 1, 'KE0004'),
('VN-10040-01', 'Folic Acid 5mg', 1, '5mg', 2500, 2000, 0.05, 1, 'KE0005'),
('VN-10041-01', 'Zinc Gluconate 10mg', 1, '10mg', 2200, 1800, 0.05, 1, 'KE0001'),
('VN-10042-01', 'Magnesium B6', 1, '—', 3000, 2400, 0.05, 1, 'KE0002'),
('VN-10043-01', 'Probiotic 3Billion CFU', 9, '3 tỷ CFU', 4500, 3500, 0.05, 1, 'KE0003'),
('VN-10044-01', 'Loperamide 2mg', 1, '2mg', 2700, 2100, 0.05, 1, 'KE0004'),
('VN-10045-01', 'ORS Gói', 7, '20.5g', 1500, 1200, 0.05, 1, 'KE0005'),
('VN-10046-01', 'Clotrimazole 1%', 6, '1%', 5000, 4200, 0.05, 6, 'KE0001'),
('VN-10047-01', 'Mupirocin 2%', 6, '2%', 6500, 5500, 0.05, 6, 'KE0002'),
('VN-10048-01', 'Hydrocortisone Cream 1%', 6, '1%', 4800, 4000, 0.05, 6, 'KE0003'),
('VN-10049-01', 'Betamethasone 0.05%', 6, '0.05%', 5500, 4500, 0.05, 6, 'KE0004'),
('VN-10050-01', 'Silver Sulfadiazine 1%', 6, '1%', 6000, 5000, 0.05, 6, 'KE0005');


-- 9) PhieuNhap (10)
INSERT INTO PhieuNhap (MaPhieuNhap, NhaCungCap, NgayNhap, DiaChi, LyDo, MaNV, TongTien)
VALUES
('PN000001', N'Công ty Dược A', '2024-05-02', 'Hà Nội', N'Nhập hàng định kỳ', 'NV00001', 1500000),
('PN000002', N'Công ty Dược B', '2024-06-16', 'Hồ Chí Minh', N'Bổ sung kho', 'NV00002', 2400000),
('PN000003', N'Công ty Dược C', '2024-04-21', 'Đà Nẵng', N'Nhập thử', 'NV00003', 1800000),
('PN000004', N'Công ty Dược D', '2024-03-11', 'Hải Phòng', N'Nhập hàng', 'NV00004', 900000),
('PN000005', N'Công ty Dược E', '2024-02-02', 'Cần Thơ', N'Nhập hàng', 'NV00005', 1100000),
('PN000006', N'Công ty Dược F', '2024-01-25', 'Nghệ An', N'Nhập hàng', 'NV00006', 1250000),
('PN000007', N'Công ty Dược G', '2024-05-20', 'Quảng Ninh', N'Nhập hàng', 'NV00007', 980000),
('PN000008', N'Công ty Dược H', '2024-06-05', 'Hồ Chí Minh', N'Nhập hàng', 'NV00008', 2100000),
('PN000009', N'Công ty Dược I', '2024-04-30', 'Hà Nội', N'Nhập hàng', 'NV00009', 500000),
('PN000010', N'Công ty Dược J', '2024-03-18', 'Đồng Nai', N'Nhập hàng', 'NV00010', 1750000),
('PN000011', N'Công ty Dược A', '2024-05-02', 'Hà Nội', N'Nhập hàng định kỳ', 'NV00001', 1500000);

-- 11) Chi tiết thuốc (50 dòng)
INSERT INTO LoThuoc (MaLoThuoc, MaThuoc, HanSuDung, NgaySanXuat, TonKho)
VALUES
(1, 'VN-10001-01', '2027-12-31', '2024-05-01', 200),
(2, 'VN-10001-01', '2028-12-31', '2025-05-01', 100),
(3, 'VN-10002-01', '2026-11-30', '2024-06-15', 150),
(4, 'VN-10002-01', '2027-11-30', '2025-06-15', 150),
(5, 'VN-10003-01', '2027-10-15', '2024-04-20', 120),
(6, 'VN-10004-01', '2026-09-01', '2024-03-10', 300),
(7, 'VN-10005-01', '2027-08-01', '2024-05-22', 180),
(8, 'VN-10006-01', '2027-06-30', '2024-06-01', 160),
(9, 'VN-10007-01', '2027-05-15', '2024-04-30', 190),
(10, 'VN-10008-01', '2026-12-31', '2024-03-05', 140),
(11, 'VN-10009-01', '2027-02-28', '2024-05-10', 130),
(12, 'VN-10010-01', '2028-01-15', '2024-04-01', 170),
(13, 'VN-10011-01', '2028-03-01', '2024-05-25', 150),
(14, 'VN-10012-01', '2027-11-30', '2024-06-10', 190),
(15, 'VN-10013-01', '2027-12-15', '2024-03-20', 160),
(16, 'VN-10014-01', '2026-10-30', '2024-04-15', 120),
(17, 'VN-10015-01', '2027-09-01', '2024-03-25', 140),
(18, 'VN-10016-01', '2027-07-31', '2024-05-01', 180),
(19, 'VN-10017-01', '2027-08-31', '2024-06-05', 200),
(20, 'VN-10018-01', '2028-02-28', '2024-04-10', 130),
(21, 'VN-10019-01', '2027-12-31', '2024-05-20', 120),
(22, 'VN-10020-01', '2028-01-15', '2024-04-01', 160),
(23, 'VN-10021-01', '2027-11-15', '2024-03-25', 300),
(24, 'VN-10022-01', '2027-10-10', '2024-05-01', 180),
(25, 'VN-10023-01', '2027-09-09', '2024-04-15', 190),
(26, 'VN-10024-01', '2027-12-30', '2024-03-10', 100),
(27, 'VN-10025-01', '2027-08-31', '2024-06-20', 90),
(28, 'VN-10026-01', '2026-12-01', '2024-04-30', 160),
(29, 'VN-10027-01', '2027-03-01', '2024-03-15', 200),
(30, 'VN-10028-01', '2027-05-15', '2024-04-12', 170),
(31, 'VN-10029-01', '2027-06-01', '2024-05-01', 120),
(32, 'VN-10030-01', '2027-09-30', '2024-06-01', 130),
(33, 'VN-10031-01', '2026-12-31', '2024-03-10', 100),
(34, 'VN-10032-01', '2026-11-30', '2024-04-05', 120),
(35, 'VN-10033-01', '2026-10-31', '2024-03-01', 150),
(36, 'VN-10034-01', '2027-01-31', '2024-04-10', 200),
(37, 'VN-10035-01', '2027-04-30', '2024-05-20', 180),
(38, 'VN-10036-01', '2027-06-30', '2024-03-15', 170),
(39, 'VN-10037-01', '2027-08-15', '2024-04-05', 160),
(40, 'VN-10038-01', '2027-10-01', '2024-06-25', 150),
(41, 'VN-10039-01', '2027-12-31', '2024-05-10', 140),
(42, 'VN-10040-01', '2028-01-15', '2024-04-01', 120),
(43, 'VN-10041-01', '2027-09-09', '2024-03-25', 130),
(44, 'VN-10042-01', '2027-07-31', '2024-05-10', 200),
(45, 'VN-10043-01', '2027-06-15', '2024-06-01', 150),
(46, 'VN-10044-01', '2027-05-01', '2024-03-10', 140),
(47, 'VN-10045-01', '2027-03-31', '2024-04-10', 300),
(48, 'VN-10046-01', '2027-02-15', '2024-05-01', 90),
(49, 'VN-10047-01', '2027-09-30', '2024-04-25', 100),
(50, 'VN-10048-01', '2027-10-15', '2024-06-01', 110),
(51, 'VN-10049-01', '2027-11-01', '2024-03-30', 95),
(52, 'VN-10050-01', '2028-01-01', '2024-05-05', 85);

-- 10) ChiTietPhieuNhap (50 dòng, chuẩn hóa)
INSERT INTO ChiTietPhieuNhap (MaPN, MaLoThuoc, MaDVT, SoLuong, GiaNhap, ThanhTien)
VALUES
-- PN000001 (5 dòng)
('PN000001', 1, 1, 200, 1200, 240000),
('PN000001', 3, 1, 150, 1500, 225000),
('PN000001', 5, 1, 120, 2000, 240000),
('PN000001', 6, 1, 300, 2400, 720000),
('PN000001', 7, 1, 180, 3000, 540000),

-- PN000002 (5 dòng)
('PN000002', 8, 1, 160, 2600, 416000),
('PN000002', 9, 1, 190, 3500, 665000),
('PN000002', 10, 1, 140, 2000, 280000),
('PN000002', 11, 1, 130, 1500, 320000),
('PN000002', 12, 1, 170, 2500, 378000),

-- PN000003 (5 dòng)
('PN000003', 13, 1, 150, 1800, 360000),
('PN000003', 14, 1, 190, 2500, 250000),
('PN000003', 15, 1, 160, 2000, 300000),
('PN000003', 16, 1, 120, 2700, 324000),
('PN000003', 17, 1, 140, 3000, 240000),

-- PN000004 (5 dòng)
('PN000004', 18, 1, 180, 2400, 260000),
('PN000004', 19, 1, 200, 2000, 196000),
('PN000004', 20, 1, 130, 2500, 270000),
('PN000004', 21, 1, 120, 3000, 432000),
('PN000004', 22, 1, 160, 4000, 500000),

-- PN000005 (5 dòng)
('PN000005', 23, 1, 300, 1000, 300000),
('PN000005', 24, 1, 180, 2500, 280000),
('PN000005', 25, 1, 190, 3000, 280000),
('PN000005', 26, 1, 100, 4500, 270000),
('PN000005', 27, 1, 90, 3200, 288000),

-- PN000006 (5 dòng)
('PN000006', 28, 1, 160, 1600, 448000),
('PN000006', 29, 1, 200, 1800, 348000),
('PN000006', 30, 1, 170, 1000, 420000),
('PN000006', 31, 1, 120, 2000, 450000),
('PN000006', 32, 1, 130, 3200, 520000),

-- PN000007 (5 dòng)
('PN000007', 33, 1, 100, 17000, 360000),
('PN000007', 34, 1, 120, 7000, 420000),
('PN000007', 35, 1, 150, 6800, 476000),
('PN000007', 36, 1, 200, 6600, 528000),
('PN000007', 37, 1, 180, 1400, 576000),

-- PN000008 (5 dòng)
('PN000008', 38, 1, 170, 1200, 820000),
('PN000008', 39, 1, 160, 1800, 546000),
('PN000008', 40, 1, 150, 2500, 450000),
('PN000008', 41, 1, 140, 2000, 400000),
('PN000008', 42, 1, 120, 1500, 340000),

-- PN000009 (5 dòng)
('PN000009', 43, 1, 130, 1400, 1400000),
('PN000009', 44, 1, 200, 1300, 1040000),
('PN000009', 45, 1, 150, 2000, 840000),
('PN000009', 46, 1, 140, 2500, 1125000),
('PN000009', 47, 1, 300, 1000, 810000),

-- PN000010 (5 dòng)
('PN000010', 48, 6, 90, 3800, 3600000),
('PN000010', 49, 6, 100, 5000, 3000000),
('PN000010', 50, 6, 110, 3000, 3750000),
('PN000010', 51, 6, 95, 3200, 3600000),
('PN000010', 52, 6, 85, 2200, 2200000),

('PN000011', 2, 1, 100, 1200, 120000),
('PN000011', 4, 1, 150, 1500, 225000);


-- 12) HoaDon (10)
INSERT INTO HoaDon (MaHD, NgayTao, MaKH, MaNV, MaKM, TongTien) VALUES
('HD001', '2025-01-10', 'KH000000001', 'NV00001', 'KM001', 18900),
('HD002', '2025-01-15', 'KH000000002', 'NV00002', 'KM002', 60000),
('HD003', '2025-02-05', 'KH000000003', 'NV00003', 'KM003', 70125),
('HD004', '2025-02-10', 'KH000000004', 'NV00004', 'KM004', 29500),
('HD005', '2025-03-01', 'KH000000005', 'NV00005', 'KM005', 148300),
('HD006', '2025-03-12', 'KH000000006', 'NV00006', 'KM006', 116875),
('HD007', '2025-04-05', 'KH000000007', 'NV00007', 'KM007', 182325),
('HD008', '2025-04-18', 'KH000000008', 'NV00008', 'KM008', 21250),
('HD009', '2025-05-01', 'KH000000009', 'NV00009', 'KM009', 17556),
('HD010', '2025-05-10', 'KH000000010', 'NV00010', 'KM010', 42525);

-- 13) ChiTietHoaDon (10)
INSERT INTO ChiTietHoaDon (MaHD, MaLoThuoc, SoLuong, MaDVT, TinhTrang, ThanhTien) VALUES
('HD001',1,10,1, N'Bán', 20000),
('HD002',2,20,2, N'Bán', 70000),
('HD003',3,15,3, N'Bán', 75000),
('HD004',4,12,4, N'Bán', 30000),
('HD005',5,18,5, N'Bán', 153000),
('HD006',6,25,6, N'Bán', 212500),
('HD007',7,30,7, N'Bán', 195000),
('HD008',8,10,8, N'Bán', 25000),
('HD009',9,8,9, N'Bán', 17600),
('HD010',10,15,10, N'Bán', 45000);

-- 14) PhieuDatThuoc (10)
INSERT INTO PhieuDatThuoc (MaPDT, NgayTao, IsThanhToan, MaKH, MaNV, MaKM, TongTien) VALUES
('PDT001', '2025-01-05', 0, 'KH000000002', 'NV00002', 'KM001', 17600),
('PDT002', '2025-01-20', 1, 'KH000000003', 'NV00003', 'KM002', 9900),
('PDT003', '2025-02-18', 0, 'KH000000004', 'NV00001', 'KM003', 10500),
('PDT004', '2025-03-10', 1, 'KH000000005', 'NV00004', 'KM004', 55000),
('PDT005', '2025-03-15', 0, 'KH000000006', 'NV00005', 'KM005', 7875),
('PDT006', '2025-03-20', 1, 'KH000000007', 'NV00006', NULL, 28280),
('PDT007', '2025-04-02', 0, 'KH000000008', 'NV00007', NULL, 6300),
('PDT008', '2025-04-14', 1, 'KH000000009', 'NV00008', NULL, 2310),
('PDT009', '2025-05-03', 0, 'KH000000010', 'NV00009', NULL, 2940),
('PDT010', '2025-05-09', 1, 'KH000000001', 'NV00010', NULL, 26250);

-- 15) ChiTietPhieuDatThuoc (10+)
INSERT INTO ChiTietPhieuDatThuoc (MaPDT, MaLoThuoc, SoLuong, MaDVT, ThanhTien) VALUES
('PDT001',26,2,1,17600),
('PDT001',27,1,2,9900),
('PDT002',1,5,3,10500),
('PDT003',5,10,4,55000),
('PDT004',7,3,5,7875),
('PDT005',9,4,6,28280),
('PDT006',12,2,7,6300),
('PDT007',11,1,8,2310),
('PDT008',41,1,9,2940),
('PDT009',33,1,10,26250);

select * from phieudatthuoc;
