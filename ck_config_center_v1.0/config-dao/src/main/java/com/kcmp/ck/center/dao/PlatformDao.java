package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 平台dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface PlatformDao extends JpaRepository<Platform, String> {

    Platform findByCode(String code);

    List<Platform> findByNameIsLike(String name);
}
