# Project structure

- Use a domain-driven design approach
- Keep track of the features in the AGENTS.md file (package, base URL, entities only)
- Use a packagE by feature approach bundling rest controllers, services, repositories, models, configurations
  - **Each feature MUST have its own dedicated package** under `com.deviceinsight.template.<feature-name>`
  - **NEVER mix features in the same package** - each feature is completely self-contained
  - Package naming: use lowercase, plural or singular based on domain concept (e.g., `devices`, `devicegroups`, `users`, `orders`)
  - All feature code (controllers, services, repositories, DTOs, exceptions, configurations) goes in the feature package
  - Example: `devices/` and `devicegroups/` are separate packages, not mixed together
  - **When creating a new feature, ALWAYS create a new package** - do not add to existing feature packages
  - Exception: User may explicitly instruct to add functionality to an existing package if it extends that feature

## Features

- **Device Management**
  - Package: `com.deviceinsight.template.devices`
  - Base URL: `/api/devices`
  - Entities: Device (id, name, type, capabilities)

# Pattern Documentation

Detailed patterns and guidelines are documented in `./doc`:

- **data_model.md** - Data model

# General

- When you've adapted a single class or implemented a new test, run the test with `gradlew test -Dtest=YourTestClassName`
- When there is a corresponding integration test, run it with `gradlew verify -Dit.test=YourIntegrationTestClassName`
- Run all tests with `gradlew verify` when you've changed large parts of the code
- When you should reproduce a bug, write a test that fails and then fix the bug
- If `gradlew verify` fails, check the container logs in `target/application-container.log` and `target/postgres-container.log` for debugging

# Code Style and Structure

- Write code like a book with clear narrative flow from high-level intent to low-level details:
  - **Short, focused functions**: Each function does one thing well
  - **Decompose long functions**: Break into smaller, well-named helper functions
  - **Orchestrating functions**: Main function reads like a table of contents, delegating to helpers
  - **Extract complex conditions**: Long boolean expressions become well-named functions
  - **Descriptive names**: Function names clearly express intent
- Use comments and KDoc sparsely - prefer self-documenting code with clear naming
- Comments should explain "why" not "what"
- Document class purpose when helpful, but well-named functions/parameters should be self-explanatory
- Only create interface when there will be multiple implementations or requirements from a framework
- Do not add a `Impl` suffix to interface implementation. Use a name based on the technology used to implement the interface, e.g. `JpaUserRepository` or `InMemoryUserRepository`
- Clean up unused imports after code changes and organize: standard library, third-party, project imports

# Layered Architecture

- Follow a strict layered architecture: **Interfaces Layer** → calls → **Services Layer** → uses → **Infrastructure Layer**
- Services should not directly access repositories from other service packages
- Interfaces should not directly access repositories; they must use services
- **Repositories must use `internal` modifier** to enforce they are only accessed within their feature package
  - Mark repository interfaces as `internal`
  - Mark service constructors as `internal constructor` when they take internal repositories
  - Mark configuration classes as `internal` when they wire internal components
  - This enforces module-level encapsulation and prevents cross-package repository access
- DTOs should only be used inside the Rest Controller layer
- Services should work with the entity model

# Kotlin

- Use Kotlin 2.2 or later features when applicable
- Write **idiomatic code** that leverages Kotlin's unique capabilities and built-in features
- Reference: [Kotlin idioms and conventions](https://kotlinlang.org/docs/basic-syntax.html)

## Prefer Kotlin Built-in Features Over Third-Party Libraries

- Leverage Kotlin's Standard Library before reaching for third-party libraries (collections, ranges, sequences, scope functions)
- **Reduce dependencies**: Minimize external libraries when Kotlin's built-in features provide the required functionality
- Use built-in methods like `isEmpty()`, `isNotEmpty()`, `toList()` instead of manual implementations

## Scope Functions

- Use scope functions (`let`, `apply`, `run`, `with`, `also`) for cleaner, more concise code
- `let`: Chaining and handling nullable types
- `apply`: Configure objects after initialization
- `run`: Execute calculations and return a value
- `also`: Perform side-effects without altering the object

## Type Safety and Null Safety

- Prefer `data classes` for DTOs and immutable data carriers
- Use `sealed classes` for controlled type hierarchies
- Leverage Kotlin's null safety features - avoid using `!!` operator
- Use nullable types (e.g., `String?`) only when necessary and handle `null` values explicitly
- Use `val` for immutable properties, `var` only when mutability is required
- **Prefer typed parameters over strings** in service methods
- **Parse strings at the boundary** (controllers), not in services
- **Examples of preferred types**: `LocalTime`, `LocalDate`, `ZoneId`, `Instant`, `Duration`
- Use value classes (`@JvmInline`) to represent specific types and avoid parameter mix-ups
- **Example**:

  ```kotlin
  @JvmInline
  value class UserId(val id: String)

  @JvmInline
  value class Email(val value: String)

  fun sendEmail(userId: UserId, email: Email) // Type-safe, prevents mixing up parameters
  ```

### Unused Variables

- **ALWAYS use `_` for unused caught exceptions**: `catch (_: IllegalArgumentException)`
  - ❌ Bad: `catch (e: IllegalArgumentException)` when `e` is not used
  - ✅ Good: `catch (_: IllegalArgumentException)`
  - Only use a named variable if you log or use the exception: `catch (e: Exception) { logger.error(e) { ... } }`
- Use `_` for unused lambda parameters: `map.forEach { _, value -> ... }`

## Package-Level Functions Over Companion Objects

- **Prefer package-level functions** for factory methods and utilities instead of companion object methods
- More idiomatic Kotlin style following stdlib conventions (like `listOf()`, `mapOf()`)
- Better discoverability with IDE autocomplete
- Cleaner, less verbose API
- **Bad** (companion object):
  ```kotlin
  value class UserId private constructor(val value: String) {
      companion object {
          fun create(value: String) = UserId(value)
          fun fromString(value: String) = UserId(value)
      }
  }
  // Usage: UserId.create("123")
  ```
- **Good** (package-level functions):

  ```kotlin
  fun createUserId(value: String) = UserId(value)
  fun userIdFromString(value: String) = UserId(value)

  @JvmInline
  value class UserId internal constructor(val value: String)

  // Usage: createUserId("123")
  ```

- Use `internal` constructor to force usage of package-level factory functions
- Companion objects are still appropriate for constants and type-specific operations

## Readability Patterns

- **Use expression body syntax for single-line methods** - omit braces and `return` keyword
- **Omit return type when it's clear from context** - let type inference work for you
- Use named arguments for better readability when calling functions with multiple parameters
- Use `when` expressions instead of complex if-else chains
- Prefer Kotlin's collection operations (map, filter, etc.) over imperative loops
- **Return early pattern**: Check exception cases at the start of methods and return early to separate error handling from main logic
- **Break logic across multiple lines**: Use intermediate variables with descriptive names instead of cramming logic into a single line

## Extension Functions

- Leverage Kotlin's extension functions to add functionality to existing classes
- Write extension functions for clean code without modifying third-party or system classes
- **Avoid "Utils" classes** - use extension functions instead for better organization and discoverability

# Spring Framework Integration

- Leverage Spring Boot 3.x features and best practices
- Use Spring Boot starters for quick project setup and dependency management
- Use `@ConfigurationProperties` with constructor binding for type-safe configuration

## Bean Configuration

- Use constructor injection over field injection for better testability
- **Never use `@Component`, `@Service`, or `@Repository` annotations** on classes
- **Always use `@Configuration` classes** to explicitly define beans and manage their lifecycles
- This provides explicit control over bean creation, dependencies, and conditional logic
- Reuse configuration classes in tests if possible
- **Good**:

  ```kotlin
  // In MyFeatureConfiguration.kt
  @Configuration
  class MyFeatureConfiguration {
      @Bean
      fun myService(repository: MyRepository) = MyService(repository)
  }

  // Service class - no annotations
  class MyService(private val repository: MyRepository) { ... }
  ```

- **Bad**:
  ```kotlin
  @Service  // Don't use stereotype annotations
  class MyService(private val repository: MyRepository) { ... }
  ```

## Open Modifier for Spring

- Kotlin classes and methods are `final` by default, but Spring requires them to be `open` for CGLIB proxying
- **Do not use** `kotlin-maven-allopen` plugin - explicitly mark classes/methods as `open` instead
- Mark as `open`: `@Configuration` classes, `@Bean` methods, `@Transactional` methods, and service classes with `@Transactional` methods
- Private methods do not need to be `open` - Spring only proxies public methods

## Transaction Management

- Transactions should be started on service layer
- Multiple service calls can participate in the same transaction

# REST Controllers

- Use DTOs for request and response
- Name DTOs with `Dto` suffix (e.g., `UserDto`, `OrderDto`)
- Use Kotlin `data classes` for DTOs
- Implement input validation using Bean Validation (e.g., @Valid, custom validators)
- Implement proper exception handling using @ControllerAdvice and @ExceptionHandler
- Apply a RESTful API design (proper use of HTTP methods, status codes, etc.)
- Return a http status 409 when a resource already exists
- Return a http status 404 when a resource does not exist
- Return a http status 400 when a request is invalid
- Return a http status 500 when an internal server error occurs
- Use Springdoc OpenAPI (formerly Swagger) for API documentation
- Implement a toEntity function in the DTOs to convert the DTO to an entity if needed

# JSON Model Classes

- Use Kotlin `data classes` for JSON models (DTOs, API request/response objects)
- **Always use `@param:JsonProperty` instead of `@JsonProperty`** for constructor parameters in data classes (ensures proper deserialization with Jackson)
- Use `@JsonIgnoreProperties(ignoreUnknown = true)` to handle additional fields gracefully
- Map snake_case JSON field names to camelCase Kotlin properties

# Configuration and Properties

- Use application.yaml for configuration.
- Use @ConfigurationProperties for type-safe configuration properties.

# Pagination

- Prefer **keyset pagination** (seek method) over offset-based pagination for better performance
- See `PAGINATION.md` for detailed implementation patterns and examples

# HTTP Client Usage

- Use Spring's `RestClient` (Spring Boot 3.2+) or `WebClient` for calling external HTTP services
- Create HTTP client beans in a `@Configuration` class within the feature package
- **Always set timeouts** to prevent indefinite blocking (connection, read, and response timeouts)
- Make timeout values configurable via `@ConfigurationProperties`
- Create a dedicated client class per external service in the infrastructure layer
- Test HTTP clients using WireMock or MockWebServer in integration tests
- Document external service dependencies in feature package documentation

## Error Handling

- **Ask the user** how to handle errors for each external service integration:
  - **Option 1: Fallback strategy** - Use cached data, default values, or alternative service
  - **Option 2: Propagate error** - Throw custom exception and let caller handle it
  - **Option 3: Circuit breaker** - Use Resilience4j to prevent cascading failures
  - **Option 4: Retry** - Use Resilience4j to retry failed requests
- Log all external service errors at ERROR level with context (URL, status code, error message)
- Wrap external service exceptions in custom domain exceptions (e.g., `ExternalServiceException`)
- Never expose raw HTTP client exceptions to the REST API layer
- Return appropriate HTTP status codes:
  - 503 Service Unavailable when external service is down
  - 504 Gateway Timeout when external service times out
  - 500 Internal Server Error for unexpected errors

# Database Schema and Migrations

- **Follow database schema design guidelines** - see `DATABASE_SCHEMA.md` for comprehensive rules on naming conventions, keys, indexing, data types, and JPA entity mapping
- Use Flyway for database schema versioning and migrations
- Name migration files: `V<timestamp with format VYYYYMMDDHHmm>_description.sql` (e.g., `V202511140900_add_asset_table.sql`)
- Place migration files in `src/main/resources/db/migration/`
- Keep migrations idempotent when possible
- Never modify existing migration files after they've been applied

# Testing

- Write unit tests using JUnit 5 and Spring Boot Test
- Use MockMvc for testing web layers
- **Always create tests when implementing new features**
- **Always update tests when modifying existing code**

## AssertJ Assertions

- **Use AssertJ for all assertions** - NOT JUnit assertions (`assertEquals`, `assertTrue`, etc.)
- Import: `import org.assertj.core.api.Assertions.assertThat`
- AssertJ provides fluent, readable assertions with better error messages
- **Common patterns**:
  - Equality: `assertThat(actual).isEqualTo(expected)`
  - Null checks: `assertThat(value).isNull()` or `assertThat(value).isNotNull()`
  - Boolean: `assertThat(condition).isTrue()` or `assertThat(condition).isFalse()`
  - Collections size: `assertThat(list).hasSize(3)` or `assertThat(list).isEmpty()`
  - Collections content: `assertThat(list).contains(item)` or `assertThat(list).containsExactly(item1, item2)`
  - String checks: `assertThat(text).startsWith("prefix")` or `assertThat(text).contains("substring")`
  - Collection predicates: `assertThat(list).allMatch { condition }` or `assertThat(list).anyMatch { condition }`
  - Negation: `assertThat(list).noneMatch { condition }`
- **Example**:

  ```kotlin
  // Bad - JUnit assertions
  assertEquals(3, list.size)
  assertTrue(list.contains(item))
  assertNotNull(result)

  // Good - AssertJ assertions
  assertThat(list).hasSize(3)
  assertThat(list).contains(item)
  assertThat(result).isNotNull
  ```

## Entity Test Data Creation

- **When you need to create entity instances with specific field values in tests** (e.g., entities with private setters, immutable fields, or generated IDs)
- **Never use reflection** to set entity fields in tests
- Use helper functions (e.g., `createDevice(id = 1, name = "Test")`) or the test subclass pattern
- See `ENTITY_TEST_DATA.md` for the recommended test subclass pattern and detailed examples

## Controller Tests

- **Every controller must have a controller test** - test class name: `<ControllerName>Test`
- See `CONTROLLER_TESTING.md` for detailed patterns, MockMvc usage, validation testing, and error handling examples

## JSON Model Classes

- **Every JSON model class** must have marshalling and unmarshalling tests
- See `JSON-MODEL-TESTING.md` for detailed guidance, templates, and examples

## Repository Integration Tests

- **Every repository must have an integration test** - test class name: `<RepositoryName>IT`
- All repository integration tests must extend the `RepositoryIT` abstract base class
- See `REPOSITORY_TESTING.md` for details

## Service Unit Tests

- **Every service class MUST have a corresponding unit test** (`*ServiceTest.kt` or `*Test.kt`)
- See `SERVICE_TESTING.md` for detailed patterns, examples, and test coverage guidelines

# Logging and Monitoring

- Use **KotlinLogging** (kotlin-logging-jvm) for logging: `private val logger = KotlinLogging.logger {}`
- Initialize logger using: `private val logger = KotlinLogging.logger {}`
- Keep logging in the service layer
- Write INFO level logs when entities are modified
- Write DEBUG level logs when entities are retrieved
- Write WARN level logs when recoverable errors occur
- Write ERROR level logs when unrecoverable errors occur
- **Control logging via log level configuration** - do not use property-based debug flags
- Use Spring Boot Actuator for application monitoring and metrics
- **Use trace IDs to follow requests** - leverage Spring Boot's built-in tracing (Micrometer Tracing) with MDC

## Logging Patterns

- **Use post-action logging with `.also {}`** for read operations - log after the action with result info (found/not found, counts)
- **Include result counts**: `"Found ${teams.size} teams with status channel configured"`
- **Explain impact in log messages**: `"No teams configured - skipping scheduled updates"` (not just `"No teams configured"`)
- **Good**: `findAll().filter { ... }.also { logger.debug { "Found ${it.size} items" } }`
- **Bad**: `logger.debug { "Fetching items" }; return findAll()` (no result info)

## GDPR Compliance

- **Never log personal data or user information** to ensure GDPR compliance
- **Never log user IDs** - this includes primary keys, usernames, email addresses, or any other user identifiers
- Log only technical information: entity types, operation types, counts, status codes, error types
- Example compliant log: `"Device created successfully"` or `"Failed to update device: validation error"`
- Example non-compliant log: `"Device created for user 12345"` or `"User john.doe@example.com logged in"`
- When debugging is necessary, use anonymized identifiers or aggregate metrics instead of real user data
