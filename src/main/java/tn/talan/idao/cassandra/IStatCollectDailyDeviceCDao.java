package tn.talan.idao.cassandra;

import tn.talan.entity.cassandra.StatCollectDailyDeviceCEntity;
import tn.talan.entity.cassandra.StatCollectDeviceByDateCEntity;
import tn.talan.exception.DateBeginEndError;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDailyDeviceCDao {

    void add(StatCollectDailyDeviceCEntity object);

    void addAsynch(StatCollectDailyDeviceCEntity object);

    List<StatCollectDailyDeviceCEntity> iISGetDevDailyCollStats(String dev_id, String task_id, LocalDate begin,
                                                                LocalDate end) throws DateBeginEndError;

    void createAggregationInCollectDailyDevice(List<StatCollectDeviceByDateCEntity> tableStatCollectDeviceByDate,
                                               boolean Asynch);

}