package tn.talan.idao.cassandra;

import tn.talan.entity.cassandra.StatCollectDailyDcCEntity;
import tn.talan.entity.cassandra.StatCollectDailyDeviceByDateCEntity;
import tn.talan.exception.DateBeginEndError;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDailyDcCDao {

    void add(StatCollectDailyDcCEntity object);

    void addAsync(StatCollectDailyDcCEntity object);

    /**
     * Executes SELECT query with the provided params example
     * (dc_100,taskId_1h,LocalDate.Of(year,month,day)) task_id is optional This
     * method return rows from stat_collect_daily_dc This method blocks until a
     * row has been insert to the database. However, for SELECT queries, it does
     * not guarantee that the result has been received in full. But it does
     * guarantee that some response has been received from the database,
     *
     * @param dc
     * @param task_id
     * @param begin
     * @param end
     * @return
     * @throws DateBeginEndError
     */

    List<StatCollectDailyDcCEntity> iISGetDDailyCollStats(String dc, String task_id, LocalDate begin, LocalDate end)
            throws DateBeginEndError;

    /**
     * This method
     *
     * @param tableStatCollectDeviceByDate
     * @param Asynch
     */
    void createAggregationInCollectDailyDc(List<StatCollectDailyDeviceByDateCEntity> tableStatCollectDeviceByDate,
                                           boolean Asynch);

}