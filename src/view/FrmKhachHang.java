/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import DAO.KhachHangDao;
import Dialog.DlgKhachHang;
import entity.KhachHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FrmKhachHang extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLoai;

    private KhachHangDao dao = new KhachHangDao();
    private List<KhachHang> fullList = new ArrayList<>();

    public FrmKhachHang() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 650));

        initToolbar();
        initTable();
        loadTable();
    }

    // Load icon 
    private ImageIcon loadIcon(String path, int size) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_FAST);
        return new ImageIcon(img);
    }

    // TOOLBAR 
    private void initToolbar() {
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlTop.setPreferredSize(new Dimension(0, 130));

        // GROUP CHỨC NĂNG
        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlChucNang.setBackground(Color.WHITE);
        pnlChucNang.setBorder(createGroupBorder("Chức năng"));

        JButton btnThem = createToolButton("Thêm", new Color(46, 204, 113), "/icons/plus.png");
        JButton btnSua = createToolButton("Sửa", new Color(241, 196, 15), "/icons/edit.png");
        JButton btnXoa = createToolButton("Xóa", new Color(231, 76, 60), "/icons/delete.png");

        pnlChucNang.add(btnThem);
        pnlChucNang.add(btnSua);
        pnlChucNang.add(btnXoa);

        // GROUP TÌM KIẾM
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(createGroupBorder("Tìm kiếm"));

        // ComboBox tìm kiếm
        cboLoai = new JComboBox<>(new String[]{"Tất cả", "Tên khách hàng", "Số điện thoại"});
        cboLoai.setPreferredSize(new Dimension(130, 40));
        cboLoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLamMoi = createToolButton("Làm mới", new Color(52, 152, 219), "/icons/refresh.png");

        pnlTimKiem.add(cboLoai);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnLamMoi);

        pnlTop.add(pnlChucNang, BorderLayout.WEST);
        pnlTop.add(pnlTimKiem, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        // bắt sự kiện
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> loadTable());

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiem();
            }
        });
    }

    private TitledBorder createGroupBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(new Color(100, 100, 100));
        return border;
    }

    private JButton createToolButton(String text, Color color, String iconPath) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15) 
        ));

        ImageIcon icon = loadIcon(iconPath, 28);
        if (icon != null) {
            btn.setIcon(icon);
        }

        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(10);

        return btn;
    }

    // TABLE
    private void initTable() {
        String[] cols = {"Mã KH", "Tên khách hàng", "Số điện thoại", "Email"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        table.setSelectionBackground(new Color(220, 235, 250));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new EmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    //CRUD
    private void themKhachHang() {
        // Gọi Dialog Khách hàng (null = thêm mới)
        DlgKhachHang dlg = new DlgKhachHang(SwingUtilities.getWindowAncestor(this), null);
        dlg.setVisible(true);
        if (dlg.getResult()) {
            loadTable();
        }
    }

    private void suaKhachHang() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }

        KhachHang kh = new KhachHang();
        kh.setMaKhachHang(model.getValueAt(row, 0).toString());
        kh.setTenKhachHang(model.getValueAt(row, 1).toString());
        kh.setSoDienThoai(model.getValueAt(row, 2).toString());
        kh.setEmail(model.getValueAt(row, 3).toString());

        // Gọi Dialog Khách hàng (có dữ liệu = sửa)
        DlgKhachHang dlg = new DlgKhachHang(SwingUtilities.getWindowAncestor(this), kh);
        dlg.setVisible(true);
        if (dlg.getResult()) {
            loadTable();
        }
    }

    private void xoaKhachHang() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }
        String maKH = model.getValueAt(row, 0).toString();
        if (JOptionPane.showConfirmDialog(this, "Xóa khách hàng " + maKH + "?") 
                    == JOptionPane.YES_OPTION) {
            dao.delete(maKH);
            loadTable();
        }
    }

    private void loadTable() {
        fullList = dao.selectAll();
        hienThiDanhSach(fullList);
    }

    private void hienThiDanhSach(List<KhachHang> list) {
        model.setRowCount(0);
        for (KhachHang kh : list) {
            model.addRow(new Object[]{
                kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(), kh.getEmail()
            });
        }
    }

    // SEARCH
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        int loai = cboLoai.getSelectedIndex();
        if (keyword.isEmpty()) {
            hienThiDanhSach(fullList);
            return;
        }

        List<KhachHang> ketQua = new ArrayList<>();
        for (KhachHang kh : fullList) {
            boolean match = false;
            switch (loai) {
                case 0: 
                    match = kh.getMaKhachHang().toLowerCase().contains(keyword)
                                || kh.getTenKhachHang().toLowerCase().contains(keyword)
                                || kh.getSoDienThoai().contains(keyword);
                    break;
                case 1: 
                    match = kh.getTenKhachHang().toLowerCase().contains(keyword);
                    break;
                case 2: 
                    match = kh.getSoDienThoai().contains(keyword);
                    break;
            }
            if (match) {
                ketQua.add(kh);
            }
        }
        hienThiDanhSach(ketQua);
    }
}
