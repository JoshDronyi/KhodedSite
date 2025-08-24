#!/bin/bash

# Kobweb Project Diagnostic Script
# Usage: ./kobweb-diagnostic-script.sh > diagnostic-report.txt

echo "=== KOBWEB PROJECT DIAGNOSTIC REPORT ==="
echo "Generated on: $(date)"
echo "Project Directory: $(pwd)"
echo ""

echo "=== VERSION INFORMATION ==="
echo "Current Kobweb version:"
grep -A 1 "kobweb" gradle/libs.versions.toml || echo "❌ libs.versions.toml not found"

echo ""
echo "Current Kotlin version:"
grep -A 1 "kotlin" gradle/libs.versions.toml || echo "❌ Kotlin version not found"

echo ""
echo "Compose version:"
grep -A 1 "jetbrains-compose" gradle/libs.versions.toml || echo "❌ Compose version not found"

echo ""
echo "=== KOBWEB CLI STATUS ==="
if command -v kobweb &> /dev/null; then
    echo "✅ Kobweb CLI found"
    echo "Version: $(kobweb --version 2>/dev/null || echo 'Version not available')"
    echo ""
    echo "Running servers:"
    kobweb list 2>/dev/null || echo "❌ Cannot check running servers"
else
    echo "❌ Kobweb CLI not found in PATH"
fi

echo ""
echo "=== PROJECT STRUCTURE VALIDATION ==="
if [ -d "site" ]; then
    echo "✅ site/ directory exists"
else
    echo "❌ site/ directory missing"
fi

if [ -f "site/build.gradle.kts" ]; then
    echo "✅ site/build.gradle.kts exists"
else
    echo "❌ site/build.gradle.kts missing"
fi

if [ -d ".kobweb" ]; then
    echo "✅ .kobweb/ directory exists"
    if [ -f ".kobweb/conf.yaml" ]; then
        echo "✅ .kobweb/conf.yaml exists"
    else
        echo "⚠️  .kobweb/conf.yaml missing (will be created on first run)"
    fi
else
    echo "⚠️  .kobweb/ directory missing (will be created on first run)"
fi

echo ""
echo "=== SOURCE STRUCTURE CHECK ==="
if [ -d "site/src/jsMain/kotlin" ]; then
    echo "✅ JS main source directory exists"
    js_pages=$(find site/src/jsMain/kotlin -name "*.kt" -path "*/pages/*" | wc -l)
    echo "   📄 Found $js_pages page files"
    js_components=$(find site/src/jsMain/kotlin -name "*.kt" -path "*/components/*" | wc -l)
    echo "   🧩 Found $js_components component files"
else
    echo "❌ JS main source directory missing"
fi

if [ -d "site/src/jvmMain/kotlin" ]; then
    echo "✅ JVM main source directory exists"
    jvm_apis=$(find site/src/jvmMain/kotlin -name "*.kt" -path "*/api/*" | wc -l)
    echo "   🔌 Found $jvm_apis API files"
else
    echo "❌ JVM main source directory missing"
fi

if [ -d "site/src/commonMain/kotlin" ]; then
    echo "✅ Common main source directory exists"
    common_files=$(find site/src/commonMain/kotlin -name "*.kt" | wc -l)
    echo "   📋 Found $common_files shared files"
else
    echo "⚠️  Common main source directory missing"
fi

echo ""
echo "=== BUILD STATUS CHECK ==="
echo "Last Gradle build status:"
if ./gradlew build --dry-run &>/dev/null; then
    echo "✅ Gradle build configuration valid"
else
    echo "❌ Gradle build configuration has issues"
fi

echo ""
echo "Port availability check:"
if command -v netstat &> /dev/null; then
    if netstat -tuln 2>/dev/null | grep -q ":8080 "; then
        echo "⚠️  Port 8080 is occupied"
    else
        echo "✅ Port 8080 is available"
    fi
    if netstat -tuln 2>/dev/null | grep -q ":8081 "; then
        echo "⚠️  Port 8081 is occupied"
    else
        echo "✅ Port 8081 is available"
    fi
else
    echo "⚠️  Cannot check port availability (netstat not found)"
fi

echo ""
echo "=== DEPENDENCY ANALYSIS ==="
echo "Critical dependencies check:"
if grep -q "kobweb-core" site/build.gradle.kts 2>/dev/null; then
    echo "✅ kobweb-core dependency found"
else
    echo "❌ kobweb-core dependency missing"
fi

if grep -q "kobweb-silk" site/build.gradle.kts 2>/dev/null; then
    echo "✅ kobweb-silk dependency found"
else
    echo "⚠️  kobweb-silk dependency missing (optional but recommended)"
fi

if grep -q "compose-html-ext" site/build.gradle.kts 2>/dev/null; then
    echo "✅ compose-html-ext dependency found"
else
    echo "❌ compose-html-ext dependency missing"
fi

echo ""
echo "=== RECENT ERROR LOGS ==="
echo "Checking for recent error logs..."

if [ -f "current_errors.log" ] && [ -s "current_errors.log" ]; then
    echo "❌ Found errors in current_errors.log:"
    tail -5 current_errors.log
    echo ""
else
    echo "✅ No current errors found"
fi

if [ -f "kobweb_errors.log" ] && [ -s "kobweb_errors.log" ]; then
    echo "❌ Found errors in kobweb_errors.log:"
    tail -5 kobweb_errors.log
    echo ""
else
    echo "✅ No Kobweb-specific errors found"
fi

echo ""
echo "=== DEVELOPMENT ENVIRONMENT ==="
echo "Node.js version:"
if command -v node &> /dev/null; then
    echo "✅ $(node --version)"
else
    echo "❌ Node.js not found"
fi

echo ""
echo "Yarn version:"
if command -v yarn &> /dev/null; then
    echo "✅ $(yarn --version)"
else
    echo "⚠️  Yarn not found (npm can be used as alternative)"
fi

echo ""
echo "Java version:"
if command -v java &> /dev/null; then
    echo "✅ $(java -version 2>&1 | head -n 1)"
else
    echo "❌ Java not found"
fi

echo ""
echo "=== RECOMMENDATIONS ==="
echo "Based on the analysis above:"

# Check if Kobweb version is outdated
current_version=$(grep "kobweb.*=" gradle/libs.versions.toml 2>/dev/null | sed 's/.*"\(.*\)".*/\1/')
if [ "$current_version" != "0.23.1" ]; then
    echo "🔄 RECOMMENDED: Update Kobweb from $current_version to 0.23.1"
fi

# Check for common issues
if [ ! -f ".kobweb/conf.yaml" ]; then
    echo "🛠️  RECOMMENDED: Run 'kobweb run' once to initialize configuration"
fi

if ! command -v kobweb &> /dev/null; then
    echo "📦 REQUIRED: Install Kobweb CLI"
fi

echo ""
echo "=== END OF DIAGNOSTIC REPORT ==="
echo "For assistance, provide this report to the Kobweb specialist agent."