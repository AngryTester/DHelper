package travelsky;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import elements.DTool;

public class DHelper extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DHelper() {
		JPanel contentPane = new JPanel();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(new Dimension(screenSize.width * 9 / 10, screenSize.height * 8 / 10));
		setTitle("DHelper");// 标题栏
		setFont(new Font("微软雅黑", Font.PLAIN, 12));
		this.setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/super.png")));// 用于生成标题栏图标
		add(contentPane);// 添加窗体
		setBounds(130, 100, screenSize.width / 5 * 4, screenSize.height / 5 * 4);// 位置大小

		contentPane.setBorder(new LineBorder(Color.white, 2, true));// 白色边框
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "确定关闭", "温馨提示", JOptionPane.YES_NO_OPTION);
				if (a == JOptionPane.YES_OPTION) {
					String command = "cmd.exe /C taskkill /f /im node.exe";
					try {
						Runtime.getRuntime().exec(command);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.exit(0);
					setVisible(false);// 关闭
				}
			}
		});
	}

	public static void main(String[] args) {
		// 仿mac风格
		setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
		} catch (Exception e) {
			System.err.println("Oops!  Something went wrong!");
		}
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
					DHelper debugHelper = new DHelper();// 窗体实例化
					debugHelper.add(new DTool(), BorderLayout.CENTER);
					debugHelper.setDefaultCloseOperation(debugHelper.DO_NOTHING_ON_CLOSE);// 不关闭
					debugHelper.setVisible(true);// 显示
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
