package com.mclegoman.perspective.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerspectiveData {
    public static final String NAME = "Perspective";
    public static final String ID = "perspective";
    public static final String VERSION = "1.0.0";
    public static final String PREFIX = ("[" + NAME + " " + VERSION + "] ");
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final Boolean DEVELOPMENT_BUILD = true;
}