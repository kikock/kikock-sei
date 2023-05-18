package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.jpa.BaseDao;
import com.kcmp.core.ck.service.BaseService;
import com.kcmp.ck.flow.api.IBusinessWorkPageUrlService;
import com.kcmp.ck.flow.dao.BusinessWorkPageUrlDao;
import com.kcmp.ck.flow.entity.BusinessWorkPageUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kikock
 * 业务实体工作界面配置管理服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class BusinessWorkPageUrlService extends BaseService<BusinessWorkPageUrl, String> implements IBusinessWorkPageUrlService {

    @Autowired
    private BusinessWorkPageUrlDao businessWorkPageUrlDao;

    @Override
    protected BaseDao<BusinessWorkPageUrl, String> getDao(){
        return this.businessWorkPageUrlDao;
    }

    /**
     * 保存设置的工作界面
     * @param id                业务实体id
     * @param selectWorkPageIds 工作界面的所有id
     * @param id                流程id
     */
    @Override
    public void saveBusinessWorkPageUrlByIds(String id, String selectWorkPageIds) {
        businessWorkPageUrlDao.deleteBybusinessModuleId(id);
        if (StringUtils.isBlank(selectWorkPageIds)) {
            return;
        } else {
            String[] str = selectWorkPageIds.split(",");
            for (int i = 0; i < str.length; i++) {
                BusinessWorkPageUrl businessWorkPageUrl = new BusinessWorkPageUrl();
                businessWorkPageUrl.setBusinessModuleId(id);
                businessWorkPageUrl.setWorkPageUrlId(str[i]);
                businessWorkPageUrlDao.save(businessWorkPageUrl);
            }
        }
    }
}

