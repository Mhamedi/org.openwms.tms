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
package org.openwms.tms.domain.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.openwms.common.domain.Location;
import org.openwms.common.domain.LocationGroup;
import org.openwms.common.domain.TransportUnit;
import org.openwms.common.domain.values.Problem;
import org.openwms.core.domain.AbstractEntity;
import org.openwms.core.domain.DomainObject;
import org.openwms.core.exception.StateChangeException;
import org.openwms.tms.domain.values.PriorityLevel;
import org.openwms.tms.domain.values.TransportOrderState;

/**
 * A TransportOrder is used to move {@link TransportUnit}s from a current {@link Location} to a target {@link Location}.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision$
 * @since 0.1
 * @see org.openwms.common.domain.TransportUnit
 * @see org.openwms.common.domain.Location
 */
@Entity
@Table(name = "TMS_TRANSPORT_ORDER")
@NamedQueries({
        @NamedQuery(name = TransportOrder.NQ_FIND_ALL, query = "select to from TransportOrder to order by to.id"),
        @NamedQuery(name = TransportOrder.NQ_FIND_BY_TU, query = "select to from TransportOrder to where to.transportUnit = :transportUnit"),
        @NamedQuery(name = TransportOrder.NQ_FIND_FOR_TU_IN_STATE, query = "select to from TransportOrder to where to.transportUnit = :transportUnit and to.state in (:states)") })
public class TransportOrder extends AbstractEntity<Long> implements DomainObject<Long>, Serializable {

    private static final long serialVersionUID = 4586898047981474230L;

    /**
     * Query to find all <code>TransportOrder</code>s.
     */
    public static final String NQ_FIND_ALL = "TransportOrder.findAll";

    /**
     * Query to find all <code>TransportOrder</code>s for a certain {@link TransportUnit}. <li>Query parameter name
     * <strong>transportUnit</strong> : The {@link TransportUnit} to search for.</li>
     */
    public static final String NQ_FIND_BY_TU = "TransportOrder.findByTU";

    /**
     * Query to find all <code>TransportOrder</code>s for a particular {@link TransportUnit} in certain states. <li>Query parameter name
     * <strong>transportUnit</strong> : The {@link TransportUnit} to search for.</li> <li>Query parameter name <strong>states</strong> : A
     * list of {@link TransportOrderState}s.</li>
     */
    public static final String NQ_FIND_FOR_TU_IN_STATE = "TransportOrder.findForTuInState";

    /**
     * Unique technical key.
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    /**
     * The {@link TransportUnit} to be moved by this <code>TransportOrder</code> . Allowed to be <code>null</code> to keep
     * {@link TransportOrder}s without {@link TransportUnit}s.
     */
    @ManyToOne
    @JoinColumn(name = "TRANSPORT_UNIT", nullable = true)
    private TransportUnit transportUnit;

    /**
     * Date when the <code>TransportOrder</code> was updated the last time.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_UPDATED")
    private Date dateUpdated = new Date();

    /**
     * A priority level of the <code>TransportOrder</code>. The lower the value the lower the priority.<br>
     * The priority level affects the execution of the <code>TransportOrder</code>. An order with high priority will be processed faster
     * than those with lower priority.
     */
    @Column(name = "PRIORITY")
    @Enumerated(EnumType.STRING)
    private PriorityLevel priority = PriorityLevel.NORMAL;

    /**
     * Date when the <code>TransportOrder</code> was started.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    /**
     * Last reported problem on the <code>TransportOrder</code>.
     */
    @Column(name = "PROBLEM")
    private Problem problem;

    /**
     * Date when the <code>TransportOrder</code> was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private final Date creationDate = new Date();

    /**
     * Date when the <code>TransportOrder</code> ended.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    /**
     * State of the <code>TransportOrder</code>.
     */
    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private TransportOrderState state = TransportOrderState.CREATED;

    /**
     * The source {@link Location} of the <code>TransportOrder</code>.<br>
     * This property is set before the <code>TransportOrder</code> was started.
     */
    @ManyToOne
    @JoinColumn(name = "SOURCE_LOCATION")
    private Location sourceLocation;

    /**
     * The target {@link Location} of the <code>TransportOrder</code>.<br>
     * This property is set before the <code>TransportOrder</code> was started.
     */
    @ManyToOne
    @JoinColumn(name = "TARGET_LOCATION")
    private Location targetLocation;

    /**
     * A {@link LocationGroup} can also be set as target. At least one target must be set when the <code>TransportOrder</code> is being
     * started.
     */
    @ManyToOne
    @JoinColumn(name = "TARGET_LOCATION_GROUP")
    private LocationGroup targetLocationGroup;

    /**
     * Version field.
     */
    @Version
    @Column(name = "C_VERSION")
    private long version;

    /* ------------------- lifecycle callback methods ---------- */

    /**
     * JPA Lifecycle callback method to set the dateUpdated property when the <code>TransportOrder</code> is being modified.
     */
    @PreUpdate
    protected void postUpdate() {
        this.dateUpdated = new Date();
    }

    /* ----------------------------- methods ------------------- */
    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return (this.id == null);
    }

    /**
     * Returns the priority level of the <code>TransportOrder</code>.
     * 
     * @return The priority
     */
    public PriorityLevel getPriority() {
        return this.priority;
    }

    /**
     * Set the priority level of the <code>TransportOrder</code>.
     * 
     * @param priority
     *            The priority to set
     */
    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    /**
     * Returns the date when the <code>TransportOrder</code> was started.
     * 
     * @return The date when started
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Get the {@link TransportUnit} assigned to the <code>TransportOrder</code> .
     * 
     * @return The assigned transportUnit
     */
    public TransportUnit getTransportUnit() {
        return this.transportUnit;
    }

    /**
     * Assign a {@link TransportUnit} to the <code>TransportOrder</code>. Setting the {@link TransportUnit} to <code>null</code> is allowed
     * here to unlink both.
     * 
     * @param transportUnit
     *            The transportUnit to be assigned
     */
    public void setTransportUnit(TransportUnit transportUnit) {
        this.transportUnit = transportUnit;
    }

    /**
     * Returns the date when the <code>TransportOrder</code> was created.
     * 
     * @return The creation date
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * Returns the state of the <code>TransportOrder</code>.
     * 
     * @return The state of the order
     */
    public TransportOrderState getState() {
        return this.state;
    }

    private void validateInitializationCondition() {
        if (transportUnit == null || (targetLocation == null && targetLocationGroup == null)) {
            throw new IllegalStateException("Not all properties set to turn TransportOrder into next state");
        }
    }

    /**
     * Validate whether a state change is valid or not. States must be changed in a defined order. Mostly the order is defined by the
     * ordering if the states in {@link TransportOrderState} enum class. But some other rules are checked here too and an exception is
     * thrown in case the sequence of states is violated.
     * 
     * @param newState
     *            The new state of the order
     * @throws StateChangeException
     *             when <li>newState is <code>null</code> or</li><li>the state shall be turned back to a prior state or</li><li>when the
     *             caller tries to leap the state {@link TransportOrderState#INITIALIZED}</li>
     */
    protected void validateStateChange(TransportOrderState newState) throws StateChangeException {
        if (newState == null) {
            throw new StateChangeException("New TransportState cannot be set to null");
        }
        if (getState().compareTo(newState) > 0) {
            // Don't allow to turn back the state!
            throw new StateChangeException("Turning back the state of a TransportOrder is not allowed");
        }
        if (getState() == TransportOrderState.CREATED) {
            if (newState != TransportOrderState.INITIALIZED && newState != TransportOrderState.CANCELED) {
                // Don't allow to except the initialization
                throw new StateChangeException("A new TransportOrder must first be initialized or be canceled");
            }
            try {
                validateInitializationCondition();
            } catch (IllegalStateException ise) {
                throw new StateChangeException(ise);
            }
        }
    }

    /**
     * Change the state of the <code>TransportOrder</code> regarding some rules.
     * 
     * @param newState
     *            The new state of the order
     * @throws StateChangeException
     *             in case
     *             <ul>
     *             <li>the newState is <code>null</code> or</li>
     *             <li>the newState is less than the old state or</li>
     *             <li>the <code>TransportOrder</code> is in state {@link TransportOrderState#CREATED} and shall be manually turned into
     *             something else then {@link TransportOrderState#INITIALIZED} or {@link TransportOrderState#CANCELED}</li>
     *             <li>the <code>TransportOrder</code> is {@link TransportOrderState#CREATED} and shall be
     *             {@link TransportOrderState#INITIALIZED} but it is incomplete</li>
     *             </ul>
     */
    public void setState(TransportOrderState newState) throws StateChangeException {
        validateStateChange(newState);
        switch (newState) {
        case STARTED:
            startDate = new Date();
            break;
        case FINISHED:
        case ONFAILURE:
        case CANCELED:
            endDate = new Date();
            break;
        default:
            break;
        }
        state = newState;
    }

    /**
     * Get the target {@link Location} of this <code>TransportOrder</code>.
     * 
     * @return The targetLocation if any, otherwise <code>null</code>
     */
    public Location getTargetLocation() {
        return this.targetLocation;
    }

    /**
     * Set the target {@link Location} of this <code>TransportOrder</code>.
     * 
     * @param targetLocation
     *            The location to move on
     */
    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    /**
     * Get the date when the <code>TransportOrder</code> was changed last time.
     * 
     * @return The date of the last update
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * Get the targetLocationGroup.
     * 
     * @return The targetLocationGroup if any, otherwise <code>null</code>
     */
    public LocationGroup getTargetLocationGroup() {
        return targetLocationGroup;
    }

    /**
     * Set the targetLocationGroup.
     * 
     * @param targetLocationGroup
     *            The targetLocationGroup to set.
     */
    public void setTargetLocationGroup(LocationGroup targetLocationGroup) {
        this.targetLocationGroup = targetLocationGroup;
    }

    /**
     * Get the last {@link Problem}.
     * 
     * @return The last problem.
     */
    public Problem getProblem() {
        return problem;
    }

    /**
     * Set the last {@link Problem}.
     * 
     * @param problem
     *            The {@link Problem} to set.
     */
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    /**
     * Get the endDate.
     * 
     * @return The date the order ended
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Get the sourceLocation.
     * 
     * @return The sourceLocation
     */
    public Location getSourceLocation() {
        return sourceLocation;
    }

    /**
     * Set the sourceLocation.
     * 
     * @param sourceLocation
     *            The sourceLocation to set
     */
    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getVersion() {
        return this.version;
    }

    public static enum State {
        /**
         * Status of new created <code>TransportOrder</code>s.
         */
        CREATED(10),

        /**
         * Status of a full initialized <code>TransportOrder</code>, ready to be started.
         */
        INITIALIZED(20),

        /**
         * A started and active<code>TransportOrder</code>, ready to be executed.
         */
        STARTED(30),

        /**
         * Status to indicate that the <code>TransportOrder</code> is paused. Not active anymore.
         */
        INTERRUPTED(40),

        /**
         * Status to indicate a failure on the <code>TransportOrder</code>. Not active anymore.
         */
        ONFAILURE(50),

        /**
         * Status of a aborted <code>TransportOrder</code>. Not active anymore.
         */
        CANCELED(60),

        /**
         * Status to indicate that the <code>TransportOrder</code> completed successfully.
         */
        FINISHED(70);

        private final int order;

        private State(int sortOrder) {
            this.order = sortOrder;
        }

        /**
         * Get the order.
         * 
         * @return the order.
         */
        public int getOrder() {
            return order;
        }
    }
}
