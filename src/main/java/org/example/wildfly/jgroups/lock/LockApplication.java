package org.example.wildfly.jgroups.lock;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@ApplicationScoped
public class LockApplication extends Application {

}
