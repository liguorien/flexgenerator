
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

package com.liguorien.flex.generator.writers.as3.methods;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.utils.LazyMode;
import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.writers.PropertyWriter;


import com.liguorien.flex.generator.writers.as3.BuilderMethodWriter;
import com.liguorien.flex.generator.writers.as3.properties.ArrayElement;
import com.liguorien.flex.generator.writers.as3.properties.BasicElement;
import com.liguorien.flex.generator.writers.as3.properties.BasicMapElement;
import com.liguorien.flex.generator.writers.as3.properties.EntityElement;
import com.liguorien.flex.generator.writers.as3.properties.EntityMapElement;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetXML extends BuilderMethodWriter {
    
    public static GetXML getDefault(){
        final GetXML w = new GetXML();
        w.addPropertyWriter(new ArrayElement());
        w.addPropertyWriter(new EntityElement());
        w.addPropertyWriter(new BasicMapElement());
        w.addPropertyWriter(new EntityMapElement());
        w.addPropertyWriter(new BasicElement());
        //w.addPropertyWriter(E)
        return w;
    }
    
    
    private final List<PropertyWriter<FlexModelBuilderHandler>> _propertyWriters
            = new ArrayList<PropertyWriter<FlexModelBuilderHandler>>();
    
    
    public void addPropertyWriter(
            PropertyWriter<FlexModelBuilderHandler> writer){
        
        _propertyWriters.add(writer);
        Collections.sort(_propertyWriters, PropertyWriter.COMPARATOR);
    }
    
    public void writeMethod(Writer w,
            FlexGenerator g, FlexModelBuilderHandler handler, Class<?> clazz)
            throws IOException {
        
        final String classNodeName = FlexGenerator.getNodeName(clazz);
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 2, clazz.getSimpleName());
        g.writeIndentation(w, 2);
        w.write("public static function ");
        g.writeMethodName(w, getClass());
        w.write("(o:");
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        w.write(", name:String=null):XML");
        g.writeCurlyBrace(w, 2);
        w.flush();
        g.writeIndentation(w, 3);
        w.write("if(o == null) return new XML(\"\");\n");
        g.writeIndentation(w, 3);
        w.write("var el:XML = <");
        w.write(classNodeName);
        
        for (PropertyDescriptor prop : handler.getAttributes()) {
            if (!FlexGenerator.isLazy(prop, LazyMode.CLIENT_WRITE)) {
                w.write(" ");
                w.write(FlexGenerator.getNodeName(prop));
                w.write("={o.");
                w.write(prop.getName());
                w.write("}");
            }
        }
        
        if (handler.getElements().size() == 0) {
            w.write("\n");
            g.writeIndentation(w, 3);
            w.write("/>;");
        } else {
            w.write(">");            
            
            g.writeProperties(w, g, handler, "o", 
                handler.getElements(), 
                _propertyWriters, 
                LazyMode.CLIENT_WRITE);
            
            w.write("\n");
            g.writeIndentation(w, 3);
            w.write("</");
            w.write(classNodeName);
            w.write(">;\n");
            g.writeIndentation(w, 3);
            w.write("if(name != null) el.setName(name);\n");
            g.writeIndentation(w, 3);
            w.write("return el;\n");
            g.writeIndentation(w, 2);
            w.write("}");
        }
    }
}