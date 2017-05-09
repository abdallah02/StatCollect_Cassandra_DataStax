package tn.talan.dao.postgresql;
// Generated 15 ao�t 2016 00:21:53 by Hibernate Tools 5.1.0.Alpha1

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import tn.talan.dao.util.SessionSING;
import tn.talan.entity.postgresql.StatCollectDeviceEntity;

import java.util.ArrayList;
import java.util.List;

public class StatCollectDeviceDao {

    private static final Log log = LogFactory.getLog(StatCollectDeviceDao.class);
    private final SessionFactory sessionFactory = SessionSING.getSessionFactory();
    private Session session;
    private int i;

    public void persistlot(ArrayList<StatCollectDeviceEntity> alscd) {
        i = 0;
        session = sessionFactory.openSession();

        session.beginTransaction();
        alscd.forEach(x -> {

            session.persist(x);

            i++;
            if (i % 20 == 0) {
                session.flush();
                session.clear();
            }
        });

        session.getTransaction().commit();
        session.close();
    }

    public void persist(StatCollectDeviceEntity transientInstance) {
        log.debug("persisting StatCollectDeviceId instance");
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.persist(transientInstance);
            session.flush();
            session.clear();
            session.getTransaction().commit();

            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(StatCollectDeviceEntity instance) {
        log.debug("attaching dirty StatCollectDeviceId instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(StatCollectDeviceEntity instance) {
        log.debug("attaching clean StatCollectDeviceId instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(StatCollectDeviceEntity persistentInstance) {
        log.debug("deleting StatCollectDeviceId instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public StatCollectDeviceEntity merge(StatCollectDeviceEntity detachedInstance) {
        log.debug("merging StatCollectDeviceId instance");
        try {
            StatCollectDeviceEntity result = (StatCollectDeviceEntity) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public StatCollectDeviceEntity findById(tn.talan.entity.postgresql.StatCollectDeviceEntity id) {
        log.debug("getting StatCollectDeviceId instance with id: " + id);
        try {
            StatCollectDeviceEntity instance = (StatCollectDeviceEntity) sessionFactory.getCurrentSession()
                    .get("dao.StatCollectDeviceId", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(StatCollectDeviceEntity instance) {
        log.debug("finding StatCollectDeviceId instance by example");
        try {
            List results = sessionFactory.getCurrentSession().createCriteria("dao.StatCollectDeviceId")
                    .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<StatCollectDeviceEntity> displayAll() {
        session = sessionFactory.openSession();

        String sql = "SELECT * FROM stat_collect_device";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(StatCollectDeviceEntity.class);
        List<StatCollectDeviceEntity> results = query.list();
        session.close();

        return results;
    }

}
