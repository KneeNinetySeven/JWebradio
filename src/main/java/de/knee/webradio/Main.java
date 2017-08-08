package de.knee.webradio;

import de.knee.webradio.media.Radio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        boolean found = new NativeDiscovery().discover();
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

        if (!found) {
            logger.warn("libVlc not found. Aborting.");
            System.exit(404);
        }

        Radio r = new Radio();
        r.play();

        String input;
        do {
            System.out.print("> ");
            input = new Scanner(System.in).next();
            if (input.equalsIgnoreCase("next")) {
                String old = r.getCurrentStation().getName();
                r.nextStation();
                System.out.println(old + " --> " + r.getCurrentStation().getName());
            } else if (input.equalsIgnoreCase("prev")) {
                String old = r.getCurrentStation().getName();
                r.prevStation();
                System.out.println(old + " --> " + r.getCurrentStation().getName());
            } else if (input.equalsIgnoreCase("reload")) {
                r.reloadStations();
            } else if (input.equalsIgnoreCase("play")) {
                r.play();
            } else if (input.equalsIgnoreCase("stop")) {
                r.stop();
            } else if (input.equalsIgnoreCase("current")) {
                System.out.println(r.getCurrentStation().getName());
                System.out.println(r.getCurrentTitle());
            } else if (input.equalsIgnoreCase("all")) {
                System.out.println(r.getAllStations());
            } else if(!input.equalsIgnoreCase("exit")) {
                System.out.println("Unknown Command");
            }
        } while (!input.equalsIgnoreCase("exit"));

        System.out.println("\n\n\n\n\n\nExiting... ");
        System.exit(0);
    }

}
