Changelog
=========

2.0.0
* Now using JSON files instead of storing it all in a config file
* Added the ability to over-write existing aliases

1.3.1
* Fixed a bug that would throw "too many arguments" 
* Dropped support for 1.7 - only Factions 2.x and FactionsUUID 1.6 supported 
* `/factionsalias list` is now more informative 
* Improved Factions UUID 1.6 support (more to do though) 

1.3.0
* Fixed support for latest FactionsUUID
* Fixed support for latest Factions 2.x
* Fixed a bug for checking if Factions is enabled  

1.2.0
* Updating 2.x support to latest version of Factions

1.1.1
* Added debug information
* Added new requirement 'executerIsLeader'
* Fixes a bug related to unregistering 1.x commands 

1.1.0
* Added command `/factionsalias reload` with permission: factionsalias.reload
* Added command `/factionsalias list` with permission: factionsalias.list
* Added command `/factionsalias help` with permission: factionsalias.help
* Commands now properly unregister when the plugin disables 
* Help integration for Factions 1.x
* Fixes integration with new MassiveCore 

1.0.0
* Initial Release