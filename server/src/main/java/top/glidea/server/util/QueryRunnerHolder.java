package top.glidea.server.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import top.glidea.server.config.Config;

public class QueryRunnerHolder {
    private static final QueryRunner QUERY_RUNNER;
    private static final HikariDataSource DATA_SOURCE;
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.get("url"));
        config.setUsername(Config.get("username"));
        config.setPassword(Config.get("password"));
        // TODO Hikari参数调优
        DATA_SOURCE = new HikariDataSource(config);
        QUERY_RUNNER = new QueryRunner(DATA_SOURCE);
    }

    public static QueryRunner get() {
        return QUERY_RUNNER;
    }
}
