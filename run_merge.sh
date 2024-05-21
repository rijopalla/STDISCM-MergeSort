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
echo "Thread Count,Run Number,Elapsed Time (seconds)" >> $OUTPUT_FILE

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

        # Print the output and the elapsed time
        echo "$OUTPUT"
        echo "Elapsed time: $ELAPSED_TIME seconds"

        # Save the thread count, run number, and elapsed time to the output file
        echo "$THREAD_COUNT,$RUN,$ELAPSED_TIME" >> $OUTPUT_FILE

        echo "------------------------------------------"
    done
done

echo "Elapsed times saved to $OUTPUT_FILE"
