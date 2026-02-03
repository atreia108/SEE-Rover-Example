/*****************************************************************
 SEE Tutorial - Tutorial projects demonstrating how to use the
 SEE HLA Starter Kit Framework.
 Copyright (c) 2026, Hridyanshu Aatreya - Modelling & Simulation
 Group (MSG) at Brunel University of London. All rights reserved.

 GNU Lesser General Public License (GNU LGPL).

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3.0 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library.
 If not, see http://http://www.gnu.org/licenses/
 *****************************************************************/

package org.see.roverexample;

import hla.rti1516_2025.exceptions.*;
import org.apache.commons.geometry.euclidean.threed.Vector3D;
import org.see.roverexample.listeners.ReferenceFrameListener;
import org.see.roverexample.models.LunarRover;
import org.see.roverexample.models.ReferenceFrame;
import org.see.roverexample.models.VehicleStatusTransmission;
import org.see.skf.conf.FederateConfiguration;
import org.see.skf.core.SEEFederateAmbassador;
import org.see.skf.core.SEELateJoinerFederate;

import java.io.File;

public class RoverFederate extends SEELateJoinerFederate {
    private static final File confFile = new File("src/main/resources/rover_federate.conf");

    private final LunarRover rover;
    private int counter;

    public RoverFederate(SEEFederateAmbassador federateAmbassador, FederateConfiguration federateConfiguration) {
        super(federateAmbassador, federateConfiguration);

        // Create a starting point for the rover.
        Vector3D spawnPoint = Vector3D.of(100, 500, -5590);
        rover = new LunarRover("lunar_rover", "Transport", "Deployed", "AitkenBasinLocalFixed", spawnPoint);

        counter = 0;
    }

    @Override
    public void declareClasses() throws FederateNotExecutionMember, AttributeNotDefined, ObjectClassNotDefined, RestoreInProgress, NameNotFound, NotConnected, RTIinternalError, InvalidObjectClassHandle, SaveInProgress, InvalidInteractionClassHandle, InteractionClassNotDefined {
        // We are publishing the PhysicalEntity class since we're creating the lunar rover. It is an object instance
        // belonging to the PhysicalEntity object class. In a similar vein, we are also publishing the VehicleStatusTransmission
        // interaction class because we want to be able to transmit the status of our rover every few steps.
        publishObjectClass(LunarRover.class);
        publishInteractionClass(VehicleStatusTransmission.class);

        // We want to be notified of updates pertaining to the MoonCentricInertial reference frame. It has been
        // chosen arbitrarily just to demonstrate how updates are received.
        subscribeObjectClass(ReferenceFrame.class);

        // Add an event listener to watch out for when a specific reference frame is discovered.
        addRemoteObjectInstanceListener(new ReferenceFrameListener());
    }

    @Override
    public void declareObjectInstances() throws FederateNotExecutionMember, ObjectClassNotPublished, ObjectClassNotDefined, RestoreInProgress, ObjectInstanceNotKnown, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, NotConnected, RTIinternalError, SaveInProgress {
        // This creates an object instance at the RTI with the name "lunar_rover".
        registerObjectInstance(rover, "lunar_rover");
    }

    @Override
    public void update() {
        // This segment is run every time the simulation is updated. Our rover moves 10 units on the X-axis with each
        // time step.
        rover.move();

        // The rover has only moved locally in our federate. We need to dispatch updates for it so that other federates
        // that are subscribed can learn about this.
        updateObjectInstance(rover);

        // Send a transmission with the rover's current position and the number of time steps elapsed since mission start.
        String transmissionMessage = "Mission Time Elapsed: " + counter;
        sendTransmission(transmissionMessage);
        ++counter;
    }

    private void sendTransmission(String message) {
        try {
            VehicleStatusTransmission transmission = new VehicleStatusTransmission(rover.getName(), rover.getState().getPosition(), message);
            sendInteraction(transmission);
        } catch (Exception e) {
            throw new IllegalStateException("Error encountered while trying to transmit vehicle status.", e);
        }
    }

    public static void main(String[] args) {
        FederateConfiguration config = FederateConfiguration.Factory.create(confFile);
        RoverFederate federate = new RoverFederate(new SEEFederateAmbassador(), config);
        federate.configureAndStart();
    }
}
