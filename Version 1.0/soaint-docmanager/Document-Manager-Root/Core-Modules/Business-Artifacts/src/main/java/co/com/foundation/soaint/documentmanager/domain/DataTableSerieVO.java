package co.com.foundation.soaint.documentmanager.domain;

public class DataTableSerieVO {

    private String codSerie;
    private String nomSerie;
    private String actAdministrativo;
    private String nomMotivo;
    private String estSerie;
    private String carConcat;
    private String conConcat;

    public DataTableSerieVO(String codSerie, String nomSerie, String actAdministrativo, String nomMotivo, int estSerie,
                            String carAdministrativa, String carLegal, String carContable, String carJuridica, String carTecnica,
                            String conPublica, String conClasificada, String conReservada) {
        this.codSerie = codSerie;
        this.nomSerie = nomSerie;
        this.actAdministrativo = actAdministrativo;
        this.nomMotivo = nomMotivo;


        concatCaracteristicas(carAdministrativa, carLegal, carContable, carJuridica, carTecnica);
        concatConfidencialidad(conPublica, conClasificada, conReservada);
        convetEstToString(estSerie);

    }

    private void convetEstToString(int estado){

        if (estado == 1){

            estSerie = "Activo";

        }else {

            estSerie = "Inactivo";
        }



    }

    private void concatCaracteristicas (String carAdministrativa, String carLegal, String carContable, String carJuridica, String carTecnica){

        carConcat = "";

        if (carAdministrativa == "on") {
            carConcat = "A";
        }
        if (carContable == "on"){
            carConcat += carConcat.length() == 0 ? "C" : ", C";
        }
        if (carJuridica == "on"){
            carConcat += carConcat.length() == 0 ? "J" : ", J";
        }
        if (carLegal == "on"){
            carConcat += carConcat.length() == 0 ? "L" : ", L";
        }
        if (carTecnica == "on"){
            carConcat += carConcat.length() == 0 ? "T" : ", T";
        }
    }

    private void concatConfidencialidad (String conPublica, String conClasificada, String conReservada){

        conConcat = "";

        if (conClasificada == "on"){
            conConcat = "C";
        }
        if (conPublica == "on"){
            conConcat += conConcat.length() == 0 ? "P" : ", P";

        }
        if (conReservada == "on"){
            conConcat += conConcat.length() == 0 ? "R" : ", R";
        }
    }

    public String getCodSerie() {
        return codSerie;
    }

    public void setCodSerie(String codSerie) {
        this.codSerie = codSerie;
    }

    public String getNomSerie() {
        return nomSerie;
    }

    public void setNomSerie(String nomSerie) {
        this.nomSerie = nomSerie;
    }

    public String getActAdministrativo() {
        return actAdministrativo;
    }

    public void setActAdministrativo(String actAdministrativo) {
        this.actAdministrativo = actAdministrativo;
    }

    public String getNomMotivo() {
        return nomMotivo;
    }

    public void setNomMotivo(String nomMotivo) {
        this.nomMotivo = nomMotivo;
    }

    public String getEstSerie() {
        return estSerie;
    }

    public void setEstSerie(String estSerie) {
        this.estSerie = estSerie;
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
        return "DataTableSerieVO{" +
                "codSerie='" + codSerie + '\'' +
                ", nomSerie='" + nomSerie + '\'' +
                ", actAdministrativo='" + actAdministrativo + '\'' +
                ", nomMotivo='" + nomMotivo + '\'' +
                ", estSerie='" + estSerie + '\'' +
                ", carConcat='" + carConcat + '\'' +
                ", conConcat='" + conConcat + '\'' +
                '}';
    }
}

