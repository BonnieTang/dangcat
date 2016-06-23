package org.dangcat.framework.pool;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * �����
 *
 * @param <T>
 * @author dangcat
 */
public abstract class ObjectPool<T> {
    protected final Logger logger = Logger.getLogger(this.getClass());
    private boolean isPoolEnabled = true;
    /**
     * ���ӳض��С�
     */
    private Queue<T> pooledObjectQueue = new LinkedList<T>();
    /**
     * ����ʹ�õ����ӳض��С�
     */
    private Queue<T> usedObjectPool = new LinkedList<T>();

    public void close() {
        this.close(this.usedObjectPool);
        this.close(this.pooledObjectQueue);
    }

    private synchronized void close(Queue<T> pooledObjectQueue) {
        while (pooledObjectQueue.size() > 0) {
            T pooledObject = pooledObjectQueue.poll();
            if (pooledObject != null)
                this.close(pooledObject);
        }
    }

    /**
     * �ر�����
     */
    protected abstract void close(T pooledObject);

    public void closePooled() {
        this.close(this.pooledObjectQueue);
    }

    protected abstract T create();

    public synchronized void destroy(T pooledObject) {
        this.usedObjectPool.remove(pooledObject);
        this.pooledObjectQueue.remove(pooledObject);
        this.close(pooledObject);
    }

    public int getPoolSize() {
        return this.pooledObjectQueue.size();
    }

    public int getUsedSize() {
        return this.usedObjectPool.size();
    }

    public boolean isPoolEnabled() {
        return this.isPoolEnabled;
    }

    public void setPoolEnabled(boolean isPoolEnabled) {
        this.isPoolEnabled = isPoolEnabled;
    }

    public synchronized T peekPooled() {
        return this.pooledObjectQueue.peek();
    }

    /**
     * �������ӳ��е�һ�����ݿ����ӡ�
     */
    public synchronized T poll() {
        T pooledObject = null;
        if (this.pooledObjectQueue.size() > 0)
            pooledObject = this.pooledObjectQueue.poll();
        else
            pooledObject = this.create();
        if (pooledObject != null)
            this.usedObjectPool.add(pooledObject);
        return pooledObject;
    }

    /**
     * �������ӵ����ӳ��С�
     */
    public synchronized void release(T usedObject) {
        if (usedObject != null) {
            if (this.isPoolEnabled()) {
                if (this.usedObjectPool.contains(usedObject)) {
                    this.usedObjectPool.remove(usedObject);
                    this.pooledObjectQueue.add(usedObject);
                }
            } else
                this.destroy(usedObject);
        }
    }
}
