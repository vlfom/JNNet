package com.vlfom.graphics.mouse;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Created by @vlfom.
 */
public class RequestFocusListener implements AncestorListener {
    private boolean removeListener;

    public RequestFocusListener()
    {
        this(true);
    }

    public RequestFocusListener(boolean removeListener)
    {
        this.removeListener = removeListener;
    }

    @Override
    public void ancestorAdded(AncestorEvent e)
    {
        JComponent component = e.getComponent();
        component.requestFocusInWindow();

        if (removeListener)
            component.removeAncestorListener( this );
    }

    @Override
    public void ancestorMoved(AncestorEvent e) {}

    @Override
    public void ancestorRemoved(AncestorEvent e) {}
}
