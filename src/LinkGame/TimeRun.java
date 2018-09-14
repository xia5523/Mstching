package LinkGame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Ö÷Ò³ http://www.leaf123.cn/
 * @ÓÊÏä xia5523@qq.com
 */
public class TimeRun implements Runnable{
	Game game = null;
	public TimeRun(Game game){
		this.game = game;
	}

	public void run() {
		while (true) {
			Calendar rightNow = Calendar.getInstance();
			Date da = rightNow.getTime();
			SimpleDateFormat myFmt = new SimpleDateFormat("HH:mm:ss");
			game.lblCurrent.setText(myFmt.format(da));
			long sub = (System.currentTimeMillis() - game.startTime) / 1000;
			if(sub > 60){
				game.lblUseTime.setText("User for " + sub / 60 + " min " + sub % 60 + " s");
			} else {
				game.lblUseTime.setText("User for " + sub + " s");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String getNowTime(){
		return "";
	}

}
