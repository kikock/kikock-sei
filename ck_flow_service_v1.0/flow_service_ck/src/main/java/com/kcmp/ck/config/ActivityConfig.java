package com.kcmp.ck.config;


import com.kcmp.ck.flow.activiti.ext.ActivityBehaviorFactoryExt;
import com.kcmp.ck.flow.activiti.ext.ExclusiveGatewayActivityBehaviorExt;
import com.kcmp.ck.flow.activiti.ext.ServiceTaskDelegate;
import com.kcmp.ck.flow.listener.CommonCounterSignCompleteListener;
import com.kcmp.ck.flow.listener.CommonUserTaskCompleteListener;
import com.kcmp.ck.flow.listener.CommonUserTaskCreateListener;
import com.kcmp.ck.flow.listener.EndEventCompleteListener;
import com.kcmp.ck.flow.listener.MessageAfterListener;
import com.kcmp.ck.flow.listener.MessageBeforeListener;
import com.kcmp.ck.flow.listener.PoolTaskBeforeListener;
import com.kcmp.ck.flow.listener.ReceiveTaskAfterListener;
import com.kcmp.ck.flow.listener.ReceiveTaskBeforeListener;
import com.kcmp.ck.flow.listener.StartEventCompleteListener;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.flow.util.FlowListenerTool;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by kikock
 * 流配置
 * @author kikock
 * @email kikock@qq.com
 **/
@Configuration
public class ActivityConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("none");
        configuration.setJobExecutorActivate(false);
        configuration.setJpaHandleTransaction(true);
        configuration.setJpaCloseEntityManager(true);
        configuration.setIdGenerator(new StrongUuidGenerator());
        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        //用于更改流程节点的执行行为
        configuration.setActivityBehaviorFactory(activityBehaviorFactoryExt());
        return configuration;
    }

    @Bean
    public ActivityBehaviorFactoryExt activityBehaviorFactoryExt() {
        ActivityBehaviorFactoryExt activityBehaviorFactory = new ActivityBehaviorFactoryExt();
        activityBehaviorFactory.setExclusiveGatewayActivityBehaviorExt(exclusiveGatewayActivityBehaviorExt());
        return activityBehaviorFactory;
    }

    @Bean
    public ExclusiveGatewayActivityBehaviorExt exclusiveGatewayActivityBehaviorExt() {
        return new ExclusiveGatewayActivityBehaviorExt();
    }

    @Bean("processEngine")
    public ProcessEngine processEngine(ApplicationContext applicationContext) throws Exception {
        ProcessEngineFactoryBean processEngine = new ProcessEngineFactoryBean();
        processEngine.setProcessEngineConfiguration(processEngineConfiguration());
        processEngine.setApplicationContext(applicationContext);
        return processEngine.getObject();
    }

    @Bean("identityService")
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean("formService")
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean("repositoryService")
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean("runtimeService")
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean("taskService")
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean("historyService")
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean("managementService")
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public CommonUserTaskCompleteListener commonUserTaskCompleteListener(){
        return new CommonUserTaskCompleteListener();
    }

    @Bean
    public CommonCounterSignCompleteListener commonCounterSignCompleteListener(){
        return new CommonCounterSignCompleteListener();
    }

    @Bean
    public ServiceTaskDelegate serviceTaskDelegate(){
        return new ServiceTaskDelegate();
    }

    @Bean
    public CommonUserTaskCreateListener commonUserTaskCreateListener(){
        return new CommonUserTaskCreateListener();
    }

    @Bean
    public EndEventCompleteListener endEventCompleteListener(){
        return new EndEventCompleteListener();
    }

    @Bean
    public MessageAfterListener messageAfterListener(){
        return new MessageAfterListener();
    }

    @Bean
    public MessageBeforeListener messageBeforeListener(){
        return new MessageBeforeListener();
    }

    @Bean
    public ReceiveTaskAfterListener receiveTaskAfterListener(){
        return new ReceiveTaskAfterListener();
    }

    @Bean
    public ReceiveTaskBeforeListener receiveTaskBeforeListener(){
        return new ReceiveTaskBeforeListener();
    }

    @Bean
    public StartEventCompleteListener startEventCompleteListener(){
        return new StartEventCompleteListener();
    }

    @Bean
    public FlowListenerTool flowListenerTool(){
        return new FlowListenerTool();
    }

    @Bean
    public FlowCommonUtil flowCommonUtil(){
        return new FlowCommonUtil();
    }

    @Bean
    public PoolTaskBeforeListener poolTaskBeforeListener(){return new PoolTaskBeforeListener();}

}
