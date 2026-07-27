import car.Car;
import car.DefaultCar;
import driver.UserDriver;
import gui.loading.OpponentFactory;
import gui.raceview.RacePanel;
import race.Race;
import track.Obstacle;
import track.RaceTrack;
import track.TrackPoint;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::startGame);   //startGame رو روی thread مناسب Swing اجرا کن
    }

    private static void startGame() {
        RaceTrack track = createTrack();

        UserDriver userDriver = new UserDriver("Player");
        Car playerCar = new DefaultCar(null);

        Car opponentCar = OpponentFactory.createRandomOpponent();

        System.out.println("Opponent car: " + opponentCar.getClass().getSimpleName());
        System.out.println("Opponent driver: " + opponentCar.getDriver().getClass().getSimpleName());

        Race race = new Race(playerCar, opponentCar, track);

        JFrame frame = new JFrame("Car Race Test");
        RacePanel racePanel = new RacePanel(race);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(racePanel);
        frame.pack();     //preferred size component (setPreferredSize)
        frame.setLocationRelativeTo(null);    //پنجره رو وسط مانیتور باز کن.
        frame.setResizable(false);
        frame.setVisible(true);

        racePanel.requestGameFocus();

        startCountdownThenGame(race, racePanel, frame);
    }

    private static RaceTrack createTrack() {
        RaceTrack track = new RaceTrack(List.of(//immutable
                new TrackPoint(0, 0),
                new TrackPoint(260, 0),
                new TrackPoint(260, 160),
                new TrackPoint(900, 160)

        ));

        track.addObstacle(new Obstacle(140));
        track.addObstacle(new Obstacle(760));

        return track;
    }

    private static void startCountdownThenGame(Race race, RacePanel racePanel, JFrame frame) {
        final String[] countdown = {"3", "2", "1", "GO!"};
        final int[] index = {0};

        Timer countdownTimer = new Timer(700, null);

        countdownTimer.addActionListener((ActionEvent e) -> {//WHEN TIMER TRIGGERED
            if (index[0] < countdown.length) {
                racePanel.setCountdownText(countdown[index[0]]);
                index[0]++;
                return;
            }

            countdownTimer.stop();
            racePanel.setCountdownText(""); //NULL
            startRaceLoop(race, racePanel, frame);
        });

        countdownTimer.setInitialDelay(0);
        countdownTimer.start();
    }

    private static void startRaceLoop(Race race, RacePanel racePanel, JFrame frame) {
        Timer gameTimer = new Timer(120, null);

        gameTimer.addActionListener((ActionEvent e) -> {
            if (race. isFinished()) {
                gameTimer.stop();
                racePanel.repaint();

                JOptionPane.showMessageDialog(
                        frame,
                        race.getResult(),
                        "Race Finished",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            race.updateStep();
            racePanel.repaint();
        });

        gameTimer.start();
        racePanel.requestGameFocus();//to be sure its focused

    }
}