package Xeia.Data;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
    @Bean
    public DataSource userSource() {
        DataSourceBuilder dsb = DataSourceBuilder.create();
        dsb.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dsb.url("jdbc:sqlserver://xeiastore.database.windows.net:1433;database=Xeia Data;user=ReinaSparklechu@xeiastore;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        dsb.username("ReinaSparklechu");
        dsb.password("!Nfryd12345");
        return dsb.build();
    }
}
