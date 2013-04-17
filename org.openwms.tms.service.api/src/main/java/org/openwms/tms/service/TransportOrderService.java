/*
 * openwms.org, the Open Warehouse Management System.
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
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software. If not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.tms.service;

import java.util.List;

import org.openwms.common.domain.Location;
import org.openwms.common.domain.LocationGroup;
import org.openwms.common.domain.values.Barcode;
import org.openwms.tms.domain.order.TransportOrder;
import org.openwms.tms.domain.values.PriorityLevel;
import org.openwms.tms.domain.values.TransportOrderState;

/**
 * A TransportService offers some useful methods regarding the general handling
 * of {@link TransportOrder}s.
 * 
 * @param <T>
 *            Any kind of {@link TransportOrder}
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision$
 * @since 0.1
 */
public interface TransportOrderService<T extends TransportOrder> {

    /**
     * Return a List with all {@link TransportOrder}s.
     * 
     * @return a List with all {@link TransportOrder}s
     */
    List<T> findAll();

    /**
     * Returns the actual number of active transports that have the
     * <tt>locationGroup</tt> as target {@link LocationGroup}.
     * 
     * @param locationGroup
     *            {@link LocationGroup} to count all active transports for
     * @return Number of all active transports that are on the way to this
     *         {@link LocationGroup}
     */
    int getTransportsToLocationGroup(LocationGroup locationGroup);

    /**
     * Create a new {@link TransportOrder}.
     * 
     * @param barcode
     *            The {@link Barcode} of the <tt>TransportUnit</tt>
     * @param targetLocationGroup
     *            The target {@link LocationGroup}
     * @param targetLocation
     *            The target {@link Location}
     * @param priority
     *            A {@link PriorityLevel} of the new {@link TransportOrder}
     * @return The new created {@link TransportOrder}
     */
    T createTransportOrder(Barcode barcode, LocationGroup targetLocationGroup, Location targetLocation,
            PriorityLevel priority);

    /**
     * Create a new {@link TransportOrder}.
     * 
     * @param barcode
     *            The {@link Barcode} of the <tt>TransportUnit</tt>
     * @param targetLocationGroup
     *            The target {@link LocationGroup}
     * @param priority
     *            A {@link PriorityLevel} of the new {@link TransportOrder}
     * @return The new created {@link TransportOrder}
     */
    T createTransportOrder(Barcode barcode, LocationGroup targetLocationGroup, PriorityLevel priority);

    /**
     * Create a new {@link TransportOrder}.
     * 
     * @param barcode
     *            The {@link Barcode} of the <tt>TransportUnit</tt>
     * @param targetLocation
     *            The target {@link Location}
     * @param priority
     *            A {@link PriorityLevel} of the new {@link TransportOrder}
     * @return The new created {@link TransportOrder}
     */
    T createTransportOrder(Barcode barcode, Location targetLocation, PriorityLevel priority);

    /**
     * Try to turn a list of {@link TransportOrder}s into state.
     * 
     * @param transportOrders
     *            The IDs of {@link TransportOrder}s
     * @param state
     *            The state to change all orders to
     * @return A list of {@link TransportOrder} IDs that have not been canceled
     *         successfully
     */
    List<Integer> cancelTransportOrders(List<Integer> transportOrders, TransportOrderState state);

    /**
     * Try to redirect a list of {@link TransportOrder}s to a new target
     * {@link LocationGroup}.
     * 
     * @param transportOrders
     *            The IDs of {@link TransportOrder}s to be redirected
     * @param targetLocationGroup
     *            The new target {@link LocationGroup} for the
     *            {@link TransportOrder}s or <code>null</code>
     * @param targetLocation
     *            The new target {@link Location} for the {@link TransportOrder}
     *            s, or <code>null</code>
     * @return A list of {@link TransportOrder} IDs that couldn't be redirected
     *         successfully
     */
    List<Integer> redirectTransportOrders(List<Integer> transportOrders, LocationGroup targetLocationGroup,
            Location targetLocation);
}