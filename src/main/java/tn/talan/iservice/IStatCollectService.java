package tn.talan.iservice;

import java.time.LocalDate;

public interface IStatCollectService {

    /**
     * Add rows to the Postgresql stat_collect_device table according to the
     * parameters
     *
     * @param dc
     * @param device
     * @param taskId
     * @param nombreDevice
     */
    void addRandomValuesStatDevicePostgres(String dc, String device, String taskId, int nombreDevice);

    /**
     * Migrate data from the stat_collect_device Postgresql table to Cassandra
     */
    void migrateStatCollectDevice();

    /**
     * Executes SELECT query with the provided params Example
     * (dev_100,taskId_1h,LocalDate.Of(year,month,day)) Task_id is optional This
     * method return rows from stat_collect_device This method blocks until a
     * row has been insert to the database. However, for SELECT queries, it does
     * not guarantee that the result has been received in full. But it does
     * guarantee that some response has been received from the database,
     *
     * @param dev
     * @param taskId
     * @param date
     */
    void dispIisGetDevCollStats(String dev, String taskId, LocalDate date);

    /**
     * Insert the data of the table STAT_COLLECT_DEVICE [Cassandra] Group by
     * {dc, task, device} to STAT_COLLECT_DAILY_DEVICE [Cassandra] according to
     * the provided Date and asynch True asynch is to do the processing
     * asynchronously and synchronous with false
     *
     * @param date
     * @param asynch
     */
    void createAggregationInCollectDailyDeviceExec(LocalDate date, boolean asynch);

    /**
     * Executes SELECT query with the provided params Example
     * (dev_100,taskId_1h,LocalDate.Of(year,month,day)) Task_id is optional This
     * method return rows from stat_collect_daily_device This method blocks
     * until a row has been insert to the database. However, for SELECT queries,
     * it does not guarantee that the result has been received in full. But it
     * does guarantee that some response has been received from the database,
     *
     * @param dev_id
     * @param task_id
     * @param begin
     * @param end
     */

    void dispIisGetDevDailyCollStats(String dev_id, String task_id, LocalDate begin, LocalDate end);

    /**
     * Insert the data of the table STAT_COLLECT_DAILY_DEVICE [Cassandra] Group
     * by {dc, task} to STAT_COLLECT_DAILY_DC [Cassandra] according to the
     * provided Date and asynch True asynch is to do the processing
     * asynchronously and synchronous with false
     *
     * @param date
     * @param asynch
     */
    void createAggregationInCollectDailyDcExec(LocalDate date, boolean asynch);

    /**
     * Executes SELECT query with the provided params Example
     * (dc_1,taskId_1h,LocalDate.Of(year,month,day)) Task_id is optional This
     * method return rows from stat_collect_daily_device This method blocks
     * until a row has been insert to the database. However, for SELECT queries,
     * it does not guarantee that the result has been received in full. But it
     * does guarantee that some response has been received from the database,
     *
     * @param dc
     * @param taskId
     * @param begin
     * @param end
     */
    void dispIisGetDcDailyCollStats(String dc, String taskId, LocalDate begin, LocalDate end);

    /**
     * Insert the data of the table STAT_COLLECT_DAILY_DC [Cassandra] Group by
     * {task} to STAT_COLLECT_DAILY_GLOBAL [Cassandra] according to the provided
     * Date and asynch True asynch is to do the processing asynchronously and
     * synchronous with false
     *
     * @param date
     * @param asynch
     */
    void createAggregationInCollectDailyGlobalExec(LocalDate date, boolean asynch);

    /**
     * Executes SELECT query with the provided params Example
     * (taskId_1h,LocalDate.Of(year,month,day)) Task_id is optional This method
     * return rows from stat_collect_daily_device This method blocks until a row
     * has been insert to the database. However, for SELECT queries, it does not
     * guarantee that the result has been received in full. But it does
     * guarantee that some response has been received from the database,
     *
     * @param taskId
     * @param begin
     * @param end
     */
    void dispIisGetGlobalDailyCollStats(String taskId, LocalDate begin, LocalDate end);

}