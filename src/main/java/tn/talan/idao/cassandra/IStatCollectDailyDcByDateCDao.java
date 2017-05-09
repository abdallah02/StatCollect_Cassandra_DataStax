package tn.talan.idao.cassandra;

import tn.talan.entity.cassandra.StatCollectDailyDcByDateCEntity;

import java.time.LocalDate;
import java.util.List;

public interface IStatCollectDailyDcByDateCDao {
    /**
     * Saves an entity mapped by this mapper. T his method is basically
     * equivalent to: getManager().getSession().execute(saveQuery(entity)).
     * <p>
     * <p>
     * Executes insert query the provided StatCollectDaiyDcByDateCEntity. This
     * method blocks until a row has been insert to the database. However, for
     * SELECT queries, it does not guarantee that the result has been received
     * in full. But it does guarantee that some response has been received from
     * the database, and in particular guarantees that if the request is
     * invalid, an exception will be thrown by this method.
     *
     * @param object
     */
    void add(StatCollectDailyDcByDateCEntity object);

    /**
     * Saves an entity mapped by this mapper. This method is basically
     * equivalent to: getManager().getSession().executeAsync(saveQuery(entity)).
     *
     * @param object
     */

    void addAsync(StatCollectDailyDcByDateCEntity object);

    /**
     * Executes SELECT query with the provided LocalDate example
     * LocalDate.Of(year,month,day) This method blocks until a row has been
     * insert to the database. However, for SELECT queries, it does not
     * guarantee that the result has been received in full. But it does
     * guarantee that some response has been received from the database,
     *
     * @param date
     * @return
     */
    List<StatCollectDailyDcByDateCEntity> displayByDate(LocalDate date);

}