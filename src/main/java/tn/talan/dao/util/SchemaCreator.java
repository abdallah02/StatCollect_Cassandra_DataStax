package tn.talan.dao.util;

import com.datastax.driver.core.Session;

public class SchemaCreator {

    public static final String keySpace = "TalanKeySpace";
    public static final String tableStatCollectDevice = "stat_collect_device";
    public static final String tableStatCollectDailyDevice = "stat_collect_daily_device";
    public static final String tableStatCollectDailyDc = "stat_collect_daily_dc";
    public static final String tableStatCollectDailyGlobal = "stat_collect_daily_global";
    public static final String tableStatCollectDeviceByDate = "stat_Collect_device_by_date";
    public static final String tableStatCollectDailyDeviceByDate = "stat_Collect_daily_device_by_date";
    public static final String tableStatCollectDailyDcByDate = "stat_Collect_daily_dc_by_date";
    private static final int replicationFactior = 1;
    private static final String keySpaceStrategy = "SimpleStrategy";
    private DSConnectionProvider ds;
    private Session session;

    public void createSchema() {

        ds = DSConnectionProvider.getInstance();
        session = ds.getSession();
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + keySpace + " WITH replication " + "= {'class':'"
                + keySpaceStrategy + "', 'replication_factor':" + replicationFactior + "};");

        session.execute(
                "CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDevice + "(dc text," + "  device text,"
                        + "  timestamp_collect timestamp," + "  year_month_day_collect date," + "  task_Id text,"
                        + "  success int," + "  failures int," + "  successrate decimal," + "  duration int,"
                        + "  hopCount int," + "  PRIMARY KEY ((year_Month_Day_Collect,device),timestamp_Collect))"
                        + "WITH CLUSTERING ORDER BY (timestamp_Collect DESC)"
                        + "AND compaction = {'class': 'DateTieredCompactionStrategy'};");

        session.execute("CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDailyDevice + "(" + "dc text,"
                + "device text," + "date_Collect date," + "year_Month_Collect text," + "task_Id text,"
                + "min_Success_Rate decimal," + "mean_Success_Rate decimal," + "max_Success_Rate decimal,"
                + "min_Task_Duration int," + "mean_Task_Duration int," + "max_Task_Duration int," + "min_Hop int,"
                + "mean_Hop int," + "max_Hop int," + "PRIMARY KEY ((year_Month_Collect,device),date_Collect,dc,task_Id)"
                + ")" + "WITH CLUSTERING ORDER BY (date_Collect DESC)"
                + "AND compaction = {'class': 'DateTieredCompactionStrategy'};"

        );

        session.execute("CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDailyDc + "(" + "dc text,"
                + "device_Count int," + "date_Collect date," + "year_Month_Collect text," + "task_Id text,"
                + "full_Success_Rate decimal," + "partial_Success_Rate decimal," + "min_Task_Duration int,"
                + "mean_Task_Duration int," + "max_Task_Duration int," + "min_Hop int," + "mean_Hop int,"
                + "max_Hop int," + "  PRIMARY KEY ((year_Month_Collect,dc),date_Collect,task_Id)" + ")"
                + "WITH CLUSTERING ORDER BY (date_Collect DESC)"
                + "AND compaction = {'class': 'DateTieredCompactionStrategy'};"

        );

        session.execute("CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDailyGlobal + "("
                + "date_Collect date," + "year_Month_Collect text," + " dc_Count int," + "device_Count int,"
                + "task_Id text," + "full_Success_Rate decimal," + "partial_Success_Rate decimal,"
                + "min_Task_Duration int," + "mean_Task_Duration int," + "max_Task_Duration int," + "min_Hop int,"
                + "mean_Hop int," + "max_Hop int," + "    PRIMARY KEY (year_Month_Collect,date_Collect,task_Id)" + ")"
                + "WITH CLUSTERING ORDER BY (date_Collect DESC);"

        );

        session.execute("CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDeviceByDate + "(" + "dc text,"
                + "device text," + "timestamp_Collect timestamp," + "date_Collect date," + "task_Id text,"
                + "success int," + "failures int," + "success_Rate decimal," + "duration int," + "hop_Count int,"
                + "PRIMARY KEY ((date_Collect),timestamp_Collect,device))"

                + "WITH CLUSTERING ORDER BY (timestamp_Collect DESC)"
                + "AND compaction = {'class': 'DateTieredCompactionStrategy'};"

        );
        session.execute("CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDailyDeviceByDate + "("
                + "dc text," + "device text," + "date_Collect date," + "task_Id text," + "min_Success_Rate decimal,"
                + "mean_Success_Rate decimal," + "max_Success_Rate decimal," + "min_Task_Duration int,"
                + "mean_Task_Duration int," + "max_Task_Duration int," + "min_Hop int," + "mean_Hop int,"
                + "max_Hop int," + "PRIMARY KEY ((date_Collect),dc,device,task_Id)" + ")"
                + "WITH compaction = {'class': 'DateTieredCompactionStrategy'};");

        session.execute("CREATE TABLE IF NOT EXISTS " + keySpace + "." + tableStatCollectDailyDcByDate + "("
                + "dc text," + "device_Count int," + "date_Collect date," + "task_Id  text,"
                + "full_Success_Rate decimal," + "partial_Success_Rate decimal," + "min_Task_Duration int,"
                + "mean_Task_Duration int," + "max_Task_Duration int," + "min_Hop int," + "mean_Hop int,"
                + "max_Hop int," + "PRIMARY KEY ((date_Collect),dc,task_Id)"

                + ")" + "WITH compaction = {'class': 'DateTieredCompactionStrategy'};");
    }

}
