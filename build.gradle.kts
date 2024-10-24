plugins {
  java
  id("com.diffplug.spotless") version "6.25.0"
  id("org.sonarqube") version "5.1.0.4882"
}

group = "uk.gov.di"

version = "1.0-SNAPSHOT"

sonar {
  properties {
    property("sonar.projectKey", "govuk-one-login_authentication-acceptance-tests")
    property("sonar.organization", "govuk-one-login")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

spotless {
  if (project.hasProperty("ratchetFrom")) ratchetFrom(project.property("ratchetFrom") as String?)
  java {
    target("**/*.java")
    googleJavaFormat("1.11.0").aosp()
    importOrder("", "javax", "java", "\\#")
  }
  groovyGradle {
    target("**/*.gradle")
    greclipse().configFile("tools/spotless-gradle.properties")
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    ktfmt().googleStyle()
  }
}

repositories { mavenCentral() }

dependencies {
  implementation("org.apache.maven.plugins:maven-surefire-plugin:3.5.1")

  testImplementation(platform("org.junit:junit-bom:5.11.3"))
  testImplementation("org.junit.platform:junit-platform-suite")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-engine")

  testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
  testImplementation("io.cucumber:cucumber-java")
  testImplementation("io.cucumber:cucumber-junit")
  testImplementation("io.cucumber:cucumber-junit-platform-engine")
  testImplementation("io.cucumber:cucumber-picocontainer")

  testImplementation(platform("org.seleniumhq.selenium:selenium-dependencies-bom:4.25.0"))
  testImplementation("org.seleniumhq.selenium:selenium-java")
  testImplementation("com.deque:axe-selenium:3.0")

  testImplementation(platform("software.amazon.awssdk:bom:2.28.29"))
  testImplementation("software.amazon.awssdk:sso")
  testImplementation("software.amazon.awssdk:ssooidc")
  testImplementation("software.amazon.awssdk:dynamodb")
  testImplementation("software.amazon.awssdk:dynamodb-enhanced")
  testImplementation("software.amazon.awssdk:ssm")

  testImplementation("org.apache.commons:commons-text:1.12.0")

  testImplementation("commons-codec:commons-codec")

  testImplementation("com.google.guava:guava:33.3.1-jre")

  testImplementation("org.springframework.security:spring-security-crypto:6.3.4")
  testImplementation("org.bouncycastle:bcpkix-jdk18on")

  testImplementation("org.json:json:20240303")
  testImplementation("io.rest-assured:rest-assured:5.5.0")
}

version = "1.0.0"

// description = "acceptance-tests"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.AMAZON)
  }
  consistentResolution { useCompileClasspathVersions() }
}

dependencyLocking { lockAllConfigurations() }

tasks {
  test {
    useJUnitPlatform {
      excludeTags("disabled")
      if (project.hasProperty("excludeTags"))
        excludeTags(project.property("excludeTags") as String?)
      if (project.hasProperty("includeTags"))
        includeTags(project.property("includeTags") as String?)

      if (project.hasProperty("failFast")) failFast = true

      // OPTIONAL: Copy all system properties from the command line (-D...) to the test environment
      systemProperties(project.gradle.startParameter.systemPropertiesArgs)

      val parallelism = System.getenv("PARALLEL_BROWSERS").toIntOrNull() ?: 1

      if (parallelism > 1) {
        systemProperty("cucumber.execution.parallel.enabled", true)
        systemProperty("cucumber.execution.parallel.config.strategy", "fixed")
        systemProperty("cucumber.execution.parallel.config.fixed.parallelism", parallelism)
        systemProperty("cucumber.execution.parallel.config.fixed.max-pool-size", parallelism + 1)
      }

      // OPTIONAL: Enable Cucumber plugins, enable/disable as desired
      systemProperty(
        "cucumber.plugin",
        "summary, pretty, message:target/cucumber-report/cucumber.ndjson, timeline:target/cucumber-report/timeline, html:target/cucumber-report/index.html",
      )

      // OPTIONAL: Improve readability of test names in reports
      systemProperty("cucumber.junit-platform.naming-strategy", "long")
      // OPTIONAL: Don't show Cucumber ads
      systemProperty("cucumber.publish.quiet", "true")
      // OPTIONAL: Force test execution even if they are up-to-date according to Gradle or use
      // "gradle test --rerun"
      outputs.upToDateWhen { false }
      testLogging {
        events(
          "passed",
          "skipped",
          "failed",
          org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
        )
      }
    }
  }
}

tasks.named<Wrapper>("wrapper") { distributionType = Wrapper.DistributionType.ALL }
