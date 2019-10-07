package co.com.foundation.soaint.documentmanager.domain;

public class DataTrdVO {

    private String codigo;
    private String instrumentos;
    private Long archivoGestion;
    private Long archivoCentral;
    private int disposicionFinal;
    private String procedimientos;
    private String soporte;
    private String confidencialidad;

    public DataTrdVO(String codigo, String instrumentos, Long archivoGestion, Long archivoCentral, int disposicionFinal,
                     String procedimientos, String soporte, String confidencialidad) {
        this.codigo = codigo;
        this.instrumentos = instrumentos;
        this.archivoGestion = archivoGestion;
        this.archivoCentral = archivoCentral;
        this.disposicionFinal = disposicionFinal;
        this.procedimientos = procedimientos;
        this.soporte = soporte;
        this.confidencialidad = confidencialidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getInstrumentos() {
        return instrumentos;
    }

    public Long getArchivoGestion() {
        return archivoGestion;
    }

    public Long getArchivoCentral() {
        return archivoCentral;
    }

    public int getDisposicionFinal() {
        return disposicionFinal;
    }

    public String getProcedimientos() {
        return procedimientos;
    }

    public String getSoporte() {
        return soporte;
    }

    public String getConfidencialidad() {
        return confidencialidad;
    }

    public void addInstrumento(String instrumentos) {
        this.instrumentos = this.instrumentos.concat(instrumentos);
    }

    public void addSoporte(String soporte) {
        this.soporte = this.soporte.concat(soporte);
    }

    @Override
    public String toString() {
        return "{" +
                "codigo='" + codigo + '\'' +
                ", instrumentos='" + instrumentos + '\'' +
                ", soporte='" + soporte + '\'' +
                ", confidencialidad='" + confidencialidad + '\'' +
                ", archivoGestion=" + archivoGestion +
                ", archivoCentral=" + archivoCentral +
                ", disposicionFinal=" + disposicionFinal +
                ", procedimientos='" + procedimientos + '\'' +
                '}';
    }
}
