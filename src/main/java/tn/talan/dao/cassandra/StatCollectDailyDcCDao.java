package tn.talan.dao.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tn.talan.dao.util.DSConnectionProvider;
import tn.talan.dao.util.SchemaCreator;
import tn.talan.entity.cassandra.StatCollectDailyDcByDateCEntity;
import tn.talan.entity.cassandra.StatCollectDailyDcCEntity;
import tn.talan.entity.cassandra.StatCollectDailyDeviceByDateCEntity;
import tn.talan.exception.DateBeginEndError;
import tn.talan.idao.cassandra.IStatCollectDailyDcCDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatCollectDailyDcCDao implements IStatCollectDailyDcCDao {

    private static final Log log = LogFactory.getLog(StatCollectDailyDcCDao.class);
    private final Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);
    private final Mapper<StatCollectDailyDcCEntity> mapper;
    private final Mapper<StatCollectDailyDcByDateCEntity> mapperDailyDcByDate;

    public StatCollectDailyDcCDao() {
        mapper = (Mapper<StatCollectDailyDcCEntity>) manager
                .mapper(StatCollectDailyDcCEntity.class);
        mapperDailyDcByDate = (Mapper<StatCollectDailyDcByDateCEntity>) manager
                .mapper(StatCollectDailyDcByDateCEntity.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyDcCDao#add(entitycassandra.
     * StatCollectDailyDcCEntity)
     */
    @Override
    public void add(StatCollectDailyDcCEntity object) {
        try {

            mapper.save(object);
            log.debug("persist successful");

        } catch (RuntimeException e) {

            log.error("persist failed", e);

            throw e;

        }

    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyDcCDao#addAsync(entitycassandra.
     * StatCollectDailyDcCEntity)
     */
    @Override
    public void addAsync(StatCollectDailyDcCEntity object) {
        try {

            mapper.saveAsync(object);
            log.debug("persist successful");

        } catch (RuntimeException e) {

            log.error("persist failed", e);

            throw e;

        }

    }

	/*
     * (non-Javadoc)
	 * 
	 * @see
	 * daocassandra.IStatCollectDailyDcCDao#iISGetDDailyCollStats(java.lang.
	 * String, java.lang.String, java.time.LocalDate, java.time.LocalDate)
	 */

    /*
     * (non-Javadoc)
     *
     * @see
     * daocassandra.IStatCollectDailyDcCDao#iISGetDDailyCollStats(java.lang.
     * String, java.lang.String, java.time.LocalDate, java.time.LocalDate)
     */
    @Override
    public List<StatCollectDailyDcCEntity> iISGetDDailyCollStats(String dc, String task_id, LocalDate begin,
                                                                 LocalDate end) throws DateBeginEndError {
        List<StatCollectDailyDcCEntity> cl;
        log.debug("getting StatCollectDailyDcCEntity instance with dc: " + dc + ",taskId: " + task_id + ",date-min: "
                + begin + ",date-max" + end);

        if (!(begin.getMonthValue() == end.getMonthValue()) || !(begin.getYear() == end.getYear())) {
            log.error("getting StatCollectDailyDcCEntity error cause begin :year/month different to end :year/month");
            throw new DateBeginEndError("l'année et le mois des deux (date début et date fin) sont différents ");
        }
        try {

            String month = begin.getYear() + "-" + begin.getMonthValue();

            Statement statement1 = QueryBuilder.select().all()
                    .from(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDailyDc)

                    .where(QueryBuilder.eq("dc", dc)).and(QueryBuilder.eq("year_Month_Collect", month))
                    .and(QueryBuilder.gte("date_collect",
                            com.datastax.driver.core.LocalDate.fromYearMonthDay(begin.getYear(), begin.getMonthValue(),
                                    begin.getDayOfMonth())))
                    .and(QueryBuilder.lte("date_collect", com.datastax.driver.core.LocalDate
                            .fromYearMonthDay(end.getYear(), end.getMonthValue(), end.getDayOfMonth())));

            ResultSet resultSet = session.execute(statement1);
            cl = mapper.map(resultSet).all();

            if (StringUtils.isNotEmpty(task_id)) {
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
	 * @see
	 * daocassandra.IStatCollectDailyDcCDao#createAggregationInCollectDailyDc(
	 * java.util.List)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * daocassandra.IStatCollectDailyDcCDao#createAggregationInCollectDailyDc(
	 * java.util.List)
	 */
    // TODO out of memory error + date is not correct

    @Override
    public void createAggregationInCollectDailyDc(
            List<StatCollectDailyDeviceByDateCEntity> tableStatCollectDeviceByDate, boolean asynch) {

        Map<Object, List<StatCollectDailyDeviceByDateCEntity>> mapObjList;

        // Attention les opérations de stream sont excuter en multi threads
        // au cas ou vous voulez les rendrent en un seul thread changer
        // [parallelStream] en [stream]

        // groupement par {task,dc}

        mapObjList = tableStatCollectDeviceByDate.parallelStream()
                .collect(Collectors.groupingBy(dtc -> Arrays.asList(dtc.getTaskId(), dtc.getDc())));

        // parcourir Map<Object[groupe de {dc,task},
        // List<StatCollectDailyDeviceCEntity>>
        // output
        mapObjList.forEach((m, y) -> {
            StatCollectDailyDcCEntity entityDaily = new StatCollectDailyDcCEntity();

            List<String> groupByValues = new ArrayList<>();
            groupByValues = (List<String>) m;

            // remplir l'entité dailydc par {dc,task}
            if (StringUtils.isNotEmpty(groupByValues.get(0)))
                entityDaily.setTaskId(groupByValues.get(0));

            if (StringUtils.isNotEmpty(groupByValues.get(1)))
                entityDaily.setDc(groupByValues.get(1));

            // Calculer min,avg,max(TaskDuration) depuis la liste de chaque
            // groupe
            OptionalInt minTaskDuration = y.parallelStream().mapToInt(StatCollectDailyDeviceByDateCEntity::getMinTaskDuration)
                    .min();
            OptionalDouble meanTaskDuration = y.parallelStream()
                    .mapToInt(StatCollectDailyDeviceByDateCEntity::getMeanTaskDuration).average();
            OptionalInt maxTaskDuration = y.parallelStream().mapToInt(StatCollectDailyDeviceByDateCEntity::getMaxTaskDuration).
                    max();
            if (minTaskDuration.isPresent())
                entityDaily.setMinTaskDuration(minTaskDuration.getAsInt());
            if (meanTaskDuration.isPresent())
                entityDaily.setMeanTaskDuration((int) Math.round(meanTaskDuration.getAsDouble()));
            if (maxTaskDuration.isPresent())
                entityDaily.setMaxTaskDuration(maxTaskDuration.getAsInt());
            // Calculer min,avg,max(Hop) depuis la liste de chaque groupe

            OptionalInt minHop = y.parallelStream().mapToInt(StatCollectDailyDeviceByDateCEntity::getMinHop).min();
            OptionalDouble meanHop = y.parallelStream().mapToInt(StatCollectDailyDeviceByDateCEntity::getMeanHop).average();
            OptionalInt maxHop = y.parallelStream().mapToInt(StatCollectDailyDeviceByDateCEntity::getMaxHop).max();
            if (minHop.isPresent())
                entityDaily.setMinHop(minHop.getAsInt());
            if (meanHop.isPresent())
                entityDaily.setMeanHop((int) Math.round(meanHop.getAsDouble()));
            if (maxHop.isPresent())
                entityDaily.setMaxHop(maxHop.getAsInt());

            // Calculer count(Device_id) depuis la liste de chaque groupe

            int countDevice = (int) y.parallelStream().map(StatCollectDailyDeviceByDateCEntity::getDevice).count();

            entityDaily.setDeviceCount(countDevice);
            // remplissement des deux valeurs (partial_success_rate et
            // full_success_rate)
            entityDaily.setPartialSuccessRate(BigDecimal.ONE);
            entityDaily.setFullSuccessRate(BigDecimal.TEN);

            // !!! DATE COLLECT !!!
            // date actuel

            entityDaily.setDateCollect(com.datastax.driver.core.LocalDate.fromYearMonthDay(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));
            entityDaily.setYearmonthcollect(LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue());

            // remplir les clés primaires composites

            // remplir statCollectDailyDcByDate
            StatCollectDailyDcByDateCEntity statCollectDcByDate = new StatCollectDailyDcByDateCEntity(
                    entityDaily.getDateCollect(), entityDaily.getTaskId(), entityDaily.getDc(),
                    entityDaily.getDeviceCount(), entityDaily.getFullSuccessRate(), entityDaily.getPartialSuccessRate(),
                    entityDaily.getMinTaskDuration(), entityDaily.getMeanTaskDuration(),
                    entityDaily.getMaxTaskDuration(), entityDaily.getMinHop(), entityDaily.getMeanHop(),
                    entityDaily.getMaxHop());

            // Insertion dans la base de donnée
            try {
                if (!asynch) {
                    mapper.save(entityDaily);
                    mapperDailyDcByDate.save(statCollectDcByDate);
                } else {
                    mapper.saveAsync(entityDaily);
                    mapperDailyDcByDate.saveAsync(statCollectDcByDate);

                }
                log.debug("persist entityDailyDc success");

            } catch (RuntimeException e) {

                log.error("persist entityDailyDc failed", e);
                throw e;
            }

        });
    }

}