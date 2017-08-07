package de.knee.webradio.util;

import de.knee.webradio.media.Station;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class StationParser {

    private String stationConfig;
    private String configurationPath = "config.properties";

    public List<Station> parseFromJson() throws IOException, ParseException {
        List<Station> parsed = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(this.stationConfig));
        JSONArray list = (JSONArray) jsonObject.get("stations");

        Iterator<JSONObject> iterator = list.iterator();
        while (iterator.hasNext()) {
            JSONObject station = iterator.next();
            Station gen = new Station((String) station.get("name"), (String) station.get("url"));
            parsed.add(gen);
        }

        return parsed;
    }

    private void importConfig(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(this.configurationPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stationConfig = properties.getProperty("stationConfig");
    }

    public StationParser() {
        importConfig();
    }
}
