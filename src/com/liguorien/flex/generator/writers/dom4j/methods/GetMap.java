
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
import com.liguorien.flex.generator.FlexMap;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.utils.PropertyDescriptorProxy;
import com.liguorien.flex.generator.writers.dom4j.*;
import com.liguorien.flex.generator.writers.dom4j.MapKey;
import com.liguorien.flex.generator.writers.dom4j.TextElement;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetMap extends ContextMethodWriter  {

    private FlexMap _map;
    private PropertyDescriptor _prop;
    
    public GetMap(FlexMap map, String methodName,  PropertyDescriptor prop){
        super(methodName);    
        _map = map;
        _prop = prop;
    }
    
    public void writeMethod(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("org.dom4j.Element");
        handler.addImport("java.util.Map");
        handler.addImport(_map.key().getName());
        handler.addImport(_map.entry().getName());
        
        final String keyClassName = 
                Dom4jWriter.getWrapperName(_map.key().getSimpleName()); 
        
        final String entryClassName = 
                Dom4jWriter.getWrapperName(_map.entry().getSimpleName()); 
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 1, 
                entryClassName, keyClassName, getTypeName());
        g.writeIndentation(w, 1);
        w.write("public static <T extends Map<");
        w.write(keyClassName);
        w.write(", ");
        w.write(entryClassName);
        w.write(">> \n");
        g.writeIndentation(w, 3);
        w.write("T ");
        g.writeMethodName(w, getClass(), getTypeName());       
        w.write("(T map, Element els)");
        g.writeCurlyBrace(w, 1);
        g.writeIndentation(w, 2);
        w.write("if(els == null) return null;\n");
        g.writeIndentation(w, 2);
        w.write("for(Object obj: els.elements())");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("final Element el = (Element)obj;\n");
        g.writeIndentation(w, 3);
        w.write("map.put(");
        
        Dom4jWriter.setContextWriter(new MapKey(_map));
        
        final List<PropertyWriter<Dom4jBuilderHandler>> pws =
                handler.getPropertyWriters();
        try {
            
            final PropertyDescriptor proxy =
                    new PropertyDescriptorProxy(_map.key());
            
            for(PropertyWriter<Dom4jBuilderHandler> pw : pws){
                if(pw.acceptProperty(proxy, handler)){
                    pw.writeProperty(w, g, handler, "el", proxy);
                }
            }
            
        } catch (IntrospectionException ex) {
            Dom4jWriter.setContextWriter(null);
            throw new RuntimeException(ex);
        }
        
        
        w.write(", ");
        
        if(g.isGeneratedClass(_map.entry())){
           w.write('\n');
           g.writeIndentation(w, 5);
            handler.writeClassName(w, _map.entry(),
                    g.getClassPrefix(), g.getClassSuffix());
            w.write(handler.getBuilderSuffix());
            w.write(".getInstance(el)");
        }else{
            try {
                Dom4jWriter.setContextWriter(new TextElement());
                final PropertyDescriptor proxy =
                        new PropertyDescriptorProxy(_map.entry());
                
                for(PropertyWriter<Dom4jBuilderHandler> pw : pws){
                    if(pw.acceptProperty(proxy, handler)){
                        pw.writeProperty(w, g, handler, "el", proxy);
                    }
                }
                
            } catch (IntrospectionException ex) {
                Dom4jWriter.setContextWriter(null);
                throw new RuntimeException(ex);
            }
        }
        
        Dom4jWriter.setContextWriter(null);
        
        w.write(");\n");        
        g.writeIndentation(w, 2);
        w.write("}\n");
        g.writeIndentation(w, 2);
        w.write("return map;\n");
        g.writeIndentation(w, 1);
        w.write('}');
    }
}