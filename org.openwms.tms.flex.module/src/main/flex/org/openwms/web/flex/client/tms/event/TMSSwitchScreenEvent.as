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
package org.openwms.web.flex.client.tms.event {

    import flash.events.Event
    import mx.collections.ArrayCollection;
    import org.openwms.web.flex.client.event.SwitchScreenEvent;

    /**
     * A TMSSwitchScreenEvent.
     *
     * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
     * @version $Revision$
     * @since 0.1
     */
    public class TMSSwitchScreenEvent extends SwitchScreenEvent {

        /**
         * Name of the TMS:TransportOrdersView.
         */
        public static const SHOW_TRANSPORT_ORDERS_VIEW : String = "tmsTransportOrdersView";

        /**
         * Constructor.
         */
        public function TMSSwitchScreenEvent(type : String, bubbles : Boolean=true, cancelable : Boolean=false) {
            super(type, bubbles, cancelable);
        }

        /**
         * Just a copy of the event itself.
         *
         * @return a copy of this
         */
        public override function clone() : Event {
            return new TMSSwitchScreenEvent(type);
        }

        /**
         * Simple override.
         *
         * @return the type of event
         */
        public override function toString() : String {
            return formatToString("TMSSwitchScreenEvent", "type");
        }
    }
}
