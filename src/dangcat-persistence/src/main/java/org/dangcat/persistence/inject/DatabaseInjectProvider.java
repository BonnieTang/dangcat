package org.dangcat.persistence.inject;

import java.lang.annotation.Annotation;

import org.dangcat.framework.service.InjectProvider;
import org.dangcat.framework.service.ServiceProvider;
import org.dangcat.persistence.annotation.Database;
import org.dangcat.persistence.entity.EntityManagerFactory;

public class DatabaseInjectProvider extends InjectProvider
{
    @Override
    public Class<? extends Annotation> getAnnotation()
    {
        return Database.class;
    }

    @Override
    protected Object getObject(ServiceProvider serviceProvider, Object serviceInstance, Class<?> accessClassType, Annotation annotation)
    {
        return EntityManagerFactory.getInstance().open(((Database) annotation).value());
    }
}
