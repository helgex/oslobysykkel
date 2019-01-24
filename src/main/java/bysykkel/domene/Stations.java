package bysykkel.domene;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "stations"
})
public class Stations {

    @JsonProperty("stations")
    private List<Station> stations = null;//NOSONAR
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("stations")
    public List<Station> getStations() {
        return stations;
    }

    @JsonProperty("stations")
    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
