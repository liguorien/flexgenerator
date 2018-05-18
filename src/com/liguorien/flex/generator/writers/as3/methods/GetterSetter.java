
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

import com.liguorien.flex.generator.FlexBindable;
import com.liguorien.flex.generator.FlexDefault;
import com.liguorien.flex.generator.FlexGenerator;
import com.liguorien.flex.generator.writers.MethodWriter;
import com.liguorien.flex.generator.handlers.FlexModelHandler;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class GetterSetter
        implements MethodWriter<FlexModelHandler> {
    
    /**
     * 
     * @param w 
     * @param gen 
     * @param handler 
     * @param clazz 
     * @throws java.io.IOException 
     */
    public void writeMethod(Writer w, FlexGenerator gen,
            FlexModelHandler handler, Class<?> clazz) throws IOException {
        
        handler.addImport("flash.events.Event");
        handler.addImport("flash.events.EventDispatcher");
        handler.addImport("flash.utils.Dictionary");
        
        for(PropertyDescriptor prop : handler.getAttributes()){
            handleProperty(w, prop, handler);
        }
        
        for(PropertyDescriptor prop : handler.getElements()){
            handleProperty(w, prop, handler);
        }
    }
    
    /**
     * 
     * @param property 
     * @param handler 
     */
    public void handleProperty(Writer w,
            PropertyDescriptor property, FlexModelHandler handler) {
        
        final Class type = property.getPropertyType();
        final Method getter = property.getReadMethod();
        
        String bindingEvent = null;
        String defaultValue = null;
        
        if(getter.isAnnotationPresent(FlexBindable.class)){
            bindingEvent = getter.
                    getAnnotation(FlexBindable.class).
                    event();
        }else if(handler.isBindingEverything()){
            bindingEvent = handler.getDefaultEvent();
        }
        
        if(getter.isAnnotationPresent(FlexDefault.class)){
            defaultValue = getter.
                    getAnnotation(FlexDefault.class).
                    value();
        }
        
        final String typeName = handler.getGenerator().isGeneratedClass(type)
            ? handler.getClassName(type).toString()
            : AS3Writer.getTypeName(type);
        
        appendProperty(w, handler, property.getName(), 
                typeName, bindingEvent, defaultValue, 
                property.getWriteMethod() == null);
    }        
    
    
    /**
     * Append a property to the model
     * @param handler 
     * @param readOnly 
     * @param name The name of the property
     * @param type The type of the property
     * @param binding Indicated if the property should be bindable
     * @param defaultValue The default value of the property
     */
    private void appendProperty(Writer w, FlexModelHandler handler, String name,
            String type, String binding, String defaultValue, boolean readOnly){
        
        try {
       
            final FlexGenerator g = handler.getGenerator();
            
            w.write("\n\n");
            g.writeIndentation(w, 2);
            w.write("private var ");
            
            if(g.isUsingUnderscore()){
                w.write('_');
            }
            
            w.write(name);
            w.write(':');
            w.write(type);
            
            if(defaultValue != null){
                if("new".equals(defaultValue)){
                    w.write(" = new ");
                    w.write(type);
                    w.write("()");
                }else{
                    if("String".equals(type)){
                        w.write(" = \"");
                        w.write(defaultValue);
                        w.write("\"");
                    }else{
                        w.write(" = ");
                        w.write(defaultValue);
                    }
                }
            }
            
            w.write(";\n\n");
            
            if(binding != null){
                g.writeIndentation(w, 2);
                w.write("[Bindable(");
                
                if(!"".equals(binding)){
                    w.write("event=\"");
                    w.write(binding);
                    w.write("\"");
                }
                
                w.write(")]\n");
            }
            
            g.writeIndentation(w, 2);
            w.write("public function get ");
            w.write(name);
            w.write("():");
            w.write(type);
            g.writeCurlyBrace(w, 2);
            g.writeIndentation(w, 3);
            w.write("return ");
            
            if(g.isUsingUnderscore()){
                w.write('_');
            }else{
                w.write("this.");
            }
            
            w.write(name);
            w.write(";\n");
            g.writeIndentation(w, 2);
            w.write("}\n");
            
            if(!readOnly){
                g.writeIndentation(w, 2);
                w.write("public function set ");
                w.write(name);
                w.write("(value:");
                w.write(type);
                w.write("):void");
                g.writeCurlyBrace(w, 2);
                g.writeIndentation(w, 3);
                
                if(g.isUsingUnderscore()){
                    w.write('_');
                }else{
                    w.write("this.");
                }
                
                w.write(name);
                w.write(" = value;\n");
                
                if(binding != null && !"".equals(binding)){
                    g.writeIndentation(w, 3);
                    w.write("dispatchEvent(new Event(\"");
                    w.write(binding);
                    w.write("\"));\n");
                }
                
                g.writeIndentation(w, 2);
                w.write("}\n");
            }
            
            w.flush();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean acceptClass(Class<?> clazz) {
        return true;
    }

    public int getPriority() {
        return 0;
    }
}

