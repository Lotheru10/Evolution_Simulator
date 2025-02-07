This project was created for the Object-Oriented Programming course at AGH WI in the 2024/2025 academic year.

# Description

This project is an implementation of Darwinian Evolution. Animals move across the map, eat grass, and pass their genes through reproduction. When an animal's energy reaches zero, it dies. Each move requires some energy, so finding food is essential for survival. Sometimes, fights over food or reproduction occur.
Each animal has its own genotype, which is a combination of its parents' genotypes. The genotype consists of numbers from 0 to 7 and determines how the animal moves each day.

# Core Technology Stack

+ Java 16
+ Gradle
+ JavaFX

# Features

+ Highlighting animals with dominant genomes
+ Displaying a list of dominant genomes and the animals that possess them
+ Tracking a specific animal (including the number of its direct offspring and indirect descendants)
+ Checking which animals occupy a selected field
+ Displaying a simulation preview window
+ Presenting a statistics chart
+ Running simulations on two types of maps:
 + Globe Map – a map where animals that cross the border appear on the opposite side
 + Fire Map – a map where fires periodically start, spreading to adjacent grass and killing animals that enter burning fields
+ Allowing users to adjust various settings before starting the simulation
  
# Settings

# Running the Simulation
