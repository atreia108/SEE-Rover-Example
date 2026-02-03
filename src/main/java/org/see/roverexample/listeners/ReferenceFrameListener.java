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

package org.see.roverexample.listeners;

import org.see.roverexample.models.ReferenceFrame;
import org.see.skf.core.RemoteObjectInstanceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceFrameListener implements RemoteObjectInstanceListener {
    private static final Logger logger = LoggerFactory.getLogger(ReferenceFrameListener.class);
    private static final String REFERENCE_FRAME_NAME = "MoonCentricInertial";

    // This method gets called whenever ANY remote entity is discovered by the federate.
    @Override
    public void instanceAdded(String instanceName, Object objectInstance) {
        if (instanceName.equals(REFERENCE_FRAME_NAME) && objectInstance instanceof ReferenceFrame) {
            ReferenceFrame referenceFrame = (ReferenceFrame) objectInstance;

            referenceFrame.addPropertyListener(evt -> {
                if (evt.getPropertyName().equals("state")) {
                    String positionInfo = referenceFrame.getState().getPosition().toString();
                    logger.info("Position of \"{}\" was updated to: {}", REFERENCE_FRAME_NAME, positionInfo);

                    // Other operations you may want to do in response to this event.
                    // ...
                }
            });
        }
    }

    // This gets called whenever ANY remote entity is removed.
    @Override
    public void instanceRemoved(String instanceName) {
        if (instanceName.equals(REFERENCE_FRAME_NAME)) {
            logger.info("The reference frame \"{}\" is no longer available in the federation execution.", instanceName);
        }
    }
}
