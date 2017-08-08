package de.knee.webradio.media;

import de.knee.webradio.util.StationParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.*;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Radio {

    private static final Logger logger = LoggerFactory.getLogger(Radio.class);

    private List<Station> stationList;
    private int currentStationIndex;

    private HeadlessMediaPlayer mediaPlayer;

    public Radio() {
        this.stationList = new ArrayList<>();
        this.currentStationIndex = 0;

        this.mediaPlayer = new MediaPlayerFactory().newHeadlessMediaPlayer();
        this.mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
                System.out.println(getCurrentMediaMeta().getNowPlaying());
            }
        });
        this.mediaPlayer.setStandardMediaOptions("--vout", "dummy");
        this.mediaPlayer.setRepeat(true);
        this.mediaPlayer.setPlaySubItems(true);

        this.reloadStations();
        this.mediaPlayer.playMedia(getCurrentStation().getUrl().toString());
        System.out.println(getAllStations());
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
        this.play();
    }

    public void play() {
        this.mediaPlayer.play();
    }

    public void stop() {
        this.mediaPlayer.stop();
    }

    public void nextStation() {
        this.stop();
        if (this.currentStationIndex + 1 > stationList.size() - 1) {
            this.currentStationIndex = 0;
        } else {
            this.currentStationIndex++;
        }
        mediaPlayer.playMedia(this.stationList.get(currentStationIndex).getUrl().toString());
        this.play();
    }

    public void prevStation() {
        this.stop();
        if (this.currentStationIndex - 1 < 0) {
            this.currentStationIndex = stationList.size() - 1;
        } else {
            this.currentStationIndex--;
        }
        mediaPlayer.playMedia(this.stationList.get(currentStationIndex).getUrl().toString());
        this.play();
    }

    public String getAllStations() {
        String ret = "";
        for (Station station : this.stationList) {
            ret += station.getName() + " @ " + station.getUrl() + "\n";
        }
        return ret;
    }

    public Station getCurrentStation() {
        return this.stationList.get(this.currentStationIndex);
    }

    public String getCurrentTitle() {
        while (!this.mediaPlayer.isPlaying()) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.mediaPlayer.parseMedia();
        MediaMeta meta = getCurrentMediaMeta();
        meta.parse();
        String ret = meta.getNowPlaying();
        return ret;
    }

    private MediaMeta getCurrentMediaMeta() {
        MediaMeta meta;
        if (mediaPlayer.subItems().size() >= 1) {
            meta = mediaPlayer.getSubItemMediaMeta().get(0);
        } else {
            meta = mediaPlayer.getMediaMeta();
        }
        return meta;
    }
}
