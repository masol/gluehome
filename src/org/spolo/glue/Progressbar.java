package org.spolo.glue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.spolo.glue.StrRegex;

public class Progressbar extends JFrame {

	private static final long serialVersionUID = 1L;
	public JProgressBar progressbar;
	public JFrame frame;
	public int proc;
	private JLabel taskOutput, taskOutput1, taskOutput2, taskOutput3;
	public int count, totalCount;

	// ï¿½ï¿½ï¿½ìº¯ï¿½ï¿½
	public Progressbar() {
		super();
		frame = new JFrame("\u98DE\u9E7F\u5BB6\u5C45");
		progressbar = new JProgressBar(0, 100);
		progressbar.setIndeterminate(false);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		progressbar.setBorderPainted(true);
		progressbar.setBounds(new Rectangle(15, 75, 300, 20));
		taskOutput = new JLabel();
		taskOutput.setFont(new Font("\u5B8B\u4F53", Font.PLAIN, 12));
		taskOutput.setBounds(new Rectangle(15, 15, 900, 20));
		taskOutput1 = new JLabel();
		taskOutput1.setFont(new Font("\u5B8B\u4F53", Font.PLAIN, 12));
		taskOutput1.setBounds(new Rectangle(180, 15, 900, 20));

		taskOutput2 = new JLabel();
		taskOutput2.setFont(new Font("\u5B8B\u4F53", Font.PLAIN, 12));
		taskOutput2.setBounds(new Rectangle(15, 40, 900, 20));

		taskOutput3 = new JLabel();
		taskOutput3.setFont(new Font("\u5B8B\u4F53", Font.PLAIN, 12));
		taskOutput3.setBounds(new Rectangle(180, 40, 900, 20));
		// frame.add(panel, BorderLayout.NORTH);
		frame.setSize(340, 150);
		frame.setBackground(Color.lightGray);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.add(progressbar);
		frame.add(taskOutput);
		frame.add(taskOutput1);
		frame.add(taskOutput2);
		frame.add(taskOutput3);

		//frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	// ï¿½ï¿½ï¿½ï¿½UIï¿½ï¿½Ï¢
	public LogOutputStream getStreamHandler() {

		LogOutputStream output = new LogOutputStream() {
			protected void processLine(String line, int level) {
				// ï¿½ï¿½ï¿½ï¿½StrRegexï¿½ï¿½ï¿½regexï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ò·½·ï¿½
				int proc = 0;
//				proc = StrRegex.getProcess(line);
				try {
					StrRegex.regex(line);
					proc = StrRegex.process;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// System.out.println(line);
				Progressbar.this.getProgressbar().setValue(proc);
				// ï¿½ï¿½Ê¾ï¿½Ä±ï¿½ï¿½ï¿½Ï¢
				// ï¿½ï¿½È¡ï¿½ï¿½ï¿½ï¿½ï¿½Ä¼ï¿½ï¿½ï¿½ï¿½ï¿½
				if (StrRegex.isDownloadNewFile(line))
					count += 1;
				taskOutput.setText(String.format("\u6587\u4EF6\u4E2A\u6570：%d/%d", count,
						totalCount));
				// 文件大小，以M为单位
				double l = (double) (StrRegex.len / 1024 / 1024);
				// 已下载大小，以M为单位
				double d = (double) (proc * StrRegex.len / 1024 / 1024 / 100);

				taskOutput1.setText(String.format("\u5269\u4F59\u65F6\u95F4：" + StrRegex.time));
				taskOutput2.setText(String.format("\u4E0B\u8F7D\u5927\u5C0F：" + "%.2fM" + "/%.2fM",
						d, l));
				taskOutput3.setText(String.format("\u4E0B\u8F7D\u901F\u5EA6：" + StrRegex.speed
						+ "/s"));

				System.out.println("output line: " + line);
				System.out.println("proc: " + StrRegex.len);
			}
		};
		return output;
	}

	// ï¿½Ø±ï¿½UI
	public void close() {
		this.frame.dispose();
	}

	protected JProgressBar getProgressbar() {
		return this.progressbar;
	}

	public static void main(String[] args) {

		CommandLine cmdLine = CommandLine.parse("wget -i down.txt");
		Progressbar probar = new Progressbar();
		DefaultExecutor executor = new DefaultExecutor();
		LogOutputStream output = probar.getStreamHandler();
		PumpStreamHandler streamHandler = new PumpStreamHandler(output);
		executor.setStreamHandler(streamHandler);
		try {
			executor.execute(cmdLine);
			StrRegex.exit();
		} catch (Exception e) {
			e.printStackTrace();
			StrRegex.exit();
		}
	}
}
