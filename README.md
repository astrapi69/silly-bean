# silly-bean

**Small Java beans that hold two, three or four values: a pair, a triple, a quattro, a key with a
value, a list, a set or a map of values, a box for one value, and an enum that can carry a value it
does not have yet.**

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-bean/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-bean/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-bean/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-bean)
[![Mutation Score](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fastrapi69%2Fsilly-bean%2Fgh-pages%2Fbadges%2Fmutation-score-develop.json)](https://pitest.org/)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/silly-bean.svg?style=flat)](https://github.com/astrapi69/silly-bean/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-bean.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-bean)
[![Javadoc](https://www.javadoc.io/badge/io.github.astrapi69/silly-bean.svg)](https://www.javadoc.io/doc/io.github.astrapi69/silly-bean)
[![MIT license](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/projects/jdk/25/)

</div>

silly-bean holds the value beans that the libraries of this family pass around, silly-collection's
`MapFactory` first among them, and keeps them tested: PIT checks that the tests notice when the
code changes. It has no dependency.

## Why silly-bean

Each class of the pair package is a plain bean: a builder, `toBuilder`, getters and, but for
`ImmutableBox`, setters, `equals`, `hashCode` and `toString` over all of its fields, and
`Serializable`. `Pair`, `Triple`
and `Quattro` hold two, three and four values of their own types; `KeyValuePair` a key and a value,
and turns a `Map` or `Properties` into a list of pairs and back; `KeyValuesPair`, `KeySetPair` and
`KeyMapPair` a key with a collection, a set or a map of values; `ValueBox` and `ImmutableBox` one
value, to change or not. `DecoratedEnum` names an enum constant, or a value the enum does not have
yet. Three comparators order the key pairs by their keys.

If a value holder is all you need, the JDK is enough: since Java 16 a `record` is a one-line
immutable pair or triple with `equals`, `hashCode` and `toString`, and `Map.entry(k, v)` (no
`null`) or `AbstractMap.SimpleEntry` and `SimpleImmutableEntry` (with `null`) are a key and a value.
If you use [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) already, its
`org.apache.commons.lang3.tuple` package has `Pair`, `ImmutablePair` and `MutablePair`, `Triple`,
`ImmutableTriple` and `MutableTriple`, all `Comparable`, and its `Pair` is a `Map.Entry`. silly-bean is
for code of this family that takes its types, and for a quattro or a key with a set or a map, which
neither has.

## Table of contents

- [Why silly-bean](#why-silly-bean)
- [Requirements](#requirements)
- [Installation](#installation)
- [Upgrading from 3.0](#upgrading-from-30)
- [Quick start](#quick-start)
- [Features](#features)
- [Quality](#quality)
- [Verifying the release signatures](#verifying-the-release-signatures)
- [Releasing](#releasing)
- [Contributing](#contributing)
- [How this project is made](#how-this-project-is-made)
- [License](#license)
- [Support this project](#support-this-project)
- [Similar projects](#similar-projects)
- [Credits](#credits)

## Requirements

**JDK 25 or newer**, at build and at runtime: from 4.0 on its jar holds Java 25 bytecode and does not
load on an older JVM. **3.0 is the last release for Java 17**; a project below Java 25 stays on it.
silly-bean has no dependency.

## Installation

Latest release on Maven Central: 4.0, for Java 25 (for Java 17: 3.0)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-bean.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-bean)

**Gradle** (version catalog, `gradle/libs.versions.toml`):

```toml
[versions]
silly-bean-version = "4.0"

[libraries]
silly-bean = { module = "io.github.astrapi69:silly-bean", version.ref = "silly-bean-version" }
```

```groovy
dependencies {
    implementation libs.silly.bean
}
```

**Gradle** (plain):

```groovy
dependencies {
    implementation "io.github.astrapi69:silly-bean:4.0"
}
```

**Maven**:

```xml
<dependency>
    <groupId>io.github.astrapi69</groupId>
    <artifactId>silly-bean</artifactId>
    <version>4.0</version>
</dependency>
```

The module name is `io.github.astrapisixtynine.silly.bean`.

## Upgrading from 3.0

4.0 needs Java 25; its classes and their methods are the ones of 3.0. The CHANGELOG's
[Version 4.0](CHANGELOG.md#version-40) lists what else changed; a **Note** marks what a caller may
notice.

## Quick start

The examples are checked: each block runs, with the values its comments name as assertions, in
[`ReadmeExamplesTest`](src/test/java/io/github/astrapi69/ReadmeExamplesTest.java).

**Pairs, triples and quattros.** The builder names each value; `toBuilder` starts a new object from
the values of one.

```java
Pair<String, Integer> pair = new Pair<>("one", 1);
pair.getLeftContent();  // "one"
pair.getRightContent(); // 1

Triple<String, Integer, Boolean> triple = Triple.<String, Integer, Boolean> builder()
    .left("a").middle(2).right(true).build();
Triple<String, Integer, Boolean> changed = triple.toBuilder().middle(3).build(); // a, 3, true
triple.getMiddle(); // 2: toBuilder leaves the triple as it is

Quattro<String, String, String, String> corners = Quattro.<String, String, String, String> builder()
    .topLeft("nw").topRight("ne").bottomLeft("sw").bottomRight("se").build();
corners.getTopRight(); // "ne"
```

**A key with its values.**

```java
Map<String, Integer> numbers = new LinkedHashMap<>();
numbers.put("one", 1);
numbers.put("two", 2);
List<KeyValuePair<String, Integer>> pairs = KeyValuePair.toKeyValuePairs(numbers); // [one=1, two=2], in the order of the map
Map<String, Integer> map = KeyValuePair.toMap(pairs);                              // {one=1, two=2}, a HashMap

KeyValuesPair<String, String> vowels = KeyValuesPair.<String, String> builder()
    .key("vowels").value("a").value("e").build();
vowels.getValues(); // [a, e]
```

**Sorting by the key.**

```java
List<KeyValuePair<String, Integer>> list = new ArrayList<>();
list.add(new KeyValuePair<>("b", 1));
list.add(new KeyValuePair<>("a", 2));
list.sort(new KeyValuePairKeyComparator<>()); // a=2, b=1
```

**Boxes.**

```java
ValueBox<String> box = new ValueBox<>("first");
box.setValue("second");                                                // a ValueBox changes its value
ImmutableBox<String> fixed = new ImmutableBox<>("only");               // an ImmutableBox has no setter
ImmutableBox<String> other = fixed.toBuilder().value("other").build(); // a new box
```

**An enum with a value it does not have yet.**

```java
DecoratedEnum<ModeContext, String> known = new DecoratedEnum<>(ModeContext.CREATE, null);
known.name(); // "CREATE"
DecoratedEnum<ModeContext, String> added = new DecoratedEnum<>(null, "ARCHIVE");
added.name(); // "ARCHIVE", a mode ModeContext does not have
```

## Features

| Package | What it holds |
|---|---|
| `io.github.astrapi69.collection.pair` | `Pair`, `Triple`, `Quattro`; `KeyValuePair` (with `toKeyValuePairs` of a `Map` or `Properties`, and `toMap`), `KeyValuesPair`, `KeySetPair`, `KeyMapPair`; `ValueBox`, `ImmutableBox` |
| `io.github.astrapi69.collection.comparator` | `KeyValuePairKeyComparator`, `KeyValuesPairKeyComparator`, `KeyMapPairKeyComparator`: the key pairs by their keys |
| `io.github.astrapi69.bean.enumeration` | `DecoratedEnum`, an enum constant or a value the enum does not have yet; `ModeContext`, the modes create, delete, none and update |
| `io.github.astrapi69.annotation` | `Premium`, which tags a class or a field for use with a premium permission or version only |

The [Javadoc](https://www.javadoc.io/doc/io.github.astrapi69/silly-bean) has the details.

## Quality

As of 4.0, measured on Temurin 25.0.5+8, the JDK `.sdkmanrc` names; each release takes the numbers of
its own release gate:

| | |
|---|---|
| tests | 430 in 16 test classes, from 49 `@Test` and 73 `@ParameterizedTest` methods; 3 are skipped, the `BeanTester` tests of `DecoratedEnum`, `Triple` and `Quattro`, which were `@Disabled` before 4.0 |
| line and branch coverage (JaCoCo) | 547 of 547 lines, 259 of 260 branches; the build fails below 98% of the lines or 97% of the branches (`jacocoTestCoverageVerification` in `gradle/testing.gradle`). The missed branch is the one of `KeySetPair`'s builder for 2^30 values or more |
| mutation score (PIT) | 338 of 342 mutants detected, 99%; the run fails below 97% (`mutationThreshold` in `build.gradle`). The 4 survivors are equivalent: they change only the initial capacity of the `LinkedHashSet` that `KeySetPair`'s builder makes, never its content |
| javadoc | no warning: the build runs javadoc with `-Werror` |
| reproducible | two builds of the same commit give the same bytes for all five artifacts (`scripts/check-reproducible.sh`) |

The CI build runs the tests, the coverage check and javadoc with `-Werror` on every pull request,
and the workflow "Mutation testing" runs PIT on GitHub, on every pull request and every push to
`develop` and `master`, and publishes the mutation score of `develop` as the badge above. A pull
request into `develop` is merged only when both are green.

## Verifying the release signatures

The releases on Maven Central are signed with the OpenPGP key

```text
5B2F A6B1 1E29 8BC0 9287  F423 D8C4 0351 8C49 CA75
```

Since 4.0 they are signed by its signing subkey

```text
6D67 F844 2A6F CC96 BD7C  1B5E 9FCF 7C97 10E2 BD8D
```

valid until 2028-10-09; 3.0 was signed by the primary key itself. The key, with the subkey, is on
keys.openpgp.org. Fetch it once, then verify the signature next to each file:

```sh
gpg --keyserver hkps://keys.openpgp.org --recv-keys 5B2FA6B11E298BC09287F423D8C403518C49CA75
gpg --verify silly-bean-4.0.jar.asc silly-bean-4.0.jar
```

gpg answers `Good signature` and prints the `Primary key fingerprint`, and for a release signed by
the subkey the `Subkey fingerprint`; both have to be the ones above. The warning that the key "is not
certified with a trusted signature" only says that you have not marked it as trusted in your own
keyring.

## Releasing

What a release consists of, in the order it is run:

1. **No open issue** but those of a later milestone: every issue of the release's milestone is
   closed, and a finding on the way is fixed in the release or, if it changes what callers rely
   on, assigned to the next major release.
2. **One pull request** with the CHANGELOG entry and `projectVersion` without `-SNAPSHOT`
   (`docs: changelog for X.Y, and the version that carries it`), the full build green on it.
3. **The release gate** on that pull request's tree: the tests and `jacocoTestCoverageVerification`
   with its floor in one run, `--rerun-tasks --no-build-cache`, on the JDK `.sdkmanrc` names, with
   an empty local Maven repository (`-Dmaven.repo.local=<an empty directory>`;
   `gradle/repositories.gradle` reads `mavenLocal()` only with `-PuseMavenLocal`); javadoc prints no
   warning (the build runs it with `-Werror`), the class files are of Java 25 (major version 69),
   and the resolved dependencies hold no `-SNAPSHOT`; `scripts/check-reproducible.sh` builds the
   five artifacts twice and finds them identical. PIT does not run on a local machine: its numbers
   and its floor come from the run of "Mutation testing" on the pull request, and again from its run
   on the merge commit of the pull request, whose tree is the tag's tree. The GitHub release
   (step 6) links the run on the merge commit, with the proof that its tree is the tag's tree:
   `git rev-parse "<the run's commit>^{tree}" "RELEASE-X.Y^{tree}"` prints the same tree twice
   (`gh run view <run> --json headSha` names the run's commit).
4. **Tag the merge commit** with `./gradlew tagRelease` (`make tag-release CONFIRM=yes`), which names
   it `RELEASE-X.Y`, once its tree is the tree of the gate and the "Mutation testing" run on it has
   the numbers of the gate, and push the tag. `publish.yml` uploads to the Central Portal, where the
   maintainer releases it. A pushed tag is never moved or deleted; a failed run is fixed on the
   branch and run again.
5. **Verify on Central**: the jar, pom, `.module`, `-sources.jar` and `-javadoc.jar`, their sha1, and
   `gpg --verify` on the `.asc`; a local build of the tag, on the JDK `.sdkmanrc` names, gives the
   same bytes as Central for all five. A release is uploaded only by `publish.yml`, from its tag;
   `make tag-release` creates the tag and nothing else.
6. **GitHub release** from the CHANGELOG entry.
7. **`master` follows the release** by a merge of the tag: on `master`,
   `git merge --no-ff "RELEASE-X.Y^{commit}" -m "Merge tag RELEASE-X.Y into master"`. Before
   `master` is pushed, `git diff RELEASE-X.Y master` has to be empty; if it is not, nothing is
   pushed. Never a force push: `master` is always the latest release, nothing else.
8. **Open the next cycle**: `projectVersion` to the next `-SNAPSHOT`, by a pull request, as
   `develop` takes no direct push.

## Contributing

Pull requests are welcome: fork [astrapi69/silly-bean](https://github.com/astrapi69/silly-bean/fork)
and [open a pull request](https://github.com/astrapi69/silly-bean/pull/new/develop) against
`develop`. [CONTRIBUTING.md](.github/CONTRIBUTING.md) has the details.

Please add tests for your change; the coverage and mutation floors above hold for it too. The
`Makefile` runs the usual tasks:

```shell
make build              # build: tests, coverage check, javadoc, formatting check
make spotless           # format, and write the license headers
make pitest             # mutation testing on GitHub for the pushed branch, fails below the floor
make check-reproducible # build the five artifacts twice and compare them
```

PIT does not run on your machine: `make pitest` starts `mutation-testing.yml` on GitHub for the
current branch, which has to be pushed first, and prints how to follow the run; `make pitest-status`
shows the summary of the newest run of the branch: how many mutants were killed of how many, the
score, the floor and every mutant that survived. PIT's report of a run is its artifact
`pitest-report`: `gh run download <run> -n pitest-report -D <directory>`.

The versions follow the
[Simplified Semantic Versioning](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning)
guidelines, `<major>.<minor>.<patch>`. Questions, bug reports and feature requests belong on the
[issues page](https://github.com/astrapi69/silly-bean/issues).

## How this project is made

silly-bean was written by its maintainer since 2015, before there were AI tools; release 4.0 was
implemented mostly by AI agents, under the maintainer's review and with the maintainer's decision on
every change of behaviour. How AI is used in the projects of this profile:
[AI_USAGE.md](https://github.com/astrapi69/astrapi69/blob/main/AI_USAGE.md).

## License

MIT - see [LICENSE](LICENSE).

## Support this project

If silly-bean is useful to you, a star on [GitHub](https://github.com/astrapi69/silly-bean) helps
others find it. Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button,
which reads [.github/FUNDING.yml](.github/FUNDING.yml): GitHub Sponsors, Liberapay, Ko-fi and PayPal.

## Similar projects

- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/): `Pair` and `Triple`,
  mutable and immutable, in `org.apache.commons.lang3.tuple`
- [javatuples](https://www.javatuples.org/): tuples of one to ten values
- [vavr](https://vavr.io/): immutable tuples of up to eight values, with functional operations

## Credits

| | |
|---|---|
| [Maven Central](https://central.sonatype.com/artifact/io.github.astrapi69/silly-bean) | for hosting the artifacts of this open source project |
| [codecov.io](https://codecov.io) | for free code coverage reporting |
| [javadoc.io](https://www.javadoc.io) | for free javadoc hosting |
| [PIT](https://pitest.org/) | for mutation testing |

No animals were harmed in the making of this library.
