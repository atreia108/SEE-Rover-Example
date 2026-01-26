/*-
 * Copyright (c) 2026 Hridyanshu Aatreya
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */

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

        // Every 11th step, send a transmission about the vehicle status and a message.
        if (counter % 11 == 0) {
            sendTransmission();
            counter = 0;
        } else {
            ++counter;
        }
    }

    private void sendTransmission() {
        try {
            VehicleStatusTransmission transmission = new VehicleStatusTransmission(rover.getName(), rover.getState().getPosition());
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
