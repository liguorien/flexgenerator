
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
import com.liguorien.flex.generator.utils.NodeType;
import com.liguorien.flex.generator.writers.as3.BuilderPropertyWriter;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityArray;
import com.liguorien.flex.generator.writers.as3.methods.GetInstance;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public class EntityProperty extends BuilderPropertyWriter {
    
    /** Creates a new instance of EntityProperty */
    public EntityProperty() {
    }

    public boolean acceptProperty(
            PropertyDescriptor prop, FlexModelBuilderHandler handler) {
        return handler.getGenerator().isGeneratedClass(prop.getPropertyType());
    }

    public void writeProperty(Writer w, FlexGenerator g, 
            FlexModelBuilderHandler handler, String varName, 
            PropertyDescriptor prop) throws IOException {
        
        final Class<?> clazz = prop.getPropertyType();
        
        handler.addImport(
                g.getPackageName(clazz) + '.' +
                handler.getClassName(clazz) + handler.getBuilderSuffix());
    
        AS3Writer.getContextWriter().writeProperty(w, g, varName, prop,
                handler.getClassName(clazz) +
                handler.getBuilderSuffix() +
                "." + g.getMethodName(GetInstance.class, clazz)
                + "(", "[0])", NodeType.ELEMENT);
    }
    
    
    
}
