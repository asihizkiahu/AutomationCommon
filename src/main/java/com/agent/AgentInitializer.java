package com.agent;

import com.config.base.ConfigItemsRouter;
import com.config.data.le.LeConfigData;
import com.liveperson.AgentAPIFactory;
import com.liveperson.AgentState;
import com.liveperson.Rep;
import com.liveperson.impl.ChatAPIClientObject;;
import com.liveperson.utils.RestAPI.AgentAndVisitorUtils;
import com.util.properties.PropertiesHandlerImpl;
import humanclick.logging.ContextLogger;
import org.apache.log4j.Logger;
import com.config.data.le.LeConfigData.Site.UsersData;
import com.config.data.le.LeConfigData.Site.UsersData.CreateUser;
import org.junit.Assert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class Info:
 * <p/>
 * User: ilanm
 * Date: 6/23/14
 * Time: 2:39 PM
 */
public class AgentInitializer {

    private static Properties prop;
    private static final Logger logger = Logger.getLogger(AgentInitializer.class);
    private String propsFileName = "agent.properties";
    private static String confFileName = "Legacy_config_data.xml";
    private static List<LeConfigData.Site.UsersData> usersData;

    public static void initTest(String propsFilePath, List<Rep> agents, PreConfiguredSite siteEntity) {
        prop = PropertiesHandlerImpl.getInstance().parse(propsFilePath);
        usersData = ConfigItemsRouter.getInstance().initService(confFileName, LeConfigData.class).getSite().getUsersData();
        try {
            siteEntity = new PreConfiguredSite(prop, (InetAddress.getLocalHost().getHostName()));
        } catch (UnknownHostException e) {
            logger.error("Unrecognized host");
        }
        initReps(agents, siteEntity);
    }

    private static void initReps(List<Rep> agents, PreConfiguredSite siteEntity){
        agents.clear();
        CreateUser create;
        for(UsersData userData : usersData) { // take data from conf data file
            create = userData.getCreateUser();
            agents.add(AgentAPIFactory.createAgent(
                    siteEntity.getSiteId(), siteEntity.getAppKey(), siteEntity.getAppSecret(), siteEntity.getTokenKey(), siteEntity.getTokenSecret(),
                            create.getUser(), create.getPassword(), create.getSkill().get(0), siteEntity.getProtocol(), siteEntity.getDomain(), siteEntity.getSiteURL())
            );
        }
    }




}