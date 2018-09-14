package LinkGame;

import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * @主页 http://www.leaf123.cn/
 * @邮箱 xia5523@qq.com
 */
public class MusicPlayer {
	public static int removeCount = 0;

	public MusicPlayer(final URL musicUrl, final boolean falgPlay){
		Thread threadstartmusic = new Thread(){
			@Override
			public void run() {
				MusicPlayer.this.clickElement(musicUrl, falgPlay);
			}
		};
		threadstartmusic.start();
	}
	
	private void clickElement(URL musicUrl, boolean falgPlay) {
		if (falgPlay) {
			try {
				AudioInputStream ais = AudioSystem
						.getAudioInputStream(musicUrl);// 获得音频输入流
				AudioFormat baseFormat = ais.getFormat();// 指定声音流中特定数据安排
				DataLine.Info info = new DataLine.Info(SourceDataLine.class,
						baseFormat);
				SourceDataLine line = (SourceDataLine) AudioSystem
						.getLine(info);
				// 从混频器获得源数据行
				line.open(baseFormat);// 打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
				line.start();// 允许数据行执行数据 I/O
				int BUFFER_SIZE = 4000 * 4;
				int intBytes = 0;
				byte[] audioData = new byte[BUFFER_SIZE];
				while (intBytes != -1) {
					intBytes = ais.read(audioData, 0, BUFFER_SIZE);// 从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
					if (intBytes >= 0) {
						line.write(audioData, 0, intBytes);// 通过此源数据行将音频数据写入混频器。
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
