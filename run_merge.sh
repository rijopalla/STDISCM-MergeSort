#!/bin/bash

# Compile the Java program
javac Main.java

# Define the array size and thread counts
ARRAY_SIZE=$((2**23))
THREAD_COUNTS=(1 2 4 8 16 32 64 128 256 512 1024)

# File to save elapsed times
OUTPUT_FILE="elapsed_times.txt"

# Clear the output file and write the header
echo "Array Size: $ARRAY_SIZE" > $OUTPUT_FILE
echo "Thread Count,Run Number,Elapsed Time (seconds),Is Sorted" >> $OUTPUT_FILE

# Run the Java program with different thread counts
for THREAD_COUNT in "${THREAD_COUNTS[@]}"
do
    for RUN in {1..5}
    do
        echo "Running with array size: $ARRAY_SIZE, thread count: $THREAD_COUNT, run number: $RUN"

        # Run the Java program and capture the output
        OUTPUT=$(java Main <<< "$ARRAY_SIZE
$THREAD_COUNT")

        # Extract the elapsed time from the output
        ELAPSED_TIME=$(echo "$OUTPUT" | grep 'Execution time' | awk '{print $3}')

        # Extract the sanity check result from the output
        IS_SORTED=$(echo "$OUTPUT" | grep 'The array is')

        # Print the output, the elapsed time, and the sanity check result
        echo "$OUTPUT"
        echo "Elapsed time: $ELAPSED_TIME seconds"
        echo "Sanity check: $IS_SORTED"

        # Save the thread count, run number, elapsed time, and sanity check result to the output file
        echo "$THREAD_COUNT,$RUN,$ELAPSED_TIME,$IS_SORTED" >> $OUTPUT_FILE

        echo "------------------------------------------"
    done
done

echo "Elapsed times and sanity check results saved to $OUTPUT_FILE"
