package org.dangcat.commons.serialize.json;

import com.google.gson.stream.JsonReader;

/**
 * �����л��ӿڡ�
 */
public interface JsonDeserialize
{
    public Object deserialize(JsonReader jsonReader, Class<?> classType, Object instance) throws Exception;
}
