#!/bin/bash

echo "=== COMPREHENSIVE FILE USAGE ANALYSIS ==="

# Get all Kotlin files
find site/src -name "*.kt" > all_files.txt

echo "Total Kotlin files found: $(wc -l < all_files.txt)"

# Get test files
grep -E "Test\.kt$|test/" all_files.txt > test_files.txt || true
echo "Test files: $(wc -l < test_files.txt)"

# Get main source files (excluding tests)
grep -v -E "Test\.kt$|test/" all_files.txt > main_files.txt
echo "Main source files: $(wc -l < main_files.txt)"

# Entry points
echo "
=== ENTRY POINTS ==="
echo "MyApp.kt"
echo "Index.kt" 
echo "About.kt"
echo "Contact.kt"
echo "Services.kt"

# Get all import statements from entry points
echo "
=== IMPORTS FROM ENTRY POINTS ==="
echo "--- MyApp.kt imports ---"
grep "^import com\.probro\.khoded" site/src/jsMain/kotlin/com/probro/khoded/MyApp.kt || echo "No internal imports"

echo "--- Index.kt imports ---"
grep "^import com\.probro\.khoded" site/src/jsMain/kotlin/com/probro/khoded/pages/Index.kt || echo "No internal imports"

echo "--- About.kt imports ---"
grep "^import com\.probro\.khoded" site/src/jsMain/kotlin/com/probro/khoded/pages/About.kt || echo "No internal imports"

echo "--- Contact.kt imports ---"
grep "^import com\.probro\.khoded" site/src/jsMain/kotlin/com/probro/khoded/pages/Contact.kt || echo "No internal imports"

echo "--- Services.kt imports ---"
grep "^import com\.probro\.khoded" site/src/jsMain/kotlin/com/probro/khoded/pages/Services.kt || echo "No internal imports"

