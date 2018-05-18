
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

package com.liguorien.flex.generator.handlers;

import com.liguorien.flex.generator.*;
import com.liguorien.flex.generator.utils.ClassType;
import com.liguorien.flex.generator.writers.ClassWriter;
import com.liguorien.flex.generator.writers.as3.AS3Writer;
import com.liguorien.flex.generator.writers.as3.methods.GetterSetter;
import java.io.File;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class FlexModelHandler
        extends AbstractGeneratorHandler<FlexModelHandler> {
    
    
    private boolean _bindEverything = false;
    private String _defaultEvent = null;
    
    /**
     * create a new instance of FlexModelHandler
     */
    public FlexModelHandler(){
        super();
        addMethodWriter(new GetterSetter());
    }
    
    /**
     * Determine if all properties of the class should be bindable
     * @return A boolean which indicate if all properties of the class 
     * should be bindable
     */
    public boolean isBindingEverything(){
        return _bindEverything;
    }
    
    /**
     * <p>Return the default event for bindable properties. It's declared on the
     * Java entity class with the {@link FlexBindable} annotation.</p>
     * @return The default event.
     */
    public String getDefaultEvent(){
        return _defaultEvent;
    }
    
    
    /**
     * @inheritDoc
     * @return {@link ClassType#CLIENT_ENTITIES}
     */
    public ClassType getClassType(){
        return ClassType.CLIENT_ENTITIES;
    }
    
    /**
     * @inheritDoc 
     */
    public void handleClassBegin(Class<?> clazz) {
        
        super.handleClassBegin(clazz, AS3Writer.getContextWriter(),
                getClassName(clazz).toString(), "EventDispatcher", null);        
        
        if(clazz.isAnnotationPresent(FlexBindable.class)){
            _bindEverything = true;
            _defaultEvent = clazz.
                    getAnnotation(FlexBindable.class).
                    event();
        }else{
            _bindEverything = false;
            _defaultEvent = null;
        }
    }
    
    
    /**
     * @inheritDoc
     * return {@link FlexGenerator#getFlexOutputDirectory}
     */
    public File getOutputDirectory() {
        return getGenerator().getFlexOutputDirectory();
    }
    
    /**
     * Append ".as"
     * @inheritDoc
     */
    protected void appendExtension(StringBuilder buffer) {
        buffer.append(".as");
    }
    
    /**
     * 
     * 
     * @return {@link FlexWriters#AS3Writersriter}
     * @inheritDoc 
     */
    public ClassWriter getClassWriter() {
        return AS3Writer.getContextWriter();
    }
}
