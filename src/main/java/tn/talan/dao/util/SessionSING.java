package tn.talan.dao.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionSING {

    // creation SessionFactory a partir de cfg.xml

    private static final SessionFactory sessionFactory;
    private static Session session; // NULL

    static {
        try {

            Configuration cf = new Configuration().configure();
            System.out.println(cf.getProperties());
            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (Throwable ex) {
            System.out.println("la creation de la sessionFacotroy a échoué" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;

    }

    public static Session getSession() {
        if (session != null && !session.isOpen()) {
            setSession();
        }
        if (session == null) { // singleton
            session = getSessionFactory().openSession(); //
        }
        return session;
    }

    private static void setSession() {
        SessionSING.session = null;
    }

}
