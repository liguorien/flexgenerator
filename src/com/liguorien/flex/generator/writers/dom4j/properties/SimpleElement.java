
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
import com.liguorien.flex.generator.writers.PropertyWriter;
import com.liguorien.flex.generator.handlers.Dom4jBuilderHandler;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class SimpleElement implements PropertyWriter<Dom4jBuilderHandler> {
    
    public int getPriority() {
        return 0;
    }
    
    public boolean acceptProperty(PropertyDescriptor prop, Dom4jBuilderHandler handler) {
        return true;
    }
    
    public void writeProperty(Writer w, FlexGenerator g, Dom4jBuilderHandler handler, String varName, PropertyDescriptor prop) throws IOException {
        
        final Method getter = prop.getReadMethod();
        w.write("\n");
        g.writeIndentation(w, 2);
        w.write("str = String.valueOf(o.");
        w.write(getter.getName());
        w.write("());\n");
        g.writeIndentation(w, 2);
        w.write("el.addElement(\"");
        w.write(FlexGenerator.getNodeName(prop));
        w.write("\").setText((str == null) ? \"\" : str);");
    }
    
}