# Kobweb Specialist Agent Implementation Guide

## Agent Integration Instructions

### 1. Agent Activation Protocol

When encountering Kobweb-related tasks, activate the specialist agent using this prompt:

```
Activate KobwebDev Agent:
- Current Kobweb version: [detected_version]
- Target version: 0.23.1
- Project type: [multiplatform/spa/static]
- Issue category: [compilation/server/build/migration]
- Specific error: [error_description]

Request: [specific_task_or_problem]
```

### 2. Pre-Diagnostic Checklist

Before engaging the agent, gather this information:

#### Version Information
```bash
# Check current Kobweb version
grep "kobweb" gradle/libs.versions.toml

# Check Kotlin version
grep "kotlin" gradle/libs.versions.toml

# Check development server status
kobweb list
```

#### Error Context
```bash
# Capture build errors
./gradlew build --stacktrace > build_errors.log 2>&1

# Capture Kobweb server errors
kobweb run --debug > server_errors.log 2>&1

# Check configuration
cat .kobweb/conf.yaml
```

### 3. Agent Task Categories

#### Category A: Version Updates & Migrations
**Trigger Conditions:**
- Kobweb version < 0.23.1
- Compatibility issues between dependencies
- Breaking changes in framework APIs

**Agent Response Pattern:**
1. Analyze current version configuration
2. Provide step-by-step migration guide
3. Identify breaking changes
4. Verify post-migration functionality

#### Category B: Compilation Issues
**Trigger Conditions:**
- Frontend JS compilation failures
- Backend JVM compilation works but JS fails
- KSP processing errors
- Import resolution problems

**Agent Response Pattern:**
1. Diagnose compilation logs
2. Identify specific error patterns
3. Provide targeted fixes
4. Verify build success

#### Category C: Development Server Problems
**Trigger Conditions:**
- `kobweb run` command failures
- Hot reloading not working
- Port conflicts
- Asset loading issues

**Agent Response Pattern:**
1. Analyze server configuration
2. Check port availability and conflicts
3. Verify development environment setup
4. Restore hot reloading functionality

#### Category D: Performance Optimization
**Trigger Conditions:**
- Slow build times
- Large bundle sizes
- Runtime performance issues
- Resource loading problems

**Agent Response Pattern:**
1. Analyze build output and bundle composition
2. Identify optimization opportunities
3. Implement code splitting strategies
4. Optimize asset loading

### 4. Current Project Priority Actions

Based on the detected project state, these actions should be prioritized:

#### Immediate (Priority 1)
```bash
# 1. Kobweb Version Update
# Update libs.versions.toml
kobweb = "0.23.1"

# Clean and rebuild
./gradlew clean
./gradlew build
```

#### Short-term (Priority 2)
```bash
# 2. Fix JS Compilation Issues
# Analyze and fix frontend compilation errors
# Update import statements for 0.23.1 compatibility
# Resolve any breaking API changes
```

#### Medium-term (Priority 3)
```bash
# 3. Development Server Restoration
# Clear .kobweb directory
# Reconfigure development environment
# Test hot reloading functionality
```

### 5. Agent Communication Templates

#### For Version Updates
```
KobwebDev Agent Request:
- Task: Migrate from Kobweb 0.22.0 to 0.23.1
- Current state: [compilation_status]
- Blockers: [specific_errors]
- Requirements: Maintain current functionality

Please provide:
1. Step-by-step migration procedure
2. Breaking changes checklist
3. Verification tests
4. Rollback plan if needed
```

#### For Compilation Fixes
```
KobwebDev Agent Request:
- Task: Fix frontend JS compilation errors
- Current state: Backend JVM compiles successfully, frontend fails
- Error patterns: [attach_error_logs]
- Dependencies: [current_dependency_list]

Please provide:
1. Error analysis and root cause
2. Specific code fixes required
3. Dependency updates if needed
4. Testing strategy
```

#### For Development Server
```
KobwebDev Agent Request:
- Task: Restore development server functionality
- Current state: `kobweb run` fails to start
- Environment: [development_machine_specs]
- Port status: [port_availability_check]

Please provide:
1. Server configuration diagnosis
2. Environment requirements verification
3. Step-by-step repair procedure
4. Alternative development setup if needed
```

### 6. Quality Assurance Integration

#### Pre-Implementation Checklist
- [ ] Agent has access to current project structure
- [ ] Version compatibility matrix verified
- [ ] Official documentation referenced
- [ ] Breaking changes identified
- [ ] Rollback plan prepared

#### Post-Implementation Verification
```bash
# Verify successful implementation
./gradlew clean build                    # Full build test
kobweb run --port 8080                  # Dev server test  
kobweb build --env prod                 # Production build test
kobweb export --layout static           # Export test
```

#### Success Criteria
- [ ] All builds complete successfully
- [ ] Development server starts and hot reloads
- [ ] Production builds generate correctly
- [ ] No regressions in existing functionality
- [ ] Performance maintained or improved

### 7. Collaboration Protocols

#### With Other Development Agents
- **Backend Agent**: Coordinate JVM/JS shared model changes
- **Frontend Agent**: Ensure UI/UX consistency during migrations
- **Testing Agent**: Validate all changes with comprehensive tests
- **Deployment Agent**: Update CI/CD for Kobweb version changes

#### Information Handoff Requirements
```json
{
  "project_state": {
    "kobweb_version": "current_version",
    "kotlin_version": "current_version",
    "compilation_status": "pass/fail",
    "server_status": "working/broken",
    "last_working_commit": "commit_hash"
  },
  "changes_made": [
    "version_updates",
    "configuration_changes", 
    "code_modifications"
  ],
  "verification_results": {
    "build_status": "pass/fail",
    "server_status": "working/broken",
    "tests_passing": true/false
  }
}
```

### 8. Emergency Procedures

#### If Agent Recommendations Fail
1. **Immediate Rollback**: Git reset to last working state
2. **Alternative Approach**: Consult Kobweb community/GitHub issues
3. **Incremental Updates**: Smaller version jumps if full update fails
4. **Expert Escalation**: Contact Varabyte team for complex issues

#### Compatibility Crisis Management
```bash
# Create safe working branch
git checkout -b kobweb-emergency-fix

# Isolate problematic changes
git cherry-pick [working_commits_only]

# Test minimal viable configuration
# Document all changes for future reference
```

This implementation guide ensures the Kobweb specialist agent can be effectively integrated into your development workflow while maintaining system stability and providing clear success criteria.