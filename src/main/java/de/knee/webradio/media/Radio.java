package de.knee.webradio.media;

import de.knee.webradio.util.StationParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Radio {

    private static final Logger logger = LoggerFactory.getLogger(Radio.class);
    private MediaListPlayer mediaPlayer;
    private int currentStationIndex;

    private List<Station> stationList;

    public Radio() {
        this.stationList = new ArrayList<>();
        this.currentStationIndex = 0;
        this.mediaPlayer = new MediaPlayerFactory().newMediaListPlayer();
        this.reloadStations();
        mediaPlayer.setMediaList(new MediaPlayerFactory().newMediaList());
        mediaPlayer.getMediaList().addMedia(getCurrentStation().getUrl().toString());
    }

    public void reloadStations() {
        if (mediaPlayer.isPlaying()) this.stop();

        List<Station> parsed = new ArrayList<>();
        try {
            parsed = new StationParser().parseFromJson();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.stationList = parsed;
        this.stationList.forEach(station -> System.out.println(station.getName() + " @ " + station.getUrl()));
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public Station getCurrentStation() {
        return this.stationList.get(this.currentStationIndex);
    }

    public void nextStation() {
        this.stop();
        if (this.currentStationIndex + 1 > stationList.size() - 1) {
            this.currentStationIndex = 0;
        } else {
            this.currentStationIndex++;
        }
        mediaPlayer.getMediaList().clear();
        mediaPlayer.getMediaList().addMedia(getCurrentStation().getUrl().toString());
        this.play();
    }
}
