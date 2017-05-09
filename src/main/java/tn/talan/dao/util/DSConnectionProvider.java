package tn.talan.dao.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RoundRobinPolicy;

public class DSConnectionProvider {
    private static DSConnectionProvider instance;
    private final Session session;
    private final Cluster cluster;

    private DSConnectionProvider() {
        // TODO node address
        String node2 = "10.7.1.100";
        String node1 = "10.7.1.101";
        cluster = Cluster.builder().addContactPoints(node1, node2).withLoadBalancingPolicy(new RoundRobinPolicy())
                .build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(),
                    host.getRack());
        }
        session = cluster.connect();

    }

    public static synchronized DSConnectionProvider getInstance() {
        if (instance == null) {
            instance = new DSConnectionProvider();
        }
        return instance;
    }

    public void close() {

        cluster.close();
    }

    public Session getSession() {

        return this.session;
    }

}
