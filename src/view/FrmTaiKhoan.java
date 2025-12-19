/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import DAO.TaiKhoanDao;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class FrmTaiKhoan extends JPanel{
    private JTextField txtMaTK, txtTenDN, txtTenHienThi;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboPhanQuyen;
    private JTable table;
    private DefaultTableModel model;
    private TaiKhoanDao dao = new TaiKhoanDao();

    public FrmTaiKhoan() {
        setLayout(new BorderLayout());
        
        // --- PANEL NHẬP LIỆU (NORTH) ---
        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        pnlInput.setPreferredSize(new Dimension(0, 150));

        pnlInput.add(new JLabel("Mã TK:"));
        txtMaTK = new JTextField();
        pnlInput.add(txtMaTK);

        pnlInput.add(new JLabel("Tên đăng nhập:"));
        txtTenDN = new JTextField();
        pnlInput.add(txtTenDN);

        pnlInput.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JPasswordField();
        pnlInput.add(txtMatKhau);

        pnlInput.add(new JLabel("Tên hiển thị:"));
        txtTenHienThi = new JTextField();
        pnlInput.add(txtTenHienThi);

        pnlInput.add(new JLabel("Vai trò:"));
        cboPhanQuyen = new JComboBox<>(new String[]{"Nhân viên", "Quản lý (Admin)"});
        pnlInput.add(cboPhanQuyen);

        // --- PANEL NÚT BẤM (CENTER TOP) ---
        JPanel pnlButton = new JPanel(new FlowLayout());
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnMoi = new JButton("Làm mới");
        
        pnlButton.add(btnThem);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnMoi);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlInput, BorderLayout.CENTER);
        pnlTop.add(pnlButton, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU (CENTER) ---
        String[] cols = {"Mã TK", "Tên Đăng Nhập", "Mật Khẩu", "Tên Hiển Thị", "Vai Trò"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- LOAD DỮ LIỆU BAN ĐẦU ---
        loadTable();

        // --- SỰ KIỆN CLICK BẢNG ---
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaTK.setText(model.getValueAt(row, 0).toString());
                    txtTenDN.setText(model.getValueAt(row, 1).toString());
                    txtMatKhau.setText(model.getValueAt(row, 2).toString());
                    txtTenHienThi.setText(model.getValueAt(row, 3).toString());
                    
                    String role = model.getValueAt(row, 4).toString();
                    cboPhanQuyen.setSelectedIndex(role.equals("Quản lý") ? 1 : 0);
                    
                    txtMaTK.setEditable(false); // Không cho sửa khóa chính
                }
            }
        });

        // --- SỰ KIỆN THÊM ---
        btnThem.addActionListener(e -> {
            if(validateForm()) {
                TaiKhoan tk = getForm();
                if (dao.selectByUsername(tk.getTenDangNhap()) != null) {
                    JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!");
                    return;
                }
                if (dao.insert(tk)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Trùng Mã TK hoặc lỗi SQL!");
                }
            }
        });

        // --- SỰ KIỆN SỬA ---
        btnSua.addActionListener(e -> {
             if(validateForm()) {
                if (dao.update(getForm())) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi cập nhật!");
                }
             }
        });

        // --- SỰ KIỆN XÓA ---
        btnXoa.addActionListener(e -> {
            String ma = txtMaTK.getText();
            if (ma.isEmpty()) return;
            // Không cho tự xóa chính mình
            // if (util.Auth.user.getMaTK().equals(ma)) { ... }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadTable();
                    clearForm();
                }
            }
        });

        // --- SỰ KIỆN LÀM MỚI ---
        btnMoi.addActionListener(e -> clearForm());
    }

    // Helper: Lấy dữ liệu từ bảng đổ vào Model
    private void loadTable() {
        model.setRowCount(0);
        List<TaiKhoan> list = dao.selectAll();
        for (TaiKhoan tk : list) {
            model.addRow(new Object[]{
                tk.getMaTK(),
                tk.getTenDangNhap(),
                tk.getMatKhau(),
                tk.getTenHienThi(),
                tk.getPhanQuyen() == 1 ? "Quản lý" : "Nhân viên"
            });
        }
    }

    // Helper: Lấy dữ liệu từ Form
    private TaiKhoan getForm() {
        String ma = txtMaTK.getText();
        String user = txtTenDN.getText();
        String pass = new String(txtMatKhau.getPassword());
        String ten = txtTenHienThi.getText();
        int role = cboPhanQuyen.getSelectedIndex(); // 0: NV, 1: Admin
        return new TaiKhoan(ma, user, pass, ten, role);
    }
    
    // Helper: Validate
    private boolean validateForm() {
        if (txtMaTK.getText().isEmpty() || txtTenDN.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMaTK.setText("");
        txtTenDN.setText("");
        txtMatKhau.setText("");
        txtTenHienThi.setText("");
        cboPhanQuyen.setSelectedIndex(0);
        txtMaTK.setEditable(true); // Cho nhập lại khóa chính
    }
}
