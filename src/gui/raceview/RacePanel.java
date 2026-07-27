package gui.raceview;

import car.Car;
import race.Race;

import javax.swing.*;
import java.awt.*;

public class RacePanel extends JPanel {

    private final Race race;
    private String countdownText;

    private final RoadRenderer roadRenderer;
    private final ObstacleRenderer obstacleRenderer;
    private final EventRenderer eventRenderer;
    private final CarRenderer carRenderer;
    private final InfoRenderer infoRenderer;
    private final CountdownRenderer countdownRenderer;

    private static final double PLAYER_LANE_OFFSET = -12;
    private static final double OPPONENT_LANE_OFFSET = 12;

    public RacePanel(Race race) {
        if (race == null) {
            throw new IllegalArgumentException("Race cannot be null.");
        }

        this.race = race;
        this.countdownText = "";

        this.roadRenderer = new RoadRenderer();
        this.obstacleRenderer = new ObstacleRenderer();
        this.eventRenderer = new EventRenderer();
        this.carRenderer = new CarRenderer(eventRenderer);
        this.infoRenderer = new InfoRenderer();
        this.countdownRenderer = new CountdownRenderer();

        setPreferredSize(new Dimension(1150, 500));
        setBackground(new Color(80, 160, 80));
        setFocusable(true);//فوکس میگیره که اینپوت ها کار کنن
        setFocusTraversalKeysEnabled(false);

        new RaceInputBinder(this, race.getPlayerInput()).install();
        //بعد از اینکه پنل روی UI واقعاً قرار گرفت، ازش بخواه focus بگیره.
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    public void setCountdownText(String countdownText) {
        this.countdownText = countdownText;
        repaint();
    }

    public void requestGameFocus() {
        requestFocusInWindow();
    }
    //هر بار که پنل repaint میشه، این متد اجرا میشه و کل صحنه‌ی بازی رو دوباره می‌کشه.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  //اول پنل رو پاک کن و background رو رسم کن.
        //A COPY OF G
        Graphics2D g2 = (Graphics2D) g.create();
        enableSmoothRendering(g2);

        TrackViewport viewport = new TrackViewport(race.getTrack(), getWidth(), getHeight() - 90, 70);

        roadRenderer.drawRoad(g2, race.getTrack(), viewport);
        obstacleRenderer.drawObstacles(g2, race.getTrack(), viewport, PLAYER_LANE_OFFSET, OPPONENT_LANE_OFFSET);
        //داخلی
        drawCars(g2, viewport);

        infoRenderer.drawInfo(g2, race.getPlayerCar(), race.getOpponentCar());
        eventRenderer.drawEventLogs(g2, race.getEventLogs(), 60, getHeight() - 24);
        countdownRenderer.drawCountdown(g2, countdownText, getWidth());
        //CLOSE THE COPY
        g2.dispose();
    }

    private void drawCars(Graphics2D g2, TrackViewport viewport) {
        Car playerCar = race.getPlayerCar();
        Car opponentCar = race.getOpponentCar();

        carRenderer.drawCar(
                g2,
                playerCar,
                race.getTrack(),
                viewport,
                PLAYER_LANE_OFFSET,
                new Color(30, 100, 255),
                "Player"
        );

        carRenderer.drawCar(
                g2,
                opponentCar,
                race.getTrack(),
                viewport,
                OPPONENT_LANE_OFFSET,
                new Color(255, 140, 30),
                "Opponent"
        );
    }
    //کیفیت رسم رو بهتر می‌کنه.
    private void enableSmoothRendering(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}