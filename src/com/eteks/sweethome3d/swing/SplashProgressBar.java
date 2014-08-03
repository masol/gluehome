package com.eteks.sweethome3d.swing;

import javax.swing.*;  

import com.sun.awt.AWTUtilities;  
  

import java.awt.*;  
import java.io.File;  
import java.net.*;  

  
public class SplashProgressBar extends JWindow implements Runnable {  
      
      
      
    /** 
     *  
     */  
    private static final long serialVersionUID = -983869148219123774L;  
    // private  long furnitureNum = 0;
    
    Thread splashThread; // 进度条更新线程  
    JProgressBar progress; // 进度条  
    boolean exit = false;
  
    public SplashProgressBar(URL url) {  
          
        Container container = getContentPane(); // 得到容器  
        JPanel pane = new JPanel();  
        pane.setBackground(Color.WHITE);  
        pane.setOpaque(true);  
        pane.setBorder(null);  
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // 设置光标  
        if (url != null) {  
            JLabel j = new JLabel(new ImageIcon(url));  
            pane.add(j);  
            container.add(pane, BorderLayout.CENTER); // 增加图片  
        }  
    //  container.add(new JButton("dsadsadsads"), BorderLayout.);  
        progress = new JProgressBar(1, 100); // 实例化进度条  
        progress.setIndeterminate(true);
        progress.setStringPainted(true); // 描绘文字  
        progress.setString("\u6B63\u5728\u52A0\u8F7D\u7A0B\u5E8F\uFF0C\u8BF7\u7A0D\u540E\u2026\u2026"); // 设置显示文字
        progress.setBackground(Color.white); // 设置背景色  
        container.add(progress, BorderLayout.SOUTH); // 增加进度条到容器上  
  
        Dimension screen = getToolkit().getScreenSize(); // 得到屏幕尺寸  
        pack(); // 窗口适应组件尺寸  
        setLocation((screen.width - getSize().width) / 2,  
                (screen.height - getSize().height) / 2); // 设置窗口位置  
        AWTUtilities.setWindowOpacity(this, 1f);  
          
    }  
  
    public void start() {  
        this.toFront(); // 窗口前端显示  
        splashThread = new Thread(this); // 实例化线程  
        splashThread.start(); // 开始运行线程  
    }  
  
    public void run() {  
//    	String path = System.getenv("APPDATA") + "\\eTeks\\Sweet Home 3D\\furniture";
//    	File file = new File(path);
//    	if(file.listFiles().length != 0){
//    		furnitureNum = getlist(file);
//    		System.out.println("sh3f file num:" + furnitureNum);
//    	}
        setVisible(true); // 显示窗口
        try {  
        	 while (true) { 
//        		 Thread.sleep(500);
        		 
//        		 progress.setValue(progress.getValue() + 1); // 设置进度条值  
//             }  
//            for (int i = 0; i < furnitureNum; i++) {  
                Thread.sleep(500); // 线程休眠  
//                progress.setValue(i + 1); // 设置进度条值 
//                progress.setString("正在加载第" + (i+1) + "个家具");
//                Thread.sleep(150);
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
        dispose(); // 释放窗口  
    }
    /**
     *  stop splash progress bar
     */
    public void stop(){
    	dispose();
    }
//    public long getlist(File f){
//        long size = 0;
//        for(File file:f.listFiles())
//        {
//        if(file.isFile() && file.getName().endsWith(".sh3f")){
//        	size++;
//        }
//        }
//        return size;
//    }
      
}  