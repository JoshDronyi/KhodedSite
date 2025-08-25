#!/bin/bash

echo "=== BETTER IMPORT TRACING ==="

# Initialize files
> traced_used.txt
> processing_queue.txt

# Add entry points to queue
echo "site/src/jsMain/kotlin/com/probro/khoded/MyApp.kt" >> processing_queue.txt
echo "site/src/jsMain/kotlin/com/probro/khoded/pages/Index.kt" >> processing_queue.txt
echo "site/src/jsMain/kotlin/com/probro/khoded/pages/About.kt" >> processing_queue.txt
echo "site/src/jsMain/kotlin/com/probro/khoded/pages/Contact.kt" >> processing_queue.txt
echo "site/src/jsMain/kotlin/com/probro/khoded/pages/Services.kt" >> processing_queue.txt

# Function to process a file and find its dependencies
process_file() {
    local current_file="$1"
    
    # Skip if already processed
    if grep -Fxq "$current_file" traced_used.txt; then
        return
    fi
    
    # Mark as processed
    echo "$current_file" >> traced_used.txt
    
    if [ ! -f "$current_file" ]; then
        return
    fi
    
    echo "Processing: $current_file"
    
    # Extract import statements
    grep "^import com\.probro\.khoded" "$current_file" | while read -r import_line; do
        # Extract the import path
        import_path=$(echo "$import_line" | sed 's/import com\.probro\.khoded\.//' | sed 's/\.[A-Z][A-Za-z0-9_]*$//')
        
        # Convert to file paths and check both jsMain and commonMain
        js_file="site/src/jsMain/kotlin/com/probro/khoded/${import_path//./\/}.kt"
        common_file="site/src/commonMain/kotlin/com/probro/khoded/${import_path//./\/}.kt"
        
        # Check if files exist and add to queue
        if [ -f "$js_file" ] && ! grep -Fxq "$js_file" traced_used.txt; then
            echo "$js_file" >> processing_queue.txt
        fi
        
        if [ -f "$common_file" ] && ! grep -Fxq "$common_file" traced_used.txt; then
            echo "$common_file" >> processing_queue.txt
        fi
    done
}

# Process queue until empty
iteration=0
while [ -s processing_queue.txt ] && [ $iteration -lt 50 ]; do
    iteration=$((iteration + 1))
    echo "=== Iteration $iteration ==="
    
    # Process current queue
    cp processing_queue.txt current_queue.txt
    > processing_queue.txt
    
    while read -r file; do
        process_file "$file"
    done < current_queue.txt
    
    # Remove duplicates from queue
    sort processing_queue.txt | uniq > temp_queue.txt
    mv temp_queue.txt processing_queue.txt
    
    echo "Files in queue: $(wc -l < processing_queue.txt)"
    echo "Files processed: $(wc -l < traced_used.txt)"
done

echo "=== FINAL RESULTS ==="
sort traced_used.txt | uniq > final_used_files.txt
echo "Total used files: $(wc -l < final_used_files.txt)"

# Show first 20 used files
echo "First 20 used files:"
head -20 final_used_files.txt

