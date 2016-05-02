package hongkun.tank;

import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	// No one can new a PropertyManager;
	private PropertyManager() {};
	
	private static Properties properties = new Properties();
	
	static {
		try {
			properties.load(PropertyManager.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPropertyByName(String name) {
		return properties.getProperty(name);
	}
}
