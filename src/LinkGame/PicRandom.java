package LinkGame;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import LinkGame.Game.MyButton;

/**
 * @��ҳ http://www.leaf123.cn/
 * @���� xia5523@qq.com
 */
public class PicRandom {
	/**
	 * ����õ�Ԫ�ص�ͼ��
	 */
	public void getRandom(MyButton mybtn) {
		String[] array = { "1", "2", "3", "4", "5", "6", "7" };
		int index = (int) (Math.random() * array.length);
		String imgNum = array[index];
		mybtn.imgNum = imgNum;
		String imgPath = "img/" + imgNum + ".jpg";
		ImageIcon imgIco = null;
		try {
			BufferedImage imgbuffer = ImageIO.read(this.getClass().getResource(
					imgPath));
			imgIco = new ImageIcon(imgbuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mybtn.setIcon(imgIco);
	}
}
