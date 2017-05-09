package tn.talan.idao.cassandra;

import tn.talan.entity.cassandra.StatCollectDailyDeviceByDateCEntity;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDailyDeviceByDateCDao {

    void add(StatCollectDailyDeviceByDateCEntity object);

    void addAsync(StatCollectDailyDeviceByDateCEntity object);

    List<StatCollectDailyDeviceByDateCEntity> displayByDate(LocalDate date);

}