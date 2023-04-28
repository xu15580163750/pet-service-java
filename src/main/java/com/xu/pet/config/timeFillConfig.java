package com.xu.pet.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;

@SpringBootConfiguration
@Primary
public class timeFillConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // this.strictInsertFill(metaObject,"字段名对应你的实体类的字段名",String.class,"Value");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
