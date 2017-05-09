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
import tn.talan.entity.cassandra.StatCollectDeviceByDateCEntity;
import tn.talan.idao.cassandra.IStatCollectDeviceByDateCDao;

import java.time.LocalDate;
import java.util.List;

public class StatCollectDeviceByDateCDao implements IStatCollectDeviceByDateCDao {

    private static final Log log = LogFactory.getLog(StatCollectDeviceByDateCDao.class);
    private final Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);
    private final Mapper<StatCollectDeviceByDateCEntity> mapper;

    public StatCollectDeviceByDateCDao() {
        mapper = (Mapper<StatCollectDeviceByDateCEntity>) manager
                .mapper(StatCollectDeviceByDateCEntity.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceByDateCDao#add(entitycassandra.
     * StatCollectDeviceByDateCEntity)
     */
    @Override
    public void add(StatCollectDeviceByDateCEntity object) {
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
     * @see daocassandra.IStatCollectDeviceByDateCDao#addAsynch(entitycassandra.
     * StatCollectDeviceByDateCEntity)
     */
    @Override
    public void addAsynch(StatCollectDeviceByDateCEntity object) {
        try {
            mapper.saveAsync(object);
            log.debug("persist excuted");

        } catch (RuntimeException e) {

            log.error("persist failed", e);


        }

    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDeviceByDateCDao#displayByDate(java.time.
     * LocalDate)
     */
    /*
	 * (non-Javadoc)
	 * 
	 * @see daocassandra.IStatCollectDeviceByDateCDao#displayByDate(java.time.
	 * LocalDate)
	 */
    @Override
    public List<StatCollectDeviceByDateCEntity> displayByDate(LocalDate date) {
        List<StatCollectDeviceByDateCEntity> cl;

        log.debug("getting StatCollectDeviceCEntity instance with date:" + date);

        try {

            Statement statement1 = QueryBuilder.select().all()
                    .from(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDeviceByDate)

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