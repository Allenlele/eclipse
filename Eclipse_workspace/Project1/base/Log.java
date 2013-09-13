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

	// 屏幕截图

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

	// 发送邮件
	public static void SendEmail(String content, String screenPath) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "dev.ufida.com.cn");
		props.put("mail.smtp.auth", "true"); // 允许smtp校验
		Session sendMailSession = Session.getInstance(props, null);

		try {
			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect("dev.ufida.com.cn", "nctest", "ufsoftnctest123");
			Message newMessage = new MimeMessage(sendMailSession);

			// 设置mail主题
			newMessage.setSubject("RFT 测试结果");

			// 设置发信人地址
			Address from = new InternetAddress("nctest@ufida.com.cn");
			newMessage.setFrom(from);
			PropertiesRW pp = new PropertiesRW(new File(
					"./conf/mail.properties"));
			Address to = new InternetAddress(pp.readValue("to_user"));
			newMessage.setRecipient(Message.RecipientType.TO, to);

			// 设置mail正文
			newMessage.setSentDate(new java.util.Date());
			// String mail_text = "更改邮件发件人调试成功！";
			// newMessage.setText("chenjie2");
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(content, "text/html;charset=gb2312");
			mainPart.addBodyPart(mbp);
			// 创建一个包含HTML内容的MimeBodyPart
			// BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			// html.setContent("chenjie2", "text/html; charset=utf-8");
			// mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			if (new File(screenPath).exists()) {
				mbp = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(screenPath); // 得到数据源
				mbp.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
				mbp.setFileName(fds.getName()); // 得到文件名同样至入BodyPart
				mainPart.addBodyPart(mbp);
			}

			newMessage.setContent(mainPart);
			newMessage.saveChanges(); // 保存发送信息
			transport.sendMessage(newMessage, newMessage
					.getRecipients(Message.RecipientType.TO)); // 发送邮件
			transport.close();
			// Transport.send(newMessage);
			System.out.println("错误信息已发送！");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// 获取IP地址
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
