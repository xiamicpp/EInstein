package org.einstein.eproto.serilizable;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import org.einstein.core.util.PropertyUtil;
import org.einstein.eproto.api.IEInheritedProtoObject;
import org.einstein.eproto.api.ISerializable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @create by xiamicpp
 **/
public abstract class POSerializable implements ISerializable{
    private static final long serialVersionUID = 1L;
    private transient long messageRecvTime;
    private volatile transient ReadWriteLock lock;
    private volatile boolean isDirty=true;
    private volatile boolean m_immutable;
    private byte[] immutableBuffer = null;

    private static final boolean p_enableLocking = PropertyUtil.getBooleanProperty("einstein.serializable.enableLocking",true);
    private static final int p_readLockTimeoutMillis = PropertyUtil.getIntProperty("einstein.serializable.readLockTimeoutMillis",500);
    private static final int p_writeLockTimeoutMillis = PropertyUtil.getIntProperty("einstein.serializable.writeLockTimeoutMillis",500);
    private static final int p_readLockSpinCount = PropertyUtil.getIntProperty("einstein.serializable.readLockSpinCount",16);
    private static final int p_writeLockSpinCount = PropertyUtil.getIntProperty("einstein.serializable.writeLockSpinCount",16);



    public byte[] serialize(){
        __build();

        if(this instanceof IEInheritedProtoObject){
            Descriptors.FieldDescriptor parentField = ((IEInheritedProtoObject)this).getFieldInBuffer();
            POSerializable parent = (POSerializable) ((IEInheritedProtoObject)this).getParent();
           // getBuffer().setField(parentField,parent.getBuffer().build());

        }
        return __getBuffer().build().toByteArray();
    }


    abstract protected GeneratedMessageV3.Builder __getBuffer();


    protected boolean __isDirty(){
        return isDirty;
    }

    protected void __markDirty(){
        isDirty = true;
    }

    protected void __build(){
        if(isDirty){
            immutableBuffer = __getBuffer().build().toByteArray();
        }
    }


    protected boolean needThreadSafeSync() {
        return !isImmutable()&&p_enableLocking;
    }


    public boolean isImmutable(){
        return m_immutable;
    }


    protected LockResult lockRead(){
        return lock(LockType.READ_LOCK,p_readLockTimeoutMillis,p_readLockSpinCount);
    }

    protected LockResult lockWrite(){
        return lock(LockType.WRITE_LOCK,p_writeLockTimeoutMillis,p_writeLockSpinCount);
    }

    protected LockResult unLockRead(){
        return unlock(LockType.READ_LOCK);
    }

    protected LockResult unLockWrite(){
        return unlock(LockType.WRITE_LOCK);
    }

    protected ReadWriteLock getLock(){
        if(lock == null){
            synchronized (this){
                if(lock == null){
                    lock = new ReentrantReadWriteLock();
                }
            }
        }
        return lock;
    }


    private LockResult lock(LockType type,int millis, int spinCount){
        ReadWriteLock rwlock = getLock();
        Lock lock = type==LockType.READ_LOCK? rwlock.readLock():rwlock.writeLock();
        LockResult result = LockResult.SUCCESS;
        for(int i=0;i<spinCount;i++){
            try {
                if(!lock.tryLock(millis, TimeUnit.MILLISECONDS)){
                    result  = LockResult.TIMEOUT;
                }else {
                    return LockResult.SUCCESS;
                }
            } catch (InterruptedException e) {
                i = spinCount;
                result = LockResult.INTERRUPTED;
                e.printStackTrace();
                break;
            }
        }
        return result;
    }

    private LockResult unlock(LockType type){
        ReadWriteLock rwLock = getLock();
        Lock lock = type == LockType.READ_LOCK?rwLock.readLock():rwLock.writeLock();

        try{
            lock.unlock();
            return LockResult.SUCCESS;
        }catch (IllegalMonitorStateException e){
            return LockResult.ILLEGALSTATE;
        }
    }

    protected enum LockType{
        READ_LOCK,
        WRITE_LOCK
    }

    protected enum LockResult{
        NOLOCK,
        SUCCESS,
        INTERRUPTED,
        ILLEGALSTATE,
        TIMEOUT
    }



}
