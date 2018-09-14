package LinkGame;

import java.util.ArrayList;
import java.util.Collection;

import LinkGame.Game.MyButton;

/**
 * @主页 http://www.leaf123.cn/
 * @邮箱 xia5523@qq.com
 * @param <E>
 */
@SuppressWarnings("serial")
public class MyArrayList<E> extends ArrayList<MyButton> {

	public MyArrayList(Collection<? extends MyButton> c) {
		super(c);
	}

	public MyArrayList() {
		super();
	}

	/**
	 * 得到集合元素的右边一个元素
	 */
	public MyButton getRightBtn(MyButton btn) {
		int x = btn.getP().x + 1;
		int y = btn.getP().y;
		for (MyButton my : this) {
			if (my.getP().x == x && my.getP().y == y) {
				return my;
			}
		}
		return null;
	}

	public MyButton getDownBtn(MyButton btn) {
		int x = btn.getP().x;
		int y = btn.getP().y + 1;
		for (MyButton my : this) {
			if (my.getP().x == x && my.getP().y == y) {
				return my;
			}
		}
		return null;
	}

	public MyButton getPoint(int x, int y) {
		for (MyButton btn : this) {
			if (btn.getP().x == x && btn.getP().y == y) {
				return btn;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < Matrix.LONG; j++) {
			for (int i = 0; i < Matrix.WITH; i++) {
				sb.append(getPoint(i, j));
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}

}