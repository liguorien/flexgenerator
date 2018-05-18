
/**
 *   Copyright (C) 2006 Nicolas Désy.  All rights reserved.
 *   
 *   This file is part of FlexGenerator
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *   
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package com.liguorien.flex.generator.writers.dom4j.properties;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.FlexList;
import com.liguorien.flex.generator.utils.NodeType;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.FlexSet;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.dom4j.*;
import com.liguorien.flex.generator.writers.dom4j.methods.GetCollection;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 
 * @version 0.2
 * @author Nicolas Désy
 */
public class CollectionProperty
        implements PropertyWriter<Dom4jBuilderHandler> {
    
    private int _priority;
    
    public CollectionProperty(int priority){
        _priority = priority;
    }
    
    public boolean acceptProperty(
            PropertyDescriptor prop, Dom4jBuilderHandler handler) {
        return Collection.class.isAssignableFrom(prop.getPropertyType()) &&
                (prop.getReadMethod().isAnnotationPresent(FlexList.class) ||
                prop.getReadMethod().isAnnotationPresent(FlexSet.class));
    }
    
    public void writeProperty(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, String varName,
            PropertyDescriptor prop)
            throws IOException {
        
        final Class<?> type = prop.getPropertyType();
        final Method getter = prop.getReadMethod();
        
        Class<?> entryClass = null;
        Class<?> collClass = null;
        
        if(getter.isAnnotationPresent(FlexList.class)){
            entryClass = getter.getAnnotation(FlexList.class).value();
            collClass = getter.getAnnotation(FlexList.class).clazz();
        }else if(getter.isAnnotationPresent(FlexSet.class)){
            entryClass = getter.getAnnotation(FlexSet.class).value();
            collClass = getter.getAnnotation(FlexSet.class).clazz();
        }
        
        if(entryClass == null){
            return ;
        }
        
        handler.addImport(entryClass.getName());
        handler.addImport(collClass.getName());
        
        if (g.isGeneratedClass(entryClass)){
            
            handler.addImport(
                    g.getPackageName(entryClass) + '.' +
                    entryClass.getSimpleName() + handler.getBuilderSuffix());
            
            Dom4jWriter.getContextWriter().writeProperty(w, g, varName, prop,
                    entryClass.getSimpleName() +
                    handler.getBuilderSuffix() +
                    ".getEntityCollection(\n" + g.getIndentation(4) +
                    "new " + collClass.getSimpleName()
                    + "<" +
                    Dom4jWriter.getWrapperName(entryClass.getSimpleName()
                    ) + ">(), ", ")", NodeType.ELEMENT);
        }else{
            handler.addContextMethodWriter(
                    new GetCollection(
                    entryClass, collClass, getter.getName(), prop));
            Dom4jWriter.getContextWriter().writeProperty(w, g, varName, prop,
                    getter.getName() + "(\n" + g.getIndentation(4) +
                    "new " + collClass.getSimpleName()
                    + "<" +
                    Dom4jWriter.getWrapperName(entryClass.getSimpleName()
                    ) + ">(), ", ")", NodeType.ELEMENT);
            
            //Dom4jWriter.setContextWriter(null);
        }
    }
    
    
    /**
     * @inheritDoc
     */
    public int getPriority(){
        return _priority;
    }
}
