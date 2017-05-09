package tn.talan.test;

import tn.talan.dao.util.DSConnectionProvider;
import tn.talan.dao.util.SchemaCreator;
import tn.talan.iservice.IStatCollectService;
import tn.talan.service.StatCollectService;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        DSConnectionProvider DataSourceConn = DSConnectionProvider.getInstance();

        SchemaCreator sc = new SchemaCreator();

        sc.createSchema();

        long beginTime = System.currentTimeMillis();

        IStatCollectService service = new StatCollectService();

        service.migrateStatCollectDevice();

        service.dispIisGetDevCollStats("dev100000", "task_1h", LocalDate.of(2016, 8, 23));

        service.createAggregationInCollectDailyDeviceExec(LocalDate.of(2016, 8, 23), false);

        service.dispIisGetDevDailyCollStats("dev100000", null, LocalDate.now(), LocalDate.now().plusDays(5));

        service.createAggregationInCollectDailyDcExec(LocalDate.now(), false);

        service.dispIisGetDcDailyCollStats("DC_0000004", "", LocalDate.now(), LocalDate.now().plusDays(5));

        service.createAggregationInCollectDailyGlobalExec(LocalDate.now(), false);

        service.dispIisGetGlobalDailyCollStats("task_1h", LocalDate.now(), LocalDate.now().plusDays(5));
        long endTime = System.currentTimeMillis();
        double timeConsume = (endTime - beginTime) / 1000.0;
        System.out.println(timeConsume + "s");
        DataSourceConn.close();
        System.exit(0);

    }
}