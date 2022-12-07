package com.kcmp.ck.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by kikock
 * 配置业务实体基类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@MappedSuperclass
public abstract class BaseConfigEntity implements Serializable, Persistable<String> {
    private static final long serialVersionUID = 1L;

    /**
     * Id标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2",
            parameters = {@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    private String id;

    /**
     * Returns if the {@code Persistable} is new or was persisted already.
     *
     * @return if the object is new
     */
    @Override
    @JsonIgnore
    public boolean isNew() {
        return Objects.isNull(id) || id.isEmpty();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
