/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Admin
 */

import entity.TaiKhoan;

public class Auth {
    /**
     * Đối tượng này chứa thông tin người sử dụng sau khi đăng nhập.
     * Nếu user = null nghĩa là chưa đăng nhập.
     */
    public static TaiKhoan user = null;

    /**
     * Xóa thông tin của người sử dụng khi có yêu cầu đăng xuất.
     */
    public static void clear() {
        Auth.user = null;
    }

    /**
     * Kiểm tra xem đã đăng nhập hay chưa.
     * @return true nếu đã đăng nhập, false nếu chưa.
     */
    public static boolean isLogin() {
        return Auth.user != null;
    }

    /**
     * Kiểm tra xem người đang đăng nhập có phải là Admin (Quản lý) hay không.
     * Quy ước: LoaiTaiKhoan = 1 là Admin, 0 là Nhân viên.
     * @return true nếu là Admin, false nếu là Nhân viên.
     */
    public static boolean isManager() {
        // Phải đăng nhập rồi VÀ loại tài khoản là 1 (Admin)
        return isLogin() && user.getPhanQuyen() == 1;
    }
}
