package org.einstein.codegen.core;

import org.einstein.codegen.api.IField;

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
