## Change log
----------------------

Version 3-SNAPSHOT
-------------

ADDED:

- new libs.versions.toml file for new automatic catalog versions update

CHANGED:

- update gradle to new version 8.10
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor version 7.0.0.BETA1
- update of test dependency junit-jupiter to new version 5.11.0
- update of test dependency junit-platform-launcher to new version 1.11.0
- update of test dependency jobj-contract-verifier version to new minor version 5.2
- update of test dependency commons-lang3 version to 3.16.0
- replaced obsolete package.html with package-info.java files

Version 2
-------------

ADDED:

- new unit test framework junit-jupiter-api(junit5)
- new enum Premium that tags a class or field for use only with a premium permission or version
- new decorator class that can decorate an enum class with a specified value
- new module-info.java file for modularize this repository

CHANGED:

- update to jdk version 11
- update gradle to new version 7.5.1
- remove of unit test framework testng
- rename of module and repository from plural-form silly-beans to singular-form silly-bean
- update of test dependency jobj-contract-verifier version to 3.5
- update of test dependency silly-collections version to 19
- update of test dependency test-object version to 7

Version 1.2
-------------

CHANGED:

- changed to new package io.github.astrapi69
- upgrade gradle version to 6.9
- update of test dependency jobj-contract-verifier version to 3.4
- update of test dependency silly-collections version to 8.6
- update of test dependency test-objects version to 5.4
- update of test dependency commons-lang3 version to 3.12.0
- update of test dependency testng version to 7.4.0

Version 1.1
-------------

ADDED:

- new enum created that express the modification state
- new build system gradle
- new branch with lombok dependency

CHANGED:

- update of test dependency jobj-contract-verifier version to 3.1.1
- update of test dependency test-objects version to 5.2
- removed lombok dependency for the develop branch
- removed maven build system and all related files
- removed all lombok dependent imports

Version 1
-------------

ADDED:

- this changelog file
- created PULL_REQUEST_TEMPLATE.md file
- created CODE_OF_CONDUCT.md file
- created CONTRIBUTING.md file
- provide package.html for the javadoc of packages
- moved classes from obsolet jcommons-lang project
