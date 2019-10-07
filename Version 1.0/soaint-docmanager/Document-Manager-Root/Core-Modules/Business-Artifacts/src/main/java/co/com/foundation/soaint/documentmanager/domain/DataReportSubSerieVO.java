package co.com.foundation.soaint.documentmanager.domain;

import java.util.List;

public class DataReportSubSerieVO {

    private List<DataTableSubSerieVO> rows;

    public DataReportSubSerieVO() {

    }

    public DataReportSubSerieVO(List<DataTableSubSerieVO> rows) {
        super();
        this.rows = rows;
    }

    public List<DataTableSubSerieVO> getRows() {
        return rows;
    }

    public void setRows(List<DataTableSubSerieVO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "DataReportSubSerieVO{" +
                "rows=" + rows +
                '}';
    }
}
