package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import java.util.Random;

public class CompteARebours implements TimerChangeListener {

    private int count;
    private TimerService timerService;

    public CompteARebours(TimerService timerService, int initialCount) {
        this.timerService = timerService;
        this.count = initialCount;
        this.timerService.addTimeChangeListener(this);
    }

    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        if (prop.equals(TimerChangeListener.SECONDE_PROP)) {
            if (count > 0) {
                System.out.println("Compte à rebours: " + count);
                count--;
            } else {
                timerService.removeTimeChangeListener(this);
            }
        }
    }
}
