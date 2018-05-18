
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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Used to define the Generics type on a List property.</p>
 * <p>You can also define the List implementation class for the generated 
 * java code.</p>
 *
 * <p><b>Exemple :</b></p>
 * <pre>    
 *    
 *    public class MyEntity  {
 *
 *        private List&lt;String&gt; _names;
 *        private List&lt;Integer&gt; _selectedIds;
 *
 *        &#064;FlexList(String.class)
 *        public List&lt;String&gt; getNames(){
 *            return _names;
 *        }
 *
 *        public void setName(List&lt;String&gt; names){
 *            _names = names;
 *        }
 *
 *
 *        &#064;FlexList(value=Integer.class, clazz=LinkedList.class)
 *        public List&lt;Integer&gt; getSelectedIds(){
 *            return _selectedIds;
 *        }
 *
 *        public void setSelectedIds(List&lt;Integer&gt; ids){
 *            _selectedIds = names;
 *        }
 *    }
 * </pre>
 * @version 0.2
 * @author Nicolas Désy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface FlexList {    
       
    /**
     * <p>The Generics type declared on the List property.</p> 
     */
    Class<?> value();
    
    
    /**
     * <p>Defines the List implementation class for the generated java code.</p>
     * <p>The default value is java.util.ArrayList</p>
     */
    Class<? extends List> clazz() default ArrayList.class;
}
