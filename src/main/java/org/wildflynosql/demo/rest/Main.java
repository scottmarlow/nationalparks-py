package org.wildflynosql.demo.rest;

import java.util.HashMap;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.mongodb.Mongo;
import org.wildfly.swarm.config.mongodb.mongo.Host;
import org.wildfly.swarm.config.security.Flag;
import org.wildfly.swarm.config.security.SecurityDomain;
import org.wildfly.swarm.config.security.security_domain.ClassicAuthentication;
import org.wildfly.swarm.config.security.security_domain.authentication.LoginModule;
import org.wildfly.swarm.mongodb.MongoDBFraction;
import org.wildfly.swarm.security.SecurityFraction;
import org.wildfly.swarm.spi.api.OutboundSocketBinding;

public class Main {

    public static void main(String... args) throws Exception {
        Swarm swarm = new Swarm(args)
                .outboundSocketBinding("standard-sockets",
                        new OutboundSocketBinding("mongotesthost")
                                .remoteHost("localhost")
                                .remotePort(27017))
                .fraction(SecurityFraction.defaultSecurityFraction()
                        .securityDomain(
                                new SecurityDomain("mongoRealm")
                                        .classicAuthentication(
                                                new ClassicAuthentication().loginModule(
                                                        new LoginModule("ConfiguredIdentity").code("ConfiguredIdentity")
                                                                .flag(Flag.REQUIRED)
                                                                .moduleOptions(new HashMap<Object, Object>() {
                                                                                   {
                                                                                       put("principal", "devuser");
                                                                                       put("username", "devuser");
                                                                                       put("password", "changethis");
                                                                                   }
                                                                               }
                                                                )
                                                )
                                        )
                        )
                )
                .fraction(new MongoDBFraction()
                        .mongo(new Mongo("mongodbtestprofile")
                                .host(new Host("mongotesthost")
                                        .outboundSocketBindingRef("mongotesthost")
                                )
                                .database("mongotestdb")
                                .jndiName("java:jboss/mongodb/test")
                                .id("mongodbtestprofile")
                                .securityDomain("mongoRealm")
                                // .authType(Mongo.AuthType.GSSAPI)
                                // .authType(Mongo.AuthType.PLAIN_SASL)
                                // .authType(Mongo.AuthType.SCRAM_SHA_1)
                                // .authType(Mongo.AuthType.MONGODB_CR)
                                // .authType(Mongo.AuthType.MONGODB_X509)
                                .authType(Mongo.AuthType.DEFAULT)
                                .ssl(false)

                        )
                ).fraction(new org.wildfly.swarm.ee.EEFraction())
                .start()
                .deploy();
    }
}
