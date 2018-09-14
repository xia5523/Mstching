package LinkGame;

import java.awt.Point;
import java.util.concurrent.CountDownLatch;

import LinkGame.Game.MyButton;

public class BtnMove implements Runnable {
	private MyButton oldBtn;// on
	private Point pend;// down
	private CountDownLatch countdownLatch;

	public BtnMove(MyButton oldBtn, Point pend, 
			CountDownLatch countdownLatch) {
		this.oldBtn = oldBtn;
		this.pend = pend;
		this.countdownLatch = countdownLatch;
	}

	public void run() {
		int rank = Matrix.GAP;
		int x = oldBtn.getLocation().x;
		int y = oldBtn.getLocation().y;
		int endY = pend.y * Matrix.LENGTH;// 横坐标位置表示纵向空间位置
		while (true) {
			try {
				Thread.sleep(8);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			y += rank;
			oldBtn.setLocation(x, y);
			if (oldBtn.getP().y > pend.y) {
				break;
			}
			if (y > endY) {
				break;
			}
		}
		oldBtn.setP(pend);
//		System.out.println(countdownLatch.getCount() + "nei");
		countdownLatch.countDown();
		// LogicElementMove.threadCount--;// 线程结束时计数器减1
		// // 当所有元素都运行完毕，并且需要移除的集合也为非空的时候
		// if (LogicElementMove.threadCount == 0) {
		// helpclass.afresh(helpclass.list);
		// LogicElementMove.threadCount = Matrix.WITH * Matrix.LONG;
		// helpclass.runElement(helpclass.list);
		// }

	}
}
