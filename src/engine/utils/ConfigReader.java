package engine.utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class ConfigReader {  
	
	public static String[] readJson(String path) {
		JSONParser parser = new JSONParser();
		
		try {
			JSONArray json = (JSONArray) parser.parse(new FileReader(path));
			for (Object obj : json)
			  {
			    JSONObject config = (JSONObject) obj;
			    
	            String width = (String) config.get("width");
	            String height = (String) config.get("height");
	            String n_objects = (String) config.get("n_objects");
	            String multiObject = (String) config.get("multiObject");
	            String model = (String) config.get("model");
	            String camera = (String) config.get("camera");

			    String[] data = {width, height, n_objects, multiObject, model, camera};
			    
			    return data;
			  }
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
	}
}
