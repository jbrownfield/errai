package org.jboss.errai.widgets.client;

import org.jboss.errai.common.client.framework.AcceptsCallback;

public class WSAlert {
    public static void alert(String message) {
        WSModalDialog panel = new WSModalDialog();
        panel.ask(message, new AcceptsCallback() {
            public void callback(Object message, Object data) {
            }
        });
        panel.showModal();
    }
}