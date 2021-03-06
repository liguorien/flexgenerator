
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

package com.liguorien.flex.generator.writers.as3.methods;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.writers.as3.ContextMethodWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @version 0.2
 * @author Nicolas D�sy
 */
public class GetString extends ContextMethodWriter {
    
    /** Creates a new instance of GetString */
    public GetString() {
        super(GetString.class.getName());
    }

    public void writeMethod(Writer w, FlexGenerator g, 
            FlexModelBuilderHandler handler, Class<?> clazz) throws IOException{
        
        w.write("\n\n");
        g.writeDocumentation(w, getClass(), 2);
        g.writeIndentation(w, 2);
        w.write("public static function ");
        g.writeMethodName(w, getClass());
        w.write("(nod:XML):String");
        g.writeCurlyBrace(w, 2);
        g.writeIndentation(w, 3);
        w.write("if(nod == null) return null;\n");
        g.writeIndentation(w, 3);        
        w.write("return nod.toString();\n");
        g.writeIndentation(w, 2);
        w.write('}');
    }
    
}
