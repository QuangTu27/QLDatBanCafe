package view;

import entity.ChiTietDatBan;
import util.XImage;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuRenderer extends DefaultListCellRenderer {

    private NumberFormat vnMoney = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ChiTietDatBan) {
            ChiTietDatBan item = (ChiTietDatBan) value;

            // Text
            String html = "<html>"
                    + "<div style='padding:5px;'>"
                    + "<b style='font-size:12px; color:#003366;'>"
                    + item.getTenMon()
                    + "</b><br/>"
                    + "<span style='color:red; font-weight:bold;'>"
                    + vnMoney.format(item.getDonGia())
                    + "</span>"
                    + "</div></html>";
            setText(html);

            // Image
            String maMenu = item.getMaMenu().trim(); // QUAN TRỌNG
            ImageIcon icon = XImage.getResizedIcon(maMenu + ".png", 70, 70);
            setIcon(icon);

            setIconTextGap(15);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        }
        return this;
    }
}
