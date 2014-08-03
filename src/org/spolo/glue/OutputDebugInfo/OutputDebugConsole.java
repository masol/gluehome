/**
 * This file is part of the spp(Superpolo Platform). Copyright (C) by SanPolo
 * Co.Ltd. All rights reserved.
 * 
 * See http://www.spolo.org/ for more information.
 * 
 * SanPolo Co.Ltd http://www.spolo.org/ Any copyright issues, please contact:
 * copr@spolo.org
 **/
package org.spolo.glue.OutputDebugInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.model.UserPreferences;

public class OutputDebugConsole extends JFrame {
	private static final long serialVersionUID = 1L;
	private static OutputDebugConsole m_OutputDebugConsole;
	private JTextArea alltextarea;
	private JScrollPane alljp;
	private UserPreferences preferences;

	public static OutputDebugConsole getInstance() throws IOException {
		if (m_OutputDebugConsole == null) {
			m_OutputDebugConsole = new OutputDebugConsole();
		}
		return m_OutputDebugConsole;
	}

	/**
	 * 默认开启文件记录的log的方式
	 */
	public void startDafaultDebug() {
		// 重新定义输出流到安装目录下的log.txt中
		File log = new File("log.txt");
		try {
			LogFileOutputStream lfos = new LogFileOutputStream(log);
			System.setOut(lfos);
			System.setErr(lfos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动控制台方式
	 * 
	 * @throws Exception
	 */
	public void startConsoleDebug() throws Exception {
		// 将程序的输出的信息输出到控制窗口中,关闭之后将流重新定向到log.txt文件中
		this.setLayout(new BorderLayout());
		this.add(new ButtonList(), BorderLayout.SOUTH);
		this.add(getConsole(), BorderLayout.CENTER);
		this.setBounds(0, 0, 500, 300);
		this.setName(preferences.getLocalizedString(OutputDebugConsole.class,
				"Console.title"));
		NPrintStream Nps = new NPrintStream(System.out, this.alltextarea);
		System.setOut(Nps);
		System.setErr(Nps);
		this.setVisible(true);
	}

	private OutputDebugConsole() throws IOException {
		this.preferences = new DefaultUserPreferences();
	}

	// 所有内容
	public JScrollPane getConsole() throws IOException {
		if (this.alltextarea == null) {
			this.alltextarea = new JTextArea();
		}
		alltextarea.setBackground(Color.BLACK);
		alltextarea.setForeground(Color.GREEN);
		alltextarea.setLineWrap(true);
		alltextarea.setWrapStyleWord(true);
		alltextarea.setEditable(false);
		alltextarea.setText(preferences.getLocalizedString(
				OutputDebugConsole.class, "TextArea.debuginfo"));
		if (alljp == null) {
			alljp = new JScrollPane(alltextarea);
		}
		return alljp;
	}

	/**
	 * 获得输出文件的按钮
	 * 
	 * @return JButton按钮对象
	 * @throws IOException
	 */
	public JButton getOutputButton() throws IOException {
		JButton button = new JButton(preferences.getLocalizedString(
				OutputDebugConsole.class, "Button.output.name"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String logfilepath = "";
				try {
					File logfile = new File("log.txt");
					logfilepath = logfile.getAbsolutePath();
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							logfile));
					writer.write(alltextarea.getText());
					writer.close();
					JOptionPane.showMessageDialog(
							null,
							preferences.getLocalizedString(
									OutputDebugConsole.class, "Logfile.action")
									+ logfilepath
									+ preferences.getLocalizedString(
											OutputDebugConsole.class,
											"Logfile.action.Nstatu"));
				} catch (IOException e) {
					e.printStackTrace();
					try {
						JOptionPane.showMessageDialog(
								null,
								preferences.getLocalizedString(
										OutputDebugConsole.class,
										"Logfile.action")
										+ logfilepath
										+ preferences.getLocalizedString(
												OutputDebugConsole.class,
												"Logfile.action.Wstatu"));
					} catch (HeadlessException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		return button;
	}

	/**
	 * 获得清楚界面的按钮
	 * 
	 * @return JButton按钮对象
	 * @throws IOException
	 */
	public JButton getClearButton() throws IOException {
		JButton button = new JButton(preferences.getLocalizedString(
				OutputDebugConsole.class, "Button.clear.name"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == preferences.getLocalizedString(
						OutputDebugConsole.class, "Button.clear.name")) {
					if (alltextarea != null) {
						alltextarea.setText("");
					}
				}
			}
		});
		return button;
	}

	/**
	 * 把按钮都放到JPanel组建当中因为整个JFrame界面是用的是BorderLayout，所以如果将按钮单独放在最下边只能显示一个按钮，
	 * 这里将按钮放到JPanel组建当中，在把该组建添加到Layout中
	 */
	public class ButtonList extends JPanel {
		private static final long serialVersionUID = 1L;

		public ButtonList() throws IOException {
			FlowLayout fl = new FlowLayout();
			this.setLayout(fl);
			this.add(getClearButton());
			this.add(getOutputButton());
			this.setVisible(true);
		}
	}
}
