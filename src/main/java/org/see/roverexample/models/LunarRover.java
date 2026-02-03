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

package org.see.roverexample.models;

import org.apache.commons.geometry.euclidean.threed.Vector3D;
import org.apache.commons.numbers.quaternion.Quaternion;
import org.see.skf.annotations.Attribute;
import org.see.skf.annotations.ObjectClass;
import org.see.skf.runtime.ScopeLevel;
import org.see.skf.util.encoding.HLAunicodeStringCoder;
import org.see.roverexample.encoding.QuaternionCoder;
import org.see.roverexample.encoding.SpaceTimeCoordinateStateCoder;
import org.see.roverexample.encoding.Vector3DCoder;
import org.see.roverexample.types.SpaceTimeCoordinateState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ObjectClass(name = "HLAobjectRoot.PhysicalEntity")
public class LunarRover {
    private static final Logger logger = LoggerFactory.getLogger(LunarRover.class);

    private static final Vector3D POSITION_INCREMENT = Vector3D.of(10, 0, 0);

    @Attribute(name = "name", coder = HLAunicodeStringCoder.class, scope = ScopeLevel.PUBLISH)
    private String name;

    @Attribute(name = "type", coder = HLAunicodeStringCoder.class, scope = ScopeLevel.PUBLISH)
    private String type;

    @Attribute(name = "status", coder = HLAunicodeStringCoder.class, scope = ScopeLevel.PUBLISH)
    private String status;

    @Attribute(name = "parent_reference_frame", coder = HLAunicodeStringCoder.class, scope = ScopeLevel.PUBLISH)
    private String parentReferenceFrame;

    @Attribute(name = "state",  coder = SpaceTimeCoordinateStateCoder.class, scope = ScopeLevel.PUBLISH)
    private SpaceTimeCoordinateState state;

    @Attribute(name = "acceleration", coder = Vector3DCoder.class, scope = ScopeLevel.PUBLISH)
    private Vector3D acceleration;

    @Attribute(name = "rotational_acceleration", coder = Vector3DCoder.class, scope = ScopeLevel.PUBLISH)
    private Vector3D rotationalAcceleration;

    @Attribute(name = "center_of_mass", coder = Vector3DCoder.class, scope = ScopeLevel.PUBLISH)
    private Vector3D centerOfMass;

    @Attribute(name = "body_wrt_structural", coder = QuaternionCoder.class, scope = ScopeLevel.PUBLISH)
    private Quaternion bodyWrtStructural;

    public LunarRover() {
        this.name = "";
        this.type = "";
        this.status = "";
        this.parentReferenceFrame = "";
        this.state = new SpaceTimeCoordinateState();
        this.acceleration = Vector3D.of(0, 0, 0);
        this.rotationalAcceleration = Vector3D.of(0, 0, 0);
        this.centerOfMass = Vector3D.of(0, 0, 0);
        this.bodyWrtStructural = Quaternion.of(0, 0, 0, 0);
    }

    public LunarRover(String name, String type, String status, String parentReferenceFrame, Vector3D spawnPoint) {
        this();

        this.name = name;
        this.type = type;
        this.status = status;
        this.parentReferenceFrame = parentReferenceFrame;
        this.state.setPosition(spawnPoint);
    }

    public void move() {
        Vector3D currentPosition = state.getPosition();
        state.setPosition(currentPosition.add(POSITION_INCREMENT));
        currentPosition = state.getPosition();
        logger.info("Rover has moved to: {}", currentPosition);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentReferenceFrame() {
        return parentReferenceFrame;
    }

    public void setParentReferenceFrame(String parentReferenceFrame) {
        this.parentReferenceFrame = parentReferenceFrame;
    }

    public SpaceTimeCoordinateState getState() {
        return state;
    }

    public void setState(SpaceTimeCoordinateState state) {
        this.state = state;
    }

    public Vector3D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3D acceleration) {
        this.acceleration = acceleration;
    }

    public Vector3D getRotationalAcceleration() {
        return rotationalAcceleration;
    }

    public void setRotationalAcceleration(Vector3D rotationalAcceleration) {
        this.rotationalAcceleration = rotationalAcceleration;
    }

    public Vector3D getCenterOfMass() {
        return centerOfMass;
    }

    public void setCenterOfMass(Vector3D centerOfMass) {
        this.centerOfMass = centerOfMass;
    }

    public Quaternion getBodyWrtStructural() {
        return bodyWrtStructural;
    }

    public void setBodyWrtStructural(Quaternion bodyWrtStructural) {
        this.bodyWrtStructural = bodyWrtStructural;
    }
}
