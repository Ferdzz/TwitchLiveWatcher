import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

public class TwitchLiveWatcher {

	private static boolean hasGUI = true;
	private static TwitchLiveWatcherGUI gui;
	private static JTable table;

	private static String[] colName = { "Name", "Status" };
	private static Object[][] data = new Object[300][2];

	public static void main(String[] args) throws Exception {
		if (args.length > 0 && args[0].equals("nogui"))
			hasGUI = false;

		BufferedReader reader = new BufferedReader(new InputStreamReader(TwitchLiveWatcher.class.getResourceAsStream("/usernames.txt")));

		String line = null;
		int count = 0;
		while ((line = reader.readLine()) != null) {
			if (isOnline(line)) {
				if (hasGUI) {
					data[count][0] = line;
					data[count][1] = "Online";
				} else
					System.out.println("Streamer " + line + ": Online");
			} else {
				if (hasGUI) {
					data[count][0] = line;
					data[count][1] = "Offline";
				} else
					System.out.println("Streamer " + line + ": Offline");
			}
			count++;
		}
		reader.close();

		// Create the GUI and handlers
		if (hasGUI) {
			table = new JTable();
			table.setModel(new DefaultTableModel(data, colName) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});

			// On double click, we want to open the browser at the selected channel
			table.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					JTable table = (JTable) e.getSource();
					Point p = e.getPoint();
					int row = table.rowAtPoint(p);
					if (e.getClickCount() == 2 && table.getValueAt(row, 0) != null) {
						try {
							if (Desktop.isDesktopSupported())
								Desktop.getDesktop().browse(new URI("http://www.twitch.tv/" + table.getValueAt(row, 0)));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			gui = new TwitchLiveWatcherGUI(table);
		}
	}

	/**
	 * Checks if streamer is live
	 * 
	 * @param name
	 *            Name of the stream
	 * @return true if streamer is live
	 * @throws Exception
	 *             if could not parse the given name
	 */
	public static boolean isOnline(String name) throws Exception {
		URL url = new URL("https://api.twitch.tv/kraken/streams/" + name);
		URLConnection connection = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
		Map map = parser.parseJson(reader.readLine());

		return map.get("stream") instanceof Map;
	}
}