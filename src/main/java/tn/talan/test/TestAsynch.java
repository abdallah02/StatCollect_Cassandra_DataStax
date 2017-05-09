package tn.talan.test;

import tn.talan.dao.util.DSConnectionProvider;
import tn.talan.dao.util.SchemaCreator;
import tn.talan.iservice.IStatCollectService;
import tn.talan.service.StatCollectService;

import java.time.LocalDate;

public class TestAsynch {
    public static void main(String[] args) {
        DSConnectionProvider DataSourceConn = DSConnectionProvider.getInstance();

        SchemaCreator sc = new SchemaCreator();

        sc.createSchema();

        long beginTime = System.currentTimeMillis();

        IStatCollectService service = new StatCollectService();

        service.migrateStatCollectDevice();

        service.dispIisGetDevCollStats("dev100000", "task_1h", LocalDate.of(2016, 8, 23));

        service.createAggregationInCollectDailyDeviceExec(LocalDate.of(2016, 8, 23), true);

        service.dispIisGetDevDailyCollStats("dev100000", null, LocalDate.now(), LocalDate.now().plusDays(5));

        service.createAggregationInCollectDailyDcExec(LocalDate.now(), true);

        service.dispIisGetDcDailyCollStats("DC_0000004", "", LocalDate.now(), LocalDate.now().plusDays(5));

        service.createAggregationInCollectDailyGlobalExec(LocalDate.now(), true);

        service.dispIisGetGlobalDailyCollStats("task_1h", LocalDate.now(), LocalDate.now().plusDays(5));
        long endTime = System.currentTimeMillis();
        double timeConsume = (endTime - beginTime) / 1000.0;
        System.out.println(timeConsume + "s");
        DataSourceConn.close();
        System.exit(0);

    }
}
