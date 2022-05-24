package com.mb.lab.banks.user.util.persistence;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MariaDB103FtsDialect extends MariaDB103Dialect {

    public MariaDB103FtsDialect() {
        super();
        registerFunction("MATCH_AGAINST", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "MATCH(?1) AGAINST(?2 IN BOOLEAN MODE)"));
        registerFunction("MATCH_AGAINST2", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "MATCH(?1,?2) AGAINST(?3 IN BOOLEAN MODE)"));
        registerFunction("MATCH_AGAINST3", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "MATCH(?1,?2,?3) AGAINST(?4 IN BOOLEAN MODE)"));
        registerFunction("MATCH_AGAINST4", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "MATCH(?1,?2,?3,?4) AGAINST(?5 IN BOOLEAN MODE)"));
    }
    
}
