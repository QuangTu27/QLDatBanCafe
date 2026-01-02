package util;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class XImage {

    private static final String IMAGE_PATH = "/image/";

    public static ImageIcon getIcon(String fileName) {
        if (fileName == null) {
            return getDefaultIcon();
        }

        // Chuẩn hoá tên file
        fileName = fileName.trim().replaceAll("\\s+", "");

        // Nếu chưa có đuôi .png thì tự thêm
        if (!fileName.toLowerCase().endsWith(".png")) {
            fileName += ".png";
        }

        URL url = XImage.class.getResource(IMAGE_PATH + fileName);

        if (url != null) {
            return new ImageIcon(url);
        }

        System.err.println("Không tìm thấy ảnh: " + IMAGE_PATH + fileName);
        return getDefaultIcon();
    }

    private static ImageIcon getDefaultIcon() {
        URL url = XImage.class.getResource(IMAGE_PATH + "no_image.png");
        return url != null ? new ImageIcon(url) : null;
    }

    public static ImageIcon getResizedIcon(String fileName, int width, int height) {
        ImageIcon icon = getIcon(fileName);
        if (icon == null) {
            return null;
        }

        Image img = icon.getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static Image getAppIcon() {
        URL url = XImage.class.getResource(IMAGE_PATH + "cafe_logo.png");
        return url != null ? new ImageIcon(url).getImage() : null;
    }
}
