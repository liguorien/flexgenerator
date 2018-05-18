
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

package com.liguorien.flex.generator.writers.as3;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.handlers.FlexGeneratorHandler;
import com.liguorien.flex.generator.handlers.FlexModelBuilderHandler;
import com.liguorien.flex.generator.writers.PropertyWriter;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;

/**
 * @version 0.2
 * @author Nicolas Désy
 */
public class E4XElement 
        implements PropertyWriter<FlexModelBuilderHandler> {
    
    private PropertyWriter<FlexModelBuilderHandler> _nestedWriter;
    
    /**
     * Creates a new instance of E4XElement
     */
    public E4XElement(PropertyWriter<FlexModelBuilderHandler> pw) {
        _nestedWriter = pw;
    }
    
    public void writeProperty(Writer w, FlexGenerator g,
            FlexModelBuilderHandler handler, String varName,
            PropertyDescriptor prop) throws IOException{        
      
        w.write("\n");
        g.writeIndentation(w, 4);
        w.write('{');
        _nestedWriter.writeProperty(w, g, handler, varName, prop);
        w.write('}');
    }
    
    public int getPriority() {
        return _nestedWriter.getPriority();
    }
    
    public boolean acceptProperty(
            PropertyDescriptor prop, FlexModelBuilderHandler handler) {
        return _nestedWriter.acceptProperty(prop, handler);
    }
}
