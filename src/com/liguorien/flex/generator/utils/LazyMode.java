
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

/**
 * Represents the four possible lazy modes for a property.
 * @see com.liguorien.flex.generator.FlexGenerator#isLazy
 *
 * @version 0.2
 * @author Nicolas Désy
 */
public enum LazyMode {                
        
        /**
         * Indicates when the server reads data from the client
         */
        SERVER_READ,         
        
        /**
         * Indicates when the server writes data to the client
         */
        SERVER_WRITE,         
        
        /**
         * Indicates when the client reads data from the server
         */
        CLIENT_READ,         
        
        /**
         * Indicates when the client writes data to the server
         */
        CLIENT_WRITE

}