
package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class HorlogeGUI implements TimerChangeListener {

    private final TimerService timerService;
    private final JLabel timeLabel = new JLabel();

    public HorlogeGUI(TimerService timerService) {
        this.timerService = timerService;
        this.timerService.addTimeChangeListener(this);

        JFrame frame = new JFrame("Horloge Graphique");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null); // Center the window

        timeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        frame.add(timeLabel);
        
        updateTimeDisplay(); // Initial display
        
        frame.setVisible(true);
    }

    private void updateTimeDisplay() {
        String time = String.format("%02d:%02d:%02d",
                timerService.getHeures(),
                timerService.getMinutes(),
                timerService.getSecondes());
        timeLabel.setText(time);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TimerChangeListener.SECONDE_PROP.equals(evt.getPropertyName())) {
            // Ensure UI updates are on the Event Dispatch Thread
            SwingUtilities.invokeLater(this::updateTimeDisplay);
        }
    }
}
