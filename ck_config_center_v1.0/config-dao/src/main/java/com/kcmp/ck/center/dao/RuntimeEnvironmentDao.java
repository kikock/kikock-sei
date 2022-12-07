package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.RuntimeEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 运行环境dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface RuntimeEnvironmentDao extends JpaRepository<RuntimeEnvironment, String> {

   List<RuntimeEnvironment> findByNameIsLike(String name);
}
