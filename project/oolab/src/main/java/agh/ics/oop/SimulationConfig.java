package agh.ics.oop;

public class SimulationConfig {
    private final int mapWidth;
    private final int mapHeight;
    private final int numberOfAnimals;
    private final int numberOfGrass;
    private final int geneLength;
    private final int numberOfDailyGrass;
    private final int numberOfEnergy;
    private final int energyNeededToCopulate;
    private final int energyLose;
    private final int energyGain;
    private final int minMut;
    private final int maxMut;

    private final String selectedGenetype;


    private final int fireDuration;
    private final int fireFrequency;

    private final String selectedMapType;

    // Konstruktor z pełnymi parametrami, w tym pożarami
    public SimulationConfig(String mapWidthInput, String mapHeightInput, String numberOfAnimalsInput,
                            String numberOfGrassInput, String geneLengthInput, String numberOfDailyGrassInput,
                            String numberOfEnergyInput, String energyNeededToCopulate, String energyLose, String energyGain, String minMut, String maxMut, String selectedGenetype,
                            String fireDuration, String fireFrequency, String selectedMapType) throws IllegalArgumentException {

        // Walidacja i konwersja wartości
        this.mapWidth = validatePositiveInteger(mapWidthInput, "Map width");
        this.mapHeight = validatePositiveInteger(mapHeightInput, "Map height");
        this.numberOfAnimals = validateInteger(numberOfAnimalsInput, "Number of Animals");
        this.numberOfGrass = validateInteger(numberOfGrassInput, "Number of Grass");
        this.geneLength = validatePositiveInteger(geneLengthInput, "Gene Length");
        this.numberOfDailyGrass = validateInteger(numberOfDailyGrassInput, "Number of Daily Grass");
        this.numberOfEnergy = validatePositiveInteger(numberOfEnergyInput, "Number of Energy");
        this.energyNeededToCopulate = validatePositiveInteger(energyNeededToCopulate, "Number of energy needed to copulate");
        this.energyLose = validatePositiveInteger(energyLose, "Number of energy lose during copulate");
        this.energyGain = validatePositiveInteger(energyGain, "Number of energy gain");
        this.minMut = validateInteger(minMut, "Minimum of mutations");
        this.maxMut = validateInteger(maxMut, "Minimum of mutations");
        this.selectedGenetype = selectedGenetype;
        this.fireDuration = validateInteger(fireDuration, "Fire duration");
        this.fireFrequency = validatePositiveInteger(fireFrequency, "Fire Frequency");
        this.selectedMapType = selectedMapType;
    }

    public SimulationConfig(String mapWidthInput, String mapHeightInput, String numberOfAnimalsInput,
                            String numberOfGrassInput, String geneLengthInput, String numberOfDailyGrassInput,
                            String numberOfEnergyInput, String energyNeededToCopulate, String energyLose, String energyGain, String minMut, String maxMut, String selectedGenetype,
                            String selectedMapType) throws IllegalArgumentException {

        this.mapWidth = validatePositiveInteger(mapWidthInput, "Map width");
        this.mapHeight = validatePositiveInteger(mapHeightInput, "Map height");
        this.numberOfAnimals = validateInteger(numberOfAnimalsInput, "Number of Animals");
        this.numberOfGrass = validateInteger(numberOfGrassInput, "Number of Grass");
        this.geneLength = validatePositiveInteger(geneLengthInput, "Gene Length");
        this.numberOfDailyGrass = validateInteger(numberOfDailyGrassInput, "Number of Daily Grass");
        this.numberOfEnergy = validatePositiveInteger(numberOfEnergyInput, "Number of Energy");
        this.energyNeededToCopulate = validatePositiveInteger(energyNeededToCopulate, "Number of energy needed to copulate");
        this.energyLose = validatePositiveInteger(energyLose, "Number of energy lose during copulate");
        if (this.energyNeededToCopulate<this.energyLose){
            throw new IllegalArgumentException("Number of energy needed to copulate must be greater than energy lose during copulate");
        }
        this.energyGain = validatePositiveInteger(energyGain, "Number of energy gain");
        this.minMut = validateInteger(minMut, "Minimum of mutations");
        this.maxMut = validateInteger(maxMut, "Maximum of mutations");
        if (this.minMut>this.maxMut){
            throw new IllegalArgumentException("Maximum mutations must be greater or equal then minimum mutation");
        }
        this.selectedGenetype = selectedGenetype;

        // Ustawiamy domyślne wartości dla parametrów związanych z pożarami
        this.fireDuration = 0; // Domyślny czas trwania pożaru
        this.fireFrequency = 0; // Domyślna częstotliwość pożarów
        this.selectedMapType = selectedMapType;
    }

    private int validateInteger(String input, String fieldName) {
        try {
            int value = Integer.parseInt(input);
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " must be greater or equal than 0.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }

    private int validatePositiveInteger(String input, String fieldName) {
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }

    // Gettery
    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getNumberOfGrass() {
        return numberOfGrass;
    }

    public int getGeneLength() {
        return geneLength;
    }

    public int getNumberOfDailyGrass() {
        return numberOfDailyGrass;
    }

    public int getNumberOfEnergy() {
        return numberOfEnergy;
    }

    public int getEnergyNeededToCopulate() {
        return energyNeededToCopulate;
    }

    public int getEnergyLose() {
        return energyLose;
    }

    public int getFireDuration() {
        return fireDuration;
    }

    public int getFireFrequency() {
        return fireFrequency;
    }

    public String getSelectedMapType() {
        return selectedMapType;
    }
    public int getMaxMut() {
        return maxMut;
    }

    public String getSelectedGenetype() {
        return selectedGenetype;
    }

    public int getMinMut() {
        return minMut;
    }

    public int getEnergyGain() {
        return energyGain;
    }
}
