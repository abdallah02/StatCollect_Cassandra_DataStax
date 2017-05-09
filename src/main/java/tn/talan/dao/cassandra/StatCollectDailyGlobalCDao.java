package tn.talan.dao.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tn.talan.dao.util.DSConnectionProvider;
import tn.talan.dao.util.SchemaCreator;
import tn.talan.entity.cassandra.StatCollectDailyDcByDateCEntity;
import tn.talan.entity.cassandra.StatCollectDailyGlobalCEntity;
import tn.talan.exception.DateBeginEndError;
import tn.talan.idao.cassandra.IStatCollectDailyGlobalCDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static com.datastax.driver.mapping.Mapper.Option.ttl;

public class StatCollectDailyGlobalCDao implements IStatCollectDailyGlobalCDao {

    private static final Log log = LogFactory.getLog(StatCollectDailyGlobalCDao.class);
    private final Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);
    private final Mapper<StatCollectDailyGlobalCEntity> mapper;

    public StatCollectDailyGlobalCDao() {
        mapper = (Mapper<StatCollectDailyGlobalCEntity>) manager
                .mapper(StatCollectDailyGlobalCEntity.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyGlobalCDao#add(entitycassandra.
     * StatCollectDailyGlobalCEntity)
     */
    @Override
    public void add(StatCollectDailyGlobalCEntity Object) {
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
     * @see daocassandra.IStatCollectDailyGlobalCDao#addWithTTL(entitycassandra.
     * StatCollectDailyGlobalCEntity, int)
     */
    @Override
    public void addWithTTL(StatCollectDailyGlobalCEntity Object, int tempExpiration) {
        try {
            mapper.save(Object, ttl(tempExpiration));
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
     * @see daocassandra.IStatCollectDailyGlobalCDao#addAsync(entitycassandra.
     * StatCollectDailyGlobalCEntity)
     */
    @Override
    public void addAsync(StatCollectDailyGlobalCEntity Object) {
        try {
            mapper.saveAsync(Object);

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
     * @see
     * daocassandra.IStatCollectDailyGlobalCDao#iIs_Get_Global_Daily_Stats(java.
     * lang.String, java.time.LocalDate, java.time.LocalDate)
     */
    @Override
    public List<StatCollectDailyGlobalCEntity> iIs_Get_Global_Daily_Stats(String task_id, LocalDate begin,
                                                                          LocalDate end) throws DateBeginEndError {
        List<StatCollectDailyGlobalCEntity> cl;
        log.debug("getting StatCollectDailyGlobalCEntity instance taskId: " + task_id + ",date-min: " + begin
                + ",date-max" + end);

        if (!(begin.getMonthValue() == end.getMonthValue()) || !(begin.getYear() == end.getYear())) {
            log.error(
                    "getting StatCollectDailyGlobalCEntity error cause begin :year/month different to end :year/month");
            throw new DateBeginEndError("l'année et le mois des deux (date début et date fin) sont différents ");
        }
        try {

            String month = begin.getYear() + "-" + begin.getMonthValue();
            Statement statement1 = QueryBuilder.select().all()
                    .from(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDailyGlobal)

                    .where(QueryBuilder.eq("year_Month_Collect", month))
                    .and(QueryBuilder.gte("date_collect",
                            com.datastax.driver.core.LocalDate.fromYearMonthDay(begin.getYear(), begin.getMonthValue(),
                                    begin.getDayOfMonth())))
                    .and(QueryBuilder.lte("date_collect", com.datastax.driver.core.LocalDate
                            .fromYearMonthDay(end.getYear(), end.getMonthValue(), end.getDayOfMonth())));

            ResultSet resultSet = session.execute(statement1);

            cl = mapper.map(resultSet).all();

            if (!(task_id == null) && (!task_id.isEmpty())) {
                cl = cl.stream().filter(x -> x.getTaskId().equals(task_id)).collect(Collectors.toList());
            }
            log.debug("getting StatCollectDailyDcCEntity success with size:" + cl.size());
        } catch (RuntimeException e) {

            log.error("getting StatCollectDailyDcCEntity failed");

            throw e;
        }

        return cl;
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see daocassandra.IStatCollectDailyGlobalCDao#
	 * createAggregationInCollectGlobalDc(java.util.List)
	 */
    // TODO out of memory error + date is not correct

    @Override
    public void createAggregationInCollectGlobalDc(List<StatCollectDailyDcByDateCEntity> tableStatCollectDevice,
                                                   boolean asynch) {

        // Attention les opérations de stream sont excuter en multi threads
        // au cas ou vous voulez les rendrent en un seul thread changer
        // [parallelStream] en [stream]

        // groupement par {task}

        // [StatCollectDailyDcCEntity]
        tableStatCollectDevice.parallelStream().collect(Collectors.groupingBy(StatCollectDailyDcByDateCEntity::getTaskId))

                // parcourir Map<String[groupe de {task},
                // List<StatCollectDailyDeviceCEntity>>
                // output
                .forEach((m, y) -> {
                    StatCollectDailyGlobalCEntity entityDaily = new StatCollectDailyGlobalCEntity();

                    if (!m.isEmpty())
                        entityDaily.setTaskId(m);

                    // Calculer min,avg,max(TaskDuration) depuis la liste de
                    // chaque groupe
                    OptionalInt minTaskDuration = y.parallelStream()
                            .mapToInt(StatCollectDailyDcByDateCEntity::getMeanTaskDuration).min();
                    OptionalDouble meanTaskDuration = y.parallelStream()
                            .mapToInt(StatCollectDailyDcByDateCEntity::getMeanTaskDuration).average();
                    OptionalInt maxTaskDuration = y.parallelStream()
                            .mapToInt(StatCollectDailyDcByDateCEntity::getMaxTaskDuration).max();
                    if (minTaskDuration.isPresent())
                        entityDaily.setMinTaskDuration(minTaskDuration.getAsInt());
                    if (meanTaskDuration.isPresent())
                        entityDaily.setMeanTaskDuration((int) Math.round(meanTaskDuration.getAsDouble()));
                    if (maxTaskDuration.isPresent())
                        entityDaily.setMaxTaskDuration(maxTaskDuration.getAsInt());
                    // Calculer min,avg,max(Hop) depuis la liste de chaque
                    // groupe

                    OptionalInt minHop = y.parallelStream().mapToInt(StatCollectDailyDcByDateCEntity::getMinHop).min();
                    OptionalDouble meanHop = y.parallelStream().mapToInt(StatCollectDailyDcByDateCEntity::getMeanHop).average();
                    OptionalInt maxHop = y.parallelStream().mapToInt(StatCollectDailyDcByDateCEntity::getMaxHop).max();
                    if (minHop.isPresent())
                        entityDaily.setMinHop(minHop.getAsInt());
                    if (meanHop.isPresent())
                        entityDaily.setMeanHop((int) Math.round(meanHop.getAsDouble()));
                    if (maxHop.isPresent())
                        entityDaily.setMaxHop(maxHop.getAsInt());

                    // Calculer sum(count_device) && sum(count_dc) depuis la
                    // liste de chaque groupe

                    LongAdder a = new LongAdder();
                    y.parallelStream().mapToInt(StatCollectDailyDcByDateCEntity::getDeviceCount).forEach(a::add);
                    int countDevice = a.intValue();
                    entityDaily.setDeviceCount(countDevice);
                    a.reset();
                    y.parallelStream().map(StatCollectDailyDcByDateCEntity::getDc).forEach(x -> a.add(1));
                    countDevice = a.intValue();
                    entityDaily.setDcCount(countDevice);
                    // remplissement des deux valeurs (partial_success_rate et
                    // full_success_rate)
                    entityDaily.setPartialSuccessRate(BigDecimal.ONE);
                    entityDaily.setFullSuccessRate(BigDecimal.TEN);

                    // !!! DATE COLLECT !!!
                    // date actuel
                    // TODO date value
                    entityDaily.setDateCollect(
                            com.datastax.driver.core.LocalDate.fromYearMonthDay(LocalDate.now().getYear(),
                                    LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));
                    entityDaily.setYearmonthcollect(LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue());

                    // fixé les clés primaires composites
                    // Insertion dans la base de donnée
                    try {
                        if (!asynch)
                            mapper.save(entityDaily);
                        else
                            mapper.saveAsync(entityDaily);
                        log.debug("persist entityDailyDc success");

                    } catch (RuntimeException e) {

                        log.error("persist entityDailyDc failed", e);
                        throw e;
                    }
                });
    }

}