package tn.talan.idao.cassandra;

import tn.talan.entity.cassandra.StatCollectDeviceByDateCEntity;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDeviceByDateCDao {

    void add(StatCollectDeviceByDateCEntity object);

    void addAsynch(StatCollectDeviceByDateCEntity object);

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceByDateCDao#displayByDate(java.time.
     * LocalDate)
     */
    List<StatCollectDeviceByDateCEntity> displayByDate(LocalDate date);

}