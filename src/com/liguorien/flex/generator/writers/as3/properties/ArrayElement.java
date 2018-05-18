
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

package com.liguorien.flex.generator.writers.as3.properties;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.FlexList;
import com.liguorien.flex.generator.FlexSet;
import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.writers.as3.BuilderPropertyWriter;
import com.liguorien.flex.generator.writers.as3.methods.GetBasicXMLList;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityXMLList;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public class ArrayElement extends BuilderPropertyWriter {
    
    private Class<?> _entryClass;
      
    /**
     * @inheritDoc
     */
    public boolean acceptProperty(
            PropertyDescriptor prop, FlexModelBuilderHandler handler) {
        
        _entryClass = null;
        
        final Class<?> type = prop.getPropertyType();
        
        if(type.isArray()){
            _entryClass = type.getComponentType();
        }
        
        if(Collection.class.isAssignableFrom(type)){
            
            final Method getter = prop.getReadMethod();
            
            if(getter.isAnnotationPresent(FlexList.class)){
                _entryClass = getter.getAnnotation(FlexList.class).value();
            } else if(getter.isAnnotationPresent(FlexSet.class)){
                _entryClass = getter.getAnnotation(FlexSet.class).value();
            }
        }
        
        return _entryClass != null;
    }
    
    /**
     * @inheritDoc
     */
    public void writeProperty(Writer w, FlexGenerator g,
            FlexModelBuilderHandler handler, String varName,
            PropertyDescriptor prop) throws IOException {
        
        
        w.write('\n');
        g.writeIndentation(w, 4);
        w.write('{');
        
        if(g.isGeneratedClass(_entryClass)){
            
            // ENTITY ARRAY
            final String entityClassName = _entryClass.getSimpleName();
            
            w.write(handler.getClassName(_entryClass).toString());
            w.write(handler.getBuilderSuffix());
            w.write('.');
            g.writeMethodName(w, GetEntityXMLList.class, entityClassName);
            
        }else{
            // BASIC ARRAY
            g.writeMethodName(w, GetBasicXMLList.class,
                    _entryClass.getSimpleName());
        }
        
        w.write("(o.");
        w.write(prop.getName());
        w.write(", \"");
        w.write(FlexGenerator.getNodeName(prop));
        w.write("\")}");
    }
}
