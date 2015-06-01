import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

public class TwitchLiveWatcher {

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(TwitchLiveWatcher.class.getResourceAsStream("/usernames.txt")));
		
		String line;
		while ((line = reader.readLine()) != null)
			if (isOnline(line))
				System.out.println("Streamer " + line + ": Online");
			else
				System.out.println("Streamer " + line + ": Offline");
	}

	public static boolean isOnline(String name) throws Exception {
		URL url = new URL("https://api.twitch.tv/kraken/streams/" + name);
		URLConnection connection = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
		Map map = parser.parseJson(reader.readLine());

		return map.get("stream") instanceof Map;
	}
}