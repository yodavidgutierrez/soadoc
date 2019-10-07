package co.com.foundation.soaint.documentmanager.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cgutierrez on 08/10/2018.
 */
public class DataReportTipoDocVO {

    private List<DataTableTipDocVO> rows;
    public DataReportTipoDocVO() {
    }

    public DataReportTipoDocVO(List<DataTableTipDocVO> rows) {
        super();
        this.rows = rows;
    }

    public List<DataTableTipDocVO> getRows() {
        return rows;
    }

    public void setRows(List<DataTableTipDocVO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "DataReportTipDocVO{" +
                ", rows=" + rows +
                '}';
    }
}
