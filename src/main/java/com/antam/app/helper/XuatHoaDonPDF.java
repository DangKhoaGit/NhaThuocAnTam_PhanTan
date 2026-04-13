package com.antam.app.helper;

import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.Thuoc;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 18/12/2025
 * @version: 1.0
 */

public class XuatHoaDonPDF {

    /**
     * Lấy đường dẫn font phù hợp với hệ điều hành
     */
    private static String getFontPath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            return "c:/windows/fonts/arial.ttf";
        } else if (os.contains("mac")) {
            // macOS - sử dụng font system
            return "/System/Library/Fonts/Helvetica.ttc";
        } else {
            // Linux/Unix
            return "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf";
        }
    }

    /**
     * @description Phương thức xuất hóa đơn bán thuốc ra file PDF với ArrayList
     * @param file      File PDF đích
     * @param dsThuoc  Danh sách chi tiết hóa đơn
     * @param thue      Số tiền thuế
     * @param tongTien  Tổng số tiền thanh toán
     * @throws Exception Nếu có lỗi trong quá trình xuất file
     */
    public static void xuatFilePDF(
            File file,
            HoaDon hoaDon,
            List<ChiTietHoaDon> dsThuoc,
            double thue,
            double tongTien
    ) throws Exception {

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // ===== FONT =====
        BaseFont bf;
        try {
            // Thử sử dụng font hệ thống trước
            bf = BaseFont.createFont(
                    getFontPath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
        } catch (Exception e) {
            // Nếu không tìm thấy font hệ thống, sử dụng font built-in của iText
            bf = BaseFont.createFont(
                    BaseFont.HELVETICA,
                    BaseFont.CP1252,
                    BaseFont.NOT_EMBEDDED
            );
        }

        Font titleFont = new Font(bf, 18, Font.BOLD);
        Font normalFont = new Font(bf, 12);
        Font boldFont = new Font(bf, 12, Font.BOLD);

        // ===== TIÊU ĐỀ =====
        Paragraph title = new Paragraph("HÓA ĐƠN BÁN THUỐC", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        // ===== THÔNG TIN HÓA ĐƠN =====
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(15);
        infoTable.setWidths(new float[]{1, 2});

        addInfo(infoTable, "Mã hóa đơn:", hoaDon.getMaHD(), boldFont, normalFont);
        addInfo(infoTable, "Ngày tạo:", hoaDon.getNgayTao().toString(), boldFont, normalFont);
        addInfo(infoTable, "Nhân viên:", hoaDon.getMaNV().getHoTen(), boldFont, normalFont);
        addInfo(infoTable, "Khách hàng:", hoaDon.getMaKH().getTenKH(), boldFont, normalFont);

        String km = hoaDon.getMaKM() != null
                ? hoaDon.getMaKM().getTenKM()
                : "Không có";
        addInfo(infoTable, "Khuyến mãi:", km, boldFont, normalFont);

        document.add(infoTable);

        // ===== BẢNG CHI TIẾT THUỐC =====
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 4, 2, 2, 2});

        addHeader(table, "STT", boldFont);
        addHeader(table, "Tên thuốc", boldFont);
        addHeader(table, "SL", boldFont);
        addHeader(table, "Đơn giá", boldFont);
        addHeader(table, "Thành tiền", boldFont);

        int stt = 1;
        for (ChiTietHoaDon ct : dsThuoc) {
            Thuoc t = ct.getMaCTT().getMaThuoc();
            double thanhTien = ct.getSoLuong() * t.getGiaBan();

            table.addCell(String.valueOf(stt++));
            table.addCell(t.getTenThuoc());
            table.addCell(String.valueOf(ct.getSoLuong()));
            table.addCell(formatTien(t.getGiaBan()));
            table.addCell(formatTien(thanhTien));
        }

        document.add(table);

        // ===== TỔNG KẾT =====
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thuế: " + formatTien(thue), normalFont));

        Paragraph total = new Paragraph(
                "TỔNG THANH TOÁN: " + formatTien(tongTien),
                titleFont
        );
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
    }


    // ===== HỖ TRỢ =====
    private static void addHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }

    private static void addInfo(
            PdfPTable table,
            String label,
            String value,
            Font bold,
            Font normal
    ) {
        PdfPCell c1 = new PdfPCell(new Phrase(label, bold));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(5);

        PdfPCell c2 = new PdfPCell(new Phrase(value, normal));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setPadding(5);

        table.addCell(c1);
        table.addCell(c2);
    }



    private static String formatTien(double tien) {
        return String.format("%,.0f đ", tien);
    }

}
