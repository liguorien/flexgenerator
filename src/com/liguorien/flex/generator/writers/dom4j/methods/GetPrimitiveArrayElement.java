
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
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.dom4j.BuilderMethodWriter;
import com.liguorien.flex.generator.writers.dom4j.ContextMethodWriter;
import com.liguorien.flex.generator.writers.dom4j.Dom4jWriter;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetPrimitiveArrayElement extends ContextMethodWriter {
    
    private Class<?> _entryClass;
    private PropertyDescriptor _prop;
    
    public GetPrimitiveArrayElement(
            Class<?> entryClass, PropertyDescriptor prop){
        super(entryClass.getSimpleName());
        _entryClass = entryClass;
        _prop = prop;
    }
    
    public void writeMethod(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("org.dom4j.Element");
        handler.addImport("org.dom4j.DocumentHelper");
        
        final String nodeName = FlexGenerator.getNodeName(clazz);
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 1,
                handler.getClassName(clazz).toString());
        g.writeIndentation(w, 1);
        w.write("public static Element ");
        g.writeMethodName(w, getClass(), _prop.getReadMethod().getName());
        w.write("(");
        w.write(getTypeName());
        w.write("[] arr)");
        g.writeCurlyBrace(w, 1);
        g.writeIndentation(w, 2);
        w.write("if(arr == null) return null;\n");
        g.writeIndentation(w, 2);
        w.write("final Element el = DocumentHelper.createElement(\"");
        w.write(nodeName);
        w.write("\");\n");       
        g.writeIndentation(w, 2);
        w.write("for(");
        w.write(Dom4jWriter.getWrapperName(getTypeName()));
        w.write(" obj : arr)");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("el.addElement(\"e\").\n");
        g.writeIndentation(w, 5);
        w.write("setText(String.valueOf(obj.toString()));\n");
        g.writeIndentation(w, 2);
        w.write("}\n");
        g.writeIndentation(w, 2);
        w.write("return el;\n");
        g.writeIndentation(w, 1);
        w.write("}");
    }
}