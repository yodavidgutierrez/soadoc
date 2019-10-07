package co.com.foundation.soaint.documentmanager.domain;

public class DataTableTipDocVO {
    private String nomTpgDoc;
    private String estTpgDoc;
    private String nomTraDocumental;
    private String nomSoport;
    private String carConcat;

    public DataTableTipDocVO(String nomTpgDoc, int estTpgDoc, String nomTraDocumental, String nomSoport,
                             String carAdministrativa, String carLegal, String carTecnica, String carJuridico,
                             String carContable) {
        this.nomTpgDoc = nomTpgDoc;
        this.nomTraDocumental = nomTraDocumental;
        this.nomSoport = nomSoport;
        concatCaracteristicas(carAdministrativa,carLegal,carTecnica,carJuridico,carContable);
        convetEstToString(estTpgDoc);

    }

    private void concatCaracteristicas(String carAdministrativa, String carLegal, String carTecnica, String carJuridico,String carContable){

        carConcat = "";
        if (carAdministrativa == "on") {
            carConcat = "A";
        }
        if (carTecnica == "on"){
            carConcat += carConcat.length() == 0 ? "T" : ", T";
        }
        if (carLegal == "on"){
            carConcat += carConcat.length() == 0 ? "L" : ", L";
        }
        if (carJuridico == "on"){
            carConcat += carConcat.length() == 0 ? "J" : ", J";
        }
        if (carContable == "on"){
            carConcat += carConcat.length() == 0 ? "C" : ", C";
        }
    }

    private void convetEstToString(int estado){
        if (estado == 1){
            estTpgDoc = "Activo";
        }else {
            estTpgDoc = "Inactivo";
        }
    }

    public String getNomTpgDoc() {
        return nomTpgDoc;
    }

    public void setNomTpgDoc(String nomTpgDoc) {
        this.nomTpgDoc = nomTpgDoc;
    }

    public String getEstTpgDoc() {
        return estTpgDoc;
    }

    public void setEstTpgDoc(String estTpgDoc) {
        this.estTpgDoc = estTpgDoc;
    }

    public String getNomTraDocumental() {
        return nomTraDocumental;
    }

    public void setNomTraDocumental(String nomTraDocumental) {
        this.nomTraDocumental = nomTraDocumental;
    }

    public String getNomSoport() {
        return nomSoport;
    }

    public void setNomSoport(String nomSoport) {
        this.nomSoport = nomSoport;
    }


    public String getCarConcat() {
        return carConcat;
    }

    public void setCarConcat(String carConcat) {
        this.carConcat = carConcat;
    }


    @Override
    public String toString() {
        return "DataTableTipDocVO{" +
                "nomTpgDoc='" + nomTpgDoc + '\'' +
                ", estTpgDoc='" + estTpgDoc + '\'' +
                ", nomTraDocumental='" + nomTraDocumental + '\'' +
                ", nomSoport='" + nomSoport + '\'' +
                ", carConcat='" + carConcat + '\'' +
                '}';
    }
}
