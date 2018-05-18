
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

package com.liguorien.flex.generator.utils;

import com.liguorien.flex.generator.*;
import com.liguorien.flex.generator.handlers.FlexGeneratorHandler;

/**
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public class GeneratorContext {
    
    /**
     * Creates a new instance of GeneratorContext
     */
    public GeneratorContext() {
    }

    /**
     * Holds value of property _generator.
     */
    private FlexGenerator _generator;

    /**
     * Getter for property generator.
     * @return Value of property generator.
     */
    public FlexGenerator getGenerator() {
        return _generator;
    }

    /**
     * Setter for property generator.
     * @param generator New value of property generator.
     */
    public void setGenerator(FlexGenerator generator) {
        _generator = generator;
    }

    /**
     * Holds value of property _handler.
     */
    private FlexGeneratorHandler _handler;

    /**
     * Getter for property handler.
     * @return Value of property handler.
     */
    public FlexGeneratorHandler getHandler() {      
        return _handler;
    }

    /**
     * Setter for property handler.
     * @param handler New value of property handler.
     */
    public void setHandler(FlexGeneratorHandler handler) {       
        _handler = handler;
    }
    
}
