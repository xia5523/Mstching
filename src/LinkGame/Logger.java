package LinkGame;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Date;

/**
 * 用户系统错误的日志记录
 * 
 * @author Administrator
 * @主页 http://www.leaf123.cn/
 * @邮箱 xia5523@qq.com
 */
public class Logger {

	@SuppressWarnings("deprecation")
	public static void Write(String errorMsg) {
		FileOutputStream fos;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream("C:/Mstching--" + new Date(System.currentTimeMillis()).getDay() + "D.log", true);
			pw = new PrintWriter(fos);
			pw.write(errorMsg + "\r\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void WriteList(String errorMsg) {
		FileOutputStream fos;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream("C:/Mstching--List--" + new Date(System.currentTimeMillis()).getDay() + "D.log", true);
			pw = new PrintWriter(fos);
			pw.write(errorMsg + "\r\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}


}
