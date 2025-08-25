#!/bin/bash

echo "=== TRACING ALL IMPORT CHAINS ==="

# Create temp files for tracking
> used_files.txt
> direct_imports.txt

# Function to extract imports from a file
extract_imports() {
    local file="$1"
    if [ -f "$file" ]; then
        grep "^import com\.probro\.khoded" "$file" | sed 's/import com\.probro\.khoded\.//g' | sed 's/\.[^.]*$//' | while read import_path; do
            # Convert import path to file path
            file_path="site/src/jsMain/kotlin/com/probro/khoded/${import_path//./\/}.kt"
            if [ -f "$file_path" ]; then
                echo "$file_path" >> direct_imports.txt
                echo "$file_path" >> used_files.txt
            fi
            
            # Also check commonMain
            common_path="site/src/commonMain/kotlin/com/probro/khoded/${import_path//./\/}.kt"
            if [ -f "$common_path" ]; then
                echo "$common_path" >> direct_imports.txt
                echo "$common_path" >> used_files.txt
            fi
        done
    fi
}

echo "Tracing imports from entry points..."

# Trace from all entry points
extract_imports "site/src/jsMain/kotlin/com/probro/khoded/MyApp.kt"
extract_imports "site/src/jsMain/kotlin/com/probro/khoded/pages/Index.kt" 
extract_imports "site/src/jsMain/kotlin/com/probro/khoded/pages/About.kt"
extract_imports "site/src/jsMain/kotlin/com/probro/khoded/pages/Contact.kt"
extract_imports "site/src/jsMain/kotlin/com/probro/khoded/pages/Services.kt"

echo "First level imports traced. Now tracing second level..."

# Read each file found so far and trace its imports
while read file; do
    if [ -f "$file" ]; then
        extract_imports "$file"
    fi
done < direct_imports.txt

# Remove duplicates and sort
sort used_files.txt | uniq > used_files_unique.txt

echo "Direct imports found: $(wc -l < direct_imports.txt)"
echo "Total used files (including chains): $(wc -l < used_files_unique.txt)"

# Find unused files
echo "=== FINDING UNUSED FILES ==="
find site/src/jsMain -name "*.kt" > all_js_files.txt
find site/src/commonMain -name "*.kt" > all_common_files.txt

# Combine all main source files (excluding tests)
cat all_js_files.txt all_common_files.txt > all_main_source.txt

# Find files not in used list
comm -23 <(sort all_main_source.txt) <(sort used_files_unique.txt) > potentially_unused.txt

echo "Potentially unused files: $(wc -l < potentially_unused.txt)"

