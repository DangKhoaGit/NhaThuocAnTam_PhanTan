package com.antam.app.helper;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 14/10/2025
 * @version: 1.0
 */
import org.mindrot.jbcrypt.BCrypt;

public class MaKhoaMatKhau {
    /**
     * Tạo mã hóa cho mật khẩu, mỗi lần tạo là một mã hash khác nhau
     * @param plainPassword (chuỗi mật khẩu cần mã)
     * @param cost (độ phức tạp, mặc định là 10)
     * @return String(chuỗi mật khẩu đã mã hóa)
     */
    public static String hashPassword(String plainPassword, int cost) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(cost));
    }

    /**
     * Giải mã và so sánh chuỗi mật khẩu đầu vào với mật khẩu đã hash
     * @param plainPassword
     * @param hashed
     * @return true - chuỗi truyền vào trùng với mật khẩu đã hash trong dbs.
     *        false - chuỗi truyền vào không khớp với mật khẩu đã hash trong dbs.
     */
    public static boolean verifyPassword(String plainPassword, String hashed) {
        return BCrypt.checkpw(plainPassword, hashed);
    }
}
