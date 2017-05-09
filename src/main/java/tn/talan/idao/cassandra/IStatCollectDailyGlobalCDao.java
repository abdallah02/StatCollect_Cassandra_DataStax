package tn.talan.idao.cassandra;

import tn.talan.entity.cassandra.StatCollectDailyDcByDateCEntity;
import tn.talan.entity.cassandra.StatCollectDailyGlobalCEntity;
import tn.talan.exception.DateBeginEndError;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDailyGlobalCDao {

    void add(StatCollectDailyGlobalCEntity Object);

    void addWithTTL(StatCollectDailyGlobalCEntity Object, int tempExpiration);

    void addAsync(StatCollectDailyGlobalCEntity Object);

    List<StatCollectDailyGlobalCEntity> iIs_Get_Global_Daily_Stats(String task_id, LocalDate begin, LocalDate end)
            throws DateBeginEndError;

    void createAggregationInCollectGlobalDc(List<StatCollectDailyDcByDateCEntity> tableStatCollectDevice,
                                            boolean Asynch);

}