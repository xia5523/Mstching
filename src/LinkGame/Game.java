package LinkGame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * �����塢�������
 * 
 * @author ����
 * @��ҳ http://www.leaf123.cn/
 * @���� xia5523@qq.com
 * 
 */
@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener {

	private long currentTime = System.currentTimeMillis();
	private final int CHEAT_SLEEP_TIME = 5;// ���׼��ʱ��5��
	// �Ѿ��洢����btn
	static MyButton oldBtn = null;
	static boolean falg = false;
	// ���а�ť�ļ��϶�ά�����
	MyButton[][] tArray = null;
	// ��������
	static MyArrayList<MyButton> list = null;
	// ��˸����
	static MyArrayList<MyButton> flicker = null;
	// ����һ��HelpClass��
	LogicElementMove helpclass = new LogicElementMove(this);
	// ��ʼ��Ϸʱ��ʱ
	long startTime = System.currentTimeMillis();
	/**
	 * �Ѿ�ѡ�е�
	 */
	public static int selectNumber = 0;
	/**
	 * ��������
	 */
	// �������
	JPanel panelMain = new JPanel();

	// �˵�����
	JMenuBar menubar = new JMenuBar();
	JMenu jMenuFile = new javax.swing.JMenu();
	JMenu seek = new javax.swing.JMenu();
	JMenuItem jMenuFileItemSeek = new JMenuItem();
	JMenuItem jMenuFileItemRefresh = new JMenuItem();
	JMenuItem jMenuFileItemRestart = new JMenuItem();
	JMenuItem jMenuFileItemAbout = new JMenuItem();
	JMenuItem jMenuFileItemExit = new JMenuItem();
	JMenuItem jMenuFileItemHelp = new JMenuItem();

	JLabel lblShow = new JLabel("scores:");
	JLabel lblScore = new JLabel("0");
	JLabel lblTitle = new JLabel("Welcome to mstching game !");
	JLabel lblCurrent = new JLabel("00:00");
	JLabel lblUseTime = new JLabel("User for 0 s");
//	seek.jpg
	JLabel lblSeek = new JLabel();

	JCheckBox checkboxVoice = new JCheckBox("Voice");

	public void initialize() {
		BufferedImage imgbuffer = null;
		try {
			imgbuffer = ImageIO.read(this.getClass().getResource(
					"img/seek.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		lblSeek.setIcon(new ImageIcon(imgbuffer));
		lblSeek.setBounds(0, 0, 50, 50);
//		lblSeek.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white,
//				new Color(148, 145, 140)));
		

		jMenuFile.add(jMenuFileItemRefresh);
		jMenuFile.add(jMenuFileItemRestart);
		jMenuFile.add(jMenuFileItemAbout);
		jMenuFile.add(jMenuFileItemHelp);
		jMenuFile.add(jMenuFileItemExit);
		seek.add(jMenuFileItemSeek);

		menubar.add(jMenuFile);
		menubar.add(seek);
		
		this.add(lblShow);
		this.add(lblScore);
		this.add(lblTitle);
		this.add(lblCurrent);
		this.add(lblUseTime);
		this.add(checkboxVoice);
		this.add(lblSeek);
		

		this.setJMenuBar(menubar);
		try {
			tArray = createBtn();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(null, "�Բ��𣬳������ʧ��,���¼�����", "����ʧ�ܣ�", 0);
			System.exit(0);
		}
		for (MyButton mybtn : list) {
			panelMain.add(mybtn);
		}
		this.add(panelMain);
		
		this.setBackground(Color.WHITE);

	}

	// �����ʼ��
	public Game() {
		initialize();
		jMenuFileItemRefresh.setText("Refresh (R)");
		jMenuFileItemRestart.setText("Restart (F5)");
		jMenuFileItemAbout.setText("About");
		jMenuFileItemExit.setText("Exit");
		jMenuFileItemHelp.setText("Help");
		jMenuFileItemSeek.setText("�鿴 (F)");
		jMenuFile.setText("File");
		seek.setText("other");

		panelMain.setBounds(50, 50, Matrix.WITH * Matrix.LENGTH, Matrix.LONG
				* Matrix.LENGTH);
		panelMain.setLayout(null);
		panelMain.setBackground(Color.cyan);

		setBounds(500, 10, panelMain.getX() + panelMain.getWidth() + 50,
				panelMain.getY() + panelMain.getHeight() + 120);
		
		this.setLayout(null);
		this.setBackground(Color.BLUE);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lblShow.setFont(new Font("Courier New", 0, 20));
		lblShow.setBounds(350, panelMain.getY() + panelMain.getHeight(), 100,
				50);
		lblScore.setFont(new Font("Courier New", 0, 20));
		lblScore.setBounds(450, panelMain.getY() + panelMain.getHeight(), 100,
				50);
		lblTitle.setFont(new Font("Courier New", Font.BOLD, 25));
		lblTitle.setBounds(100, 5, 400, 50);
		lblCurrent.setBounds(20, panelMain.getY() + panelMain.getHeight(), 100,
				50);
		lblUseTime.setBounds(80, panelMain.getY() + panelMain.getHeight(), 200,
				50);
		checkboxVoice.setBounds(250, panelMain.getY() + panelMain.getHeight(),
				80, 50);
		checkboxVoice.setSelected(true);

		/**
		 * �¼�����
		 */
		jMenuFileItemRefresh.addActionListener(this);
		jMenuFileItemRestart.addActionListener(this);
		jMenuFileItemAbout.addActionListener(this);
		jMenuFileItemExit.addActionListener(this);
		jMenuFileItemHelp.addActionListener(this);
		jMenuFileItemSeek.addActionListener(this);

		Thread threadTime = new Thread(new TimeRun(this));
		threadTime.start();

		this.requestFocus();
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_F5) {
					restart();
				} else if (e.getKeyChar() == 27) {// Esc
					exit();
				}
				if (System.currentTimeMillis() - currentTime > CHEAT_SLEEP_TIME) {
					if (e.getKeyCode() == KeyEvent.VK_F) {
						btnSeek();
					} else if (e.getKeyCode() == KeyEvent.VK_R) {
						refresh();
					}
				}
				currentTime = System.currentTimeMillis();
			}
		});

		// jMenuFileItemRefresh.setEnabled(false);
		// jMenuFileItemSeek.setEnabled(false);

	}

	/**
	 * �����ʼ��
	 * 
	 * @return
	 */
	public MyButton[][] createBtn() throws OutOfMemoryError {
		MyButton[][] btnArr = new MyButton[Matrix.WITH][Matrix.LONG];
		list = new MyArrayList<MyButton>();
		for (int i = 0; i < Matrix.WITH; i++) {
			for (int j = 0; j < Matrix.LONG; j++) {
				btnArr[i][j] = new MyButton(new Point(i, j));
				new PicRandom().getRandom(btnArr[i][j]);
				list.add(btnArr[i][j]);
			}
		}
		// ����õ��ļ�����û�п�����ȥ��Ԫ�أ��Ϳ�����
		if (helpclass.judgeSame(list).size() != 0) {
			createBtn();
		}
		return btnArr;
	}

	/**
	 * �ı������������
	 * 
	 * @param oldBtn
	 * @param newBtn
	 */
	public boolean changePosition(MyButton oldBtn, MyButton newBtn) {
		int ox = oldBtn.getP().x;
		int oy = oldBtn.getP().y;
		int nx = newBtn.getP().x;
		int ny = newBtn.getP().y;

		/**
		 * �ı�������������������
		 */
		if ((Math.abs(ox - nx) == 1 && oy == ny)
				|| (Math.abs(oy - ny) == 1 && ox == nx)) {
			ChangeImg th = new ChangeImg(oldBtn, newBtn);
			th.go();

			// �ı�����
			Point p = new Point(oldBtn.getP());
			oldBtn.setP(newBtn.getP());
			newBtn.setP(p);

			if (helpclass.judgeSame(list).size() == 0) {
				new ChangeImg(oldBtn, newBtn).go();
				newBtn.setP(oldBtn.getP());
				oldBtn.setP(p);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Ԫ����
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyButton extends JButton implements ActionListener {
		// ������ʶ��ʾ��ͼƬ��
		String imgNum = "";
		// ��һ�������������洢�ð�ť���ж��ٸ��Ѿ��յ�λ��
		public int count = 0;
		// x����ڼ��У�y����ڼ���
		// ��ʱ������
		private Point p = null;

		/**
		 * ͼƬ��˸�߳�
		 */
		private Flicker flickerThread = new Flicker(this);
		private Icon icon;

		public Point getP() {
			return p;
		}

		@Override
		public void setIcon(Icon defaultIcon) {
			super.setIcon(defaultIcon);
			icon = defaultIcon == null ? icon : defaultIcon;
		}

		public Icon getIcon1() {
			return icon;
		}

		// ���������ʱ���ȷ��λ��
		public void setP(Point p) {
			this.p = p;
			int x = p.x * Matrix.LENGTH;
			int y = p.y * Matrix.LENGTH;
			this.setLocation(x, y);
		}

		// �Ƿ�ѡ��
		public boolean falg = false;
		// �Ƿ��Ƴ�
		public boolean falgRemove = false;

		// ��ʼ��
		public MyButton(Point p) {
			setP(p);
			this.setBackground(Color.WHITE);
			this.setSize(Matrix.LENGTH, Matrix.LENGTH);
			this.addActionListener(this);
			this.setBackground(Color.white);
		}

		// �ı�ѡ�е��������λ��
		public void changeState() {
			if (falg) {// ����Ѿ�ѡ��
				falg = false;
				selectNumber--;// ѡ�и��� -1
				this.setBackground(Color.WHITE);
			} else {// ���û��ѡ��
				falg = true;
				selectNumber++;// ѡ�и��� +1
				if (selectNumber == 2) {// ���������������ѡ��
					if (changePosition(oldBtn, this)) {// ������Ըı�����ͼƬ��λ��
						MusicPlayer.removeCount = 0;
						helpclass.runElement(list);
					}
					this.setBackground(Color.WHITE);
					Game.oldBtn.setBackground(Color.WHITE);
					Game.oldBtn.falg = false;
					Game.oldBtn = null;
					this.falg = false;
					selectNumber = 0;
				} else {// ������ڽ���һ��ѡ��
					Game.oldBtn = this;
					this.setBackground(Color.BLUE);
				}
			}
		}

		// �¼�����
		public void actionPerformed(ActionEvent e) {
			new Thread() {
				public void run() {
					changeState();
				};
			}.start();
			Game.this.requestFocus();
		}

		/**
		 * ������˸�߳�
		 */
		@SuppressWarnings("deprecation")
		public void up() {
			if (flickerThread.getState() == Thread.State.NEW) {
				flickerThread.start();
			} else if (flickerThread.getState() == Thread.State.WAITING) {
				flickerThread.resume();
			} else if (flickerThread.getState() == Thread.State.TERMINATED) {
				flickerThread = new Flicker(this);
				flickerThread.start();
			}
		}

		public class Flicker extends Thread {
			private MyButton btn;

			public Flicker(MyButton btn) {
				this.btn = btn;
			}

			public void run() {
				int count = 0;
				while (true) {
					if (count++ > 6) {
						break;
					}
					if (count % 2 == 0) {
						btn.setIcon(null);
					} else {
						btn.setIcon(btn.getIcon1());
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public String toString() {
			return this.getP().x + "," + this.getP().y + " || ";
		}
	}

	/**
	 * ʹ����button����λ�õ���
	 * 
	 * @author Administrator
	 * 
	 */
	public class ChangeImg {
		MyButton oldbtn = null;
		MyButton newbtn = null;

		int oX, oY, nX, nY;
		int lx, ly;// ����
		final int SPEED = 1;

		public ChangeImg(MyButton oldbtn, MyButton newbtn) {
			this.oldbtn = oldbtn;
			this.newbtn = newbtn;

			nX = this.newbtn.getX();
			nY = this.newbtn.getY();
			oX = this.oldbtn.getX();
			oY = this.oldbtn.getY();

			lx = compare(this.newbtn.getX(), this.oldbtn.getX());
			ly = compare(this.newbtn.getY(), this.oldbtn.getY());

		}

		private int compare(int a, int b) {
			if (a > b) {
				return 1;
			} else if (a == b) {
				return 0;
			} else {
				return -1;
			}
		}

		public void go() {
			int count = 0;
			while (true) {
				this.newbtn.setLocation(this.newbtn.getX() - SPEED * lx,
						this.newbtn.getY() - SPEED * ly);
				this.oldbtn.setLocation(this.oldbtn.getX() + SPEED * lx,
						this.oldbtn.getY() + SPEED * ly);
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (++count > Matrix.LENGTH) {
					break;
				}

			}
		}

	}

	/**
	 * ���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Game();
	}

	/**
	 * �¼�
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jMenuFileItemRefresh) {
			refresh();
		} else if (e.getSource() == jMenuFileItemRestart) {
			restart();
		} else if (e.getSource() == jMenuFileItemExit) {
			exit();
		} else if (e.getSource() == jMenuFileItemHelp) {
			String stringhelp = "\r\n                    �˶Զ�����Ϸ˵��\r\n\r\n"
					+ "                �������ͼƬ��ֻҪͼƬ��\r\n"
					+ "                ���Ż�������������������\r\n"
					+ "                �������ϵ�ͼƬ������ȫһ\r\n"
					+ "                ���Ϳ�������(��Ȼ�����ڵ�\r\n"
					+ "                ͼƬ���ܽ�����)\r\n" + "               ";
			JOptionPane.showMessageDialog(null, stringhelp, "Help",
					JOptionPane.DEFAULT_OPTION);
		} else if (e.getSource() == jMenuFileItemAbout) {
			String str = "<html><font style=\"font-family: Courier New\">&nbsp;&nbsp;&nbsp;The game is made by</font><br/>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ " --&nbsp;&nbsp;<font color=\"blue\" style=\"font-family: ΢���ź�\">����</font><br/>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "QQ��552346433</html>";
			JOptionPane.showMessageDialog(null, str, "About Author",
					JOptionPane.DEFAULT_OPTION);
		} else if (e.getSource() == jMenuFileItemSeek) {
			btnSeek();
		}

	}

	/**
	 * �˳��¼�
	 */
	private void exit() {
		int result = JOptionPane.showConfirmDialog(null,
				"Are you sure to leave the game ?", "EXIT GAME", 0);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * ��ѯ
	 */
	private void btnSeek() {
		flicker = helpclass.seekMove(list);
		if (flicker != null && flicker.size() != 0) {
			for (MyButton btn : flicker) {
				btn.up();
			}
		}
	}

	/**
	 * ˢ��
	 */
	private void refresh() {
		for (MyButton mybtn : list) {
			new PicRandom().getRandom(mybtn);
		}
		new Thread() {
			public void run() {
				helpclass.runElement(list);
			};
		}.start();
	}

	/**
	 * ���¿�ʼ
	 */
	private void restart() {
		int result = JOptionPane.showConfirmDialog(null,
				"Are you sure to restart the game ?", "EXIT GAME", 0);
		if (result == JOptionPane.YES_OPTION) {
			Game.oldBtn = null;
			Game.falg = false;
			Game.list = null;
			Game.flicker = null;
			Game.selectNumber = 0;
			MusicPlayer.removeCount = 0;
			this.dispose();
			new Game();
		}
	}

}