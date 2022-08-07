# Changelog of RemoteMC-Core

## Version 0.2.0-beta (590AA40) "Universal Chat and Broadcast API" - Aug. 7 2022

**Full Changelog**: https://github.com/iXORTech/RemoteMC-Core/compare/v0.1.1-beta...v0.2.0-beta

### :warning: BREAKING CHANGES :warning:

- The dedicated messaging APIs for QQ Bots were removed
- The dedicated messaging/broadcasting APIs for Minecraft Servers were removed
- The `say` function of Minecraft Servers was replaced by `sendMessage`

### New features

- LOGs will be written to a file named `RemoteMC-Core.log`
- LOGs of every day will be archived in a file named `RemoteMC-Core.YYYY-MM-DD.log.gz`. An archive of more than 30 days or 3GB will be deleted
- Function for sending broadcast to QQ Groups was added

### Improvements

- LOGs outputting to `STDOUT` will shown in a simplified format
- Functions for sending messages to QQ Groups were moved from `QQBotEntity` to `QQGroupEntity`

### Bug fixes

`There is nothing to display yet.`

### Known Bugs

`There is nothing to display yet.`

## Version 0.1.1-beta (3CB9B5C) "User Friendly Version" - Jul. 25 2022

**Full Changelog**: https://github.com/iXORTech/RemoteMC-Core/compare/v0.1.0-beta.1...v0.1.1-beta

### :warning: BREAKING CHANGES :warning:

- The name of the built jar file no longer contains the revision number and the `-all` suffix.

### New features

- The version numbers in the software are shown in a more user-friendly way.

### Improvements

`There is nothing to display yet.`

### Bug fixes

`There is nothing to display yet.`

### Known Bugs

`There is nothing to display yet.`

## Version 0.1.0-beta (4052EB2) "The first beta version" - Jul. 16 2022

**Full Changelog**: https://github.com/iXORTech/RemoteMC-Core/commits/v0.1.0-beta

### :warning: BREAKING CHANGES :warning:

`There is nothing to display yet.`

### New features

- Configuration via a file
- I18N (Internationalization), currently supporting `en` (English) and `zh_CN` (Chinese Simplified)
- Welcome page
- Status page
- Functionalities for connecting Minecraft Servers via [`MCDR component of RemoteMC`](https://github.com/iXORTech/RemoteMC-MCDR) (tested with basic functionalities)
- APIs for operating Minecraft Server via [`MCDR component of RemoteMC`](https://github.com/iXORTech/RemoteMC-MCDR) (tested with basic functionalities)
- Connection functionalities for QQ Bots (untested)
- APIs for operating QQ Bots (untested)

### Improvements

`There is nothing to display yet.`

### Bug fixes

`There is nothing to display yet.`

### Known Bugs

`There is nothing to display yet.`
