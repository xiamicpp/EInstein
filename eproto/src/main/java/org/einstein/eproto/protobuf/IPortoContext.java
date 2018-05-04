package org.einstein.eproto.protobuf;

import org.einstein.eproto.core.IField;

import java.util.List;

/**
 * velocity variable context
 * @author kevin
 **/
public interface IPortoContext {

    String getVersion();

    String getPackage();

    String getClassname();

    String getMessage();

    List<IField> getFields();
}
