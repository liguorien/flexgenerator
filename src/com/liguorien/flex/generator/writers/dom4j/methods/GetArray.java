
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
import com.liguorien.flex.generator.writers.dom4j.BuilderMethodWriter;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.utils.PropertyDescriptorProxy;
import com.liguorien.flex.generator.writers.dom4j.ContextMethodWriter;
import com.liguorien.flex.generator.writers.dom4j.Dom4jWriter;
import com.liguorien.flex.generator.writers.dom4j.TextElement;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * <p>Generates a method who builds an array from a dom4j element.</p>
 * <p>The generated code looks like this : </p>
 * <pre>
 * /**
 *  * Build a Person array from a dom4j element.
 *  * @param el A dom4j element. 
 *  * @return A Person array.
 *  *&#47;
 * public static Person[] getContacts(Element el) {
 *     if(el == null) return null;
 *     final List els = el.elements();
 *     final int size = els.size();
 *     final Person[] items = new Person[size];
 *     for(int i = 0; i &lt; size; i++) {
 *         final Element e = (Element)els.get(i);
 *         items[i] = PersonBuilder.getInstance(e);
 *     }
 *     return items;
 * }
 * </pre>
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetArray extends ContextMethodWriter {
    
    private Class<?> _entryClass;
    private PropertyDescriptor _prop;
   
    /**
     * Create a new instance of GetArray
     * @param entryClass The entry type of the array
     * @param prop The property which requested this method generation.
     */
    public GetArray(Class<?> entryClass, PropertyDescriptor prop){
        super(entryClass.getSimpleName());
        _prop = prop;
        _entryClass = entryClass;
    }
    
    
    /**
     * @inheritDoc 
     */
    public void writeMethod(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("org.dom4j.Element");
        handler.addImport("java.util.List");
       
        if(g.isGeneratedClass(_entryClass)){
            handler.addImport(g.getPackageName(clazz) + '.' +
                    clazz.getSimpleName() + handler.getBuilderSuffix());
        }else{
            if(!_entryClass.isPrimitive()){
                handler.addImport(_entryClass.getName());
            }
        }
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 1, _entryClass.getSimpleName());
        g.writeIndentation(w, 1);
        w.write("public static ");
        w.write(_entryClass.getSimpleName());
        
        w.write("[] ");
        g.writeMethodName(w, getClass(), getTypeName(), 
                _prop.getReadMethod().getName());
      
        w.write("(Element el)");
        g.writeCurlyBrace(w, 1);
        g.writeIndentation(w, 2);
        w.write("if(el == null) return null;\n");
        g.writeIndentation(w, 2);
        w.write("final List els = el.elements();\n");
        g.writeIndentation(w, 2);
        w.write("final int size = els.size();\n");
        g.writeIndentation(w, 2);
        w.write("final ");
        w.write(_entryClass.getSimpleName());
        w.write("[] items = new ");
        w.write(_entryClass.getSimpleName());
        w.write("[size];\n");
        g.writeIndentation(w, 2);
        w.write("for(int i=0; i<size; i++)");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("final Element e = (Element)els.get(i);\n");
        g.writeIndentation(w, 3);
        w.write("items[i] = ");
        
        if(g.isGeneratedClass(_entryClass)){
            handler.writeClassName(w, _entryClass,
                    g.getClassPrefix(), g.getClassSuffix());
            w.write(handler.getBuilderSuffix());
            w.write(".");
            g.writeMethodName(w, GetInstance.class, getTypeName());
            w.write("(e)");
        }else{
            try {
                
                final List<PropertyWriter<Dom4jBuilderHandler>> pws =
                        handler.getPropertyWriters();
                
                Dom4jWriter.setContextWriter(new TextElement());
                final PropertyDescriptor proxy =
                        new PropertyDescriptorProxy(_entryClass);
                
                for(PropertyWriter<Dom4jBuilderHandler> pw : pws){
                    if(pw.acceptProperty(proxy, handler)){
                        pw.writeProperty(w, g, handler, "e", proxy);
                    }
                }
                
            } catch (IntrospectionException ex) {
                Dom4jWriter.setContextWriter(null);
                throw new RuntimeException(ex);
            }
        }
        
        w.write(";\n");
        g.writeIndentation(w, 2);
        w.write("}\n");
        g.writeIndentation(w, 2);
        w.write("return items;\n");
        g.writeIndentation(w, 1);
        w.write('}');
        
        Dom4jWriter.setContextWriter(null);
    }    
        
    /**
     * //FIXME: should not be here...
     * @param name 
     * @return The same string with the first letter capitalized
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}