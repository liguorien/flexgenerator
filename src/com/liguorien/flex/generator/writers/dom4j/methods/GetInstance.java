
/**
 *   Copyright (C) 2006 Nicolas D�sy.  All rights reserved.
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
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import com.liguorien.flex.generator.writers.dom4j.BuilderMethodWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @version 0.2
 * @author Nicolas D�sy
 */
public class GetInstance extends BuilderMethodWriter  {
    
    /**
     * @inheritDoc
     */
    public void writeMethod(Writer w, FlexGenerator g,
            Dom4jBuilderHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("org.dom4j.Element");
        handler.addImport(clazz.getName());
        
        final String className = handler.getClassName(clazz).toString();
        g.writeDocumentation(w, getClass(), 1, className);
        
        g.writeIndentation(w, 1);
        w.write("public static ");
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        w.write(" ");
        g.writeMethodName(w, getClass());
        w.write("(Element el)");
        g.writeCurlyBrace(w, 1);
        g.writeIndentation(w, 2);
        w.write("if(el == null) return null;\n");
        g.writeIndentation(w, 2);
        w.write("final ");
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        w.write(" o = new ");
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        w.write("();");
        FlexGenerator.writeProperties(w, g, handler, "o",
                handler.getAttributes(),
                handler.getPropertyWriters(), LazyMode.SERVER_READ);
        FlexGenerator.writeProperties(w, g, handler, "o",
                handler.getElements(),
                handler.getPropertyWriters(), LazyMode.SERVER_READ);
        w.write('\n');
        g.writeIndentation(w, 2);
        w.write("return o;\n");
        g.writeIndentation(w, 1);
        w.write('}');
    }
}