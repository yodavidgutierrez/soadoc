package co.com.foundation.soaint.documentmanager.domain;

import java.util.List;

public class DataReportSeriesVO {

    private List<DataTableSerieVO> rows;

    public DataReportSeriesVO() {
    }

    public DataReportSeriesVO(List<DataTableSerieVO> rows) {

        super();
        this.rows = rows;
    }

    public List<DataTableSerieVO> getRows() {
        return rows;
    }

    public void setRows(List<DataTableSerieVO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "DataReportSeriesVO{" +
                "rows=" + rows +
                '}';
    }
}
