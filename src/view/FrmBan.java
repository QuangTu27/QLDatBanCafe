package view;

import DAO.BanDao;
import Dialog.DlgBan;
import entity.Ban;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FrmBan extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLoai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private BanDao dao = new BanDao();
    private List<Ban> fullList = new ArrayList<>();

    public FrmBan() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 650));

        initToolbar();
        initTable();
        loadTable();
        addEvents();
    }

    private void initToolbar() {
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlTop.setPreferredSize(new Dimension(0, 130));

        // Nhóm Chức năng
        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlChucNang.setBackground(Color.WHITE);
        pnlChucNang.setBorder(createGroupBorder("Chức năng"));

        btnThem = createToolButton("Thêm", new Color(46, 204, 113), "/icons/plus.png");
        btnSua = createToolButton("Sửa", new Color(241, 196, 15), "/icons/edit.png");
        btnXoa = createToolButton("Xóa", new Color(231, 76, 60), "/icons/delete.png");

        pnlChucNang.add(btnThem);
        pnlChucNang.add(btnSua);
        pnlChucNang.add(btnXoa);

        // Nhóm Tìm kiếm
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(createGroupBorder("Tìm kiếm bàn"));

        cboLoai = new JComboBox<>(new String[]{"Tất cả", "Tên bàn", "Trạng thái"});
        cboLoai.setPreferredSize(new Dimension(130, 40));
        cboLoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(190, 40));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnLamMoi = createToolButton("Làm mới", new Color(52, 152, 219), "/icons/refresh.png");

        pnlTimKiem.add(cboLoai);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnLamMoi);

        pnlTop.add(pnlChucNang, BorderLayout.WEST);
        pnlTop.add(pnlTimKiem, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);
    }

    private TitledBorder createGroupBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), title);
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

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {}

        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(10);
        return btn;
    }

    private void initTable() {
        String[] cols = {"Mã bàn", "Tên bàn", "Số ghế", "Trạng thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionBackground(new Color(220, 235, 250));
        table.setSelectionForeground(Color.BLACK);

        // Căn giữa nội dung cột Số ghế và Trạng thái
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new EmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    private void loadTable() {
        fullList = dao.selectAll();
        hienThiDanhSach(fullList);
    }

    private void hienThiDanhSach(List<Ban> list) {
        model.setRowCount(0);
        for (Ban b : list) {
            model.addRow(new Object[]{ b.getMaBan(), b.getTenBan(), b.getSoGhe(), b.getTrangThai() });
        }
    }

    private void addEvents() {
        btnThem.addActionListener(e -> {
            DlgBan dlg = new DlgBan(SwingUtilities.getWindowAncestor(this), null);
            dlg.setVisible(true);
            if (dlg.getResult()) loadTable();
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần sửa!");
                return;
            }
            Ban b = new Ban(
                model.getValueAt(row, 0).toString(),
                model.getValueAt(row, 1).toString(),
                Integer.parseInt(model.getValueAt(row, 2).toString()),
                model.getValueAt(row, 3).toString()
            );
            DlgBan dlg = new DlgBan(SwingUtilities.getWindowAncestor(this), b);
            dlg.setVisible(true);
            if (dlg.getResult()) loadTable();
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần xóa!");
                return;
            }
            String maBan = model.getValueAt(row, 0).toString();
            if (JOptionPane.showConfirmDialog(this, "Xóa bàn " + maBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (dao.delete(maBan)) loadTable();
            }
        });

        btnLamMoi.addActionListener(e -> loadTable());

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { timKiem(); }
            @Override public void removeUpdate(DocumentEvent e) { timKiem(); }
            @Override public void changedUpdate(DocumentEvent e) { timKiem(); }
        });
    }

    private void timKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        int loai = cboLoai.getSelectedIndex();
        if (keyword.isEmpty()) {
            hienThiDanhSach(fullList);
            return;
        }

        List<Ban> ketQua = new ArrayList<>();
        for (Ban b : fullList) {
            boolean match = false;
            switch (loai) {
                case 0: // Tất cả
                    match = b.getMaBan().toLowerCase().contains(keyword) 
                         || b.getTenBan().toLowerCase().contains(keyword) 
                         || b.getTrangThai().toLowerCase().contains(keyword);
                    break;
                case 1: // Tên bàn
                    match = b.getTenBan().toLowerCase().contains(keyword);
                    break;
                case 2: // Trạng thái
                    match = b.getTrangThai().toLowerCase().contains(keyword);
                    break;
            }
            if (match) ketQua.add(b);
        }
        hienThiDanhSach(ketQua);
    }
}