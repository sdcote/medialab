## External Dependencies and Utilities
* This project relies on a local shared utility library located at the relative path: `../commons/`
* Before generating any new helper methods, string formatters, or UI component factories, analyze the source files located in `../commons/src/main/java/`.
* Reuse existing utility classes from that directory instead of writing redundant logic.

## Architectural and Coding Style Models

### Problems domain
* This project is intended to handle images, video, and audio media.
* Try to separate media handling classes from common, housekeeping code.
* Try to separate audio, video, and image processing classes in to different packages for easier maintenance.

### Reusability
* When designing new classes, method, or components, try to design them for reuse across other projects.
* Consider any new logic as a candidate for the commons (`../commons/`) project.

### Code Style and Conventions
* Mimic the naming conventions, logging configurations, and Java 26 idiomatic patterns established in the `../commons/` repository.
