
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
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.writers.dom4j.methods.GetEntityArrayElement;
import com.liguorien.flex.generator.writers.dom4j.methods.GetPrimitiveArrayElement;



import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class ArrayElement implements PropertyWriter<Dom4jBuilderHandler> {
    
    /**
     * @inheritDoc
     */
    public int getPriority() {
        return 3;
    }
    
    /**
     * <p>Accepts properties which type is an array.</p>
     * @inheritDoc
     */
    public boolean acceptProperty(
            PropertyDescriptor prop, Dom4jBuilderHandler handler) {
        return prop.getPropertyType().isArray();
    }
    
    /**
     * @inheritDoc
     */
    public void writeProperty(
            Writer w, FlexGenerator g, Dom4jBuilderHandler handler,
            String varName, PropertyDescriptor prop) throws IOException {
        
        final Class<?> type = prop.getPropertyType();
        final Method getter = prop.getReadMethod();
        final Class<?> entryClass = type.getComponentType();
        
        w.write('\n');
        g.writeIndentation(w, 2);
        w.write("nested = ");
        
        if(g.isGeneratedClass(entryClass)){
            
            //handler.addContextMethodWriter(new GetEntityArrayElement());
            
            handler.addImport(g.getPackageName(entryClass) + '.' +
                    entryClass.getSimpleName() +
                    handler.getBuilderSuffix());
            
            handler.writeClassName(w, entryClass, g.getClassPrefix(),
                    g.getClassSuffix());
            w.write(handler.getBuilderSuffix());
            w.write(".\n");
            g.writeIndentation(w, 4);
            g.writeMethodName(w, GetEntityArrayElement.class,
                    entryClass.getSimpleName(), getter.getName());
            w.write("(o.");
            
            w.write(getter.getName());
            w.write("());\n");
        }else{
            
           // if(entryClass.isPrimitive()){
                handler.addContextMethodWriter(
                        new GetPrimitiveArrayElement(entryClass, prop));
          //  }
            
            g.writeMethodName(w, GetPrimitiveArrayElement.class,
                    getter.getName());
         
            w.write("(o.");
            
            w.write(getter.getName());
            w.write("());\n");
        }
        
        g.writeIndentation(w, 2);
        w.write("if(nested != null)");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("nested.setName(\"");
        w.write(FlexGenerator.getNodeName(prop));
        w.write("\");\n");
        g.writeIndentation(w, 3);
        w.write("el.add(nested);\n");
        g.writeIndentation(w, 2);
        w.write("}");
    }
}