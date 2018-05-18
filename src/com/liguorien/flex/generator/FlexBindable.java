
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

/**
 * <p>Used to define a bindable property.</p>
 * <p>It can be declared on class level to define the default 
 * behavior for each properties of the class.</p>
 * <p>It can also be declared on the getter method of a property. This
 * will override the binding defined on the class declaration.</p>
 * <p>A custom event for the data binding can be speficy with the 
 * 'event' attribute.</p>
 *
 * <p><b>Exemple :</b></p>
 * <pre>
 *    // indicate that each properties in the class should be bindable
 *    &#064;FlexBindable
 *    public class Person  {
 *
 *        private int _id;
 *        private String _name;
 *
 *        public int getId() {
 *            return _id;
 *        }
 *        public void setId(int id) {
 *            _id = id;
 *        }
 *
 *        // the binding of this property will occurs on the "change" event
 *        &#064;FlexBindable(event="change")
 *        public String getName() {
 *            return _name;
 *        }
 *        public void setName(String name) {
 *            _name = name;
 *        }
 *    }
 * </pre>
 *
 * @version 0.2
 * @author Nicolas Désy
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})

public @interface FlexBindable {
    
    /**
     * <p>Used to define the binding event for the property.</p>
     * <p>Optional. The default value is an empty String, 
     * which represents a property binding instead of a event binding.</>
     */
    String event() default "";
}
