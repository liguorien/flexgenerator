
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
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.utils.PropertyDescriptorProxy;
import com.liguorien.flex.generator.writers.dom4j.BuilderMethodWriter;
import com.liguorien.flex.generator.writers.dom4j.ContextMethodWriter;
import com.liguorien.flex.generator.writers.dom4j.Dom4jWriter;
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
public class GetCollection extends ContextMethodWriter {

    private String _methodName;
    private Class<?> _entryClass;
    private Class<?> _collClass;
    private PropertyDescriptor _prop;
    
    public GetCollection(Class<?> entryClass, Class<?> collClazz, 
            String methodName, PropertyDescriptor prop){

        super(methodName);
        
        _entryClass = entryClass;
        _collClass = collClazz;
        _methodName = methodName;
        _entryClass = entryClass;
        _prop = prop;
    }
    
    public void writeMethod(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("org.dom4j.Element");
        handler.addImport("java.util.Collection");
        
        if(g.isGeneratedClass(_entryClass)){
            handler.addImport(g.getPackageName(clazz) + '.' +
                    clazz.getSimpleName() + handler.getBuilderSuffix());
        }else{
            handler.addImport(_entryClass.getName());
        }
        
        handler.addImport(_collClass.getName());
        final String typeName =
                Dom4jWriter.getWrapperName(_entryClass.getSimpleName());
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 1, typeName);
        g.writeIndentation(w, 1);
        w.write("public static <T extends Collection<");
        w.write(typeName);
        w.write(">> \n");
        g.writeIndentation(w, 3);
        w.write("T ");
        g.writeMethodName(w, getClass(), _methodName);       
        w.write("(T coll, Element els)");
        g.writeCurlyBrace(w, 1);
        g.writeIndentation(w, 2);
        w.write("if(els == null) return null;\n");
        g.writeIndentation(w, 2);
        w.write("for(Object obj: els.elements())");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("final Element el = (Element)obj;\n");
        g.writeIndentation(w, 3);
        w.write("coll.add(");
        
        if(g.isGeneratedClass(_entryClass)){
            
            handler.writeClassName(w, _entryClass,
                    g.getClassPrefix(), g.getClassSuffix());
            w.write(handler.getBuilderSuffix());
            w.write(".getInstance(el)");
        }else{
            try {
                
                final List<PropertyWriter<Dom4jBuilderHandler>> pws =
                        handler.getPropertyWriters();
                
                Dom4jWriter.setContextWriter(new TextElement());
                final PropertyDescriptor proxy =
                        new PropertyDescriptorProxy(_entryClass);
                
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
        w.write("return coll;\n");
        g.writeIndentation(w, 1);
        w.write('}');
    }
}