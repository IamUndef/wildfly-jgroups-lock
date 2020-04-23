package org.example.wildfly.jgroups.lock;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.jgroups.JChannel;
import org.jgroups.blocks.locking.LockService;
import org.jgroups.fork.ForkChannel;
import org.jgroups.protocols.CENTRAL_LOCK;

@Path("/lock")
@ApplicationScoped
public class LockResource {

    @Resource(lookup = "java:jboss/jgroups/channel/default")
    private JChannel channel;
    private LockService lockService;

    @PostConstruct
    private void init() {
        try {
            JChannel fork = new ForkChannel(channel, "lock-stack", "lock-channel", new CENTRAL_LOCK());
            lockService = new LockService(fork);
            fork.connect("lock-group");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void acquireLock(@Suspended AsyncResponse response) {
        Executors.newSingleThreadExecutor().submit(() -> {
            Lock lock = lockService.getLock("lock");
            if (lock.tryLock()) {
                response.resume("Lock was acquired");
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                } finally {
                    lock.unlock();
                }
            } else {
                response.resume("Lock isn't available");
            }
        });
    }
}
