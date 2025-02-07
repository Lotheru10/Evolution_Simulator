package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationConfig;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.awt.*;
import java.io.IOException;
import java.util.List;

import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener {
    private Simulation simulation;
    private Stats stats;
    private WorldMap map;
    //śledzenie
    private Animal trackedAnimal;
    private int trackedAnimalAge;
    private String deathDay = "";
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label movementDescriptionLabel;
    @FXML
    private VBox inputParameters;
    @FXML
    private TextField mapWidthField;
    @FXML
    private TextField mapHeightField;
    @FXML
    private TextField numberOfAnimalsField;
    @FXML
    private TextField numberOfGrassField;
    @FXML
    private TextField geneLength;
    @FXML
    private TextField numberOfDailyGrass;
    @FXML
    private TextField numberOfEnergy;
    @FXML
    private VBox animalInfoBox;
    @FXML
    private Label trackedAnimalLabel;
    @FXML
    private VBox statisticsBox;
    @FXML
    private Label statisticsLabel;
    @FXML
    private TextField energyNeededToCopulate;
    @FXML
    private TextField energyLose;
    @FXML
    private HBox pauseButton;

    @FXML
    private ToggleGroup mapTypeGroup;
    @FXML
    private ToggleGroup geneTypeGroup;

    @FXML
    private RadioButton normalGeneRadioButton;

    @FXML
    private RadioButton crazyGeneRadioButton;

    @FXML
    private RadioButton grassFieldRadioButton;

    @FXML
    private RadioButton fireMapRadioButton;

    @FXML
    private VBox fireParameters;

    @FXML
    private TextField fireDuration;

    @FXML
    private TextField fireFrequency;
    @FXML
    private TextField energyGain;
    @FXML
    private TextField minMut;
    @FXML
    private TextField maxMut;

    @FXML
    public void initialize() {
        mapTypeGroup = new ToggleGroup();

        grassFieldRadioButton.setToggleGroup(mapTypeGroup);
        fireMapRadioButton.setToggleGroup(mapTypeGroup);

        geneTypeGroup = new ToggleGroup();
        normalGeneRadioButton.setToggleGroup(geneTypeGroup);
        crazyGeneRadioButton.setToggleGroup(geneTypeGroup);

        // Listener dla zmiany wyboru mapy
        mapTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == fireMapRadioButton) {
                fireParameters.setVisible(true);
                fireParameters.setManaged(true);
            } else {
                fireParameters.setVisible(false);
                fireParameters.setManaged(false);
            }
        });
    }

    private int mapWidth;
    private int mapHeight;

    public String getSelectedGeneType() {
        if (geneTypeGroup.getSelectedToggle() == normalGeneRadioButton) {
            return "normal";
        } else  {
            return "crazy";
        }
    }

    public String getSelectedMapType() {
        if (mapTypeGroup.getSelectedToggle() == grassFieldRadioButton) {
            return "GrassField";
        } else  {
            return "FireMap";
        }
    }

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }
    public void columnsFunction() {
        double fieldSize = calculateFieldSize(); // Obliczenie rozmiaru pola
        for (int i = 0; i < mapWidth; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(fieldSize));
        }
    }

    public void rowsFunction() {
        double fieldSize = calculateFieldSize(); // Minimalnie zmniejszona wysokość
        for (int i = 0; i < mapHeight; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(fieldSize));
        }
    }

    public void addElements() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d pos = new Vector2d(x, y);

                // Sprawdzenie, czy pole jest zajęte
                if (map.isOccupied(pos)) {
                    Label label = new Label(map.objectAt(pos).toString());
                    label.setStyle("-fx-alignment: center; -fx-padding: 0;"); // Wyśrodkowanie, brak obwódek
                    mapGrid.add(label, x, mapHeight - y - 1); // Odwrócenie osi Y
                    GridPane.setHalignment(label, HPos.CENTER); // Wyśrodkowanie w poziomie
                }
            }
        }
    }

    private double calculateFieldSize() {
        double maxSize = 450.0; // Max rozmiar siatki w pikselach
        return Math.min(maxSize / mapWidth, maxSize / mapHeight); // pole
    }

    private Color interpolateColor(Color lowColor, Color midColor, Color highColor, double ratio) {
        if (ratio <= 0.5) {
            double adjustedRatio = ratio * 2;
            double red = lowColor.getRed() + adjustedRatio * (midColor.getRed() - lowColor.getRed());
            double green = lowColor.getGreen() + adjustedRatio * (midColor.getGreen() - lowColor.getGreen());
            double blue = lowColor.getBlue() + adjustedRatio * (midColor.getBlue() - lowColor.getBlue());
            return new Color(red, green, blue, 1.0);
        } else {
            double adjustedRatio = (ratio - 0.5) * 2;
            double red = midColor.getRed() + adjustedRatio * (highColor.getRed() - midColor.getRed());
            double green = midColor.getGreen() + adjustedRatio * (highColor.getGreen() - midColor.getGreen());
            double blue = midColor.getBlue() + adjustedRatio * (highColor.getBlue() - midColor.getBlue());
            return new Color(red, green, blue, 1.0);
        }
    }

    private Color getColorForEnergyLevel(Animal animal) {
        double energyRatio = Math.min(1.0, animal.getEnergy() /( (double) simulation.getEnergyNeededToCopulate()*10 ));
        return interpolateColor(Color.YELLOW, Color.ORANGE, Color.RED, energyRatio);
    }

    private void drawMap() {
        mapWidth = Integer.parseInt(mapWidthField.getText());
        mapHeight = Integer.parseInt(mapHeightField.getText());
        columnsFunction();
        rowsFunction();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d pos = new Vector2d(x, y);
                Object object = map.objectAt(pos);

                Rectangle tile = new Rectangle((int) calculateFieldSize(), (int) calculateFieldSize());

                // kolory
                if (object instanceof Animal) {
                    tile.setFill(getColorForEnergyLevel((Animal) object));
                    if (((Animal) object).getGenes().equals(stats.getMostPopularGenotype())){//nie działaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                        if (!simulation.isRunning()) {
                            tile.setFill(Color.ROSYBROWN);
                        }
                    }
                } else if (object instanceof Grass) {
                    tile.setFill(Color.GREEN);
                } else if(!simulation.isRunning() &&
                                    pos.getY()>(int)(mapHeight / 2 - 0.1 * mapHeight)-1 &&
                                            pos.getY()<(int)(mapHeight / 2 + 0.1 * mapHeight)+1) {
                        tile.setFill(Color.GRAY);
                } else {
                    tile.setFill(Color.LIGHTGRAY);
                }


                // Klikanie
                tile.setOnMouseClicked(event -> {
                    if (!simulation.isRunning()) {
                        if (object instanceof Animal) {
                            trackedAnimal = (Animal) object;
                            trackedAnimalAge = trackedAnimal.getAge();
                            updateTrackedAnimalInfo();
                        }
                    }
                });

                // Dodanie prostokąta do siatki
                mapGrid.add(tile, x, mapHeight - y - 1);
                GridPane.setHalignment(tile, HPos.CENTER);
            }
        }
        mapGrid.setGridLinesVisible(true);
    }





    private void clearGrid(){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
            movementDescriptionLabel.setText(message);
        });
    }


    @FXML
    private void onSimulationStartClicked() {
        String selectedGeneTypeGroup = getSelectedGeneType();
        String selectedMapType = getSelectedMapType();
        try {
            SimulationConfig config;

            // Jeśli mapa to FireMap, przekazujemy dodatkowe parametry dotyczące ognia
            if (selectedMapType.equals("FireMap")) {
                config = new SimulationConfig(
                        mapWidthField.getText(),
                        mapHeightField.getText(),
                        numberOfAnimalsField.getText(),
                        numberOfGrassField.getText(),
                        geneLength.getText(),
                        numberOfDailyGrass.getText(),
                        numberOfEnergy.getText(),
                        energyNeededToCopulate.getText(),
                        energyLose.getText(),
                        fireDuration.getText(),
                        fireFrequency.getText(),
                        energyGain.getText(),
                        minMut.getText(),
                        maxMut.getText(),
                        selectedGeneTypeGroup,
                        selectedMapType
                );
            } else {
                // Jeśli mapa to GrassField, używamy drugiego konstruktora, bez parametrów ognia
                config = new SimulationConfig(
                        mapWidthField.getText(),
                        mapHeightField.getText(),
                        numberOfAnimalsField.getText(),
                        numberOfGrassField.getText(),
                        geneLength.getText(),
                        numberOfDailyGrass.getText(),
                        numberOfEnergy.getText(),
                        energyNeededToCopulate.getText(),
                        energyLose.getText(),
                        energyGain.getText(),
                        minMut.getText(),
                        maxMut.getText(),
                        selectedGeneTypeGroup,
                        selectedMapType
                );
            }

            animalInfoBox.setVisible(true);
            animalInfoBox.setManaged(true);
            statisticsBox.setVisible(true);
            statisticsBox.setManaged(true);
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            inputParameters.setOpacity(0);
            inputParameters.setManaged(false);

            // Stworzenie mapy i innych elementów symulacji
            int mapWidth = config.getMapWidth();
            int mapHeight = config.getMapHeight();
            int numberOfGrass = config.getNumberOfGrass();

            AbstractWorldMap map;
            map = new GrassField(mapWidth, mapHeight, numberOfGrass);
            map.addObserver(this);

            this.stats = new Stats(map);

            simulation = new Simulation(config, map, this, stats);

            SimulationEngine engine = new SimulationEngine(List.of(simulation));

            movementDescriptionLabel.setText("Simulation started with map size: " + mapWidth + "x" + mapHeight);
            new Thread(engine::runSync).start();
        } catch (IllegalArgumentException e) {
            movementDescriptionLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void newGame(){
        SimulationApp simulationApp = new SimulationApp();
        try {
            simulationApp.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void pause(){
        simulation.pausing();
        mapChanged(map, "Paused");
    }

    public void updateTrackedAnimalInfo() {
        Platform.runLater(() -> {
            if (trackedAnimal != null) {
                if (trackedAnimalAge == trackedAnimal.getAge() + 1){
                    deathDay = "Day of death: " + simulation.getDay();
                }
                trackedAnimalAge++;

                //wyświetlanie info
                trackedAnimalLabel.setText(
                        "Tracked Animal ID: " + trackedAnimal.getId() + "\n" +
                                "Genotype:" + trackedAnimal.getGenes() +"\n" +
                                "Current gene:" + trackedAnimal.getGene() + "\n" +
                                "Energy: " + trackedAnimal.getEnergy() +"\n" +
                                "Grass eaten" + trackedAnimal.getNumberOfGrassEaten() + "\n" +
                                "Offspring number" + trackedAnimal.getNumberOfOffspring() + "\n" +
                                "Age: " + trackedAnimal.getAge() + "\n" +
                                deathDay
                );
            } else {
                trackedAnimalLabel.setText("No animal is being tracked.");
            }
        });
    }

    public void updateStatistics() {
        Platform.runLater(() -> {
            stats.updateStatistics();

            statisticsLabel.setText(
                    "Day: " + simulation.getDay() + "\n" +
                    "Total Animals: " + stats.getTotalAnimals() +"\n" +
                            "Total Grass: " + stats.getTotalGrass() +"\n" +
                            "Free Fields: " + stats.getFreeFields() +"\n" +
                            "Most Popular Genotype: " + stats.getMostPopularGenotype() +"\n" +
                            "Average Energy: " + String.format("%.2f", stats.getAverageEnergy()) +"\n" +
                            "Average Lifespan: " + String.format("%.2f", stats.getAverageLifespan()) +"\n" +
                            "Average Children: " + String.format("%.2f", stats.getAverageChildren())
                    );

        });
    }



}