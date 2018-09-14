package LinkGame;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;

import LinkGame.Game.MyButton;

/**
 * @��ҳ http://www.leaf123.cn/
 * @���� xia5523@qq.com
 */
public class LogicElementMove {
	// ��Ϸ����
	int scores = 0;
	MyArrayList<MyButton> list = null;
	Set<MyButton> hashset = null;
	Game game = null;

	public LogicElementMove(Game game) {
		this.game = game;
	}

	/**
	 * ����Ԫ�ص������
	 * 
	 * @param list
	 */
	public void runElement(MyArrayList<MyButton> list) {
		this.list = list;
		this.hashset = judgeSame(list);
		URL musicUri = null;
		if (this.hashset.size() != 0) {
			musicUri = this.getClass().getResource("music/1.wav");
			new MusicPlayer(musicUri, game.checkboxVoice.isSelected());
			MusicPlayer.removeCount = MusicPlayer.removeCount
					+ this.hashset.size();
			clearBtn(this.hashset, list);
		} else {
			if (MusicPlayer.removeCount > 6) {
				musicUri = this.getClass().getResource("music/2.wav");
				MusicPlayer.removeCount = 0;
				new MusicPlayer(musicUri, game.checkboxVoice.isSelected());
			}
		}
	}

	/**
	 * ѡ�еĶ�����Set������
	 * 
	 * @param list
	 * @return
	 */
	public Set<MyButton> judgeSame(MyArrayList<MyButton> list) {
		Set<MyButton> hashset = new HashSet<MyButton>();
		try {
			for (MyButton mybtn : list) {
				int x = mybtn.getP().x;
				int y = mybtn.getP().y;
				// �жϺ���
				if (x < Matrix.WITH - 2) {
					MyButton rbtn1 = list.getRightBtn(mybtn);
					MyButton rbtn2 = list.getRightBtn(rbtn1);
					if (mybtn.imgNum.equals(rbtn1.imgNum)
							&& mybtn.imgNum.equals(rbtn2.imgNum)) {
						hashset.add(mybtn);
						hashset.add(rbtn1);
						hashset.add(rbtn2);
					}
				}
				// �����ж�
				if (y < Matrix.LONG - 2) {
					MyButton dbtn1 = list.getDownBtn(mybtn);
					MyButton dbtn2 = list.getDownBtn(dbtn1);
					if (mybtn.imgNum.equals(dbtn1.imgNum)
							&& mybtn.imgNum.equals(dbtn2.imgNum)) {
						hashset.add(mybtn);
						hashset.add(dbtn1);
						hashset.add(dbtn2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.Write("e.getMessage -- " + e.getMessage() + "\r\n"
					+ "list -- \r\n" + list);
			JOptionPane
					.showMessageDialog(null, "�Ǻǣ����н��ˣ�\r\n" + e.getMessage());
			this.game.dispose();
			new Game();
		}
		// Logger.WriteList("list -- \r\n" + list);
		return hashset;
	}

	/**
	 * �鿴�Ƿ��п���������Ԫ��
	 * 
	 * @param list
	 * @return
	 */
	public MyArrayList<MyButton> seekMove(MyArrayList<MyButton> list) {
		MyArrayList<MyButton> mylist = new MyArrayList<MyButton>();
		for (MyButton myButton : list) {
			if (myButton.getP().getX() <= Matrix.WITH - 2) {
				MyButton right = list.getRightBtn(myButton);
				changePoint(myButton, right);// ��������Ԫ�ص�����
				if (judgeSame(list).size() != 0) {
					mylist.add(right);
					mylist.add(myButton);
					changePoint(myButton, right);// ������
					return mylist;
				} else {
					changePoint(myButton, right);// ��������Ԫ�ص�����
				}
			}
		}
		for (MyButton myButton : list) {
			if (myButton.getP().getY() <= Matrix.LONG - 2) {
				MyButton down = list.getDownBtn(myButton);
				changePoint(myButton, down);
				if (judgeSame(list).size() != 0) {
					mylist.add(down);
					mylist.add(myButton);
					changePoint(myButton, down);
					return mylist;
				} else {
					changePoint(myButton, down);
				}
			}
		}
		return mylist;
	}

	/**
	 * ��������Ԫ�ص�����λ��
	 * 
	 * @param btn1
	 * @param btn2
	 */
	private void changePoint(MyButton btn1, MyButton btn2) {
		int x = btn1.getP().x;
		int y = btn1.getP().y;
		btn1.setP(new Point(btn2.getP()));
		btn2.setP(new Point(x, y));
	}

	/**
	 * �ƶ�Ԫ��
	 * 
	 * @param hashset
	 * @param list
	 */
	private void clearBtn(Set<MyButton> hashset, MyArrayList<MyButton> list) {
		List<Thread> listmove = new ArrayList<Thread>();
		/**
		 * �̼߳�����
		 */
		CountDownLatch countdownLatch = new CountDownLatch(Matrix.WITH * Matrix.LONG);
		
		
		Thread th = null;
		// ����
		int[] countArray = new int[Matrix.WITH];
		/**
		 * ѡ����Ҫ���ߵ�Ԫ�أ����ı���״̬
		 */
		for (MyButton btnset : hashset) {
			// ��ʾ�Ѿ��Ƴ�
			btnset.falgRemove = true;
			new PicRandom().getRandom(btnset);
			btnset.setVisible(false);
		}

		// �ӷ�
		scores += hashset.size();
		game.lblScore.setText(scores + "");
		/**
		 * ������Ҫ�ƶ��ķ�Χ
		 */
		for (MyButton mybtn : list) {
			// ����Ѿ��Ƴ����Ͳ�����
			if (mybtn.falgRemove) {
				int x = mybtn.getP().x;
				mybtn.count = --countArray[x];
			} else {// ��ʾû��
				getColumCount(mybtn, hashset);
			}
		}
		/**
		 * �ƶ�λ��
		 */
		for (MyButton mybtn : list) {
			int x = mybtn.getP().x;
			int y = mybtn.getP().y;
			Point p = null;
			// ����Ѿ��Ƴ����������ƶ�
			if (mybtn.falgRemove) {
				p = new Point(x, mybtn.count);
				// ���Ѿ��Ƴ���Ԫ�ؿ����ƶ�������
				mybtn.setP(p);
				mybtn.setVisible(true);
				int move = getMaxCount(mybtn, hashset);
				Point p1 = new Point(x, mybtn.getP().y + move);
				th = new Thread(new BtnMove(mybtn, p1, countdownLatch));
				listmove.add(th);
			} else {
				p = new Point(x, y + mybtn.count);
				th = new Thread(new BtnMove(mybtn, p, countdownLatch));
				listmove.add(th);
			}
		}
		/**
		 * ��ʼ�����̣߳��ƶ�
		 */
		for (Thread thread : listmove) {
			thread.start();
		}
		try {
			countdownLatch.await();
			game.helpclass.afresh(game.helpclass.list);
			game.helpclass.runElement(game.helpclass.list);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �������Ƴ��ļ��ϣ��õ�ÿ�����ڵ�Ԫ����������Ŀռ�
	 * 
	 * @param mybtn
	 * @param hashset
	 */
	public void getColumCount(MyButton mybtn, Set<MyButton> hashset) {
		int count = 0;
		int x = mybtn.getP().x;
		int y = mybtn.getP().y;
		for (MyButton mybutton : hashset) {
			if (mybutton.getP().y > y && mybutton.getP().x == x) {
				count++;
			}
		}
		mybtn.count = count;
	}

	/**
	 * �õ���Ԫ��(��������)��Ҫ�����ƶ��ľ���
	 */
	public int getMaxCount(MyButton mybtn, Set<MyButton> hashset) {
		int count = 0;
		int x = mybtn.getP().x;
		for (MyButton mybutton : hashset) {
			if (mybutton.getP().x == x) {
				count++;
			}
		}
		return count;
	}

	/**
	 * ��������һ��
	 */
	public void afresh(MyArrayList<MyButton> list) {
		for (MyButton myButton : list) {
			myButton.count = 0;
			myButton.falg = false;
			myButton.falgRemove = false;
		}
	}
}
