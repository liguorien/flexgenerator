
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

package com.liguorien.flex.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Used to define the Generics type on a Map property.</p>
 * <p>You can also define the Map implementation class for the generated 
 * java code.</p>
 *
 * <p><b>Exemple :</b></p>
 * <pre>    
 *    
 * public class MyEntity  {
 *
 *    private Map&lt;Integer, String&gt; _names;
 *    private Map&lt;Integer, Integer&gt; _idMap;
 *
 *    &#064;FlexMap(key=Integer.class, entry=String.class)
 *    public Map&lt;Integer, String&gt; getNames(){
 *        return _names;
 *    }
 *
 *    public void setName(Map&lt;Integer, String&gt; names){
 *        _names = names;
 *    }
 *
 *
 *    &#064;FlexMap(key=Integer.class, entry=Integer.class, clazz=TreeMap.class)
 *    public List&lt;Integer, Integer&gt; getIdMap(){
 *        return _selectedIds;
 *    }
 *
 *    public void setIdMap(Map&lt;Integer, Integer&gt; ids){
 *        _selectedIds = names;
 *    }
 * }
 * </pre>
 *
 * @version 0.2
 * @author Nicolas Désy
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface FlexMap {
    
    /**
     * <p>The class used as key in the Map.</p> 
     */
    Class key();
    
    /**
     * <p>The class used as entry in the Map.</p> 
     */
    Class entry();    
    
    /**
     * <p>Defines the Map implementation class for the generated java code.</p>
     * <p>The default value is java.util.HashMap</p>
     */
    Class<? extends Map> clazz() default HashMap.class;
}
