package LinkGame;

import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * @��ҳ http://www.leaf123.cn/
 * @���� xia5523@qq.com
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
						.getAudioInputStream(musicUrl);// �����Ƶ������
				AudioFormat baseFormat = ais.getFormat();// ָ�����������ض����ݰ���
				DataLine.Info info = new DataLine.Info(SourceDataLine.class,
						baseFormat);
				SourceDataLine line = (SourceDataLine) AudioSystem
						.getLine(info);
				// �ӻ�Ƶ�����Դ������
				line.open(baseFormat);// �򿪾���ָ����ʽ���У�������ʹ�л�����������ϵͳ��Դ����ÿɲ�����
				line.start();// ����������ִ������ I/O
				int BUFFER_SIZE = 4000 * 4;
				int intBytes = 0;
				byte[] audioData = new byte[BUFFER_SIZE];
				while (intBytes != -1) {
					intBytes = ais.read(audioData, 0, BUFFER_SIZE);// ����Ƶ����ȡָ������������������ֽڣ����������������ֽ������С�
					if (intBytes >= 0) {
						line.write(audioData, 0, intBytes);// ͨ����Դ�����н���Ƶ����д���Ƶ����
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
