# AIS Marine Tracker Application

AIS Marine Tracker is a spring boot Java application specifically implemented for tracking marine vessels on a map. It uses a decoder to process Automatic Identification System (AIS) messages and display live data about the vessels such as name, ship type, destination, speed, and coordinates. The information extracted from messages is used to position markers on a map component.

## Features

- AIS Messages Decoder: A built-in AIS decoder to decode and validate incoming AIS messages.
- Live Tracking: Continuous tracking and refreshing of vessel positions on a map.
- Detailed Information: Display detailed information about a vessel when its marker is clicked on the map, including coordinates, speed, heading and more.
- Reliable: Error handling is in place for invalid messages and coordinates.
- Filterable tracking: Options to filter tracking by ship type, name or speed over ground

## Installation

1. **Pre-Requisites** Install Java 17 JDK and Apache Maven
2. **Clone the project** `git clone git@github.com:MateuszJagienski/AISMarineTracker.git`
3. **Run** `mvn clean install` then `mvn pring-boot:run`
4. **Create package** Navigate to the cloned directory and use the command `mvn clean package -Pproduction` to create jar file, then execute `java -jar AISMarineTracker-0.0.1-SNAPSHOT.jar`.
5. **Run ais simulator** `python3 src/main/java/pl/ais/aismarinetracker/decoder/aisim.py src/main/resources/META-INF/resources/aisdata/allAisData.txt`
