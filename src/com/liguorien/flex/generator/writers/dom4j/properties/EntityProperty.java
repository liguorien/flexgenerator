
/**
 *   Copyright (C) 2006 Nicolas D�sy.  All rights reserved.
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
import com.liguorien.flex.generator.utils.NodeType;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.dom4j.Dom4jWriter;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @version 0.2
 * @author Nicolas D�sy
 */
public class EntityProperty
        implements PropertyWriter<Dom4jBuilderHandler> {
    
    private int _priority;
    
    public EntityProperty(int priority){
        _priority = priority;
    }
    
    public int getPriority() {
        return _priority;
    }
    
    public boolean acceptProperty(
            PropertyDescriptor prop, Dom4jBuilderHandler handler) {
        return Dom4jWriter.getContextWriter() == Dom4jWriter.WRITER &&
                handler.getGenerator().isGeneratedClass(prop.getPropertyType());
    }
    
    public void writeProperty(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, String varName,
            PropertyDescriptor prop) throws IOException {
        
        final Class<?> entryClass = prop.getPropertyType();
        
        handler.addImport(
                g.getPackageName(entryClass) + '.' +
                entryClass.getSimpleName() + handler.getBuilderSuffix());
        
        Dom4jWriter.getContextWriter().writeProperty(w, g, varName, prop,
                entryClass.getSimpleName() +
                handler.getBuilderSuffix() +
                ".getInstance(", ")", NodeType.ELEMENT);
    }
    
}