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
 * 主窗体、程序入口
 * 
 * @author 夏忍
 * @主页 http://www.leaf123.cn/
 * @邮箱 xia5523@qq.com
 * 
 */
@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener {

	private long currentTime = System.currentTimeMillis();
	private final int CHEAT_SLEEP_TIME = 5;// 作弊间隔时间5秒
	// 已经存储过的btn
	static MyButton oldBtn = null;
	static boolean falg = false;
	// 所有按钮的集合二维数组的
	MyButton[][] tArray = null;
	// 创建集合
	static MyArrayList<MyButton> list = null;
	// 闪烁集合
	static MyArrayList<MyButton> flicker = null;
	// 创建一个HelpClass类
	LogicElementMove helpclass = new LogicElementMove(this);
	// 开始游戏时用时
	long startTime = System.currentTimeMillis();
	/**
	 * 已经选中的
	 */
	public static int selectNumber = 0;
	/**
	 * 界面设置
	 */
	// 面板设置
	JPanel panelMain = new JPanel();

	// 菜单设置
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
					.showMessageDialog(null, "对不起，程序加载失败,重新加载吗？", "加载失败！", 0);
			System.exit(0);
		}
		for (MyButton mybtn : list) {
			panelMain.add(mybtn);
		}
		this.add(panelMain);
		
		this.setBackground(Color.WHITE);

	}

	// 窗体初始化
	public Game() {
		initialize();
		jMenuFileItemRefresh.setText("Refresh (R)");
		jMenuFileItemRestart.setText("Restart (F5)");
		jMenuFileItemAbout.setText("About");
		jMenuFileItemExit.setText("Exit");
		jMenuFileItemHelp.setText("Help");
		jMenuFileItemSeek.setText("查看 (F)");
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
		 * 事件处理
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
	 * 数组初始化
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
		// 如果得到的集合中没有可以消去的元素，就可以了
		if (helpclass.judgeSame(list).size() != 0) {
			createBtn();
		}
		return btnArr;
	}

	/**
	 * 改变两个点的坐标
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
		 * 改变坐标的最低条件是相临
		 */
		if ((Math.abs(ox - nx) == 1 && oy == ny)
				|| (Math.abs(oy - ny) == 1 && ox == nx)) {
			ChangeImg th = new ChangeImg(oldBtn, newBtn);
			th.go();

			// 改变坐标
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
	 * 元素类
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyButton extends JButton implements ActionListener {
		// 用来标识显示的图片的
		String imgNum = "";
		// 顶一个变量，用来存储该按钮下有多少个已经空的位置
		public int count = 0;
		// x代表第几行，y代表第几列
		// 当时的坐标
		private Point p = null;

		/**
		 * 图片闪烁线程
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

		// 设置坐标的时候就确定位置
		public void setP(Point p) {
			this.p = p;
			int x = p.x * Matrix.LENGTH;
			int y = p.y * Matrix.LENGTH;
			this.setLocation(x, y);
		}

		// 是否选中
		public boolean falg = false;
		// 是否移除
		public boolean falgRemove = false;

		// 初始化
		public MyButton(Point p) {
			setP(p);
			this.setBackground(Color.WHITE);
			this.setSize(Matrix.LENGTH, Matrix.LENGTH);
			this.addActionListener(this);
			this.setBackground(Color.white);
		}

		// 改变选中的两个点的位置
		public void changeState() {
			if (falg) {// 如果已经选中
				falg = false;
				selectNumber--;// 选中个数 -1
				this.setBackground(Color.WHITE);
			} else {// 如果没有选中
				falg = true;
				selectNumber++;// 选中个数 +1
				if (selectNumber == 2) {// 如果现在是两个已选中
					if (changePosition(oldBtn, this)) {// 如果可以改变两个图片的位置
						MusicPlayer.removeCount = 0;
						helpclass.runElement(list);
					}
					this.setBackground(Color.WHITE);
					Game.oldBtn.setBackground(Color.WHITE);
					Game.oldBtn.falg = false;
					Game.oldBtn = null;
					this.falg = false;
					selectNumber = 0;
				} else {// 如果现在仅有一个选中
					Game.oldBtn = this;
					this.setBackground(Color.BLUE);
				}
			}
		}

		// 事件触发
		public void actionPerformed(ActionEvent e) {
			new Thread() {
				public void run() {
					changeState();
				};
			}.start();
			Game.this.requestFocus();
		}

		/**
		 * 启动闪烁线程
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
	 * 使两个button交换位置的类
	 * 
	 * @author Administrator
	 * 
	 */
	public class ChangeImg {
		MyButton oldbtn = null;
		MyButton newbtn = null;

		int oX, oY, nX, nY;
		int lx, ly;// 距离
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
	 * 入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Game();
	}

	/**
	 * 事件
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
			String stringhelp = "\r\n                    此对对碰游戏说明\r\n\r\n"
					+ "                点击交换图片，只要图片的\r\n"
					+ "                横排或竖排三个有三个或者\r\n"
					+ "                三个以上的图片样子完全一\r\n"
					+ "                样就可以消除(当然，相邻的\r\n"
					+ "                图片才能交换。)\r\n" + "               ";
			JOptionPane.showMessageDialog(null, stringhelp, "Help",
					JOptionPane.DEFAULT_OPTION);
		} else if (e.getSource() == jMenuFileItemAbout) {
			String str = "<html><font style=\"font-family: Courier New\">&nbsp;&nbsp;&nbsp;The game is made by</font><br/>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ " --&nbsp;&nbsp;<font color=\"blue\" style=\"font-family: 微软雅黑\">夏忍</font><br/>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "QQ：552346433</html>";
			JOptionPane.showMessageDialog(null, str, "About Author",
					JOptionPane.DEFAULT_OPTION);
		} else if (e.getSource() == jMenuFileItemSeek) {
			btnSeek();
		}

	}

	/**
	 * 退出事件
	 */
	private void exit() {
		int result = JOptionPane.showConfirmDialog(null,
				"Are you sure to leave the game ?", "EXIT GAME", 0);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * 查询
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
	 * 刷新
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
	 * 重新开始
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