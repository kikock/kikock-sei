package com.kcmp.ck.flow.service;

import com.kcmp.ck.flow.vo.CompleteTaskVo;
import com.kcmp.ck.flow.vo.SolidifyStartFlowVo;
import com.kcmp.ck.flow.vo.StartFlowVo;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.ResponseData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class DefaultFlowBaseServiceTest extends BaseContextTestCase{


    @Autowired
    private  DefaultFlowBaseService defaultFlowBaseService;

    @Test
    public void solidifyCheckAndSetAndStart(){
        SolidifyStartFlowVo bean  = new SolidifyStartFlowVo();
        bean.setBusinessId("300EA92B-64CA-11EA-B339-0242C0A84421");
        bean.setBusinessModelCode("com.kcmp.flow.entity.DefaultBusinessModel");
        bean.setFlowDefinationId("98307F87-5150-11EA-BBE4-0242C0A84421");
        ResponseData responseData = defaultFlowBaseService.solidifyCheckAndSetAndStart(bean);
        System.out.println(JsonUtils.toJson(responseData));
    }

    @Test
    public void sart_one() {
        StartFlowVo vo =new StartFlowVo();
        vo.setBusinessKey("2BEA9832-12F6-11E9-B111-0242C0A8440B");
        vo.setBusinessModelCode("com.kcmp.flow.entity.DefaultBusinessModel");
        ResponseData res = null;
        try{
            res =  defaultFlowBaseService.startFlow(vo);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(res));

        try{
            Thread.sleep(1000*20);  //20秒  因为有异步事件需要执行
        }catch (Exception e){

        }
    }

    @Test
    public void sart_three() {
        StartFlowVo vo =new StartFlowVo();
        vo.setBusinessKey("2BEA9832-12F6-11E9-B111-0242C0A8440B");
        vo.setBusinessModelCode("com.kcmp.ck.flow.entity.DefaultBusinessModel");
        vo.setTypeId("C35B0B09-3640-11E7-9617-3C970EA9E0F7");
        vo.setFlowDefKey("test2222");
        vo.setOpinion("");
        vo.setTaskList("[{\"nodeId\":\"UserTask_12\",\"userVarName\":\"UserTask_12_List_CounterSign\",\"flowTaskType\":\"countersign\",\"instancyStatus\":false,\"userIds\":\"1592D012-A330-11E7-A967-02420B99179E\"}]");
        vo.setAnonymousNodeId("");
        ResponseData res = null;
       try{
           res =  defaultFlowBaseService.startFlow(vo);
       }catch (Exception e){
         System.out.println(e.getMessage());
       }
        System.out.println(JsonUtils.toJson(res));


        try{
            Thread.sleep(1000*20);  //20秒  因为有异步事件需要执行
        }catch (Exception e){

        }
    }

    @Test
    public void complete() {
        CompleteTaskVo completeTaskVo  =new CompleteTaskVo();
        completeTaskVo.setApproved("true");
        completeTaskVo.setBusinessId("04483B02-1F18-11EA-91AC-0242C0A84503");
        completeTaskVo.setEndEventId("false");
        completeTaskVo.setLoadOverTime(null);
        completeTaskVo.setManualSelected(false);
        completeTaskVo.setOpinion("同意");
        completeTaskVo.setTaskId("8A5C5D6E-1FD7-11EA-84D4-0242C0A84516");
        completeTaskVo.setTaskList("[{\"nodeId\":\"UserTask_15\",\"flowTaskType\":\"approve\",\"userVarName\":\"UserTask_15_Approve\",\"callActivityPath\":null,\"instancyStatus\":false,\"solidifyFlow\":true}]");
        ResponseData res=null;
        try{
            res =  defaultFlowBaseService.completeTask(completeTaskVo);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(res));
    }
}
