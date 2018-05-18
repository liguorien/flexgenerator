
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

package com.liguorien.flex.generator.writers.dom4j;

import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.FlexNode;
import com.liguorien.flex.generator.utils.NodeType;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * Generate java code which retrieve text content of a dom4j element. 
 * @version 0.2
 * @author Nicolas Désy
 */
public class TextElement extends Dom4jWriter {    
    
    public void writeProperty(Writer w, FlexGenerator g, String varName, 
            PropertyDescriptor prop, String prefix, String suffix, 
            NodeType type) throws IOException {
        
        if(prefix != null){
            w.write(prefix);
        }
        
        w.write(varName);
        w.write(".getTextTrim()");
        
        if(suffix != null){
            w.write(suffix);
        }
    }    
}