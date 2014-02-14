/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.tms.domain.values {

    import org.granite.util.Enum;

    [Bindable]
    [RemoteClass(alias="org.openwms.tms.domain.values.PriorityLevel")]
    public class PriorityLevel extends Enum {

        public static const LOWEST:PriorityLevel = new PriorityLevel("LOWEST", _);
        public static const LOW:PriorityLevel = new PriorityLevel("LOW", _);
        public static const NORMAL:PriorityLevel = new PriorityLevel("NORMAL", _);
        public static const HIGH:PriorityLevel = new PriorityLevel("HIGH", _);
        public static const HIGHEST:PriorityLevel = new PriorityLevel("HIGHEST", _);

        function PriorityLevel(value:String = null, restrictor:* = null) {
            super((value || LOWEST.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [LOWEST, LOW, NORMAL, HIGH, HIGHEST];
        }

        public static function valueOf(name:String):PriorityLevel {
            return PriorityLevel(LOWEST.constantOf(name));
        }
    }
}