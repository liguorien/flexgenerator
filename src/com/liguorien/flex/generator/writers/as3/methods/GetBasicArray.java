
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

import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.utils.PropertyDescriptorProxy;
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.writers.as3.BuilderMethodWriter;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import com.liguorien.flex.generator.writers.as3.TextElement;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetBasicArray extends BuilderMethodWriter {
    
    private Class<?> _entryClass;
    private PropertyDescriptor _prop;
    private String _typeName;
    
    /** Creates a new instance of GetBasicArray */
    public GetBasicArray(Class<?> clazz, PropertyDescriptor prop) {
        _entryClass = clazz;
        _prop = prop;
        _typeName = com.liguorien.flex.generator.writers.dom4j.methods.GetArray.
                capitalize(AS3Writer.getTypeName(_entryClass));
    }
  
    public void writeMethod(Writer w, FlexGenerator g,
            FlexModelBuilderHandler handler, Class<?> clazz)
            throws IOException {
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 2, _typeName);
        g.writeIndentation(w, 2);
        w.write("public static function ");
        g.writeMethodName(w, GetBasicArray.class, _typeName);
        w.write("(nod:XML):Array");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("if(nod == null) return null;\n");
        g.writeIndentation(w, 3);
        w.write("var items:Array = new Array();\n");
        g.writeIndentation(w, 3);
        w.write("for each(var n:XML in nod.children())");
        g.writeCurlyBrace(w, 3);
        g.writeIndentation(w, 4);
        w.write("items.push(");
        
        try {
            
            final List<PropertyWriter<FlexModelBuilderHandler>> pws =
                    handler.getPropertyWriters();
            
            final PropertyDescriptor proxy =
                    new PropertyDescriptorProxy(_entryClass);
            
           
            AS3Writer.setContextWriter(new TextElement());
            
            for(PropertyWriter<FlexModelBuilderHandler> pw : pws){
                if(pw.acceptProperty(proxy, handler)){
                    pw.writeProperty(w, g, handler, "n", proxy);
                }
            }
            
            AS3Writer.setContextWriter(null);
            
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        }
        
        w.write(");\n");
        g.writeIndentation(w, 3);
        w.write("}\n");
        g.writeIndentation(w, 3);
        w.write("return items;\n");
        g.writeIndentation(w, 2);
        w.write('}');
    }
      
    public boolean equals(Object obj) {
        if(obj instanceof GetBasicArray){
            final GetBasicArray o = (GetBasicArray)obj;
            return _typeName.equals(o._typeName);
        }
        return false;
    }
    
    public int hashCode(){
        return _typeName.hashCode();
    }
}
