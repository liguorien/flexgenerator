
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

package com.liguorien.flex.generator.writers.dom4j.methods;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.utils.LazyMode;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.dom4j.BuilderMethodWriter;
import com.liguorien.flex.generator.writers.dom4j.properties.ArrayElement;
import com.liguorien.flex.generator.writers.dom4j.properties.CollectionElement;
import com.liguorien.flex.generator.writers.dom4j.properties.EntityElement;
import com.liguorien.flex.generator.writers.dom4j.properties.MapElement;
import com.liguorien.flex.generator.writers.dom4j.properties.SimpleElement;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetElement extends BuilderMethodWriter {
    
    public static GetElement getDefault(){
        final GetElement w = new GetElement();
        w.addPropertyWriter(new SimpleElement());
        w.addPropertyWriter(new ArrayElement());
        w.addPropertyWriter(new EntityElement());
        w.addPropertyWriter(new MapElement());
        w.addPropertyWriter(new CollectionElement());
        return w;
    }
    
    
    private final List<PropertyWriter<Dom4jBuilderHandler>> _propertyWriters
            = new ArrayList<PropertyWriter<Dom4jBuilderHandler>>();
    
    /**
     * Creates a new instance of GetElement
     */
    public GetElement() {
    }
    
    public void addPropertyWriter(
            PropertyWriter<Dom4jBuilderHandler> writer){
        _propertyWriters.add(writer);
        Collections.sort(_propertyWriters,
                PropertyWriter.COMPARATOR);
    }
    
    public void writeMethod(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("org.dom4j.Element");
        handler.addImport("org.dom4j.DocumentHelper");
        
        final String className = handler.getClassName(clazz).toString();
        
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 1, className);
        g.writeIndentation(w, 1);
        w.write("public static Element ");
        g.writeMethodName(w, getClass());
        w.write("(");
        handler.writeClassName(w, clazz, g.getClassPrefix(),
                g.getClassSuffix());
        w.write(" o)");
        g.writeCurlyBrace(w, 1);
        g.writeIndentation(w, 2);
        w.write("if(o == null) return null;\n");
        g.writeIndentation(w, 2);
        w.write("final Element el = DocumentHelper.createElement(\"");
        w.write(FlexGenerator.getNodeName(clazz));
        w.write("\");\n");
        g.writeIndentation(w, 2);
        w.write("String str = null;\n");
        g.writeIndentation(w, 2);
        w.write("Element nested = null;");
        
        for(PropertyDescriptor prop : handler.getAttributes()){
            
            if(FlexGenerator.isLazy(prop, LazyMode.SERVER_WRITE)){
                continue;
            }
            
            final Class<?> type = prop.getPropertyType();
            final Method getter = prop.getReadMethod();
                        
            if(g.isGeneratedClass(type)){
                throw new IllegalArgumentException(
                        "A complex type cannot be used as XML attribute " +
                        "("+clazz.getName()+"."+prop.getName()+")");
            }else{
                w.write("\n");
                g.writeIndentation(w, 2);
                w.write("str = String.valueOf(o.");
                w.write(getter.getName());
                w.write("());\n");
                g.writeIndentation(w, 2);
                w.write("el.addAttribute(\"");
                w.write(FlexGenerator.getNodeName(prop));
                w.write("\", ((str == null) ? \"\" : str));");
            }
        }
        
        g.writeProperties(w, g, handler, "o", 
                handler.getElements(), 
                _propertyWriters, 
                LazyMode.SERVER_WRITE);
        
        w.write('\n');
        g.writeIndentation(w, 2);
        w.write("return el;\n");
        g.writeIndentation(w, 1);
        w.write('}');
    }
}
