
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
 * <p>Used to define the default value of a property.</p>
 * <p>It can be declared on the getter method of a property</p>
 * <p>You can either put a raw value or the keyword "new" to instanciate 
 * the property using the constructor of the property type</p>
 *
 * <p><b>Exemple :</b></p>
 * <pre>
 * &#064;FlexDefault("new")
 * public Person getPerson() {
 *     return _person;
 * }</pre>
 * <p>Will generates :</p>
 * <pre>private var _person:Person = new Person();</pre> 
 * <hr/>
 * <pre>
 * &#064;FlexDefault("-1")
 * public int getId() {
 *     return _id;
 * }</pre>
 * <p>Will generates :</p>
 * <pre>private var _id:int = -1;</pre>
 *
 * @version 0.2
 * @author Nicolas Désy
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface FlexDefault {
    
    String value();
}