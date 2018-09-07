package org.einstein.eproto.util;

/**
 * TODO
 * @create by xiamicpp
 **/
public interface IRecyclable<T> {

    void recycle();

    void setHandler(IRecycleHandler<T> var1);

    IRecycleHandler<T> getHandler();

    boolean isInUse();

    void setInuse();
}
