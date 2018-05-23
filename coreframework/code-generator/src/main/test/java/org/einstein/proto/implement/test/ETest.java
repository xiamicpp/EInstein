package org.einstein.proto.implement.test;

import org.einstein.eproto.exception.EProtoException;

/**
 * @create by xiamicpp
 **/
public class ETest implements ITest,Cloneable{
    private ETestImmutable builder = new ETestImmutable();

    @Override
    public String setID() {
        return null;
    }

    @Override
    public double setPrice() {
        return 0;
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public ITest createMutable() throws EProtoException {
        try {
            return clone();
        }catch (CloneNotSupportedException e){
            throw new EProtoException(EProtoException.ERROR_TYPE.CLONEFAILED,e);
        }
    }

    public final ITestImmutable createImmutable() throws EProtoException{
        try {
            return clone().builder;
        }catch (CloneNotSupportedException e){
            throw new EProtoException(EProtoException.ERROR_TYPE.CLONEFAILED,e);
        }
    }

    @Override
    protected ETest clone() throws CloneNotSupportedException {
        ETest temp= (ETest)super.clone();
        temp.builder = builder.clone();
        return  temp;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private class ETestImmutable implements ITestImmutable,Cloneable{

        @Override
        public String getID() {
            return null;
        }

        @Override
        public double getPrice() {
            return 0;
        }

        @Override
        public ITest createMutable() {
            return null;
        }

        @Override
        public ITestImmutable createImmutable()  throws EProtoException{
            return ETest.this.createImmutable();
        }

        @Override
        protected ETestImmutable clone() throws CloneNotSupportedException {
            return (ETestImmutable)super.clone();
        }
    }

}
