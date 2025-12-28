/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dialog;

import DAO.KhachHangDao;
import entity.KhachHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class DlgKhachHang extends JDialog {
    private JTextField txtMaKH, txtTenKH, txtSDT, txtEmail;
    private JButton btnLuu, btnHuy;
    
    private KhachHangDao dao = new KhachHangDao();
    private KhachHang khEditing = null;
    private boolean result = false;

    public DlgKhachHang(Window parent, KhachHang kh) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.khEditing = kh;
        
        initComponents();
        
        if (kh != null) {
            setTitle("Cập nhật khách hàng");
            txtMaKH.setText(kh.getMaKhachHang());
            txtMaKH.setEditable(false);
            txtTenKH.setText(kh.getTenKhachHang());
            txtSDT.setText(kh.getSoDienThoai());
            txtEmail.setText(kh.getEmail());
            btnLuu.setText("Cập nhật");
        } else {
            setTitle("Thêm khách hàng mới");
        }
        
        setLocationRelativeTo(parent);
    }
    
    public boolean getResult() { return result; }

    private void initComponents() {
        setSize(400, 350);
        setLayout(new BorderLayout());
        
        // Header Xanh
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(46, 204, 113));
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        JLabel lblTitle = new JLabel(khEditing == null ? "THÊM KHÁCH HÀNG" : "CẬP NHẬT KHÁCH HÀNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);
        
        // Form nhập liệu
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20)); // 4 dòng
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlForm.add(new JLabel("Mã khách hàng:"));
        txtMaKH = new JTextField();
        pnlForm.add(txtMaKH);
        
        pnlForm.add(new JLabel("Tên khách hàng:"));
        txtTenKH = new JTextField();
        pnlForm.add(txtTenKH);
        
        pnlForm.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        pnlForm.add(txtSDT);
        
        pnlForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlForm.add(txtEmail);
        
        add(pnlForm, BorderLayout.CENTER);
        
        // Buttons
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(231, 76, 60));
        btnHuy.setForeground(Color.WHITE);
        
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);
        
        // Events
        btnHuy.addActionListener(e -> dispose());
        
        btnLuu.addActionListener(e -> {
            if (txtMaKH.getText().isEmpty() || txtTenKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã và Tên khách!");
                return;
            }
            
            KhachHang khMoi = new KhachHang(
                txtMaKH.getText(), 
                txtTenKH.getText(), 
                txtSDT.getText(), 
                txtEmail.getText()
            );
            
            boolean thanhCong;
            if (khEditing == null) {
                thanhCong = dao.insert(khMoi);
            } else {
                thanhCong = dao.update(khMoi);
            }
            
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                result = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Trùng mã hoặc lỗi CSDL!");
            }
        });
    }
}