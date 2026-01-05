package Dialog;

import DAO.KhachHangDao;
import entity.KhachHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
            txtMaKH.setText(kh.getMaKhachHang());
            txtMaKH.setEditable(false);
            txtTenKH.setText(kh.getTenKhachHang());
            txtSDT.setText(kh.getSoDienThoai());
            txtEmail.setText(kh.getEmail());
            btnLuu.setText("Cập nhật");
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 450);
        setLayout(new BorderLayout());

        // header 
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(46, 204, 113));
        pnlHeader.setPreferredSize(new Dimension(0, 30));
        JLabel lblTitle = new JLabel(khEditing == null ? "THÊM KHÁCH HÀNG" : "CẬP NHẬT KHÁCH HÀNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // form nhập
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 15, 25));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);

        JLabel lblMa = new JLabel("Mã Khách hàng:");
        lblMa.setFont(labelFont);
        pnlForm.add(lblMa);
        txtMaKH = new JTextField();
        txtMaKH.setFont(labelFont);
        pnlForm.add(txtMaKH);

        JLabel lblTenKH = new JLabel("Tên Khách hàng:");
        lblTenKH.setFont(labelFont);
        pnlForm.add(lblTenKH);
        txtTenKH = new JTextField();
        txtTenKH.setFont(labelFont);
        pnlForm.add(txtTenKH);

        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        pnlForm.add(lblSDT);
        txtSDT = new JTextField();
        txtSDT.setFont(labelFont);
        pnlForm.add(txtSDT);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        pnlForm.add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setFont(labelFont);
        pnlForm.add(txtEmail);

        add(pnlForm, BorderLayout.CENTER);

        // buttons
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));

        btnLuu = createToolButton("Lưu", new Color(46, 204, 113));
        btnHuy = createToolButton("Hủy", new Color(231, 76, 60));

        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> xuLyLuu());
    }

    // Hàm tạo Button
    private JButton createToolButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(color);
            }
        });

        return btn;
    }

    private void xuLyLuu() {
        if (!validateForm()) {
            return;
        }

        KhachHang khMoi = new KhachHang(
                    txtMaKH.getText().trim(),
                    txtTenKH.getText().trim(),
                    txtSDT.getText().trim(),
                    txtEmail.getText().trim()
        );

        boolean thanhCong = (khEditing == null) ? dao.insert(khMoi) : dao.update(khMoi);

        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Lưu dữ liệu thành công!");
            result = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: Trùng mã hoặc lỗi cơ sở dữ liệu!");
        }
    }

    private boolean validateForm() {
        String ma = txtMaKH.getText().trim();
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();

        // Kiểm tra dữ liệu
        if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã, Tên và Số điện thoại!");
            return false;
        }

        if (!sdt.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (phải là số và có 10 chữ số)!");
            txtSDT.requestFocus();
            return false;
        }

        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Định dạng Email không hợp lệ!");
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public boolean getResult() {
        return result;
    }
}
