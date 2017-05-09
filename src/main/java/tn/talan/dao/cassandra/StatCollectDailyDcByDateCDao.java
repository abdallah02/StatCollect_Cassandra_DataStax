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
import tn.talan.idao.cassandra.IStatCollectDailyDcByDateCDao;

import java.time.LocalDate;
import java.util.List;

public class StatCollectDailyDcByDateCDao implements IStatCollectDailyDcByDateCDao {

    private static final Log log = LogFactory.getLog(StatCollectDailyDcByDateCEntity.class);
    private final Session session = DSConnectionProvider.getInstance().getSession();
    private final MappingManager manager = new MappingManager(session);
    private final Mapper<StatCollectDailyDcByDateCEntity> mapper;

    public StatCollectDailyDcByDateCDao() {
        mapper = (Mapper<StatCollectDailyDcByDateCEntity>) manager
                .mapper(StatCollectDailyDcByDateCEntity.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see daocassandra.IStatCollectDailyDcByDateCDao#add(entitycassandra.
     * StatCollectDailyDcByDateCEntity)
     */
    @Override
    public void add(StatCollectDailyDcByDateCEntity object) {
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
     * @see daocassandra.IStatCollectDailyDcByDateCDao#addAsync(entitycassandra.
     * StatCollectDailyDcByDateCEntity)
     */
    @Override
    public void addAsync(StatCollectDailyDcByDateCEntity object) {
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
     * @see daocassandra.IStatCollectDailyDcByDateCDao#displayByDate(java.time.
     * LocalDate)
     */
    @Override
    public List<StatCollectDailyDcByDateCEntity> displayByDate(LocalDate date) {
        List<StatCollectDailyDcByDateCEntity> cl;

        log.debug("getting StatCollectDailyDcByDateCEntity instance with date:" + date);

        try {

            Statement statement1 = QueryBuilder.select().all()
                    .from(SchemaCreator.keySpace, SchemaCreator.tableStatCollectDailyDcByDate)

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