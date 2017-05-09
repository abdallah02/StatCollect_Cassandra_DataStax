package tn.talan.dao.cassandra;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tn.talan.dao.util.DSConnectionProvider;
import tn.talan.dao.util.SchemaCreator;
import tn.talan.entity.cassandra.StatCollectDeviceCEntity;
import tn.talan.idao.cassandra.IStatCollectDeviceCDao;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.datastax.driver.mapping.Mapper.Option.ttl;

public class StatCollectDeviceCDao implements IStatCollectDeviceCDao {
    private static final Log log = LogFactory.getLog(StatCollectDeviceCDao.class);
    private final Mapper<StatCollectDeviceCEntity> mapper;
    private Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);

    public StatCollectDeviceCDao() {
        mapper = (Mapper<StatCollectDeviceCEntity>) manager
                .mapper(StatCollectDeviceCEntity.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceCDao#
     * insertStatCollectDeviceStatementQueryBuilder(entitycassandra.
     * StatCollectDeviceCEntity)
     */
    @Override
    public void insertStatCollectDeviceStatementQueryBuilder(StatCollectDeviceCEntity Object) {
        session = DSConnectionProvider.getInstance().getSession();
        try {
            Statement statement1 = QueryBuilder.insertInto(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDevice)
                    .value("dc", Object.getDc()).value("device", Object.getDevice())
                    .value("task_id", Object.getTaskId()).value("timestamp_collect", Object.getDate_collect())
                    .value("year_month_day_collect", Object.getYearmonthdaycollect())
                    .value("success", Object.getSuccess()).value("failures", Object.getFailures())
                    .value("successrate", Object.getSuccessRate()).value("duration", Object.getDuration())
                    .value("hopcount", Object.getHopCount());

            session.execute(statement1);
            log.debug("persiste effectuer ");

        } catch (Exception e) {
            log.debug("persiste failed ", e);

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * daocassandra.IStatCollectDeviceCDao#insertStatCollectDeviceStatementBatch
     * (java.util.List)
     */
    @Override
    public void insertStatCollectDeviceStatementBatch(List<StatCollectDeviceCEntity> objects) {
        session = DSConnectionProvider.getInstance().getSession();
        BatchStatement batch = new BatchStatement();

        try {

            objects.forEach(Object -> {

                batch.add(mapper.saveQuery(Object));
                if (batch.size() % 300 == 0)
                    System.out.println(batch.size());
                session.execute(batch);
                batch.clear();

            });

            if (batch.size() > 0)
                session.execute(batch);
            log.debug("batch persiste effectuer ");

        } catch (Exception e) {
            log.debug("batch persiste failed ", e);

        }

    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceCDao#add(entitycassandra.
     * StatCollectDeviceCEntity)
     */
    @Override
    public void add(StatCollectDeviceCEntity Object) {
        try {
            mapper.save(Object);
            log.debug("persist successful");
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("persist failed", e);

            throw e;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceCDao#add(entitycassandra.
     * StatCollectDeviceCEntity, int)
     */
    @Override
    public void add(StatCollectDeviceCEntity object, int ttltime) {
        try {
            mapper.save(object, ttl(ttltime));
            log.debug("persist successful");
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("persist failed", e);

            throw e;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceCDao#addAsync(entitycassandra.
     * StatCollectDeviceCEntity)
     */
    @Override
    public ListenableFuture<?> addAsync(StatCollectDeviceCEntity object) {
        try {
            ListenableFuture<?> future = mapper.saveAsync(object);

            log.debug("persist executed");
            return future;
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("persist failed", e);

            throw e;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * daocassandra.IStatCollectDeviceCDao#iISGetDevCollStats(java.lang.String,
     * java.lang.String, java.time.LocalDate)
     */
    @Override
    public List<StatCollectDeviceCEntity> iISGetDevCollStats(String dev_id, String task_id, LocalDate date) {
        List<StatCollectDeviceCEntity> cl;

        log.debug("getting StatCollectDeviceCEntity instance with dev_id: " + dev_id + ",taskId: " + task_id + ",date: "
                + date);

        try {

            cl = mapper.map(session.execute(

                    "Select * from " + SchemaCreator.keySpace + "." + SchemaCreator.tableStatCollectDevice
                            + "  where device='" + dev_id + "' and year_month_day_collect='" + date.toString() + "'"))
                    .all();

            if (StringUtils.isNotEmpty(task_id)) {
                cl = cl.stream().filter(x -> x.getTaskId().equals(task_id)).collect(Collectors.toList());
            }

            log.debug("find by dev,task,date successful, result size: " + cl.size());

        } catch (RuntimeException e) {

            log.error("find by dev,task,date failed " + e);

            throw e;
        }

        return cl;
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceCDao#displayAll()
     */
    @Override
    public List<StatCollectDeviceCEntity> displayAll() {
        List<StatCollectDeviceCEntity> cl = null;

        log.debug("getting all StatCollectDeviceCEntity ");

        try {

            ResultSetFuture future = session.executeAsync(
                    "Select * from " + SchemaCreator.keySpace + "." + SchemaCreator.tableStatCollectDevice);

            while (!future.isDone())
                log.debug("Waiting for request to complete");

            cl = mapper.map(future.get()).all();
            System.out.println(cl.size());

            log.debug("find all entities successful, result size: " + cl.size());

        } catch (RuntimeException e) {

            log.error("find all entities failed", e);

            throw e;
        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }

        return cl;
    }

}