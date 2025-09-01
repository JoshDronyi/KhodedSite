# Local Pipeline Testing

Test your changes locally before committing to avoid pipeline failures.

## Quick Tests (30 seconds)
```bash
# Fast compilation check
quick-pipeline-check.bat
```

## Full Local Pipeline Test (5-10 minutes)
```bash
# Complete pipeline simulation with isolated environment
local-pipeline-test.bat
```
- ✅ Replicates CI/CD environment setup
- ✅ Runs JS tests matching pipeline
- ✅ Executes full build process  
- ✅ Tests Docker image creation
- ✅ Basic security scanning
- ✅ Uses isolated test directory

## Docker Environment Test (10-15 minutes)
```bash
# Exact Ubuntu + Java 21 environment match
docker-pipeline-test.bat
```
- 🐳 Runs in identical Docker environment as GitHub Actions
- 🐳 Uses eclipse-temurin:21-jdk base image
- 🐳 Replicates exact pipeline steps and environment variables

## Pipeline Phases Tested

1. **Environment Setup** - Java 21, Gradle config, environment variables
2. **JS Tests** - `./gradlew jsTest --no-daemon --console=plain`
3. **Application Build** - `./gradlew build --no-daemon --console=plain`
4. **Docker Build** - Image creation and validation
5. **Security Checks** - Basic sensitive data scanning

## When to Use Each Test

- **quick-pipeline-check.bat** - Before every commit
- **local-pipeline-test.bat** - Before important commits or after significant changes
- **docker-pipeline-test.bat** - Before releasing or when environment compatibility is critical

## Files Created

- `local-pipeline-test.bat` - Full pipeline simulation
- `docker-pipeline-test.bat` - Docker environment testing
- `quick-pipeline-check.bat` - Fast pre-commit checks
- `Dockerfile.local-test` - Docker environment matching CI/CD