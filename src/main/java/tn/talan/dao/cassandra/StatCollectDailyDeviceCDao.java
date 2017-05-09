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
import tn.talan.entity.cassandra.StatCollectDailyDeviceByDateCEntity;
import tn.talan.entity.cassandra.StatCollectDailyDeviceCEntity;
import tn.talan.entity.cassandra.StatCollectDeviceByDateCEntity;
import tn.talan.exception.DateBeginEndError;
import tn.talan.idao.cassandra.IStatCollectDailyDeviceCDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatCollectDailyDeviceCDao implements IStatCollectDailyDeviceCDao {

    private static final Log log = LogFactory.getLog(StatCollectDailyDeviceCDao.class);
    private final Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);
    private final Mapper<StatCollectDailyDeviceCEntity> mapper;
    private final Mapper<StatCollectDailyDeviceByDateCEntity> mapperDevByDate;

    public StatCollectDailyDeviceCDao() {
        mapper = (Mapper<StatCollectDailyDeviceCEntity>) manager
                .mapper(StatCollectDailyDeviceCEntity.class);
        mapperDevByDate = (Mapper<StatCollectDailyDeviceByDateCEntity>) manager
                .mapper(StatCollectDailyDeviceByDateCEntity.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyDeviceCDao#add(entitycassandra.
     * StatCollectDailyDeviceCEntity)
     */
    @Override
    public void add(StatCollectDailyDeviceCEntity object) {
        try {
            mapper.save(object);
            log.debug("persist successful");

        } catch (RuntimeException e) {

            log.error("persist failed", e);


        }

    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyDeviceCDao#addAsynch(entitycassandra.
     * StatCollectDailyDeviceCEntity)
     */
    @Override
    public void addAsynch(StatCollectDailyDeviceCEntity object) {
        try {
            mapper.saveAsync(object);
            log.debug("persist successful");

        } catch (RuntimeException e) {

            log.error("persist failed", e);


        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * daocassandra.IStatCollectDailyDeviceCDao#iISGetDevDailyCollStats(java.
     * lang.String, java.lang.String, java.time.LocalDate, java.time.LocalDate)
     */
    @Override
    public List<StatCollectDailyDeviceCEntity> iISGetDevDailyCollStats(String dev_id, String task_id, LocalDate begin,
                                                                       LocalDate end) throws DateBeginEndError {
        List<StatCollectDailyDeviceCEntity> cl;
        log.debug("getting StatCollectDailyDeviceCEntity instance with dev_id: " + dev_id + ",taskId: " + task_id
                + ",date-min: " + begin + ",date-max" + end);

        if (!(begin.getMonthValue() == end.getMonthValue()) || !(begin.getYear() == end.getYear())) {
            log.error("getting StatCollectDailyDeviceCEntity cause begin :year/month different to end :year/month");
            throw new DateBeginEndError("l'année et le mois des deux (date début et date fin) sont différents ");
        }
        try {

            String month = begin.getYear() + "-" + begin.getMonthValue();
            System.out.println(month);
            Statement statement1 = QueryBuilder.select().all()
                    .from(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDailyDevice)

                    .where(QueryBuilder.eq("device", dev_id)).and(QueryBuilder.eq("year_Month_Collect", month))
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
            log.debug("getting StatCollectDailyDeviceCEntity success with size:" + cl.size());
        } catch (RuntimeException e) {

            log.error("getting StatCollectDailyDeviceCEntity failed");

            throw e;
        }

        return cl;
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see daocassandra.IStatCollectDailyDeviceCDao#
	 * createAggregationInCollectDailyDevice(java.util.List)
	 */
    // TODO out of memory error + date is not correct

    @Override
    public void createAggregationInCollectDailyDevice(List<StatCollectDeviceByDateCEntity> tableStatCollectDeviceByDate,
                                                      boolean asynch) {

        Map<Object, List<StatCollectDeviceByDateCEntity>> mapObjList;
        // Attention les opérations de stream sont excuter en multi threads
        // au cas ou vous voulez les rendrent en un seul thread changer
        // [parallelStream] en [stream]

        mapObjList = tableStatCollectDeviceByDate.parallelStream()
                .collect(Collectors.groupingBy(dtc -> Arrays.asList(dtc.getDevice(), dtc.getDc(), dtc.getTaskId())));

        // output
        mapObjList.forEach((m, y) -> {

            StatCollectDailyDeviceCEntity entityDaily = new StatCollectDailyDeviceCEntity();

            List<String> groupByValues = new ArrayList<>();
            groupByValues = (List<String>) m;
            if (StringUtils.isNotEmpty(groupByValues.get(0)))

                entityDaily.setDevice(groupByValues.get(0));

            if (StringUtils.isNotEmpty(groupByValues.get(1)))
                entityDaily.setDc(groupByValues.get(1));

            if (StringUtils.isNotEmpty(groupByValues.get(2)))
                entityDaily.setTaskId(groupByValues.get(2));

            // Calculer min,avg,max(sucess) depuis la liste de chaque groupe
            DoubleSummaryStatistics dSS = y.parallelStream().mapToDouble(StatCollectDeviceByDateCEntity::getSuccess)
                    .summaryStatistics();

            entityDaily.setMinSuccessRate(BigDecimal.valueOf(dSS.getMin()));
            entityDaily.setMeanSuccessRate(BigDecimal.valueOf(dSS.getAverage()));
            entityDaily.setMaxSuccessRate(BigDecimal.valueOf(dSS.getMax()));

            // Calculer min,avg,max(duration) depuis la liste de chaque groupe

            IntSummaryStatistics iSSD = y.parallelStream().mapToInt(StatCollectDeviceByDateCEntity::getDuration)
                    .summaryStatistics();
            entityDaily.setMinTaskDuration(iSSD.getMin());
            entityDaily.setMeanTaskDuration((int) Math.round(iSSD.getAverage()));
            entityDaily.setMaxTaskDuration(iSSD.getMax());

            // Calculer min,avg,max(hopcount) depuis la liste de chaque groupe

            IntSummaryStatistics iSSH = y.parallelStream().mapToInt(StatCollectDeviceByDateCEntity::getHopCount)
                    .summaryStatistics();
            entityDaily.setMinHop(iSSH.getMin());
            entityDaily.setMeanHop((int) Math.round(iSSH.getAverage()));
            entityDaily.setMaxHop(iSSH.getMax());

            // !! Date a choisir !!
            // date actuel
            entityDaily.setDateCollect(com.datastax.driver.core.LocalDate.fromYearMonthDay(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));

            entityDaily.setYearmonthcollect(LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue());

            // remplir statCollectDeviceByDate
            StatCollectDailyDeviceByDateCEntity statCollectDeviceByDate = new StatCollectDailyDeviceByDateCEntity();
            statCollectDeviceByDate.setDateCollect(
                    com.datastax.driver.core.LocalDate.fromYearMonthDay(entityDaily.getDateCollect().getYear(),
                            entityDaily.getDateCollect().getMonth(), entityDaily.getDateCollect().getDay()));
            statCollectDeviceByDate.setDc(entityDaily.getDc());
            statCollectDeviceByDate.setTaskId(entityDaily.getTaskId());
            statCollectDeviceByDate.setDevice(entityDaily.getDevice());
            statCollectDeviceByDate.setMinSuccessRate(entityDaily.getMinSuccessRate());
            statCollectDeviceByDate.setMeanSuccessRate(entityDaily.getMeanSuccessRate());
            statCollectDeviceByDate.setMaxSuccessRate(entityDaily.getMaxSuccessRate());
            statCollectDeviceByDate.setMinTaskDuration(entityDaily.getMinTaskDuration());
            statCollectDeviceByDate.setMeanTaskDuration(entityDaily.getMeanTaskDuration());
            statCollectDeviceByDate.setMaxTaskDuration(entityDaily.getMaxTaskDuration());
            statCollectDeviceByDate.setMinHop(entityDaily.getMinHop());
            statCollectDeviceByDate.setMeanHop(entityDaily.getMeanHop());
            statCollectDeviceByDate.setMaxHop(entityDaily.getMaxHop());

            try {
                if (!asynch) {
                    mapper.save(entityDaily);
                    mapperDevByDate.save(statCollectDeviceByDate);
                } else {
                    mapper.saveAsync(entityDaily);
                    mapperDevByDate.saveAsync(statCollectDeviceByDate);
                }
                log.debug("persist entityDailyDevice success");

            } catch (RuntimeException e) {

                log.error("persist entityDailyDevice failed", e);
                throw e;
            }

        });
    }

}