package com.kcmp.core.ck.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kcmp.ck.util.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by kikock
 * 枚举序列化
 * @email kikock@qq.com
 **/
public class EnumJsonSerializer extends JsonSerializer<Enum> {

    @Override
    public void serialize(Enum value, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
        //自身的值
        generator.writeString(value.name());
        //新增属性：枚举类型+Remark
        generator.writeFieldName(StringUtils.uncapitalize(value.getClass().getSimpleName()) + "Remark");
        //新增属性值
        generator.writeString(EnumUtils.getEnumItemRemark(value.getClass(), value));
    }
}
