# GemStallHelper
GemStallHelper is a RuneLite plugin designed to assist players with thieving from the Ardougne gem stall in Old School RuneScape.
It provides a helpful overlay to make the thieving process smoother and more efficient.
# Features
Overlay support: Highlights Ardougne Gem stall with green overlay when available to steal and red when the player will be interupted by a guard. Availability to steal is determined by checking if there are any guard NPCs within the configured distance of the player and in line of sight. Configured distance should be 6 for Ardougne guards and knights.
# Configurable options:
Choose what is the minimum distance for the guards respective to the player to turn the stall overlay red
# Installation
Open RuneLite.
Go to the Plugin Hub.
Search for GemStallHelper.
Click Install.
Enable the plugin in your settings panel.
# Configuration
You can configure GemStallHelper from the RuneLite plugin settings panel. Right now only the minimal guard distance can be configured
# Known Issues / Limitations
Plugin is only made for the Ardougne gem stall, if there is interest it can be expanded to all gem stalls or even more stall types
# Development
If you want to contribute:
git clone https://github.com/Pepijn-V/Ardy-Gem-Stall.git

cd gemstallhelper

mvn clean install
Builds will be located in target/.
# License
This project is licensed under the BSD 2-Clause License license.
# Author
Pepijn-V
