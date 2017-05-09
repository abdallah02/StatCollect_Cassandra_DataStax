package tn.talan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tn.talan.dao.cassandra.*;
import tn.talan.dao.postgresql.StatCollectDeviceDao;
import tn.talan.entity.cassandra.StatCollectDeviceByDateCEntity;
import tn.talan.entity.cassandra.StatCollectDeviceCEntity;
import tn.talan.entity.postgresql.StatCollectDeviceEntity;
import tn.talan.entity.postgresql.StatCollectDeviceId;
import tn.talan.exception.DateBeginEndError;
import tn.talan.idao.cassandra.*;
import tn.talan.iservice.IStatCollectService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class StatCollectService implements IStatCollectService {

    private static final Log log = LogFactory.getLog(StatCollectService.class);
    private StatCollectDeviceDao collectPostgresDeviceDao;
    private IStatCollectDeviceCDao collectCassandraDeviceDao;
    private IStatCollectDeviceByDateCDao collectCassandraDeviceByDateDao;
    private IStatCollectDailyDeviceCDao collectCassandraDailyDeviceDao;
    private IStatCollectDailyDeviceByDateCDao collectCassandraDailyDeviceByDateDao;
    private IStatCollectDailyDcCDao collectCassandraDailyDcDao;
    private IStatCollectDailyDcByDateCDao collectCassandraDailyDcByDateDao;
    private IStatCollectDailyGlobalCDao collectCassandraDailyGlobalDao;

    // ajouter des lignes a la table stat_collect_device selon les paramétres
    // saisis [pour x dc,task, on a n nombre de device]
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * service.IStatCollectService#addRandomValuesStatDevicePostgres(java.lang.
	 * String, java.lang.String, java.lang.String, int)
	 */

    @Override
    public void addRandomValuesStatDevicePostgres(String dc, String device, String taskId, int nombreDevice) {
        if (collectPostgresDeviceDao == null)
            collectPostgresDeviceDao = new StatCollectDeviceDao();
        ArrayList<StatCollectDeviceEntity> listStatDevicePg = new ArrayList<>();

        for (int i = 0; i < nombreDevice; i++) {
            StatCollectDeviceId statCollectPID = new StatCollectDeviceId();
            StatCollectDeviceEntity statCollectPEntity = new StatCollectDeviceEntity();
            statCollectPID.setDc(dc);
            statCollectPID.setDevice(device + String.valueOf(i));
            statCollectPID.setTaskId(taskId);
            statCollectPID.setDateCollect(LocalDateTime.now().minusMinutes(5));
            if (i % 2 == 0) {
                statCollectPEntity.setSuccess(0);
                statCollectPEntity.setDuration(6600);
            } else {
                statCollectPEntity.setSuccess(3);
                statCollectPEntity.setDuration(7020);
            }
            if (i % 3 == 0) {
                statCollectPEntity.setFailures(2);
                statCollectPEntity.setHopCount(2);
            } else {
                statCollectPEntity.setFailures(0);
                statCollectPEntity.setHopCount(5);
            }
            statCollectPEntity.setSuccessRate(BigDecimal.ONE);
            statCollectPEntity.setId(statCollectPID);
            listStatDevicePg.add(statCollectPEntity);

        }
        collectPostgresDeviceDao.persistlot(listStatDevicePg);
    }

    // migrer les données de la table stat_collect_device(postgres) vers
    // cassandra

    /*
     * (non-Javadoc)
     *
     * @see service.IStatCollectService#migrateStatCollectDevice()
     */
    @Override
    public void migrateStatCollectDevice() {
        if (collectCassandraDeviceByDateDao == null)
            collectCassandraDeviceByDateDao = new StatCollectDeviceByDateCDao();
        if (collectCassandraDeviceDao == null)
            collectCassandraDeviceDao = new StatCollectDeviceCDao();
        if (collectPostgresDeviceDao == null)
            collectPostgresDeviceDao = new StatCollectDeviceDao();
        long beginTime = System.currentTimeMillis();

        StatCollectDeviceByDateCEntity deviceCDateEntity = new StatCollectDeviceByDateCEntity();
        collectPostgresDeviceDao.displayAll().forEach(x -> {

            deviceCDateEntity.setDevice(x.getId().getDevice());
            deviceCDateEntity.setDateCollect(
                    com.datastax.driver.core.LocalDate.fromYearMonthDay(x.getId().getDateCollect().getYear(),
                            x.getId().getDateCollect().getMonthValue(), x.getId().getDateCollect().getDayOfMonth()));

            deviceCDateEntity.setTimeStampCollect(Date.from(x.getId().getDateCollect()
                    // fuseau horaire régler sur UTC
                    .atZone(ZoneId.of("UTC")).toInstant()));
            deviceCDateEntity.setDc(x.getId().getDc());
            deviceCDateEntity.setTaskId(x.getId().getTaskId());
            deviceCDateEntity.setSuccess(x.getSuccess());
            deviceCDateEntity.setDuration(x.getDuration());
            deviceCDateEntity.setFailures(x.getFailures());
            deviceCDateEntity.setHopCount(x.getHopCount());

            deviceCDateEntity.setSuccessRate(x.getSuccessRate());
            StatCollectDeviceCEntity collectDeviceCEntity = new StatCollectDeviceCEntity(
                    deviceCDateEntity.getDateCollect(), deviceCDateEntity.getDevice(),
                    deviceCDateEntity.getTimeStampCollect(), deviceCDateEntity.getSuccess(),
                    deviceCDateEntity.getFailures(), deviceCDateEntity.getSuccessRate(),
                    deviceCDateEntity.getDuration(), deviceCDateEntity.getHopCount(), deviceCDateEntity.getDc(),
                    deviceCDateEntity.getTaskId());
            collectCassandraDeviceByDateDao.add(deviceCDateEntity);
            collectCassandraDeviceDao.add(collectDeviceCEntity);

        });
        long endTime = System.currentTimeMillis();
        double timeConsume = (endTime - beginTime) / 1000.0;
        log.debug("operation end : " + timeConsume + "s");

        System.out.println("operation end : " + timeConsume + "s");
    }

    // afficher le resultat de la requête IIS_GET_DEV_COLL_STATS selon les
    // paramétres dépendants

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.IStatCollectService#dispIisGetDevCollStats(java.lang.String,
	 * java.lang.String, java.time.LocalDate)
	 */

    @Override
    public void dispIisGetDevCollStats(String dev, String taskId, LocalDate date) {
        if (collectCassandraDeviceDao == null)
            collectCassandraDeviceDao = new StatCollectDeviceCDao();
        long beginTime = System.currentTimeMillis();

        collectCassandraDeviceDao.iISGetDevCollStats(dev, taskId, date).forEach(System.out::println);
        long endTime = System.currentTimeMillis();

        double timeConsume = (endTime - beginTime) / 1000.0;
        log.debug("operation end : " + timeConsume + "s");
    }

    // inserer les donnés de la table STAT_COLLECT_DEVICE[Cassandra] grouper par
    // {dc,task,device} vers STAT_COLLECT_DAILY_DEVICE[Cassandra]

    /*
     * (non-Javadoc)
     *
     * @see
     * service.IStatCollectService#createAggregationInCollectDailyDeviceExec(
     * java.time.LocalDate, boolean)
     */
    @Override
    public void createAggregationInCollectDailyDeviceExec(LocalDate date, boolean asynch) {
        if (collectCassandraDeviceByDateDao == null)
            collectCassandraDeviceByDateDao = new StatCollectDeviceByDateCDao();
        if (collectCassandraDailyDeviceDao == null)
            collectCassandraDailyDeviceDao = new StatCollectDailyDeviceCDao();
        long beginTime = System.currentTimeMillis();

        collectCassandraDailyDeviceDao
                .createAggregationInCollectDailyDevice(collectCassandraDeviceByDateDao.displayByDate(date), asynch);
        long endTime = System.currentTimeMillis();

        double timeConsume = (endTime - beginTime) / 1000.0;
        log.debug("operation end : " + timeConsume + "s");

    }

    // afficher le resultat de la requête IIS_GET_DEV_DAILY_COLL_STATS selon les
    // paramétres dépendants

    /*
     * (non-Javadoc)
     *
     * @see
     * service.IStatCollectService#dispIisGetDevDailyCollStats(java.lang.String,
     * java.lang.String, java.time.LocalDate, java.time.LocalDate)
     */
    @Override
    public void dispIisGetDevDailyCollStats(String dev_id, String task_id, LocalDate begin, LocalDate end) {

        if (collectCassandraDailyDeviceDao == null)
            collectCassandraDailyDeviceDao = new StatCollectDailyDeviceCDao();
        try {
            long beginTime = System.currentTimeMillis();

            collectCassandraDailyDeviceDao.iISGetDevDailyCollStats(dev_id, task_id, begin, end);
            long endTime = System.currentTimeMillis();

            double timeConsume = (endTime - beginTime) / 1000.0;
            log.debug("operation end : " + timeConsume + "s");
        } catch (DateBeginEndError e) {
            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * service.IStatCollectService#createAggregationInCollectDailyDcExec(java.
     * time.LocalDate, boolean)
     */
    @Override
    public void createAggregationInCollectDailyDcExec(LocalDate date, boolean asynch) {
        if (collectCassandraDailyDcDao == null)
            collectCassandraDailyDcDao = new StatCollectDailyDcCDao();
        if (collectCassandraDailyDeviceByDateDao == null)

            collectCassandraDailyDeviceByDateDao = new StatCollectDailyDeviceByDateCDao();
        long beginTime = System.currentTimeMillis();

        collectCassandraDailyDcDao
                .createAggregationInCollectDailyDc(collectCassandraDailyDeviceByDateDao.displayByDate(date), asynch);
        long endTime = System.currentTimeMillis();

        double timeConsume = (endTime - beginTime) / 1000.0;
        log.debug("operation end : " + timeConsume + "s");
    }

    // afficher le resultat de la requête IIS_GET_DEV_COLL_STATS selon les
    // paramétres dépendants

    /*
     * (non-Javadoc)
     *
     * @see
     * service.IStatCollectService#dispIisGetDcDailyCollStats(java.lang.String,
     * java.lang.String, java.time.LocalDate, java.time.LocalDate)
     */
    @Override
    public void dispIisGetDcDailyCollStats(String dc, String taskId, LocalDate begin, LocalDate end) {
        if (collectCassandraDailyDcDao == null)
            collectCassandraDailyDcDao = new StatCollectDailyDcCDao();

        try {
            long beginTime = System.currentTimeMillis();

            collectCassandraDailyDcDao.iISGetDDailyCollStats(dc, taskId, begin, end).forEach(System.out::println);
            long endTime = System.currentTimeMillis();

            double timeConsume = (endTime - beginTime) / 1000.0;
            log.debug("operation end : " + timeConsume + "s");
        } catch (DateBeginEndError e) {
            e.printStackTrace();
        }

    }

    // inserer les donnés de la table STAT_COLLECT_DEVICE[Cassandra] grouper par
    // {dc,task,device} vers STAT_COLLECT_DAILY_DEVICE[Cassandra]

    /*
     * (non-Javadoc)
     *
     * @see
     * service.IStatCollectService#createAggregationInCollectDailyGlobalExec(
     * java.time.LocalDate, boolean)
     */
    @Override
    public void createAggregationInCollectDailyGlobalExec(LocalDate date, boolean asynch) {
        if (collectCassandraDailyGlobalDao == null)
            collectCassandraDailyGlobalDao = new StatCollectDailyGlobalCDao();
        if (collectCassandraDailyDcByDateDao == null)
            collectCassandraDailyDcByDateDao = new StatCollectDailyDcByDateCDao();
        long beginTime = System.currentTimeMillis();

        collectCassandraDailyGlobalDao
                .createAggregationInCollectGlobalDc(collectCassandraDailyDcByDateDao.displayByDate(date), asynch);
        long endTime = System.currentTimeMillis();

        double timeConsume = (endTime - beginTime) / 1000.0;
        log.debug("operation end : " + timeConsume + "s");
    }

    // afficher le resultat de la requête IIS_GET_DEV_COLL_STATS selon les
    // paramétres dépendants

    /*
     * (non-Javadoc)
     *
     * @see
     * service.IStatCollectService#dispIisGetGlobalDailyCollStats(java.lang.
     * String, java.time.LocalDate, java.time.LocalDate)
     */
    @Override
    public void dispIisGetGlobalDailyCollStats(String taskId, LocalDate begin, LocalDate end) {
        if (collectCassandraDailyGlobalDao == null)
            collectCassandraDailyGlobalDao = new StatCollectDailyGlobalCDao();

        try {
            long beginTime = System.currentTimeMillis();

            collectCassandraDailyGlobalDao.iIs_Get_Global_Daily_Stats(taskId, begin, end);
            long endTime = System.currentTimeMillis();

            double timeConsume = (endTime - beginTime) / 1000.0;
            log.debug("operation end : " + timeConsume + "s");
        } catch (DateBeginEndError e) {
            e.printStackTrace();
        }

    }

}