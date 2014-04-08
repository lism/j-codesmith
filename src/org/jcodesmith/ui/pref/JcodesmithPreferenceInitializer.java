package org.jcodesmith.ui.pref;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jcodesmith.JCodeSmithActivator;

public class JcodesmithPreferenceInitializer extends AbstractPreferenceInitializer {
    private IPreferenceStore store;
    public JcodesmithPreferenceInitializer() {
        this.store = JCodeSmithActivator.preference();
    }

    @Override
    public void initializeDefaultPreferences() {

    
    }

}
