package org.einstein.athena.proto.core;

import org.einstein.framework.IField;

import java.util.List;

/**
 * velocity variable context
 *
 * @author kevin
 **/
public interface IPortoContext {

    String getVersion();

    String getPackage();

    String getClassname();

    String getMessage();

    List<IField> getFields();
}
