USE [master]
GO
/****** Object:  Database [QuanLyNhaThuoc]    Script Date: 21/12/2025 8:30:26 CH ******/
CREATE DATABASE [QuanLyNhaThuoc]
 CONTAINMENT = NONE
 ON  PRIMARY
( NAME = N'QuanLyNhaThuoc', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.KHOA\MSSQL\DATA\QuanLyNhaThuoc.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON
( NAME = N'QuanLyNhaThuoc_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.KHOA\MSSQL\DATA\QuanLyNhaThuoc_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [QuanLyNhaThuoc] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QuanLyNhaThuoc].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ANSI_NULLS OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ANSI_PADDING OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ARITHABORT OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET AUTO_CLOSE OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [QuanLyNhaThuoc] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [QuanLyNhaThuoc] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET  ENABLE_BROKER
GO
ALTER DATABASE [QuanLyNhaThuoc] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [QuanLyNhaThuoc] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET RECOVERY FULL
GO
ALTER DATABASE [QuanLyNhaThuoc] SET  MULTI_USER
GO
ALTER DATABASE [QuanLyNhaThuoc] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [QuanLyNhaThuoc] SET DB_CHAINING OFF
GO
ALTER DATABASE [QuanLyNhaThuoc] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF )
GO
ALTER DATABASE [QuanLyNhaThuoc] SET TARGET_RECOVERY_TIME = 60 SECONDS
GO
ALTER DATABASE [QuanLyNhaThuoc] SET DELAYED_DURABILITY = DISABLED
GO
ALTER DATABASE [QuanLyNhaThuoc] SET ACCELERATED_DATABASE_RECOVERY = OFF
GO
EXEC sys.sp_db_vardecimal_storage_format N'QuanLyNhaThuoc', N'ON'
GO
ALTER DATABASE [QuanLyNhaThuoc] SET QUERY_STORE = OFF
GO
USE [QuanLyNhaThuoc]
GO
/****** Object:  Table [dbo].[ChiTietHoaDon]    Script Date: 21/12/2025 8:30:26 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietHoaDon](
    [MaHD] [nvarchar](10) NOT NULL,
    [MaCTT] [int] NOT NULL,
    [SoLuong] [int] NULL,
    [MaDVT] [int] NOT NULL,
    [TinhTrang] [nvarchar](20) NOT NULL,
    [ThanhTien] [float] NOT NULL,
    CONSTRAINT [PK_CTHD] PRIMARY KEY CLUSTERED
(
    [MaHD] ASC,
    [MaCTT] ASC,
[TinhTrang] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[ChiTietPhieuDatThuoc]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[ChiTietPhieuDatThuoc](
    [MaPDT] [nvarchar](10) NOT NULL,
    [MaCTT] [int] NOT NULL,
    [SoLuong] [int] NULL,
    [MaDVT] [int] NOT NULL,
    [TinhTrang] [nvarchar](20) NULL,
    [ThanhTien] [float] NOT NULL,
    CONSTRAINT [PK_CTPDT] PRIMARY KEY CLUSTERED
(
    [MaPDT] ASC,
[MaCTT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[ChiTietPhieuNhap]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[ChiTietPhieuNhap](
    [MaPN] [nvarchar](10) NOT NULL,
    [MaThuoc] [nvarchar](20) NOT NULL,
    [MaDVT] [int] NOT NULL,
    [SoLuong] [int] NOT NULL,
    [GiaNhap] [float] NOT NULL,
    [ThanhTien] [float] NOT NULL,
    CONSTRAINT [PK_CTPN] PRIMARY KEY CLUSTERED
(
    [MaPN] ASC,
    [MaThuoc] ASC,
[MaDVT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[ChiTietThuoc]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[ChiTietThuoc](
    [MaCTT] [int] IDENTITY(1,1) NOT NULL,
    [MaPN] [nvarchar](10) NOT NULL,
    [MaThuoc] [nvarchar](20) NOT NULL,
    [HanSuDung] [date] NOT NULL,
    [NgaySanXuat] [date] NOT NULL,
    [TonKho] [int] NULL,
    PRIMARY KEY CLUSTERED
(
[MaCTT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[DangDieuChe]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[DangDieuChe](
    [MaDDC] [int] IDENTITY(1,1) NOT NULL,
    [TenDDC] [nvarchar](50) NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaDDC] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[DonViTinh]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[DonViTinh](
    [MaDVT] [int] IDENTITY(1,1) NOT NULL,
    [TenDVT] [nvarchar](50) NOT NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaDVT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[HoaDon](
    [MaHD] [nvarchar](10) NOT NULL,
    [NgayTao] [date] NOT NULL,
    [MaKH] [nvarchar](20) NOT NULL,
    [MaNV] [nvarchar](20) NOT NULL,
    [MaKM] [nvarchar](10) NULL,
    [TongTien] [float] NOT NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[KeThuoc]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[KeThuoc](
    [MaKe] [nvarchar](10) NOT NULL,
    [TenKe] [nvarchar](100) NULL,
    [LoaiKe] [nvarchar](50) NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaKe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[KhachHang]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[KhachHang](
    [MaKH] [nvarchar](20) NOT NULL,
    [TenKH] [nvarchar](100) NOT NULL,
    [SoDienThoai] [nvarchar](15) NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[KhuyenMai]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[KhuyenMai](
    [MaKM] [nvarchar](10) NOT NULL,
    [TenKM] [nvarchar](100) NOT NULL,
    [NgayBatDau] [date] NOT NULL,
    [NgayKetThuc] [date] NOT NULL,
    [LoaiKhuyenMai] [int] NULL,
    [So] [float] NULL,
    [SoLuongToiDa] [int] NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaKM] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[LoaiKhuyenMai]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[LoaiKhuyenMai](
    [MaLKM] [int] IDENTITY(1,1) NOT NULL,
    [TenLKM] [nvarchar](20) NULL,
    PRIMARY KEY CLUSTERED
(
[MaLKM] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[NhanVien]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[NhanVien](
    [MaNV] [nvarchar](20) NOT NULL,
    [HoTen] [nvarchar](100) NOT NULL,
    [SoDienThoai] [nvarchar](15) NULL,
    [Email] [nvarchar](100) NULL,
    [DiaChi] [nvarchar](200) NULL,
    [LuongCoBan] [float] NULL,
    [TaiKhoan] [nvarchar](60) NULL,
    [MatKhau] [nvarchar](60) NULL,
    [IsQuanLi] [bit] NOT NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaNV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[PhieuDatThuoc]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[PhieuDatThuoc](
    [MaPDT] [nvarchar](10) NOT NULL,
    [NgayTao] [date] NOT NULL,
    [IsThanhToan] [bit] NOT NULL,
    [MaKH] [nvarchar](20) NOT NULL,
    [MaNV] [nvarchar](20) NOT NULL,
    [MaKM] [nvarchar](10) NULL,
    [TongTien] [float] NOT NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaPDT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[PhieuNhap]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[PhieuNhap](
    [MaPhieuNhap] [nvarchar](10) NOT NULL,
    [NhaCungCap] [nvarchar](100) NULL,
    [NgayNhap] [date] NOT NULL,
    [DiaChi] [nvarchar](50) NOT NULL,
    [LyDo] [nvarchar](50) NOT NULL,
    [MaNV] [nvarchar](20) NOT NULL,
    [TongTien] [float] NOT NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaPhieuNhap] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Thuoc]    Script Date: 21/12/2025 8:30:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Thuoc](
    [MaThuoc] [nvarchar](20) NOT NULL,
    [TenThuoc] [nvarchar](100) NOT NULL,
    [DangDieuChe] [int] NULL,
    [HamLuong] [nvarchar](50) NULL,
    [GiaBan] [float] NULL,
    [GiaGoc] [float] NULL,
    [Thue] [float] NULL,
    [MaDVTCoso] [int] NOT NULL,
    [MaKe] [nvarchar](10) NOT NULL,
    [DeleteAt] [bit] NOT NULL,
    PRIMARY KEY CLUSTERED
(
[MaThuoc] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD001', 1, 10, 1, N'Bán', 21000)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD002', 3, 20, 1, N'Bán', 73500)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD003', 5, 15, 1, N'Bán', 82500)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD004', 6, 12, 1, N'Bán', 31500)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD005', 7, 18, 1, N'Bán', 138600)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD006', 8, 25, 1, N'Bán', 233750)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD007', 9, 30, 1, N'Bán', 214500)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD008', 10, 10, 1, N'Bán', 26250)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD009', 11, 8, 1, N'Bán', 18480)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD010', 12, 15, 1, N'Bán', 47250)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD011', 35, 10, 1, N'Bán', 95000)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD011', 37, 5, 1, N'Bán', 10000)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD024', 12, 10, 1, N'Bán', 31499.998569488525)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD025', 8, 20, 1, N'Bán', 187000.00405311585)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD026', 27, 14, 1, N'Bán', 138600.0030040741)
    INSERT [dbo].[ChiTietHoaDon] ([MaHD], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'HD027', 39, 12, 1, N'Bán', 30000)
    GO
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT001', 26, 2, 1, N'Đặt', 17600)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT001', 27, 1, 1, N'Đặt', 9900)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT002', 1, 5, 1, N'Đặt', 10500)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT003', 5, 10, 1, N'Đặt', 55000)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT004', 7, 3, 1, N'Đặt', 7875)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT005', 9, 4, 1, N'Hủy', 28280)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT006', 12, 2, 1, N'Đặt', 6300)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT007', 11, 1, 1, N'Đặt', 2310)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT008', 41, 1, 1, N'Đặt', 2940)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT009', 33, 1, 1, N'Đặt', 26250)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT011', 20, 12, 1, N'Đặt', 37799.99828338623)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT011', 31, 12, 1, N'Đặt', 32759.998512268066)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT012', 6, 24, 1, N'Đặt', 62999.997138977051)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT012', 34, 12, 1, N'Đặt', 125999.9942779541)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT013', 6, 5, 1, N'Đặt', 13124.999403953552)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT014', 5, 13, 1, N'Đặt', 71500.001549720764)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT015', 5, 14, 1, N'Đặt', 77000.001668930054)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT016', 53, 7, 1, N'Đặt', 18374.999165534973)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT017', 7, 3, 1, N'Đặt', 23100.000500679016)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT018', 8, 1, 1, N'Đặt', 9350.0002026557922)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT019', 12, 10, 1, N'Đặt', 31499.998569488525)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT020', 8, 20, 1, N'Đặt', 187000.00405311585)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT021', 27, 14, 1, N'Đặt', 138600.0030040741)
    INSERT [dbo].[ChiTietPhieuDatThuoc] ([MaPDT], [MaCTT], [SoLuong], [MaDVT], [TinhTrang], [ThanhTien]) VALUES (N'PDT022', 23, 12, 1, N'Đặt', 18899.999141693115)
    GO
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000001', N'VN-10001-01', 1, 200, 1200, 240000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000001', N'VN-10002-01', 1, 150, 1500, 225000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000001', N'VN-10003-01', 1, 120, 2000, 240000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000001', N'VN-10004-01', 1, 300, 2400, 720000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000001', N'VN-10005-01', 1, 180, 3000, 540000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000002', N'VN-10006-01', 1, 160, 2600, 416000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000002', N'VN-10007-01', 1, 190, 3500, 665000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000002', N'VN-10008-01', 1, 140, 2000, 280000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000002', N'VN-10009-01', 1, 130, 1500, 320000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000002', N'VN-10010-01', 1, 170, 2500, 378000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000003', N'VN-10011-01', 1, 150, 1800, 360000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000003', N'VN-10012-01', 1, 190, 2500, 250000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000003', N'VN-10013-01', 1, 160, 2000, 300000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000003', N'VN-10014-01', 1, 120, 2700, 324000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000003', N'VN-10015-01', 1, 140, 3000, 240000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000004', N'VN-10016-01', 1, 180, 2400, 260000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000004', N'VN-10017-01', 1, 200, 2000, 196000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000004', N'VN-10018-01', 1, 130, 2500, 270000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000004', N'VN-10019-01', 1, 120, 3000, 432000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000004', N'VN-10020-01', 1, 160, 4000, 500000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000005', N'VN-10021-01', 1, 300, 1000, 300000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000005', N'VN-10022-01', 1, 180, 2500, 280000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000005', N'VN-10023-01', 1, 190, 3000, 280000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000005', N'VN-10024-01', 1, 100, 4500, 270000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000005', N'VN-10025-01', 1, 90, 3200, 288000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000006', N'VN-10026-01', 1, 160, 1600, 448000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000006', N'VN-10027-01', 1, 200, 1800, 348000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000006', N'VN-10028-01', 1, 170, 1000, 420000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000006', N'VN-10029-01', 1, 120, 2000, 450000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000006', N'VN-10030-01', 1, 130, 3200, 520000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000007', N'VN-10031-01', 1, 100, 17000, 360000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000007', N'VN-10032-01', 1, 120, 7000, 420000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000007', N'VN-10033-01', 1, 150, 6800, 476000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000007', N'VN-10034-01', 1, 200, 6600, 528000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000007', N'VN-10035-01', 1, 180, 1400, 576000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000008', N'VN-10036-01', 1, 170, 1200, 820000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000008', N'VN-10037-01', 1, 160, 1800, 546000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000008', N'VN-10038-01', 1, 150, 2500, 450000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000008', N'VN-10039-01', 1, 140, 2000, 400000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000008', N'VN-10040-01', 1, 120, 1500, 340000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000009', N'VN-10041-01', 1, 130, 1400, 1400000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000009', N'VN-10042-01', 1, 200, 1300, 1040000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000009', N'VN-10043-01', 1, 150, 2000, 840000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000009', N'VN-10044-01', 1, 140, 2500, 1125000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000009', N'VN-10045-01', 1, 300, 1000, 810000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000010', N'VN-10046-01', 4, 90, 3800, 3600000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000010', N'VN-10047-01', 4, 100, 5000, 3000000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000010', N'VN-10048-01', 4, 110, 3000, 3750000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000010', N'VN-10049-01', 4, 95, 3200, 3600000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000010', N'VN-10050-01', 4, 85, 2200, 2200000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000011', N'VN-10001-01', 1, 100, 1200, 120000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000011', N'VN-10002-01', 1, 150, 1500, 225000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000012', N'VN-10004-01', 1, 100, 10000, 1000000)
    INSERT [dbo].[ChiTietPhieuNhap] ([MaPN], [MaThuoc], [MaDVT], [SoLuong], [GiaNhap], [ThanhTien]) VALUES (N'PN000012', N'VN-10008-01', 1, 100, 12000, 1200000)
    GO
    SET IDENTITY_INSERT [dbo].[ChiTietThuoc] ON

    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (1, N'PN000001', N'VN-10001-01', CAST(N'2027-12-31' AS Date), CAST(N'2024-05-01' AS Date), 200)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (2, N'PN000011', N'VN-10001-01', CAST(N'2028-12-31' AS Date), CAST(N'2025-05-01' AS Date), 100)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (3, N'PN000001', N'VN-10002-01', CAST(N'2026-11-30' AS Date), CAST(N'2024-06-15' AS Date), 150)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (4, N'PN000011', N'VN-10002-01', CAST(N'2027-11-30' AS Date), CAST(N'2025-06-15' AS Date), 150)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (5, N'PN000001', N'VN-10003-01', CAST(N'2027-10-15' AS Date), CAST(N'2024-04-20' AS Date), 440)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (6, N'PN000001', N'VN-10004-01', CAST(N'2026-09-01' AS Date), CAST(N'2024-03-10' AS Date), 1147)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (7, N'PN000001', N'VN-10005-01', CAST(N'2027-08-01' AS Date), CAST(N'2024-05-22' AS Date), 357)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (8, N'PN000002', N'VN-10006-01', CAST(N'2027-06-30' AS Date), CAST(N'2024-06-01' AS Date), 618)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (9, N'PN000002', N'VN-10007-01', CAST(N'2027-05-15' AS Date), CAST(N'2024-04-30' AS Date), 384)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (10, N'PN000002', N'VN-10008-01', CAST(N'2026-12-31' AS Date), CAST(N'2024-03-05' AS Date), 140)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (11, N'PN000002', N'VN-10009-01', CAST(N'2027-02-28' AS Date), CAST(N'2024-05-10' AS Date), 130)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (12, N'PN000002', N'VN-10010-01', CAST(N'2028-01-15' AS Date), CAST(N'2024-04-01' AS Date), 330)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (13, N'PN000003', N'VN-10011-01', CAST(N'2028-03-01' AS Date), CAST(N'2024-05-25' AS Date), 150)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (14, N'PN000003', N'VN-10012-01', CAST(N'2027-11-30' AS Date), CAST(N'2024-06-10' AS Date), 190)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (15, N'PN000003', N'VN-10013-01', CAST(N'2027-12-15' AS Date), CAST(N'2024-03-20' AS Date), 160)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (16, N'PN000003', N'VN-10014-01', CAST(N'2026-10-30' AS Date), CAST(N'2024-04-15' AS Date), 120)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (17, N'PN000003', N'VN-10015-01', CAST(N'2027-09-01' AS Date), CAST(N'2024-03-25' AS Date), 140)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (18, N'PN000004', N'VN-10016-01', CAST(N'2027-07-31' AS Date), CAST(N'2024-05-01' AS Date), 180)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (19, N'PN000004', N'VN-10017-01', CAST(N'2027-08-31' AS Date), CAST(N'2024-06-05' AS Date), 200)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (20, N'PN000004', N'VN-10018-01', CAST(N'2028-02-28' AS Date), CAST(N'2024-04-10' AS Date), 248)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (21, N'PN000004', N'VN-10019-01', CAST(N'2027-12-31' AS Date), CAST(N'2024-05-20' AS Date), 120)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (22, N'PN000004', N'VN-10020-01', CAST(N'2028-01-15' AS Date), CAST(N'2024-04-01' AS Date), 160)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (23, N'PN000005', N'VN-10021-01', CAST(N'2027-11-15' AS Date), CAST(N'2024-03-25' AS Date), 588)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (24, N'PN000005', N'VN-10022-01', CAST(N'2027-10-10' AS Date), CAST(N'2024-05-01' AS Date), 180)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (25, N'PN000005', N'VN-10023-01', CAST(N'2027-09-09' AS Date), CAST(N'2024-04-15' AS Date), 190)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (26, N'PN000005', N'VN-10024-01', CAST(N'2027-12-30' AS Date), CAST(N'2024-03-10' AS Date), 100)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (27, N'PN000005', N'VN-10025-01', CAST(N'2027-08-31' AS Date), CAST(N'2024-06-20' AS Date), 166)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (28, N'PN000006', N'VN-10026-01', CAST(N'2026-12-01' AS Date), CAST(N'2024-04-30' AS Date), 160)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (29, N'PN000006', N'VN-10027-01', CAST(N'2027-03-01' AS Date), CAST(N'2024-03-15' AS Date), 200)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (30, N'PN000006', N'VN-10028-01', CAST(N'2027-05-15' AS Date), CAST(N'2024-04-12' AS Date), 170)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (31, N'PN000006', N'VN-10029-01', CAST(N'2027-06-01' AS Date), CAST(N'2024-05-01' AS Date), 228)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (32, N'PN000006', N'VN-10030-01', CAST(N'2027-09-30' AS Date), CAST(N'2024-06-01' AS Date), 130)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (33, N'PN000007', N'VN-10031-01', CAST(N'2026-12-31' AS Date), CAST(N'2024-03-10' AS Date), 100)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (34, N'PN000007', N'VN-10032-01', CAST(N'2026-11-30' AS Date), CAST(N'2024-04-05' AS Date), 228)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (35, N'PN000007', N'VN-10033-01', CAST(N'2026-10-31' AS Date), CAST(N'2024-03-01' AS Date), 140)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (36, N'PN000007', N'VN-10034-01', CAST(N'2027-01-31' AS Date), CAST(N'2024-04-10' AS Date), 200)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (37, N'PN000007', N'VN-10035-01', CAST(N'2027-04-30' AS Date), CAST(N'2024-05-20' AS Date), 175)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (38, N'PN000008', N'VN-10036-01', CAST(N'2027-06-30' AS Date), CAST(N'2024-03-15' AS Date), 170)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (39, N'PN000008', N'VN-10037-01', CAST(N'2027-08-15' AS Date), CAST(N'2024-04-05' AS Date), 148)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (40, N'PN000008', N'VN-10038-01', CAST(N'2027-10-01' AS Date), CAST(N'2024-06-25' AS Date), 150)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (41, N'PN000008', N'VN-10039-01', CAST(N'2027-12-31' AS Date), CAST(N'2024-05-10' AS Date), 140)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (42, N'PN000008', N'VN-10040-01', CAST(N'2028-01-15' AS Date), CAST(N'2024-04-01' AS Date), 120)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (43, N'PN000009', N'VN-10041-01', CAST(N'2027-09-09' AS Date), CAST(N'2024-03-25' AS Date), 130)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (44, N'PN000009', N'VN-10042-01', CAST(N'2027-07-31' AS Date), CAST(N'2024-05-10' AS Date), 200)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (45, N'PN000009', N'VN-10043-01', CAST(N'2027-06-15' AS Date), CAST(N'2024-06-01' AS Date), 150)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (46, N'PN000009', N'VN-10044-01', CAST(N'2027-05-01' AS Date), CAST(N'2024-03-10' AS Date), 140)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (47, N'PN000009', N'VN-10045-01', CAST(N'2027-03-31' AS Date), CAST(N'2024-04-10' AS Date), 300)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (48, N'PN000010', N'VN-10046-01', CAST(N'2027-02-15' AS Date), CAST(N'2024-05-01' AS Date), 90)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (49, N'PN000010', N'VN-10047-01', CAST(N'2027-09-30' AS Date), CAST(N'2024-04-25' AS Date), 100)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (50, N'PN000010', N'VN-10048-01', CAST(N'2027-10-15' AS Date), CAST(N'2024-06-01' AS Date), 110)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (51, N'PN000010', N'VN-10049-01', CAST(N'2027-11-01' AS Date), CAST(N'2024-03-30' AS Date), 95)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (52, N'PN000010', N'VN-10050-01', CAST(N'2028-01-01' AS Date), CAST(N'2024-05-05' AS Date), 85)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (53, N'PN000012', N'VN-10008-01', CAST(N'2026-01-11' AS Date), CAST(N'2025-12-02' AS Date), 193)
    INSERT [dbo].[ChiTietThuoc] ([MaCTT], [MaPN], [MaThuoc], [HanSuDung], [NgaySanXuat], [TonKho]) VALUES (54, N'PN000012', N'VN-10004-01', CAST(N'2026-12-26' AS Date), CAST(N'2025-10-28' AS Date), 100)
    SET IDENTITY_INSERT [dbo].[ChiTietThuoc] OFF
    GO
    SET IDENTITY_INSERT [dbo].[DangDieuChe] ON

    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (1, N'Viên nén', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (2, N'Viên nang cứng', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (3, N'Viên nang mềm', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (4, N'Viên sủi', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (5, N'Viên ngậm', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (6, N'Viên đặt', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (7, N'Viên nén bao phim', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (8, N'Viên nén kéo dài', 1)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (9, N'Siro', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (10, N'Hỗn dịch uống', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (11, N'Dung dịch uống', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (12, N'Dung dịch nhỏ mắt', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (13, N'Dung dịch nhỏ mũi', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (14, N'Dung dịch nhỏ tai', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (15, N'Thuốc tiêm bột', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (16, N'Thuốc tiêm dung dịch', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (17, N'Thuốc mỡ', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (18, N'Kem bôi', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (19, N'Gel bôi', 0)
    INSERT [dbo].[DangDieuChe] ([MaDDC], [TenDDC], [DeleteAt]) VALUES (20, N'Xịt mũi họng', 0)
    SET IDENTITY_INSERT [dbo].[DangDieuChe] OFF
    GO
    SET IDENTITY_INSERT [dbo].[DonViTinh] ON

    INSERT [dbo].[DonViTinh] ([MaDVT], [TenDVT], [DeleteAt]) VALUES (1, N'Viên', 0)
    INSERT [dbo].[DonViTinh] ([MaDVT], [TenDVT], [DeleteAt]) VALUES (2, N'Chai', 0)
    INSERT [dbo].[DonViTinh] ([MaDVT], [TenDVT], [DeleteAt]) VALUES (3, N'Lọ', 0)
    INSERT [dbo].[DonViTinh] ([MaDVT], [TenDVT], [DeleteAt]) VALUES (4, N'Ống', 0)
    INSERT [dbo].[DonViTinh] ([MaDVT], [TenDVT], [DeleteAt]) VALUES (5, N'Tuýp', 0)
    SET IDENTITY_INSERT [dbo].[DonViTinh] OFF
    GO
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD001', CAST(N'2025-01-10' AS Date), N'KH000000001', N'NV00001', N'KM001', 18900, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD002', CAST(N'2025-01-15' AS Date), N'KH000000002', N'NV00002', N'KM002', 63500, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD003', CAST(N'2025-02-05' AS Date), N'KH000000003', N'NV00003', N'KM003', 70125, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD004', CAST(N'2025-02-10' AS Date), N'KH000000004', N'NV00004', N'KM004', 29500, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD005', CAST(N'2025-03-01' AS Date), N'KH000000005', N'NV00005', N'KM005', 118600, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD006', CAST(N'2025-03-12' AS Date), N'KH000000006', N'NV00006', N'KM006', 116875, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD007', CAST(N'2025-04-05' AS Date), N'KH000000007', N'NV00007', N'KM007', 182325, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD008', CAST(N'2025-04-18' AS Date), N'KH000000008', N'NV00008', N'KM008', 21250, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD009', CAST(N'2025-05-01' AS Date), N'KH000000009', N'NV00009', N'KM009', 17556, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD010', CAST(N'2025-05-10' AS Date), N'KH000000010', N'NV00010', N'KM010', 42525, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD011', CAST(N'2025-12-18' AS Date), N'KH000000011', N'NV00011', N'KM009', 104737.50007431954, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD012', CAST(N'2025-12-19' AS Date), N'KH000000011', N'NV00011', N'KM001', 57455.999279022217, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD013', CAST(N'2025-12-19' AS Date), N'KH000000012', N'NV00011', N'KM009', 162449.99796152115, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD014', CAST(N'2025-12-19' AS Date), N'KH000000010', N'NV00011', NULL, 2940, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD015', CAST(N'2025-12-19' AS Date), N'KH000000008', N'NV00011', NULL, 6300, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD016', CAST(N'2025-12-19' AS Date), N'KH000000002', N'NV00011', NULL, 17600, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD017', CAST(N'2025-12-19' AS Date), N'KH000000004', N'NV00011', NULL, 11874.999850988388, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD018', CAST(N'2025-12-19' AS Date), N'KH000000006', N'NV00011', NULL, 58499.998450279236, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD019', CAST(N'2025-12-19' AS Date), N'KH000000004', N'NV00011', NULL, 10500, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD020', CAST(N'2025-12-19' AS Date), N'KH000000007', N'NV00011', NULL, 62999.998331069946, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD021', CAST(N'2025-12-19' AS Date), N'KH000000006', N'NV00011', NULL, 16624.999791383743, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD022', CAST(N'2025-12-19' AS Date), N'KH000000008', N'NV00011', NULL, 18899.999499320984, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD023', CAST(N'2025-12-19' AS Date), N'KH000000007', N'NV00011', NULL, 7649.9997973442078, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD024', CAST(N'2025-12-19' AS Date), N'KH000000010', N'NV00011', NULL, 28499.999642372131, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD025', CAST(N'2025-12-19' AS Date), N'KH000000004', N'NV00011', N'KM009', 145349.99614953995, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD026', CAST(N'2025-12-19' AS Date), N'KH000000013', N'NV00011', NULL, 113399.9969959259, 0)
    INSERT [dbo].[HoaDon] ([MaHD], [NgayTao], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'HD027', CAST(N'2025-12-19' AS Date), N'KH000000011', N'NV00011', NULL, 31500.000022351742, 0)
    GO
    INSERT [dbo].[KeThuoc] ([MaKe], [TenKe], [LoaiKe], [DeleteAt]) VALUES (N'KE0001', N'Kệ 1 - Tổng hợp', N'Tổng hợp', 0)
    INSERT [dbo].[KeThuoc] ([MaKe], [TenKe], [LoaiKe], [DeleteAt]) VALUES (N'KE0002', N'Kệ 2 - Giảm đau', N'Thuốc giảm đau hạ sốt', 0)
    INSERT [dbo].[KeThuoc] ([MaKe], [TenKe], [LoaiKe], [DeleteAt]) VALUES (N'KE0003', N'Kệ 3 - Kháng sinh', N'Kháng sinh', 0)
    INSERT [dbo].[KeThuoc] ([MaKe], [TenKe], [LoaiKe], [DeleteAt]) VALUES (N'KE0004', N'Kệ 4 - Tim mạch', N'Tim mạch', 0)
    INSERT [dbo].[KeThuoc] ([MaKe], [TenKe], [LoaiKe], [DeleteAt]) VALUES (N'KE0005', N'Kệ 5 - Vitamin', N'Vitamin & Khoáng chất', 0)
    GO
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000001', N'Nguyễn Văn Nam', N'0911111111', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000002', N'Trần Thị Lan', N'0912222222', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000003', N'Lê Văn Minh', N'0913333333', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000004', N'Phạm Thị Hoa', N'0914444444', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000005', N'Đỗ Văn An', N'0915555555', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000006', N'Hoàng Văn Bình', N'0916666666', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000007', N'Nguyễn Thị Cúc', N'0917777777', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000008', N'Trần Văn Dũng', N'0918888888', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000009', N'Vũ Văn Khải', N'0919999999', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000010', N'Lê Thị Mai', N'0920000000', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000011', N'Phạm Đăng Khoa', N'0393117635', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000012', N'Vũ Văn Thanh', N'0712389125', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000013', N'Kiều Nữ Trang', N'0345159268', 0)
    INSERT [dbo].[KhachHang] ([MaKH], [TenKH], [SoDienThoai], [DeleteAt]) VALUES (N'KH000000014', N'Phạm Minh Khôi', N'0319231052', 0)
    GO
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM001', N'Giảm giá 10%', CAST(N'2025-01-01' AS Date), CAST(N'2025-12-31' AS Date), 1, 10, 100, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM002', N'Tặng Vitamin C', CAST(N'2025-02-01' AS Date), CAST(N'2025-06-30' AS Date), 2, 10000, 200, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM003', N'Mua 1 tặng 1', CAST(N'2025-03-01' AS Date), CAST(N'2025-04-30' AS Date), 1, 15, 50, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM004', N'Tích điểm x2', CAST(N'2025-01-15' AS Date), CAST(N'2025-12-15' AS Date), 2, 2000, 500, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM005', N'Giảm 20k cho HĐ >200k', CAST(N'2025-02-10' AS Date), CAST(N'2025-05-10' AS Date), 2, 20000, 300, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM006', N'Flash sale cuối tuần', CAST(N'2025-03-05' AS Date), CAST(N'2025-12-31' AS Date), 1, 50, 100, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM007', N'Combo 3 hộp giảm 15%', CAST(N'2025-04-01' AS Date), CAST(N'2025-09-30' AS Date), 1, 15, 100, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM008', N'Voucher 50k', CAST(N'2025-01-20' AS Date), CAST(N'2025-07-20' AS Date), 2, 5000, 100, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM009', N'Ưu đãi thành viên VIP', CAST(N'2025-02-25' AS Date), CAST(N'2025-12-25' AS Date), 1, 5, 500, 0)
    INSERT [dbo].[KhuyenMai] ([MaKM], [TenKM], [NgayBatDau], [NgayKetThuc], [LoaiKhuyenMai], [So], [SoLuongToiDa], [DeleteAt]) VALUES (N'KM010', N'Quà sinh nhật khách', CAST(N'2025-01-01' AS Date), CAST(N'2025-12-31' AS Date), 1, 10, 1000, 0)
    GO
    SET IDENTITY_INSERT [dbo].[LoaiKhuyenMai] ON

    INSERT [dbo].[LoaiKhuyenMai] ([MaLKM], [TenLKM]) VALUES (1, N'Giảm theo phần trăm')
    INSERT [dbo].[LoaiKhuyenMai] ([MaLKM], [TenLKM]) VALUES (2, N'Giảm theo số tiền')
    SET IDENTITY_INSERT [dbo].[LoaiKhuyenMai] OFF
    GO
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00001', N'Nguyễn Văn A', N'0901111111', N'a@demo.com', N'Hà Nội', 8000000, N'Admin001', N'$2a$10$X/xFV0CNENZygt45r6Mv4uZvpzasse8vZP4JILoCIflAJe/iV1Wo.', 1, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00002', N'Trần Thị B', N'0902222222', N'b@demo.com', N'Hồ Chí Minh', 7000000, N'Admin002', N'$2a$10$kkpueKFVcVU8G7xt18LjFeLmA9aDVIBQkj7x09r.1/ZgynqagApRS', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00003', N'Lê Văn C', N'0903333333', N'c@demo.com', N'Đà Nẵng', 7500000, N'Admin003', N'$2a$10$Rab1dh6FYroUgva3Uns2XO0XmTwV6GR6.hu3TntMdsdWDL5ImMyEe', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00004', N'Phạm Thị D', N'0904444444', N'd@demo.com', N'Hải Phòng', 6000000, N'Admin004', N'$2a$10$tKCsBoJdAMnD0U7lDi3I8.wO5aCyVdgOaGWE5IbOGjCvQESLOaMU.', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00005', N'Đỗ Văn E', N'0905555555', N'e@demo.com', N'Cần Thơ', 9000000, N'Admin005', N'$2a$10$EoEt3vVfQyjuPifUshnBp.9d2yZXD16W4hgqC7t1n8M7qNlKQYd4.', 1, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00006', N'Hoàng Văn F', N'0906666666', N'f@demo.com', N'Nghệ An', 6500000, N'Admin006', N'$2a$10$aSuLOsWiB8yKQtUiXq8Hb.YheuIeg.Cl5u/A7Tm4uOt7kclZ8fuSe', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00007', N'Nguyễn Thị G', N'0907777777', N'g@demo.com', N'Quảng Ninh', 7200000, N'Admin007', N'$2a$10$E0GiCfQc1eMA1fSxxNPlhOyPyPGqjb8L.wFfMOKe3T4hMa7tqcyy.', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00008', N'Trần Văn H', N'0908888888', N'h@demo.com', N'Hồ Chí Minh', 8100000, N'Admin008', N'$2a$10$yKREYSAWILFTrvWZqKAMaeOknKXPuAzFMNE8GLKzbsVzmfh4PxAPW', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00009', N'Vũ Văn I', N'0909999999', N'i@demo.com', N'Hà Nội', 5600000, N'Admin009', N'$2a$10$pmvOTNGy5T9XbGx15ZaNLOu65Z204ef7PGCQIEvNgMbiGQS0DIQMi', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00010', N'Lê Thị K', N'0910000000', N'k@demo.com', N'Đồng Nai', 7700000, N'Admin010', N'$2a$10$NBgxliiMLOwW2noPBBhUOuQkuDCx6.XXg8.5n78SZCHyPS7.ri.fK', 0, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00011', N'Phạm Đăng Khoa', N'0391199992', N'dk@gmail.com', N'Long Định', 15000000, N'dangkhoa123', N'$2a$10$.Zk2ghacRW2GFixl.RBHY.FSABSzOGa7RPbq3ubt3NkZRGdVJc7F.', 1, 0)
    INSERT [dbo].[NhanVien] ([MaNV], [HoTen], [SoDienThoai], [Email], [DiaChi], [LuongCoBan], [TaiKhoan], [MatKhau], [IsQuanLi], [DeleteAt]) VALUES (N'NV00012', N'Phạm Thị Kiều Trang', N'0912302951', N'kieuTrangcute123@gmail.com', N'123 Lương Sơn Trạch', 10000000, N'kieutrang123', N'$2a$10$gN5AWIvEicInHYRHUXv08..hyjLJ/qtqSRdyz9UaWbff08XbZ0DCy', 0, 0)
    GO
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT001', CAST(N'2025-01-05' AS Date), 1, N'KH000000002', N'NV00002', NULL, 17600, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT002', CAST(N'2025-01-20' AS Date), 1, N'KH000000003', N'NV00003', NULL, 9900, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT003', CAST(N'2025-02-18' AS Date), 1, N'KH000000004', N'NV00001', NULL, 10500, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT004', CAST(N'2025-03-10' AS Date), 1, N'KH000000005', N'NV00004', NULL, 55000, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT005', CAST(N'2025-03-15' AS Date), 0, N'KH000000006', N'NV00005', NULL, 7875, 1)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT006', CAST(N'2025-03-20' AS Date), 1, N'KH000000007', N'NV00006', NULL, 28280, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT007', CAST(N'2025-04-02' AS Date), 1, N'KH000000008', N'NV00007', NULL, 6300, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT008', CAST(N'2025-04-14' AS Date), 1, N'KH000000009', N'NV00008', NULL, 2310, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT009', CAST(N'2025-05-03' AS Date), 1, N'KH000000010', N'NV00009', NULL, 2940, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT010', CAST(N'2025-05-09' AS Date), 1, N'KH000000001', N'NV00010', NULL, 26250, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT011', CAST(N'2025-12-19' AS Date), 1, N'KH000000011', N'NV00011', N'KM001', 57455.999279022217, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT012', CAST(N'2025-12-19' AS Date), 1, N'KH000000012', N'NV00011', N'KM009', 162449.99796152115, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT013', CAST(N'2025-12-19' AS Date), 1, N'KH000000004', N'NV00011', NULL, 11874.999850988388, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT014', CAST(N'2025-12-19' AS Date), 1, N'KH000000006', N'NV00011', NULL, 58499.998450279236, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT015', CAST(N'2025-12-19' AS Date), 1, N'KH000000007', N'NV00011', NULL, 62999.998331069946, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT016', CAST(N'2025-12-19' AS Date), 1, N'KH000000006', N'NV00011', NULL, 16624.999791383743, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT017', CAST(N'2025-12-19' AS Date), 1, N'KH000000008', N'NV00011', NULL, 18899.999499320984, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT018', CAST(N'2025-12-19' AS Date), 1, N'KH000000007', N'NV00011', NULL, 7649.9997973442078, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT019', CAST(N'2025-12-19' AS Date), 1, N'KH000000010', N'NV00011', NULL, 28499.999642372131, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT020', CAST(N'2025-12-19' AS Date), 1, N'KH000000004', N'NV00011', N'KM009', 145349.99614953995, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT021', CAST(N'2025-12-19' AS Date), 1, N'KH000000013', N'NV00011', NULL, 113399.9969959259, 0)
    INSERT [dbo].[PhieuDatThuoc] ([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien], [DeleteAt]) VALUES (N'PDT022', CAST(N'2025-12-19' AS Date), 0, N'KH000000014', N'NV00011', N'KM001', 15389.999806880951, 0)
    GO
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000001', N'Công ty Dược A', CAST(N'2024-05-02' AS Date), N'Hà Nội', N'Nhập hàng định kỳ', N'NV00001', 1965000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000002', N'Công ty Dược B', CAST(N'2024-06-16' AS Date), N'Hồ Chí Minh', N'Bổ sung kho', N'NV00002', 2059000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000003', N'Công ty Dược C', CAST(N'2024-04-21' AS Date), N'Đà Nẵng', N'Nhập thử', N'NV00003', 1474000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000004', N'Công ty Dược D', CAST(N'2024-03-11' AS Date), N'Hải Phòng', N'Nhập hàng', N'NV00004', 1658000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000005', N'Công ty Dược E', CAST(N'2024-02-02' AS Date), N'Cần Thơ', N'Nhập hàng', N'NV00005', 1418000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000006', N'Công ty Dược F', CAST(N'2024-01-25' AS Date), N'Nghệ An', N'Nhập hàng', N'NV00006', 2186000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000007', N'Công ty Dược G', CAST(N'2024-05-20' AS Date), N'Quảng Ninh', N'Nhập hàng', N'NV00007', 2360000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000008', N'Công ty Dược H', CAST(N'2024-06-05' AS Date), N'Hồ Chí Minh', N'Nhập hàng', N'NV00008', 2556000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000009', N'Công ty Dược I', CAST(N'2024-04-30' AS Date), N'Hà Nội', N'Nhập hàng', N'NV00009', 5215000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000010', N'Công ty Dược J', CAST(N'2024-03-18' AS Date), N'Đồng Nai', N'Nhập hàng', N'NV00010', 16150000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000011', N'Công ty Dược A', CAST(N'2024-05-02' AS Date), N'Hà Nội', N'Nhập hàng định kỳ', N'NV00001', 345000, 0)
    INSERT [dbo].[PhieuNhap] ([MaPhieuNhap], [NhaCungCap], [NgayNhap], [DiaChi], [LyDo], [MaNV], [TongTien], [DeleteAt]) VALUES (N'PN000012', N'Công ty Dược An Tán', CAST(N'2025-12-18' AS Date), N'123 đường An Thần, tỉnh Hòa Bình', N'Cung cấp thuốc', N'NV00011', 2200000, 0)
    GO
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10001-01', N'Paracetamol 500mg', 1, N'500mg', 2000, 1500, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10002-01', N'Ibuprofen 400mg', 1, N'400mg', 3500, 2800, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10003-01', N'Amoxicillin 500mg', 3, N'500mg', 5000, 4000, 0.1, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10004-01', N'Vitamin C 1000mg', 9, N'1000mg', 2500, 2000, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10005-01', N'Cefuroxime 250mg', 3, N'250mg', 7000, 5800, 0.1, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10006-01', N'Azithromycin 250mg', 3, N'250mg', 8500, 7000, 0.1, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10007-01', N'Ciprofloxacin 500mg', 3, N'500mg', 6500, 5200, 0.1, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10008-01', N'Loratadine 10mg', 1, N'10mg', 2500, 2000, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10009-01', N'Cetirizine 10mg', 1, N'10mg', 2200, 1800, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10010-01', N'Metformin 500mg', 1, N'500mg', 3000, 2400, 0.05, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10011-01', N'Glimepiride 2mg', 1, N'2mg', 3500, 2700, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10012-01', N'Losartan 50mg', 1, N'50mg', 4000, 3200, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10013-01', N'Amlodipine 5mg', 1, N'5mg', 3800, 3000, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10014-01', N'Omeprazole 20mg', 1, N'20mg', 2800, 2200, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10015-01', N'Pantoprazole 40mg', 1, N'40mg', 3200, 2500, 0.05, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10016-01', N'Domperidone 10mg', 1, N'10mg', 2600, 2100, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10017-01', N'Ranitidine 150mg', 1, N'150mg', 2400, 1900, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10018-01', N'Simvastatin 20mg', 1, N'20mg', 3000, 2400, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10019-01', N'Atorvastatin 10mg', 1, N'10mg', 3400, 2700, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10020-01', N'Clopidogrel 75mg', 1, N'75mg', 5000, 4200, 0.1, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10021-01', N'Aspirin 81mg', 1, N'81mg', 1500, 1200, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10022-01', N'Metronidazole 250mg', 3, N'250mg', 2700, 2100, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10023-01', N'Doxycycline 100mg', 3, N'100mg', 3200, 2500, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10024-01', N'Clarithromycin 500mg', 3, N'500mg', 8000, 6500, 0.1, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10025-01', N'Levofloxacin 500mg', 3, N'500mg', 9000, 7500, 0.1, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10026-01', N'Bromhexine 8mg', 1, N'8mg', 2200, 1800, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10027-01', N'Ambroxol 30mg', 1, N'30mg', 2300, 1900, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10028-01', N'Guaifenesin 100mg', 1, N'100mg', 2000, 1600, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10029-01', N'Salbutamol 2mg', 1, N'2mg', 2600, 2100, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10030-01', N'Montelukast 10mg', 1, N'10mg', 5000, 4200, 0.1, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10031-01', N'Cetrimonium Chloride 5%', 9, N'5%', 25000, 20000, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10032-01', N'Sodium Chloride 0.9%', 4, N'0.9%', 10000, 8000, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10033-01', N'Glucose 5%', 4, N'5%', 9500, 7500, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10034-01', N'Ringer Lactate', 4, N'—', 12000, 9500, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10035-01', N'Vitamin B1 100mg', 9, N'100mg', 2000, 1500, 0.05, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10036-01', N'Vitamin B6 50mg', 9, N'50mg', 1800, 1400, 0.05, 2, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10037-01', N'Vitamin B12 500mcg', 9, N'500mcg', 2500, 1900, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10038-01', N'Calcium Carbonate 500mg', 1, N'500mg', 3000, 2500, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10039-01', N'Ferrous Sulfate 325mg', 1, N'325mg', 2800, 2300, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10040-01', N'Folic Acid 5mg', 1, N'5mg', 2500, 2000, 0.05, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10041-01', N'Zinc Gluconate 10mg', 1, N'10mg', 2200, 1800, 0.05, 1, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10042-01', N'Magnesium B6', 1, N'—', 3000, 2400, 0.05, 1, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10043-01', N'Probiotic 3Billion CFU', 9, N'3 tỷ CFU', 4500, 3500, 0.05, 1, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10044-01', N'Loperamide 2mg', 1, N'2mg', 2700, 2100, 0.05, 1, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10045-01', N'ORS Gói', 7, N'20.5g', 1500, 1200, 0.05, 1, N'KE0005', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10046-01', N'Clotrimazole 1%', 6, N'1%', 5000, 4200, 0.05, 4, N'KE0001', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10047-01', N'Mupirocin 2%', 6, N'2%', 6500, 5500, 0.05, 4, N'KE0002', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10048-01', N'Hydrocortisone Cream 1%', 6, N'1%', 4800, 4000, 0.05, 4, N'KE0003', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10049-01', N'Betamethasone 0.05%', 6, N'0.05%', 5500, 4500, 0.05, 4, N'KE0004', 0)
    INSERT [dbo].[Thuoc] ([MaThuoc], [TenThuoc], [DangDieuChe], [HamLuong], [GiaBan], [GiaGoc], [Thue], [MaDVTCoso], [MaKe], [DeleteAt]) VALUES (N'VN-10050-01', N'Silver Sulfadiazine 1%', 6, N'1%', 6000, 5000, 0.05, 4, N'KE0005', 0)
    GO
    SET ANSI_PADDING ON
    GO
/****** Object:  Index [UQ__KhachHan__0389B7BD1D7BAA9C]    Script Date: 21/12/2025 8:30:26 CH ******/
ALTER TABLE [dbo].[KhachHang] ADD UNIQUE NONCLUSTERED
    (
    [SoDienThoai] ASC
    )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    GO
    SET ANSI_PADDING ON
    GO
/****** Object:  Index [UQ__NhanVien__D5B8C7F036E6E2F3]    Script Date: 21/12/2025 8:30:26 CH ******/
ALTER TABLE [dbo].[NhanVien] ADD UNIQUE NONCLUSTERED
    (
    [TaiKhoan] ASC
    )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    GO
ALTER TABLE [dbo].[DangDieuChe] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[DonViTinh] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[HoaDon] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[KeThuoc] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[KhachHang] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[KhuyenMai] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[NhanVien] ADD  DEFAULT ((0)) FOR [IsQuanLi]
    GO
ALTER TABLE [dbo].[NhanVien] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[PhieuDatThuoc] ADD  DEFAULT ((0)) FOR [IsThanhToan]
    GO
ALTER TABLE [dbo].[PhieuDatThuoc] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[PhieuNhap] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[Thuoc] ADD  DEFAULT ((0)) FOR [DeleteAt]
    GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD  CONSTRAINT [FK_CTHD_ChiTietThuoc] FOREIGN KEY([MaCTT])
    REFERENCES [dbo].[ChiTietThuoc] ([MaCTT])
    GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK_CTHD_ChiTietThuoc]
    GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD  CONSTRAINT [FK_CTHD_DonVi] FOREIGN KEY([MaDVT])
    REFERENCES [dbo].[DonViTinh] ([MaDVT])
    GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK_CTHD_DonVi]
    GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD  CONSTRAINT [FK_CTHD_HoaDon] FOREIGN KEY([MaHD])
    REFERENCES [dbo].[HoaDon] ([MaHD])
    GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK_CTHD_HoaDon]
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc]  WITH CHECK ADD  CONSTRAINT [FK_CTPDT_ChiTietThuoc] FOREIGN KEY([MaCTT])
    REFERENCES [dbo].[ChiTietThuoc] ([MaCTT])
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc] CHECK CONSTRAINT [FK_CTPDT_ChiTietThuoc]
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc]  WITH CHECK ADD  CONSTRAINT [FK_CTPDT_DonVi] FOREIGN KEY([MaDVT])
    REFERENCES [dbo].[DonViTinh] ([MaDVT])
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc] CHECK CONSTRAINT [FK_CTPDT_DonVi]
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc]  WITH CHECK ADD  CONSTRAINT [FK_CTPDT_PDT] FOREIGN KEY([MaPDT])
    REFERENCES [dbo].[PhieuDatThuoc] ([MaPDT])
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc] CHECK CONSTRAINT [FK_CTPDT_PDT]
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_CTPN_DVT] FOREIGN KEY([MaDVT])
    REFERENCES [dbo].[DonViTinh] ([MaDVT])
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK_CTPN_DVT]
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_CTPN_PN] FOREIGN KEY([MaPN])
    REFERENCES [dbo].[PhieuNhap] ([MaPhieuNhap])
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK_CTPN_PN]
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_CTPN_Thuoc] FOREIGN KEY([MaThuoc])
    REFERENCES [dbo].[Thuoc] ([MaThuoc])
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK_CTPN_Thuoc]
    GO
ALTER TABLE [dbo].[ChiTietThuoc]  WITH CHECK ADD  CONSTRAINT [FK_CTT_PN] FOREIGN KEY([MaPN])
    REFERENCES [dbo].[PhieuNhap] ([MaPhieuNhap])
    GO
ALTER TABLE [dbo].[ChiTietThuoc] CHECK CONSTRAINT [FK_CTT_PN]
    GO
ALTER TABLE [dbo].[ChiTietThuoc]  WITH CHECK ADD  CONSTRAINT [FK_CTT_Thuoc] FOREIGN KEY([MaThuoc])
    REFERENCES [dbo].[Thuoc] ([MaThuoc])
    GO
ALTER TABLE [dbo].[ChiTietThuoc] CHECK CONSTRAINT [FK_CTT_Thuoc]
    GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK_HoaDon_KhachHang] FOREIGN KEY([MaKH])
    REFERENCES [dbo].[KhachHang] ([MaKH])
    GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK_HoaDon_KhachHang]
    GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK_HoaDon_KhuyenMai] FOREIGN KEY([MaKM])
    REFERENCES [dbo].[KhuyenMai] ([MaKM])
    GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK_HoaDon_KhuyenMai]
    GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK_HoaDon_NhanVien] FOREIGN KEY([MaNV])
    REFERENCES [dbo].[NhanVien] ([MaNV])
    GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK_HoaDon_NhanVien]
    GO
ALTER TABLE [dbo].[KhuyenMai]  WITH CHECK ADD  CONSTRAINT [FK_KhuyenMai_LoaiKhuyenMai] FOREIGN KEY([LoaiKhuyenMai])
    REFERENCES [dbo].[LoaiKhuyenMai] ([MaLKM])
    GO
ALTER TABLE [dbo].[KhuyenMai] CHECK CONSTRAINT [FK_KhuyenMai_LoaiKhuyenMai]
    GO
ALTER TABLE [dbo].[PhieuDatThuoc]  WITH CHECK ADD  CONSTRAINT [FK_PDT_KhachHang] FOREIGN KEY([MaKH])
    REFERENCES [dbo].[KhachHang] ([MaKH])
    GO
ALTER TABLE [dbo].[PhieuDatThuoc] CHECK CONSTRAINT [FK_PDT_KhachHang]
    GO
ALTER TABLE [dbo].[PhieuDatThuoc]  WITH CHECK ADD  CONSTRAINT [FK_PDT_KhuyenMai] FOREIGN KEY([MaKM])
    REFERENCES [dbo].[KhuyenMai] ([MaKM])
    GO
ALTER TABLE [dbo].[PhieuDatThuoc] CHECK CONSTRAINT [FK_PDT_KhuyenMai]
    GO
ALTER TABLE [dbo].[PhieuDatThuoc]  WITH CHECK ADD  CONSTRAINT [FK_PDT_NhanVien] FOREIGN KEY([MaNV])
    REFERENCES [dbo].[NhanVien] ([MaNV])
    GO
ALTER TABLE [dbo].[PhieuDatThuoc] CHECK CONSTRAINT [FK_PDT_NhanVien]
    GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_PhieuNhap_NhanVien] FOREIGN KEY([MaNV])
    REFERENCES [dbo].[NhanVien] ([MaNV])
    GO
ALTER TABLE [dbo].[PhieuNhap] CHECK CONSTRAINT [FK_PhieuNhap_NhanVien]
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD  CONSTRAINT [FK_Thuoc_DangDieuChe] FOREIGN KEY([DangDieuChe])
    REFERENCES [dbo].[DangDieuChe] ([MaDDC])
    GO
ALTER TABLE [dbo].[Thuoc] CHECK CONSTRAINT [FK_Thuoc_DangDieuChe]
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD  CONSTRAINT [FK_Thuoc_DVTC] FOREIGN KEY([MaDVTCoso])
    REFERENCES [dbo].[DonViTinh] ([MaDVT])
    GO
ALTER TABLE [dbo].[Thuoc] CHECK CONSTRAINT [FK_Thuoc_DVTC]
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD  CONSTRAINT [FK_Thuoc_Ke] FOREIGN KEY([MaKe])
    REFERENCES [dbo].[KeThuoc] ([MaKe])
    GO
ALTER TABLE [dbo].[Thuoc] CHECK CONSTRAINT [FK_Thuoc_Ke]
    GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD CHECK  (([SoLuong]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD CHECK  (([ThanhTien]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc]  WITH CHECK ADD CHECK  (([SoLuong]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietPhieuDatThuoc]  WITH CHECK ADD CHECK  (([ThanhTien]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD CHECK  (([GiaNhap]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD CHECK  (([SoLuong]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD CHECK  (([ThanhTien]>(0)))
    GO
ALTER TABLE [dbo].[ChiTietThuoc]  WITH CHECK ADD CHECK  (([NgaySanXuat]<=getdate()))
    GO
ALTER TABLE [dbo].[ChiTietThuoc]  WITH CHECK ADD CHECK  (([TonKho]>=(0)))
    GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD CHECK  (([MaHD] like 'HD[0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[KeThuoc]  WITH CHECK ADD CHECK  (([MaKe] like 'KE[0-9][0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[KhachHang]  WITH CHECK ADD CHECK  (([MaKH] like 'KH[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[KhuyenMai]  WITH CHECK ADD CHECK  (([MaKM] like 'KM[0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[KhuyenMai]  WITH CHECK ADD CHECK  (([So]>=(0)))
    GO
ALTER TABLE [dbo].[KhuyenMai]  WITH CHECK ADD CHECK  (([SoLuongToiDa]>=(0)))
    GO
ALTER TABLE [dbo].[KhuyenMai]  WITH CHECK ADD  CONSTRAINT [CK_KhuyenMai_Ngay] CHECK  (([NgayKetThuc]>[NgayBatDau]))
    GO
ALTER TABLE [dbo].[KhuyenMai] CHECK CONSTRAINT [CK_KhuyenMai_Ngay]
    GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD CHECK  (([LuongCoBan]>(0)))
    GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD CHECK  (([MaNV] like 'NV[0-9][0-9][0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[PhieuDatThuoc]  WITH CHECK ADD CHECK  (([MaPDT] like 'PDT[0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD CHECK  (([MaPhieuNhap] like 'PN[0-9][0-9][0-9][0-9][0-9][0-9]'))
    GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD CHECK  (([NgayNhap]<=getdate()))
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD CHECK  (([GiaBan]>(0)))
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD CHECK  (([GiaGoc]>(0)))
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD CHECK  (([MaThuoc] like 'VN-[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9]'))
    GO
ALTER TABLE [dbo].[Thuoc]  WITH CHECK ADD CHECK  (([Thue]>=(0)))
    GO
    USE [master]
    GO
ALTER DATABASE [QuanLyNhaThuoc] SET  READ_WRITE
GO
