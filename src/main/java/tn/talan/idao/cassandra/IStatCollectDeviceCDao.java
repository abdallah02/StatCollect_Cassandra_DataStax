package tn.talan.idao.cassandra;

import com.google.common.util.concurrent.ListenableFuture;
import tn.talan.entity.cassandra.StatCollectDeviceCEntity;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDeviceCDao {

    void insertStatCollectDeviceStatementQueryBuilder(StatCollectDeviceCEntity Object);

    void insertStatCollectDeviceStatementBatch(List<StatCollectDeviceCEntity> objects);

    void add(StatCollectDeviceCEntity Object);

    void add(StatCollectDeviceCEntity object, int ttltime);

    ListenableFuture<?> addAsync(StatCollectDeviceCEntity object);

    List<StatCollectDeviceCEntity> iISGetDevCollStats(String dev_id, String task_id, LocalDate date);

    List<StatCollectDeviceCEntity> displayAll();

}