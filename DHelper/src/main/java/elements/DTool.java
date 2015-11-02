package elements;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class DTool extends JPanel {

	String sessionId = "";
	String contextName = "";
	String elementId = "";

	private static final long serialVersionUID = 1L;
	JPanel actionPanel = new JPanel();// 操作区
	JPanel debugPanel = new JPanel();// 调试区

	JLabel apkSourceLabel = new JLabel();// apk路径
	JTextField apkSourceValue = new JTextField(15);
	JFileChooser chooser = new JFileChooser();
	JButton choose = new JButton("浏览");// 选择

	JButton startAPP = new JButton("启动APP");//
	JButton getContextButton = new JButton("获取context");// 获取当前context，赋值context下拉列表
	JLabel contextLabel = new JLabel();// 当前context文字
	@SuppressWarnings("rawtypes")
	JComboBox comboBox = new JComboBox();// context下拉列表
	JButton switchContextButton = new JButton("switchContext");// 获取当前context，赋值context下拉列表
	JButton sourceButton = new JButton("显示源码");

	JLabel locatorLabel = new JLabel();
	JButton executeButton = new JButton("executeJs");
	JLabel appiumLabel = new JLabel();
	JLabel appPackageLabel = new JLabel();
	JLabel appActivityLabel = new JLabel();
	JTextField appium = new JTextField(10);
	JTextField appPackage = new JTextField(10);
	JTextField appActivity = new JTextField(10);
	JTextField executeJs = new JTextField(20);

	JTextField locator = new JTextField(10);
	JButton click = new JButton("点击");
	JButton sendKeys = new JButton("输入");
	JTextField sendKeysValue = new JTextField(10);

	JPanel sourcePanel = new JPanel();
	JTextArea PageSource = new JTextArea();
	JTextArea script = new JTextArea();

	@SuppressWarnings("unchecked")
	public DTool() throws MalformedURLException {

		setLayout(new BorderLayout());
		actionPanel.setLayout(new FlowLayout());
		apkSourceLabel.setText("APK绝对路径:");
		apkSourceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		contextLabel.setText("当前context:");
		contextLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		appiumLabel.setText("AutomationName:");
		appiumLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		appPackageLabel.setText("appPackage：");
		appPackageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		appActivityLabel.setText("appActivity：");
		appActivityLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		actionPanel.add(apkSourceLabel);
		actionPanel.add(apkSourceValue);
		actionPanel.add(choose);
		actionPanel.add(appiumLabel);
		actionPanel.add(appium);
		actionPanel.add(appPackageLabel);
		actionPanel.add(appPackage);
		actionPanel.add(appActivityLabel);
		actionPanel.add(appActivity);
		actionPanel.add(startAPP);

		actionPanel.add(sourceButton);

		locatorLabel.setText("定位信息：");
		locatorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));

		debugPanel.add(getContextButton);
		debugPanel.add(contextLabel);
		comboBox.addItem("点左侧按钮获取");
		debugPanel.add(comboBox);
		debugPanel.add(switchContextButton);
		debugPanel.add(locatorLabel);
		debugPanel.add(locator);
		debugPanel.add(click);
		debugPanel.add(sendKeys);
		debugPanel.add(sendKeysValue);
		debugPanel.add(executeButton);
		debugPanel.add(executeJs);

		PageSource.setBorder(BorderFactory.createEmptyBorder());
		PageSource.setLineWrap(true);

		script.setBorder(BorderFactory.createEmptyBorder());
		script.setLineWrap(true);

		PageSource.setText(" 使用前请仔细阅读使用说明！！！\n\n\n使用说明：\n\n\n 1.本工具主要用于移动端自动化测试调试.\n"
				+ " 2.开启工具后，首先需要启动移动设备，然后选择APK。\n"
				+ " 3.点击启动APP，即可启动指定APP。\n" + " 4.本工具支持webview的处理，手工操作到达指定页面后，点击获取context即可将当前页面的所有context收集至下拉列表。\n"
				+ " 5.通过下拉列表选择指定context后点击switchContext即可进行context切换。\n"
				+ " 6.定位信息支持的定位格式，如\"tagName=>div,text=>登录\"，但建议单定位信息。\n"
				+ " 7.通过定位信息结合点击或输入操作，可以判断根据定位信息是否可以成功定位元素。\n" + " 8.Appium执行日志保存在当前目录下的appium.log文件中\n");

		actionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(2, 0, 2, 0)));

		debugPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(2, 0, 2, 0)));

		startAPP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = "cmd.exe /k start appium -g appium.log";
				try {
					Runtime.getRuntime().exec(command);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e2) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e2.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}
				JSONObject map1 = new JSONObject();
				JSONObject map2 = new JSONObject();
				try {
					map1.put("platformName", "Android");
					map1.put("deviceName", "192.168.56.101:5555");
					map1.put("automationName", appium.getText());
					map1.put("newCommandTimeout", 6000);
					map1.put("app", apkSourceValue.getText());
					map1.put("appPackage", appPackage.getText());
					map1.put("appActivity", appActivity.getText());
					map2.put("desiredCapabilities", map1);
					CloseableHttpClient httpClient = HttpClients.createDefault();
					HttpPost method = new HttpPost("http://127.0.0.1:4723/wd/hub/session");
					StringEntity entity = new StringEntity(map2.toString(), "utf-8");
					entity.setContentType("application/json");
					method.setEntity(entity);
					HttpResponse res = httpClient.execute(method);
					HttpEntity entitys = res.getEntity();
					JSONObject response = new JSONObject(
							new org.json.JSONTokener(new InputStreamReader(entitys.getContent(), "UTF-8")));
					sessionId = response.get("sessionId").toString();
				} catch (JSONException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

				script.append("private app = App.start();//启动应用\n");
			}

		});

		getContextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int itemCount = comboBox.getItemCount();
				if (itemCount != 1) {
					for (int i = 1; i < itemCount; i++) {
						int count = comboBox.getItemCount();
						comboBox.removeItemAt(count - 1);
					}
				}
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpGet method = new HttpGet("http://127.0.0.1:4723/wd/hub/session/" + "sessionId" + "/window_handles");
				HttpResponse res;
				try {
					res = httpClient.execute(method);
					HttpEntity entitys = res.getEntity();
					JSONObject response = new JSONObject(
							new org.json.JSONTokener(new InputStreamReader(entitys.getContent(), "UTF-8")));
					String contextsString = response.get("value").toString();
					String[] contexts = contextsString.split(",");
					for (String context : contexts) {
						comboBox.addItem(context.replace("\"", "").replace("[", "").replace("]", ""));
					}
				} catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (JSONException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contextName = comboBox.getSelectedItem().toString();
			}
		});

		switchContextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpPost method = new HttpPost("http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/context");
				JSONObject map = new JSONObject();
				try {
					map.put("name", contextName);
					StringEntity entity = new StringEntity(map.toString(), "utf-8");
					entity.setContentType("application/json");
					method.setEntity(entity);
					httpClient.execute(method);
				} catch (JSONException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} // NATIVE_APP WEBVIEW_0
				catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

				if (contextName.contains("WEBVIEW")) {
					script.append("app.contextH();//转WEBVIEW\n");
				} else {
					script.append("app.context();//转NATIVE\n");
				}

			}
		});

		sourceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HttpGet method = new HttpGet("http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/source");
				HttpResponse res;
				try {
					CloseableHttpClient httpClient = HttpClients.createDefault();
					res = httpClient.execute(method);
					HttpEntity entitys = res.getEntity();
					JSONObject response = new JSONObject(
							new org.json.JSONTokener(new InputStreamReader(entitys.getContent(), "UTF-8")));
					Document document = Jsoup.parse(response.get("value").toString());
					PageSource.setText(document.body().html());
				} catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (JSONException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		click.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent e) {
				HashMap<String, Object> selectors = toHash(locator.getText());
				int idx = 1;
				Iterator iter = selectors.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					if (key.contentEquals("index")) {
						idx = Integer.parseInt((String) selectors.remove("index"));
					}
				}
				String xpath = buildXpath(selectors);
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpPost findElement = new HttpPost("http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/elements");
				String entity_findElement = new JSONObject(ImmutableMap.of("using", "xpath", "value", xpath))
						.toString();
				StringEntity entity = new StringEntity(entity_findElement, "utf-8");
				entity.setContentType("application/json");
				findElement.setEntity(entity);
				HttpResponse res;
				try {
					CloseableHttpClient httpClient1 = HttpClients.createDefault();
					res = httpClient1.execute(findElement);
					HttpEntity entitys = res.getEntity();
					JSONObject response = new JSONObject(
							new org.json.JSONTokener(new InputStreamReader(entitys.getContent(), "UTF-8")));
					String value = response.getString("value");
					String[] values = value.substring(1, value.indexOf("]")).split(",");
					JSONObject element_json = new JSONObject(values[idx - 1]);
					elementId = element_json.get("ELEMENT").toString();

					ArrayList<ImmutableMap<String, String>> convertedArgs = Lists
							.newArrayList(ImmutableMap.of("ELEMENT", elementId));
					String entity_click = new JSONObject(ImmutableMap.of("script", "arguments[0].click();", "args",
							Lists.newArrayList(convertedArgs))).toString();
					HttpPost click = new HttpPost(
							"http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/element/" + elementId + "/click");
					click.setEntity(new StringEntity(entity_click, "utf-8"));
					httpClient.execute(click);
				} catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (JSONException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

				script.append("app.element(\"" + locator.getText() + "\").click();//点击\n");
			}
		});

		sendKeys.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent e) {
				HashMap<String, Object> selectors = toHash(locator.getText());
				int idx = 1;
				Iterator iter = selectors.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					if (key.contentEquals("index")) {
						idx = Integer.parseInt((String) selectors.remove("index"));
					}
				}
				String xpath = buildXpath(selectors);
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpPost findElement = new HttpPost("http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/elements");
				String entity_findElement = new JSONObject(ImmutableMap.of("using", "xpath", "value", xpath))
						.toString();
				StringEntity entity = new StringEntity(entity_findElement, "utf-8");
				entity.setContentType("application/json");
				findElement.setEntity(entity);
				HttpResponse res;
				try {
					CloseableHttpClient httpClient1 = HttpClients.createDefault();
					res = httpClient1.execute(findElement);
					HttpEntity entitys = res.getEntity();
					JSONObject response = new JSONObject(
							new org.json.JSONTokener(new InputStreamReader(entitys.getContent(), "UTF-8")));
					String value = response.getString("value");
					String[] values = value.substring(1, value.indexOf("]")).split(",");
					JSONObject element_json = new JSONObject(values[idx - 1]);
					elementId = element_json.get("ELEMENT").toString();
					String entity_sendKeys = new JSONObject(ImmutableMap.of("value", transit(sendKeysValue.getText())))
							.toString();

					HttpPost sendKeys = new HttpPost(
							"http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/element/" + elementId + "/value");
					StringEntity entity1 = new StringEntity(entity_sendKeys.toString(), "UTF-8");
					entity1.setContentType("application/json");
					sendKeys.setEntity(entity1);
					httpClient.execute(sendKeys);
				} catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (JSONException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

				script.append("app.element(\"" + locator.getText() + "\").sendKeys(\"" + sendKeysValue.getText()
						+ "\");//输入\n");
			}
		});

		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CloseableHttpClient httpClient = HttpClients.createDefault();
				try {
					String entity_execute = new JSONObject(
							ImmutableMap.of("script", executeJs.getText(), "args", Lists.newArrayList())).toString();

					HttpPost execute = new HttpPost("http://127.0.0.1:4723/wd/hub/session/" + sessionId + "/execute");
					StringEntity entity1 = new StringEntity(entity_execute.toString(), "UTF-8");
					entity1.setContentType("application/json");
					execute.setEntity(entity1);
					httpClient.execute(execute);
				} catch (ClientProtocolException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException e1) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, e1.toString(), "异常信息", JOptionPane.ERROR_MESSAGE);
				}

				script.append("app.exceute(\"" + executeJs.getText() + "\");//执行JS\n");
			}
		});

		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int state = chooser.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
				if (state == 1) {
					return;// 撤销则返回
				} else {
					File f = chooser.getSelectedFile();// f为选择到的文件
					apkSourceValue.setText(f.getAbsolutePath());
				}
			}
		});

		sourcePanel.setLayout(new GridLayout(1, 1));
		sourcePanel.add(new JScrollPane(PageSource));
		this.add(actionPanel, BorderLayout.NORTH);
		this.add(debugPanel, BorderLayout.SOUTH);
		this.add(sourcePanel, BorderLayout.CENTER);

		this.setBounds(10, 5, 10, 10);

	}

	public HashMap<String, Object> toHash(String options) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (options.contains("xpath=>")) {
			map.put("xpath", options.substring(options.indexOf("=>") + 2));
			return map;
		}
		String[] optionInfos = options.split(",");
		for (String optionInfo : optionInfos) {
			if (optionInfo.indexOf("=>") < 0) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, options + "格式错误,请参照（属性=>属性值）", "异常信息", JOptionPane.ERROR_MESSAGE);
				// 抛出格式错误
			}
			String[] option = optionInfo.split("=>");
			if (option.length == 2) {
				if (option[1].startsWith("/") && option[1].endsWith("/")) {
					Pattern pattern = Pattern.compile(option[1].substring(1, option[1].length() - 1), Pattern.DOTALL);
					map.put(option[0], pattern);
				} else {
					map.put(option[0], option[1]);
				}
			} else {// 抛出格式错误}
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, options + "格式错误,请参照（属性=>属性值）", "异常信息", JOptionPane.ERROR_MESSAGE);
				// 抛出格式错误
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildXpath(HashMap<String, Object> options) {
		HashMap<String, Object> selectors = (HashMap<String, Object>) options.clone();
		if (selectors.get("xpath") != null) {
			return selectors.get("xpath").toString();
		}
		String xpath = "";
		Object tagName = selectors.remove("tagName");
		if (tagName == null) {
			xpath = ".//*";

			if (selectors.size() >= 1) {
				xpath += "[";
				Iterator iter = selectors.entrySet().iterator();
				int n = 0;

				while (iter.hasNext()) {

					if (n > 0) {
						xpath += " and ";
					}
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					Object value = entry.getValue();
					xpath += equalPair(key, value);
					n += 1;

				}
			}
			xpath += "]";

		} else {
			String[] tagNames = tagName.toString().split("\\|");
			for (String tag : tagNames) {
				if (xpath.length() > 0) {
					xpath += "|";
				}
				xpath += ".//" + tag;

				if (selectors.size() >= 1) {
					Iterator iter = selectors.entrySet().iterator();
					xpath += "[";
					while (iter.hasNext()) {
						if (!xpath.endsWith("[")) {
							xpath += " and ";
						}
						Map.Entry entry = (Map.Entry) iter.next();
						String key = (String) entry.getKey();
						Object value = entry.getValue();
						xpath += equalPair(key, value);

					}
					xpath += "]";
				}
			}
		}

		return xpath;
	}

	private String equalPair(String key, Object value) {
		String pair = "";

		if (key.equals("className") || key.equals("class")) {
			pair = " @class=" + "'" + value.toString() + "'";
		} else if (key.equals("text")) {
			pair = "normalize-space()" + "='" + value.toString() + "'";
		} else if ((key.equals("suffixText"))) {
			pair = "contains(following-sibling::text()[1],'" + value.toString() + "')";
		} else if ((key.equals("prefixText"))) {
			pair = "contains(preceding-sibling::text()[1],'" + value.toString() + "')";
		} else {
			pair = "@" + key + "='" + value.toString() + "'";
		}
		return pair;
	}

	public static CharSequence[] transit(String... keysToSend) {
		return keysToSend;
	}

	public static void main(String[] args) {
		String command = "cmd.exe /k start appium -g appium.log";
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
