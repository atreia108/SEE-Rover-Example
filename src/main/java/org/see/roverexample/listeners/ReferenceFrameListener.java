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
