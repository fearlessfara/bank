package com.bok.bank.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Map;

public class JsonMapperType {
    public static ObjectMapper mapper() {
        return mapper(null, false, true, false);
    }

    public static ObjectMapper defaultMapper(Map<Class, Class> map) {
        return mapper(map, false, true, false);
    }

    public static ObjectMapper mapperWithAbstractTypeSupport() {
        return mapper(null, true, true, false);
    }

    public static ObjectMapper mapperWithAbstractTypeSupport(Map<Class, Class> map) {
        return mapper(map, true, true, false);
    }

    public static ObjectMapper mapper(Map<Class, Class> map, boolean abstractSupport, boolean onlyFields, boolean strict) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        if (map != null) {
            for (Map.Entry<Class, Class> entry : map.entrySet()) {
                SimpleModule module = new SimpleModule(entry.getKey().getName(), new Version(1, 0, 0, null));
                module.addAbstractTypeMapping(entry.getKey(), entry.getValue());
                mapper.registerModule(module);
            }
        }
        mapper.setVisibilityChecker(new VisibilityChecker.Std(JsonAutoDetect.Visibility.ANY));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        if (abstractSupport)
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS, JsonTypeInfo.As.PROPERTY);
        if (onlyFields) {
            mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
            mapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
            mapper.disable(MapperFeature.AUTO_DETECT_SETTERS);
            mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        }
        if (!strict) {
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        return mapper;
    }


}
