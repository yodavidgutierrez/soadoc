package co.com.soaint.correspondencia.hibernate.dialect;

import org.hibernate.dialect.PostgreSQL95Dialect;

import java.sql.Types;

public class SgdPostgreDialect extends PostgreSQL95Dialect {

    public SgdPostgreDialect() {
        this.registerColumnType(Types.NUMERIC, "numeric(19,0)");
        this.registerColumnType(Types.VARCHAR, "TEXT");
        this.registerColumnType(Types.BLOB, "bytea");
    }
}
