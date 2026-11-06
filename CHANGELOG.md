## Change log
----------------------

Version 4.0
-------------

4.0 builds for Java 25, where 3.0 ran on Java 17 (#12); its classes and their methods are the ones of 3.0. It is
published through the Central Portal, from CI only (#14), builds the same bytes from the same commit (#24), fails on a
javadoc warning (#22), runs PIT on GitHub only (#19), and its tests are parameterized: 430 where 3.0 had 35 (#26, #27,
#29). Measured by the release gate (Temurin 25.0.5+8, the JDK `.sdkmanrc` names; the tree of `RELEASE-4.0`): 430 tests
in 16 test classes, 3 skipped; 547 of 547 lines and 259 of 260 branches covered (JaCoCo, floors 98% and 97%); 338 of
342 mutants detected, 99% (PIT 1.30.1, floor 97%, in the workflow "Mutation testing" on GitHub).

MIGRATION FROM 3.0:

- 4.0 needs Java 25 at build and at run time: its class files are of major version 69, where 3.0 had 61 for Java 17,
  so a JVM older than 25 cannot load them (#12). A project on Java 17 or 21 stays on 3.0.
- nothing else changes for a caller: `javap -public` prints the same for the 25 classes of 3.0, nested ones
  included, and of 4.0. The jar's manifest no longer has `Build-Timestamp`, `Build-Jdk` and `Build-OS` (#24).

CHANGED:

- published through the Central Portal with com.gradleup.nmcp, from CI only: `publish.yml` on a `RELEASE-*` tag, and
  by hand for a snapshot, signs with the release subkey; an upload from a local tree, a release from anything but its
  tag, half a key and a release without a key are refused. The OSSRH repositories and properties are gone, and so is
  the raw GitHub repository the build resolved jobj-contract-verifier from (#14)
- `KeyMapPairKeyComparator`, `KeyValuePairKeyComparator` and `KeyValuesPairKeyComparator` declare their public
  no-argument constructor with a Javadoc comment, so javadoc prints no warning; the binary interface stays the same, as
  the default constructor was public too. `DecoratedEnum` and its builder document their type parameters and members,
  and say that the constructor and `build()` throw a `NullPointerException` when neither an enum constant nor a value
  is given, as they did in 3.0 (#22)
- a javadoc warning fails the build: the `javadoc` task runs with `-Werror` and `failOnError = true`, and `check`
  depends on it, so `./gradlew build` and CI run javadoc and fail on a warning in every locale (#22). **Note:** a
  javadoc error fails the build now too; `failOnError` was `false`, because the `javadocJar` line
  `javadoc.properties.failOnError = true` wrote into a copy of the task's properties
- a build of the same commit gives the same bytes: the manifest names no build time, operating system or JDK patch
  level, only `Build-Jdk-Spec: 25`; javac compiles with `--release 25`; archives have fixed entry times and order; the
  javadoc is in English in every locale; the build reads the local Maven repository only with `-PuseMavenLocal`;
  `.sdkmanrc` names the JDK of a release, Temurin 25.0.5+8, and every workflow checks that it set up exactly that one;
  `scripts/check-reproducible.sh` (`make check-reproducible`) builds the five artifacts twice and compares them (#24).
  **Note:** the manifest of the jar no longer has `Build-Timestamp`, `Build-Jdk` and `Build-OS`
- mutation testing runs on GitHub only, in the workflow "Mutation testing" (`.github/workflows/mutation-testing.yml`):
  on a push to `develop` or `master`, on a pull request against them and on demand (`gh workflow run
  mutation-testing.yml --ref <branch>`, or `make pitest`). It runs `./gradlew pitest` (PIT 1.30.1, DEFAULTS
  mutators), which fails below `mutationThreshold` in `build.gradle`, writes a summary of the run (killed of total, the
  score as PIT rounds it, the floor, every mutant not detected, also on a red run) and keeps PIT's report as the
  artifact `pitest-report`; the README shows the mutation score badge. JaCoCo fails the build below a floor of lines
  and branches. A `Makefile` with the family's targets: `make pitest` and `make pitest-status` reach the workflow,
  `make tag-release` refuses a `-SNAPSHOT` version and wants `CONFIRM=yes`; no target publishes (#19). The floors
  follow the tests of 4.0: 97% of the mutants, 98% of the lines and 97% of the branches (#33)
- the tests of one behaviour with many inputs are parameterized tests with a name for each case, over records, the
  objects built in the tests: the three key comparators, which had no test (#22), the pair package (#26), the
  enumeration package, in the package it tests (#27), and `TripleComparator` (#29). 430 tests in 16 test classes, from
  49 `@Test` and 73 `@ParameterizedTest` methods, where 3.0 had 35 `@Test` methods in 11; the four mutants that survive
  are equivalent, they change only the initial capacity of the `LinkedHashSet` `KeySetPair`'s builder makes. Tests
  only, no change to the library
- the tests run against this project's classes alone: both test class paths exclude silly-bean, which reached them as
  3.0 through silly-collection (a direct test dependency, and jobj-contract-verifier -> randomizer) and which Gradle
  resolved to this project's own jar, without a task dependency on `jar`; the tests build their lists and sets with the
  JDK, and the test dependency on silly-collection is gone (#11)
- the build uses no grgit and no license plugin: `tagRelease` runs `git tag -a` itself, and Spotless enforces the MIT
  header the license plugin only pretended to check (it ran with `ignoreFailures = true`, its tasks excluded); the
  Gradle wrapper is the one Gradle publishes for 9.8.2, and the check that every test class left a result is the
  family's plugin `io.github.astrapi69.test-results-complete` 1.0 instead of a copy (#17)
- the README says what silly-bean is for and when the JDK or Commons Lang is enough, and its examples run as
  `ReadmeExamplesTest`; `.github/CONTRIBUTING.md` says what a pull request needs (#31)

Version 3.0
-------------

ADDED:

- new libs.versions.toml file for new automatic catalog versions update
- new project structure

CHANGED:

- module name from 'silly.bean.main' to new 'io.github.astrapisixtynine.silly.bean'
- update gradle to new version 8.10.2
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor version 7.0.0.BETA1
- update of test dependency junit-jupiter to new version 5.11.1
- update of test dependency junit-platform-launcher to new version 1.11.1
- update of test dependency jobj-contract-verifier version to new minor version 5.2
- update of test dependency commons-lang3 version to 3.17.0
- update of test dependency silly-collection to new major version 28
- update of test dependency test-object version to new major version 9
- moved the enums to new package 'io.github.astrapi69.bean.enumeration'
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
