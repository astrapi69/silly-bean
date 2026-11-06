# How to contribute

Contributions are welcome, as GitHub issues and as pull requests.

- **Found a bug?** Open an [issue](https://github.com/astrapi69/silly-bean/issues) first, with what
  you called, what you expected and what you got, and the stack trace if there is one. The fix then
  starts with a test that shows today's behaviour, green on its own, and the fix turns it.
- **Pull requests** go against the `develop` branch, one concern per pull request, with tests for
  every change of behaviour. `./gradlew build` (`make build`) must be green: it runs the tests, the
  coverage floor, javadoc with `-Werror` and the Spotless format check. `make spotless` formats the
  code and writes the license header.
- **Tests**: the cases of one behaviour go into one `@ParameterizedTest` over a record, one case a
  row, each with a name that says what it shows ("another key: not equal"). The cases hold plain
  values; the test builds the objects it tests itself, not the `@MethodSource`, so that PIT gives
  each case the code it runs.
- **Mutation testing** runs on GitHub only, in the workflow "Mutation testing", on every pull
  request; `make pitest` starts it for a pushed branch and `make pitest-status` prints its summary.
  Please do not run PIT on your machine.
- **Commit messages** start with the kind of change, `feat:`, `fix:`, `docs:`, `test:`, `build:` or
  `ci:`, and name the issue, as in `test: the pair package, parameterized (#26)`.
- **Releases** leave only from CI, from a `RELEASE-*` tag (README, "Releasing").

The current coverage and mutation numbers are in the README's [Quality](../README.md#quality)
section.
