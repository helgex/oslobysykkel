package bysykkel.domene;

public class Availability {
    private Integer antallSykler;
    private Integer antallLaaser;

    public Availability(Integer sykler, Integer laaser) {
        this.antallSykler = sykler;
        this.antallLaaser = laaser;
    }

    public Integer getAntallSykler() {
        return antallSykler;
    }

    public void setAntallSykler(Integer antallSykler) {
        this.antallSykler = antallSykler;
    }

    public Integer getAntallLaaser() {
        return antallLaaser;
    }

    public void setAntallLaaser(Integer antallLaaser) {
        this.antallLaaser = antallLaaser;
    }
}
