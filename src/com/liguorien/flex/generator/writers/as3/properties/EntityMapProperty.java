
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
import com.liguorien.flex.generator.FlexMap;
import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.utils.NodeType;
import com.liguorien.flex.generator.writers.as3.BuilderPropertyWriter;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import com.liguorien.flex.generator.writers.as3.methods.GetEntityMap;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public class EntityMapProperty extends BasicMapProperty {
    
    public EntityMapProperty() {
        super();
    }
    
    public int getPriority() {
        return 3;
    }
    
    public boolean acceptProperty(
            PropertyDescriptor prop, FlexModelBuilderHandler handler) {
        
        return super.acceptProperty(prop, handler) &&
                handler.getGenerator().isGeneratedClass(_entryClass);
    }
    
    /**
     *
     */
    public void writeProperty(Writer w, FlexGenerator g,
            FlexModelBuilderHandler handler, String varName,
            PropertyDescriptor prop) throws IOException {
        
        handler.addImport(
                g.getPackageName(_entryClass) + '.' +
                handler.getClassName(_entryClass) + handler.getBuilderSuffix());
        
        AS3Writer.getContextWriter().writeProperty(w, g, varName, prop,
                handler.getClassName(_entryClass) +
                handler.getBuilderSuffix() +
                "." + g.getMethodName(GetEntityMap.class, _entryClass)
                + "(", "[0])",
                NodeType.ELEMENT);
    }
}
