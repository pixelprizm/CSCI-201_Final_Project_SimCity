CSCI-201 Final Project: SimCity
======

SimCity201 Project Repository for CSCI-201 team 18.

This is a simulation of a city, which supports a large number of autonomous agents (maximum is about 100), who have various interactions.  Each person has a job and a house, and each person's behavior is determined by their job role when they enter their workplace, or by a customer role for a restaurant or place that they're visiting.

###Run Instructions

1. Download the project to the computer either by using ```git clone git@github.com:egauderman/CSCI-201_Final_Project_SimCity.git``` or by manually downloading the repository's ```.zip``` file from GitHub.

2. In the Eclipse IDE, create a new java project. Remember to include JRE System Library (JavaSE-1.7), and JUnit 4 Library

3. Import "source" folder into the project. Right click the "source" folder. In "Bulid Path", choose "Use as a Source Folder"

4. Choose MainGui.java under source/gui. Click "Run as..." and choose "Java Application" to run the program.

5. To run scenarios, choose the Configuration tab at the top right corner, and select a configuration. Descriptions of each scenario will be displayed.  Make sure to click the start button once you have selected a scenario.  To run a second scenario, we recommend you quit and restart the program so as to assure they work correctly.

###V2 Deliverable Information

####Work Done by Each Person

- Eric Gauderman (Integration Lead/Git Lead)
  - ```PersonAgent``` class (sophisticated upgrade in v2)
    - ```sources/city/PersonAgent.java```
    - Acquiring job, home, and customer roles
    - Autonomous decision-making
    - System for running forced scenarios
  - Home Scenarios (most importantly, HomeOccupantRole)
  - Integration, major contributor (with Omar and Yixin)
  - Instantiation of Person Agent with the correct job and house; use of factory methods to generate each person's correct job and home roles (with Yixin)
  - Design of program's architecture (i.e. packages and class hierarchy)
  - Designed ```Directory``` class
  - Integrated his Restaurant (including revolving stand, market interaction, bank interaction)
  - Helped with other team members' questions & issues
  - Specified and fixed naming conventions
  - Bug fixing and Git merge conflicts
- Omar Khulusi (Team Leader)
  - Bank Scenarios (+ Robber/GuardDog for v2)
  - Console Log integration
  - Configuration Panel v2 upgrades (Upgraded configuration panels to run scenarios)
  - Gui Panel Upgrades (took over for Tanner)
  - Animation Panels (Building interior animation panels and worldview specifically)
  - Integration, major contributor (with Eric and Yixin)
  - Aided Eric with integration of Person Agent and Roles
  - Aided with Person Agent scheduler (worked closely Eric and Yixin)
  - Design of program's architecture (i.e. packages and class hierarchy)
  - Integrated his Restaurant (including revolving stand, market interaction, bank interaction)
  - Time class
  - Discussing major decisions (particularly with Eric and Yixin)
  - Bug fixing
- Yixin Cai (Restaurant Integration Lead, Largest Code Contributor)
  - First to fully integrate his Restaurant, and assisted Omar/Ryan/Eric in integrating restaurants
  - Finalized worldview (added lanes/semaphores/navigation system)
  - Added most non-normative scenarios
  - Workplace and person integration
  - Market design and scenarios (including interactions with all the restaurants)
  - Extensive Testing of city scenarios
  - Instantiation of Person Agent with the correct job and house; use of factory methods (with Eric)
  - Integration, major contributor (with Eric and Omar)
  - Bug fixing
- Ryan Hsu (Art Lead)
  - Transportation (TruckAgent, BusAgent, CommuterRole)
  - Integrating Truck and Market/Truck and Restaurant scenarios
  - Truck Gui
  - Integrated his Restaurant (integrated with revolving stand/market interaction/bank interaction)
  - Added all images and made them look spectacular
  - Worked with each individual restaurant panel to standardize them to look the same
- Tanner Zigrang -- (assessed separately from group)
  - Gui Panel Setup (more specifically, creation/configuration buttons/forms, organization of frames and panels)
  - Bank unit testing
  - Did not integrate restaurant into team's final deliverable
  - Worked on his restaurant on his own branch

####Known Issues
  - 1 restaurant not integrated (Tanner)
