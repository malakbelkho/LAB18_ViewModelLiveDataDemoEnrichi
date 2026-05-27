package com.malak.viewmodellivedatademoenrichi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class PulseCounterViewModel extends ViewModel {

    private static final String PULSE_KEY = "saved_pulse_value";

    private final SavedStateHandle stateHandle;
    private final MutableLiveData<Integer> pulseState;

    public PulseCounterViewModel(SavedStateHandle stateHandle) {
        this.stateHandle = stateHandle;

        Integer restoredValue = stateHandle.get(PULSE_KEY);

        if (restoredValue == null) {
            restoredValue = 0;
        }

        pulseState = new MutableLiveData<>(restoredValue);
    }

    public LiveData<Integer> getPulseState() {
        return pulseState;
    }

    public void boostPulse() {
        Integer currentValue = pulseState.getValue();

        if (currentValue == null) {
            currentValue = 0;
        }

        updatePulse(currentValue + 1);
    }

    public void lowerPulse() {
        Integer currentValue = pulseState.getValue();

        if (currentValue == null) {
            currentValue = 0;
        }

        updatePulse(currentValue - 1);
    }

    public void resetPulse() {
        updatePulse(0);
    }

    public void boostFromWorkerThread() {
        new Thread(() -> {
            Integer currentValue = pulseState.getValue();

            if (currentValue == null) {
                currentValue = 0;
            }

            int nextValue = currentValue + 1;

            stateHandle.set(PULSE_KEY, nextValue);
            pulseState.postValue(nextValue);
        }).start();
    }

    private void updatePulse(int newValue) {
        stateHandle.set(PULSE_KEY, newValue);
        pulseState.setValue(newValue);
    }
}