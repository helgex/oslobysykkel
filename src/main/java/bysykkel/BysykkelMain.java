package bysykkel;

import bysykkel.domene.Availability;
import bysykkel.domene.Stations;
import bysykkel.konfig.OsloBysykkelKonfig;
import bysykkel.repository.OsloBysykkelRepository;
import bysykkel.util.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class BysykkelMain {
    public static void main(String[] args) {

        long start = System.nanoTime();
        Logger.LOG_INFO("Starter BysykkelApp");

        String apiNoekkel = getApiNoekkelFraPropertiesFil();
        OsloBysykkelRepository osloBysykkelRepository = new OsloBysykkelRepository(new OsloBysykkelKonfig());

        Stations stations = osloBysykkelRepository.getStations(apiNoekkel);
        Map<Integer, Availability> availability = osloBysykkelRepository.getAvailability(apiNoekkel);

        stations.getStations().stream().forEach(e ->
                Logger.LOG_INFO("Stasjon: " + e.getTitle() + ", " +
                        "Antall sykler: " + availability.get(e.getId()).getAntallSykler() + ", " +
                        "Antall låser: " + availability.get(e.getId()).getAntallLaaser())
        );

        long slutt = System.nanoTime();
        long tid = slutt - start;

        Logger.LOG_INFO("Brukt tid: " + tid / 1000000 + " millisekunder");
    }

    private static String getApiNoekkelFraPropertiesFil() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "oslobysykkel.properties";
        Properties appProps = new Properties();

        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            Logger.LOG_WARNING("Feilet i lesingen av konfig-fil: " + e);
        }
        return appProps.getProperty("api_noekkel");
    }
}
