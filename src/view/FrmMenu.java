package view;

import DAO.MenuDao;
import entity.Menu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class FrmMenu extends JPanel {

    private JTextField txtMaMon, txtTenMon, txtGia;
    private JComboBox<String> cboLoai;
    private JLabel lblHinh;
    private JButton btnChonAnh;

    private JTable table;
    private DefaultTableModel model;

    private MenuDao dao = new MenuDao();
    private String duongDanAnh = "";

    public FrmMenu() {
        setLayout(new BorderLayout(10, 10));

        // ===== PANEL NHẬP =====
        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin món"));

        pnlInput.add(new JLabel("Mã món:"));
        txtMaMon = new JTextField();
        pnlInput.add(txtMaMon);

        pnlInput.add(new JLabel("Tên món:"));
        txtTenMon = new JTextField();
        pnlInput.add(txtTenMon);

        pnlInput.add(new JLabel("Đơn giá:"));
        txtGia = new JTextField();
        pnlInput.add(txtGia);

        pnlInput.add(new JLabel("Loại món:"));
        cboLoai = new JComboBox<>(new String[]{
                "Đồ uống", "Cafe", "Sinh tố", "Trà", "Bánh ngọt"
        });
        pnlInput.add(cboLoai);

        lblHinh = new JLabel("Chưa có ảnh", JLabel.CENTER);
        lblHinh.setPreferredSize(new Dimension(30, 30));
        lblHinh.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnChonAnh = new JButton("Chọn ảnh");

        pnlInput.add(new JLabel("Hình ảnh:"));
        pnlInput.add(lblHinh);
        pnlInput.add(new JLabel(""));
        pnlInput.add(btnChonAnh);

        // ===== PANEL BUTTON =====
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

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"Mã món", "Tên món", "Giá", "Loại", "Hình ảnh"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable();

        // ===== CHỌN ẢNH =====
        btnChonAnh.addActionListener(e -> chonAnh());

        // ===== CLICK TABLE =====
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtMaMon.setText(model.getValueAt(r, 0).toString());
                    txtTenMon.setText(model.getValueAt(r, 1).toString());
                    txtGia.setText(model.getValueAt(r, 2).toString());
                    cboLoai.setSelectedItem(model.getValueAt(r, 3).toString());

                    duongDanAnh = model.getValueAt(r, 4).toString();
                    hienThiAnh(duongDanAnh);

                    txtMaMon.setEditable(false);
                }
            }
        });

        // ===== THÊM =====
        btnThem.addActionListener(e -> {
            if (validateForm()) {
                if (dao.insert(getForm())) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    loadTable();
                    clearForm();
                }
            }
        });

        // ===== SỬA =====
        btnSua.addActionListener(e -> {
            if (dao.update(getForm())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                loadTable();
                clearForm();
            }
        });

        // ===== XÓA =====
        btnXoa.addActionListener(e -> {
            if (!txtMaMon.getText().isEmpty()) {
                int c = JOptionPane.showConfirmDialog(this, "Xóa món này?");
                if (c == JOptionPane.YES_OPTION) {
                    dao.delete(txtMaMon.getText());
                    loadTable();
                    clearForm();
                }
            }
        });

        // ===== MỚI =====
        btnMoi.addActionListener(e -> clearForm());
    }

    // ================== HÀM PHỤ ==================

    private void loadTable() {
        model.setRowCount(0);
        List<Menu> list = dao.selectAll();
        for (Menu m : list) {
            model.addRow(new Object[]{
                    m.getMaMenu(),
                    m.getTenMon(),
                    m.getDonGia(),
                    m.getLoaiMon(),
                    m.getHinhAnh()
            });
        }
    }

    private Menu getForm() {
        return new Menu(
                txtMaMon.getText(),
                txtTenMon.getText(),
                Double.parseDouble(txtGia.getText()),
                cboLoai.getSelectedItem().toString(),
                duongDanAnh
        );
    }

    private boolean validateForm() {
        if (txtMaMon.getText().isEmpty() || txtTenMon.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập thiếu thông tin!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtGia.setText("");
        cboLoai.setSelectedIndex(0);
        duongDanAnh = "";
        lblHinh.setIcon(null);
        lblHinh.setText("Chưa có ảnh");
        txtMaMon.setEditable(true);
    }

    private void chonAnh() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
                new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg")
        );
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            duongDanAnh = f.getAbsolutePath();
            hienThiAnh(duongDanAnh);
        }
    }

    private void hienThiAnh(String path) {
        if (path != null && !path.isEmpty()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(
                    lblHinh.getWidth(),
                    lblHinh.getHeight(),
                    Image.SCALE_SMOOTH
            );
            lblHinh.setIcon(new ImageIcon(img));
            lblHinh.setText("");
        }
    }
}
