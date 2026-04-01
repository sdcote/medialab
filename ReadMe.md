# Media Lab
This project is a collection of Snap Jobs (Read-Transform-Write) to help manage media opinionatedly. Media is sorted into either Images or Videos and each has a set of folders based on the date when the media was created. These serve as the backbone of the media libarary.

Other jobs are designed to help manage the media library, including performing batch converts, creating collections and scrubbing media of personal information.

## Coyote Commons
This project relies heavily on the [Coyote Commons](https://github.com/coyote-labs/coyote-commons) project. The Coyote Commons project is a collection of Java libraries that are used by the Media Lab to perform most of the heavy lifting. Only when logic is specific to media management or requires external libraries is it placed in this project. All general-purpose logic is obtained from Coyote Commons.

There are several SnapJobs that are designed to help with management of a large media library. Most of them use Commons classes, and some use classes from this project. In either case, these Snap Jobs help with mundane tasks like converting one format to another and moving files around.

## Building
This project uses Maven to build. The current JDK used is Java 25 as that is what is currently being used by Coyote Commons.