
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
 * <p>Used to define the package of generated classes.</p>
 * <p>This annotation can only be used on class declaration.</p>
 * <p>There is 3 possible package destination : {@link #clientEntities}, 
 * {@link #clientBuilders} and {@link #serverBuilders}.</p>
 * <p>By default, each generated classes are in the same package as the orginal
 * Java entity.</p>
 * <p>Here is a little intro on the syntax used to define the package name. 
 * Let's supose that we have the entity class 
 * <em>com.mycompany.myproject.entities.Person</em> : </p>
 * <table class="infotable" border="1">
 *   <tr>
 *     <td>&#064;FlexPackage(serverBuilders="builder")</td>
 *     <td>com.mycompany.myproject.entities.builder.PersonBuilder</td>
 *   </tr>
 *   <tr>
 *     <td>&#064;FlexPackage(serverBuilders="../builder")</td>
 *     <td>com.mycompany.myproject.builder.PersonBuilder</td>
 *   </tr>
 *   <tr>
 *     <td>&#064;FlexPackage(serverBuilders="/org.otherproject.builder")</td>
 *     <td>org.otherproject.builder.PersonBuilder</td>
 *   </tr>
 * </table> 
 * <p>If the annotation is not declared on the entity class, the generator
 * will lookup in his own map which can be configurated with 
 * {@link FlexGenerator#setPackage}.</p>
 *
 * @see FlexGenerator#setPackage
 * @version 0.2
 * @author Nicolas Désy
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface FlexPackage {
    
    /**
     * <p>Set the destination package for generated entity classes for the 
     * client.</p>
     */
    String clientEntities() default "";
    
    /**
     * <p>Set the destination package for generated builder classes for the 
     * client.</p>
     */
    String clientBuilders() default "";
    
    /**
     * <p>Set the destination package for generated builder classes for the 
     * server.</p>
     */        
    String serverBuilders() default "";
}
