/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
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
package org.openwms.tms;

/**
 * A TMSConstants holds general constants of this microservice module.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @since 1.0
 */
public final class TMSConstants {

    /** API version. */
    public static final String API_VERSION = "v1";
    /** API root to hit TransportOrders (plural). */
    public static final String ROOT_ENTITIES = "/" + API_VERSION + "/transportorders";
    /**
     * Bean name of the Jackson ObjectMapper to use. Dissenting from the default bean name to not come in conflict with instantiations done
     * be SpringBoot autoconfiguration.
     */
    public static final String BEAN_NAME_OBJECTMAPPER = "jacksonOM";
}
