package com.bok.bank.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class JsonType implements UserType, ParameterizedType {
    private static final int[] SQL_TYPES = {Types.LONGVARCHAR};
    public static final String CLASSNAME = "classname";
    private Class clazz;
    private final ObjectMapper mapper;


    public JsonType() {
        this.mapper = JsonMapperType.mapper();
    }


    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return clazz;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        try {
            return deserializeObject(value);
        } catch (Exception e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor sessionImplementor) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.LONGNVARCHAR);
        } else {
            try {
                String result = serializeObject(value);
                st.setString(index, result);
            } catch (Exception e) {
                throw new HibernateException(e);
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        String serializedObj = serializeObject(value);
        return deserializeObject(serializedObj);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) deepCopy(o);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public void setParameterValues(Properties parameters) {
        String className = parameters.getProperty(CLASSNAME);
        try {
            clazz = ReflectHelper.classForName(className, this.getClass());
        } catch (ClassNotFoundException exception) {
            throw new HibernateException("class not found", exception);
        }
    }

    protected String serializeObject(Object obj) {
        try {
            String result = mapper.writeValueAsString(obj);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected Object deserializeObject(String value) {
        try {
            Object obj = mapper.readValue(value, clazz);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
