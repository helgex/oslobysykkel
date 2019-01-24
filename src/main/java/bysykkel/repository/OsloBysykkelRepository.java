package bysykkel.repository;

import bysykkel.domene.Availability;
import bysykkel.domene.Stations;
import bysykkel.konfig.OsloBysykkelKonfig;
import bysykkel.util.Logger;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OsloBysykkelRepository {
    private Stations stasjoner;
    private final OsloBysykkelKonfig konfig;

    @Inject
    public OsloBysykkelRepository(OsloBysykkelKonfig konfig) {
        this.konfig = konfig;
    }

    public Stations getStations(String noekkel) {
        String stationsJson = kallApi(noekkel, "stations");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            stasjoner = objectMapper.readValue(stationsJson, Stations.class);
        } catch (IOException e) {
            Logger.LOG_ERROR("Feiler i parsing av json", e);
        }
        return stasjoner;
    }

    public Map<Integer, Availability> getAvailability(String noekkel) {
        Map<Integer, Availability> availabilityMap = new HashMap<>();
        String availabilityJson = kallApi(noekkel, "stations/availability");
        JsonFactory jfactory = new JsonFactory();

        try (JsonParser jParser = jfactory.createParser(availabilityJson);) {

            Integer parsetStasjonNummer = null;
            Integer parsedeAntallSykler = null;
            Integer parsedAntallLaaser = null;

            while (jParser.nextToken() != JsonToken.END_ARRAY) {
                String feltNavn = jParser.getCurrentName();

                if ("id".equals(feltNavn)) {
                    jParser.nextToken();
                    parsetStasjonNummer = jParser.getValueAsInt();
                }
                if ("bikes".equals(feltNavn)) {
                    jParser.nextToken();
                    parsedeAntallSykler = jParser.getValueAsInt();
                }
                if ("locks".equals(feltNavn)) {
                    jParser.nextToken();
                    parsedAntallLaaser = jParser.getValueAsInt();
                }
                if (parsetStasjonNummer != null && parsedeAntallSykler != null && parsedAntallLaaser != null) {
                    availabilityMap.put(parsetStasjonNummer, new Availability(parsedeAntallSykler, parsedAntallLaaser));
                    parsetStasjonNummer = null;
                    parsedeAntallSykler = null;
                    parsedAntallLaaser = null;
                }
            }
        } catch (IOException e) {
            Logger.LOG_ERROR("Feiler i parsing av json.", e);
        }
        return availabilityMap;
    }

    private String kallApi(String noekkel, String tjeneste) {
        String response = "";
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(konfig.getBysykkelApiUrl());
            WebTarget stasjonerWebTarget = webTarget.path(tjeneste);
            response = stasjonerWebTarget.request(MediaType.TEXT_PLAIN)
                    .header("Client-Identifier", noekkel)
                    .get(String.class);
        } catch (NotAuthorizedException e) {
            Logger.LOG_ERROR("Feiler med 401 - Not Authorized. Husk public_key", e);
        } catch (Exception e) {
            Logger.LOG_ERROR("Feiler i kall til REST-tjeneste", e);
        }
        return response;
    }
}
