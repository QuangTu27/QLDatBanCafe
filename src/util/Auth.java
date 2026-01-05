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
    public static TaiKhoan user = null;

    public static void clear() {
        Auth.user = null;
    }
    
    public static boolean isLogin() {
        return Auth.user != null;
    }

    public static boolean isManager() {
        // Phải đăng nhập rồi VÀ loại tài khoản là 1 (Admin)
        return isLogin() && user.getPhanQuyen() == 1;
    }
}
