package base;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Log {
	static String logfile = "D\\rftlog.txt";

	// ��Ļ��ͼ

	public static void printScreen(String filename) {
		try {
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
			Robot robot = new Robot();
			BufferedImage screenShot = robot.createScreenCapture(new Rectangle(
					Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "JPG", file);
		} catch (Exception e) {
			System.err.println("Unhandled Exception :  " + e);
			e.printStackTrace();
		}
	}

	// �����ʼ�
	public static void SendEmail(String content, String screenPath) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "dev.ufida.com.cn");
		props.put("mail.smtp.auth", "true"); // ����smtpУ��
		Session sendMailSession = Session.getInstance(props, null);

		try {
			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect("dev.ufida.com.cn", "nctest", "ufsoftnctest123");
			Message newMessage = new MimeMessage(sendMailSession);

			// ����mail����
			newMessage.setSubject("RFT ���Խ��");

			// ���÷����˵�ַ
			Address from = new InternetAddress("nctest@ufida.com.cn");
			newMessage.setFrom(from);
			PropertiesRW pp = new PropertiesRW(new File(
					"./conf/mail.properties"));
			Address to = new InternetAddress(pp.readValue("to_user"));
			newMessage.setRecipient(Message.RecipientType.TO, to);

			// ����mail����
			newMessage.setSentDate(new java.util.Date());
			// String mail_text = "�����ʼ������˵��Գɹ���";
			// newMessage.setText("chenjie2");
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���
			Multipart mainPart = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(content, "text/html;charset=gb2312");
			mainPart.addBodyPart(mbp);
			// ����һ������HTML���ݵ�MimeBodyPart
			// BodyPart html = new MimeBodyPart();
			// ����HTML����
			// html.setContent("chenjie2", "text/html; charset=utf-8");
			// mainPart.addBodyPart(html);
			// ��MiniMultipart��������Ϊ�ʼ�����
			if (new File(screenPath).exists()) {
				mbp = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(screenPath); // �õ�����Դ
				mbp.setDataHandler(new DataHandler(fds)); // �õ�������������BodyPart
				mbp.setFileName(fds.getName()); // �õ��ļ���ͬ������BodyPart
				mainPart.addBodyPart(mbp);
			}

			newMessage.setContent(mainPart);
			newMessage.saveChanges(); // ���淢����Ϣ
			transport.sendMessage(newMessage, newMessage
					.getRecipients(Message.RecipientType.TO)); // �����ʼ�
			transport.close();
			// Transport.send(newMessage);
			System.out.println("������Ϣ�ѷ��ͣ�");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// ��ȡIP��ַ
	public static InetAddress getIP() {
		InetAddress inet = null;
		try {
			inet = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inet;
	}
}
