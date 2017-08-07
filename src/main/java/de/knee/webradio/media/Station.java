package de.knee.webradio.media;

import java.net.MalformedURLException;
import java.net.URL;

public class Station {

    private final String name;
    private final URL url;

    public Station(String name, String url) throws MalformedURLException {
        this.name = name;
        this.url = new URL(url);
    }


    public URL getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (!name.equals(station.name)) return false;
        return url.equals(station.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
