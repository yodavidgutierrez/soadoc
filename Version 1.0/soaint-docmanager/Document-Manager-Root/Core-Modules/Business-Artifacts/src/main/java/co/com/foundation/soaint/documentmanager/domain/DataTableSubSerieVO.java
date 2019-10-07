package co.com.foundation.soaint.documentmanager.domain;

public class DataTableSubSerieVO {

    private String codSubserie;
    private String nomSubserie;
    private String estSubserie;
    private String notAlcance;
    private String fueBibliografica;
    private String carConcat;
    private String conConcat;

    public DataTableSubSerieVO(String codSubserie, String nomSubserie, int estSubserie, String notAlcance,
                               String fueBibliografica, String carAdministrativa, String carLegal, String carContable, String carJuridica, String carTecnica,
                               String conPublica, String conClasificada, String conReservada) {
        this.codSubserie = codSubserie;
        this.nomSubserie = nomSubserie;
        this.notAlcance = notAlcance;
        this.fueBibliografica = fueBibliografica;

        concatCaracSubSerie(carAdministrativa, carLegal, carContable, carJuridica, carTecnica);
        concatConfidencialidad(conPublica, conClasificada, conReservada);
        convertEstSubToString(estSubserie);

    }

    private void convertEstSubToString(int estado) {

        if (estado == 1) {
            estSubserie = "Activo";
        } else {
            estSubserie = "Inactivo";
        }
    }


    private void concatCaracSubSerie(String carAdministrativa, String carTecnica, String carLegal, String carJuridica,
                                     String carContable) {

        carConcat = "";
        if (carAdministrativa == "on") {
            carConcat = "A";
        }
        if (carContable == "on") {
            carConcat += carConcat.length() == 0 ? "C" : ", C";
        }
        if (carJuridica == "on") {
            carConcat += carConcat.length() == 0 ? "J" : ", J";
        }
        if (carLegal == "on") {
            carConcat += carConcat.length() == 0 ? "L" : ", L";
        }
        if (carTecnica == "on") {
            carConcat += carConcat.length() == 0 ? "T" : ", T";
        }

    }

    private void concatConfidencialidad(String conPublica, String conClasificada, String conReservada) {

        conConcat = "";

        if (conClasificada == "on") {
            conConcat  = "C";
        }
        if (conPublica == "on") {
            conConcat += conConcat.length() == 0 ? "P" : ", P";
        }
        if (conReservada == "on") {
            conConcat += conConcat.length() == 0 ? "R" : ", R";
        }
    }

    public String getCodSubserie() {
        return codSubserie;
    }

    public void setCodSubserie(String codSubserie) {
        this.codSubserie = codSubserie;
    }

    public String getNomSubserie() {
        return nomSubserie;
    }

    public void setNomSubserie(String nomSubserie) {
        this.nomSubserie = nomSubserie;
    }

    public String getEstSubserie() {
        return estSubserie;
    }

    public void setEstSubserie(String estSubserie) {
        this.estSubserie = estSubserie;
    }

    public String getNotAlcance() {
        return notAlcance;
    }

    public void setNotAlcance(String notAlcance) {
        this.notAlcance = notAlcance;
    }

    public String getFueBibliografica() {
        return fueBibliografica;
    }

    public void setFueBibliografica(String fueBibliografica) {
        this.fueBibliografica = fueBibliografica;
    }

    public String getCarConcat() {
        return carConcat;
    }

    public void setCarConcat(String carConcat) {
        this.carConcat = carConcat;
    }

    public String getConConcat() {
        return conConcat;
    }

    public void setConConcat(String conConcat) {
        this.conConcat = conConcat;
    }

    @Override
    public String toString() {
        return "DataTableSubSerieVO{" +
                "codSubserie='" + codSubserie + '\'' +
                ", nomSubserie='" + nomSubserie + '\'' +
                ", estSubserie='" + estSubserie + '\'' +
                ", notAlcance='" + notAlcance + '\'' +
                ", fueBibliografica='" + fueBibliografica + '\'' +
                ", carConcat='" + carConcat + '\'' +
                ", conConcat='" + conConcat + '\'' +
                '}';
    }
}


