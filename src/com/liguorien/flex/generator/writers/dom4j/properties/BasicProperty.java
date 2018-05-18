
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
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.*;
import com.liguorien.flex.generator.writers.dom4j.*;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class BasicProperty implements PropertyWriter<Dom4jBuilderHandler> {
    
    private String _typeName;
    private String _prefix;
    private String _suffix;
    private int _priority;
    
    /**
     *
     * @param typeName
     * @param prefix
     * @param suffix
     * @param priority
     */
    public BasicProperty(
            String typeName, String prefix, String suffix, int priority){
        _typeName = typeName;
        _prefix = prefix;
        _suffix = suffix;
        _priority = priority;
    }
    
    /**
     * @inheritDoc
     */
    public boolean acceptProperty(
                PropertyDescriptor prop, Dom4jBuilderHandler handler) {
        return _typeName.equals(prop.getPropertyType().getSimpleName());
    }
    
    /**
     *
     * @param w
     * @param varName
     * @param prop
     * @throws java.io.IOException
     */
    public void writeProperty(Writer w, FlexGenerator g, 
            Dom4jBuilderHandler handler, String varName, 
            PropertyDescriptor prop) throws IOException {
        
        Dom4jWriter.getContextWriter().writeProperty(
                w, g, varName, prop, _prefix, _suffix, null);
    }
    
    /**
     * @inheritDoc
     */
    public int getPriority(){
        return _priority;
    }
}