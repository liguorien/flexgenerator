
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
import com.liguorien.flex.generator.writers.*;
import com.liguorien.flex.generator.writers.as3.BuilderMethodWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetInstance extends BuilderMethodWriter {
        
    /**
     *
     * @param w
     * @param g
     * @param handler
     * @param clazz
     * @throws java.io.IOException
     */
    public void writeMethod(Writer w, FlexGenerator g,
            FlexModelBuilderHandler handler, Class<?> clazz) throws IOException{
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 2, clazz.getSimpleName());
        g.writeIndentation(w, 2);
        w.write("public static function ");
        g.writeMethodName(w, getClass(), clazz.getSimpleName()); 
        w.write("(nod:XML):");
        
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("if(nod == null) return null;\n");
        g.writeIndentation(w, 3);
        
        w.write("var o:");
        
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        w.write(" = new ");
        handler.writeClassName(w, clazz,
                g.getClassPrefix(), g.getClassSuffix());
        w.write("();");
        
        FlexGenerator.writeProperties(w, g, handler, "o",
                handler.getAttributes(),
                handler.getPropertyWriters(), LazyMode.CLIENT_READ);
        
        FlexGenerator.writeProperties(w, g, handler, "o",
                handler.getElements(),
                handler.getPropertyWriters(), LazyMode.CLIENT_READ);
        
        w.write("\n");
        g.writeIndentation(w, 3);
        w.write("return o;");
        w.write("\n");
        g.writeIndentation(w, 2);
        w.write("}");
    }    
}