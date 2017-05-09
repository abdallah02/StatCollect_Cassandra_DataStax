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
import tn.talan.entity.cassandra.StatCollectDailyDeviceByDateCEntity;
import tn.talan.idao.cassandra.IStatCollectDailyDeviceByDateCDao;

import java.time.LocalDate;
import java.util.List;

public class StatCollectDailyDeviceByDateCDao implements IStatCollectDailyDeviceByDateCDao {

    private static final Log log = LogFactory.getLog(StatCollectDailyDeviceByDateCDao.class);
    private final Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);
    private final Mapper<StatCollectDailyDeviceByDateCEntity> mapper;

    public StatCollectDailyDeviceByDateCDao() {
        mapper = (Mapper<StatCollectDailyDeviceByDateCEntity>) manager
                .mapper(StatCollectDailyDeviceByDateCEntity.class);
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see daocassandra.IStatCollectDailyDeviceByDateCDao#add(entitycassandra.
	 * StatCollectDailyDeviceByDateCEntity)
	 */

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyDeviceByDateCDao#add(entitycassandra.
     * StatCollectDailyDeviceByDateCEntity)
     */
    @Override
    public void add(StatCollectDailyDeviceByDateCEntity object) {
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
     * @see
     * daocassandra.IStatCollectDailyDeviceByDateCDao#addAsync(entitycassandra.
     * StatCollectDailyDeviceByDateCEntity)
     */
    @Override
    public void addAsync(StatCollectDailyDeviceByDateCEntity object) {
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
	 * daocassandra.IStatCollectDailyDeviceByDateCDao#displayByDate(java.time.
	 * LocalDate)
	 */

    /*
     * (non-Javadoc)
     *
     * @see
     * daocassandra.IStatCollectDailyDeviceByDateCDao#displayByDate(java.time.
     * LocalDate)
     */
    @Override
    public List<StatCollectDailyDeviceByDateCEntity> displayByDate(LocalDate date) {
        List<StatCollectDailyDeviceByDateCEntity> cl;

        log.debug("getting StatCollectDailyDeviceByDateCEntity instance with date:" + date);

        try {

            Statement statement1 = QueryBuilder.select().all()
                    .from(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDailyDeviceByDate)

                    .where(QueryBuilder.eq("date_Collect", com.datastax.driver.core.LocalDate
                            .fromYearMonthDay(date.getYear(), date.getMonthValue(), date.getDayOfMonth())));

            ResultSet resultSet = session.execute(statement1);
            cl = mapper.map(resultSet).all();

            log.debug("find by date successful, result size: " + cl.size());

        } catch (RuntimeException e) {

            log.error("find by dev,task,date failed " + e);

            throw e;
        }

        return cl;
    }

}