/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import DAO.TaiKhoanDao;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import util.Auth;

public class FrmLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private TaiKhoanDao dao = new TaiKhoanDao();
    
    public FrmLogin() {
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập hệ thống");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("PHẦN MỀM QUẢN LÝ CAFE", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel pnForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        JLabel lblPass = new JLabel("Mật khẩu:");

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(lblUser, gbc);

        gbc.gridx = 1;
        pnForm.add(txtUsername, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(lblPass, gbc);

        gbc.gridx = 1;
        pnForm.add(txtPassword, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        pnForm.add(btnLogin, gbc);

        add(pnForm, BorderLayout.CENTER);

        // ===== EVENT =====
        btnLogin.addActionListener((ActionEvent e) -> login());
    }

    private void login() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        try {
            TaiKhoan tk = dao.selectByUsername(user);
            
            // Validate
            if (tk == null) {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập!");
            } else if (!pass.equals(tk.getMatKhau())) {
                JOptionPane.showMessageDialog(this, "Sai mật khẩu!");
            } else {
                // Đăng nhập thành công
                Auth.user = tk; // LƯU THÔNG TIN NGƯỜI DÙNG VÀO HỆ THỐNG
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                
                // Mở Form Main (Giả sử file FrmMain nằm cùng package ui)
                 new FrmMain().setVisible(true);
                 this.dispose(); // Đóng form login
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}



