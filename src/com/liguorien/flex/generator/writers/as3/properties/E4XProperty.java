
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
import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.writers.as3.BuilderPropertyWriter;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class E4XProperty extends BuilderPropertyWriter {
    
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
    public E4XProperty(
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
            PropertyDescriptor prop, FlexModelBuilderHandler handler) {
        
        if(AS3Writer.getContextWriter() == AS3Writer.WRITER){
            return _typeName.equals(
                    AS3Writer.getTypeName(prop.getPropertyType()));
        }
        
        return _typeName.equals(
                AS3Writer.getTypeName(prop.getPropertyType()));
    }
    
    /**
     * @inheritDoc
     */
    public void writeProperty(Writer w, FlexGenerator gen,
            FlexModelBuilderHandler handler, String varName,
            PropertyDescriptor prop)
            throws IOException {
        AS3Writer.getContextWriter().writeProperty(
                w, gen, varName, prop, _prefix, _suffix, null);
    }
    
    /**
     * @inheritDoc
     */
    public int getPriority(){
        return _priority;
    }
}