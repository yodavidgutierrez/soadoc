package co.com.foundation.soaint.documentmanager.persistence.orm.custom;

import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * Created by jrodriguez on 29/11/2016.
 */
public class SQLServer2008DialectWithNvarchar extends SQLServer2008Dialect {
    public SQLServer2008DialectWithNvarchar() {
        super();
        registerColumnType(Types.NCLOB, "nvarchar(MAX)");
        registerColumnType(Types.LONGNVARCHAR, "nvarchar(MAX)");
        registerColumnType(Types.NVARCHAR, "nvarchar(MAX)");
        registerColumnType(Types.NVARCHAR, 4000, "nvarchar($1)");
        registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
        registerHibernateType(Types.NCHAR, 1, StandardBasicTypes.CHARACTER.getName());
        registerHibernateType(Types.NCHAR, 255, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName());
    }
}