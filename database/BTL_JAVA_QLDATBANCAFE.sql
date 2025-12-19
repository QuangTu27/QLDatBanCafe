CREATE DATABASE QL_DatBan_Cafe;
GO

USE QL_DatBan_Cafe;
GO

CREATE TABLE tbl_TaiKhoan (
    MaTK CHAR(10) PRIMARY KEY NOT NULL,
    TenDangNhap VARCHAR(50) NOT NULL UNIQUE,
    MatKhau VARCHAR(100) NOT NULL,
	TenHienThi NVARCHAR(100) DEFAULT N'Nhân viên', -- NEW: Để hiển thị "Xin chào, Tuấn"
    PhanQuyen INT DEFAULT 0 -- NEW: 1 là Admin, 0 là Nhân viên (Để phân quyền menu)
);

CREATE TABLE tbl_KhachHang (
    MaKhachHang CHAR(10) PRIMARY KEY NOT NULL,
    TenKhachHang NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(20) UNIQUE,
    Email VARCHAR(100)
);

CREATE TABLE tbl_Ban (
    MaBan CHAR(10) PRIMARY KEY NOT NULL,
    TenBan NVARCHAR(50) NOT NULL,
    SoGhe INT NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT N'Trống'
);

CREATE TABLE tbl_Menu (
    MaMenu CHAR(10) PRIMARY KEY NOT NULL,
    TenMon NVARCHAR(100) NOT NULL,
    DonGia DECIMAL(10,2) NOT NULL,
	LoaiMon NVARCHAR(50) DEFAULT N'Đồ uống' --: Cafe, Sinh tố, Bánh ngọt... (Để lọc trên menu cho nhanh)
);

CREATE TABLE tbl_DatBan (
    MaDatBan CHAR(10) PRIMARY KEY NOT NULL,
    MaKhachHang CHAR(10) NOT NULL,
    MaBan CHAR(10) NOT NULL,
    ThoiGianBatDau DATETIME DEFAULT GETDATE(),
    ThoiGianKetThuc DATETIME NULL,
    TrangThai NVARCHAR(20) DEFAULT N'Đã đặt',

    CONSTRAINT FK_DatBan_KhachHang FOREIGN KEY (MaKhachHang) REFERENCES tbl_KhachHang(MaKhachHang),

    CONSTRAINT FK_DatBan_Ban FOREIGN KEY (MaBan) REFERENCES tbl_Ban(MaBan)
);

CREATE TABLE tbl_ChiTietDatBan (
    MaCTDatBan CHAR(10) PRIMARY KEY NOT NULL,
    MaDatBan CHAR(10) NOT NULL,
    MaMenu CHAR(10) NOT NULL,
    SoLuong INT NOT NULL DEFAULT 1,
    DonGia DECIMAL(10,2) NOT NULL,

    CONSTRAINT FK_CTDB_DatBan FOREIGN KEY (MaDatBan) REFERENCES tbl_DatBan(MaDatBan),

    CONSTRAINT FK_CTDB_Menu FOREIGN KEY (MaMenu) REFERENCES tbl_Menu(MaMenu)
);


