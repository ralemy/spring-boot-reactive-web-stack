package com.curisprofound.spring.plugin;

import org.pf4j.ExtensionPoint;

public interface Pf4jPlugin extends ExtensionPoint {
    String identify();
}
