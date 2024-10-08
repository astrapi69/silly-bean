# Overview

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-bean/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-bean/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-bean/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-bean)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/silly-bean.svg?style=flat)](https://github.com/astrapi69/silly-bean/issues)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-bean/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-bean)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-bean.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-bean)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

</div>

Utility library that provides some generic data beans like pair, triple and quattro.

If you like this project put a ⭐ and donate

# Donations

If you like this library, please consider a donation through paypal: <a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=MJ7V43GU2H386" target="_blank">
<img src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" alt="PayPal this" title="PayPal – The safer, easier way to pay online!" border="0" />
</a>

or over bitcoin or bitcoin-cash with:

36JxRRDfRazLNqUV6NsywCw1q7TK38ukpC

or over ether with:

0x588Aa02De98B1Ef70afeDC3ec5290130a3E5e273

or over flattr:

<a href="http://flattr.com/thing/4067696/astrapi69silly-bean-on-GitHub" target="_blank">
<img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" />
</a>

## Note

No animals were harmed in the making of this library.

## License

The source code comes under the liberal MIT License, making silly-bean great for all types of applications.

## Import dependencies to your project

<details>
  <summary>gradle (click to expand)</summary>

## gradle dependency

Replace the variable ${latestVersion} with the current latest
version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-bean/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-bean)

You can first define the version in the ext section and add than the following gradle dependency to
your project `build.gradle` if you want to import the core functionality of silly-bean:

define version in file gradle.properties

```
sillyBeanVersion=${latestVersion}
```

or in build.gradle ext area

```
    sillyBeanVersion = "${latestVersion}"
```

then add the dependency to the dependencies area

```
    implementation("io.github.astrapi69:silly-bean:$sillyBeanVersion")
```

# with new libs.versions.toml file

If you use the new libs.versions.toml file for new automatic catalog versions update

```
[versions]
```
```
silly-bean-version= "${latestVersion}"
```
```
[libraries]
```
```
silly-bean = { module = "io.github.astrapi69:silly-bean", version.ref = "silly-bean-version" }
```

then add the dependency to the dependencies area

```
    implementation libs.silly.bean
```

</details>

<details>
  <summary>Maven (click to expand)</summary>

## Maven dependency

Maven dependency is now on sonatype.
Check
out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~silly-bean~~~)
for latest snapshots and releases.

Add the following maven dependency to your project `pom.xml` if you want to import the core
functionality of silly-bean:

Then you can add the dependency to your dependencies:

    <properties>
        ...

```xml
        <!-- silly-bean version -->
        <silly-bean.version>${latestVersion}</silly-bean.version>
```

        ...
    </properties>
        ...
        <dependencies>
        ...

```xml
            <!-- silly-bean DEPENDENCY -->
            <dependency>
                <groupId>io.github.astrapi69</groupId>
                <artifactId>silly-bean</artifactId>
                <version>${silly-bean.version}</version>
            </dependency>
```

        ...
        </dependencies>

</details>


<details>
  <summary>Snapshots (click to expand)</summary>

## 📸 Snapshots

[![Snapshot](https://img.shields.io/badge/dynamic/xml?url=https://oss.sonatype.org/service/local/repositories/snapshots/content/io/github/astrapi69/silly-bean/maven-metadata.xml&label=snapshot&color=red&query=.//versioning/latest)](https://oss.sonatype.org/content/repositories/snapshots/io/github/astrapi69/silly-bean/)

This section describes how to import snapshot versions into your project.
Add the following code snippet to your gradle file in the repositories section:

```
repositories {
   //...
```

```groovy
    maven {
        name "Sonatype Nexus Snapshots"
        url "https://oss.sonatype.org/content/repositories/snapshots"
        mavenContent {
            snapshotsOnly()
        }
    }
```

```
}
```

</details>


## Semantic Versioning

The versions of silly-bean are maintained with the Simplified Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning for this project you can visit this [wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning).

## Want to Help and improve it? ###

The source code for silly-bean are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [astrapi69/silly-bean/fork](https://github.com/astrapi69/silly-bean/fork)

To share your changes, [submit a pull request](https://github.com/astrapi69/silly-bean/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the silly-bean developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/astrapi69/silly-bean/issues).

## Credits

|**Nexus Sonatype repositories**|
|     :---:      |
|[![sonatype repository](https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.astrapi69/silly-bean.svg?style=for-the-badge)](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~silly-bean~~~)|
|Special thanks to [sonatype repository](https://www.sonatype.com) for providing a free maven repository service for open source projects|
|     <img width=1000/>     |

|**coveralls.io**|
|     :---:      |
|[![Coverage Status](https://coveralls.io/repos/github/astrapi69/silly-bean/badge.svg?branch=develop)](https://coveralls.io/github/astrapi69/silly-bean?branch=develop)|
|Special thanks to [coveralls.io](https://coveralls.io) for providing a free code coverage for open source projects|
|     <img width=1000/>     |

|**javadoc.io**|
|     :---:      |
|[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-bean.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-bean)|
|Special thanks to [javadoc.io](http://www.javadoc.io) for providing a free javadoc documentation for open source projects|
|     <img width=1000/>     |
