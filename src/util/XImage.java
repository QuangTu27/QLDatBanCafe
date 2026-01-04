package util;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class XImage {

    public static ImageIcon getIcon(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return getDefaultIcon();
        }

        fileName = fileName.trim();
        URL url = XImage.class.getResource("/image/" + fileName);

        if (url != null) {
            return new ImageIcon(url);
        }

        System.err.println("Không tìm thấy ảnh: " + fileName);
        return getDefaultIcon();
    }

    private static ImageIcon getDefaultIcon() {
        URL url = XImage.class.getResource("/image/no_image.png");
        if (url != null) {
            return new ImageIcon(url);
        }
        return null;
    }

    public static ImageIcon getResizedIcon(String fileName, int width, int height) {
        ImageIcon icon = getIcon(fileName);
        if (icon != null) {
            Image img = icon.getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/image/cafe_logo.png");
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        return null;
    }
}
