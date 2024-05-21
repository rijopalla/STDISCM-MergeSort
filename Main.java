import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // TODO: Seed your randomizer
        final int RAND_SEED = 12345;
        Random random = new Random(RAND_SEED);

        // TODO: Get array size and thread count from user
        Scanner arrayScanner = new Scanner(System.in);
        Scanner threadScanner =new Scanner(System.in);

        System.out.println("Enter array size: ");
        int arraySize = arrayScanner.nextInt();
        arrayScanner.nextLine();


        System.out.println("Enter thread count: ");
        int threadLimit = threadScanner.nextInt();
        threadScanner.nextLine();


        // TODO: Generate a random array of given size
        int[] randArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            randArray[i] = random.nextInt(arraySize);
            System.out.print(randArray[i] + " ");
        }

        //Shuffle the array
        shuffleArray(randArray, random);
        System.out.println("After shuffle: \n");
        for (int i = 0; i < arraySize; i++) {
            System.out.print(randArray[i] + " ");
        }

        // TODO: Call the generate_intervals method to generate the merge 
        // sequence
        List<Interval> intList = generate_intervals(0, arraySize - 1);

        // TODO: Call merge on each interval in sequence
        long startTime = System.nanoTime();
        for (Interval interval:intList) {
            merge(randArray, interval.getStart(), interval.getEnd());
        }
        long endTime = System.nanoTime();
        double elapsedTime = (endTime-startTime)/1_000_000_000.0;

        //Print array (for checking)
        System.out.println("\nSorted: ");
        for (int num: randArray){
            System.out.print(num+" ");
        }

        System.out.printf("\n Execution time: %.6f seconds%n", elapsedTime);
        
        arrayScanner.close();
        threadScanner.close();

        // Once you get the single-threaded version to work, it's time to 
        // implement the concurrent version. Good luck :)

    }

    /*
     * Helper function to shuffle a given array
     */
    public static void shuffleArray(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            
            //Swap
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;

        }
    }

    /*
    This function generates all the intervals for merge sort iteratively, given 
    the range of indices to sort. Algorithm runs in O(n).

    Parameters:
    start : int - start of range
    end : int - end of range (inclusive)

    Returns a list of Interval objects indicating the ranges for merge sort.
    */
    public static List<Interval> generate_intervals(int start, int end) {
        List<Interval> frontier = new ArrayList<>();
        frontier.add(new Interval(start,end));

        int i = 0;
        while(i < frontier.size()){
            int s = frontier.get(i).getStart();
            int e = frontier.get(i).getEnd();

            i++;

            // if base case
            if(s == e){
                continue;
            }

            // compute midpoint
            int m = s + (e - s) / 2;

            // add prerequisite intervals
            frontier.add(new Interval(m + 1,e));
            frontier.add(new Interval(s,m));
        }

        List<Interval> retval = new ArrayList<>();
        for(i = frontier.size() - 1; i >= 0; i--) {
            retval.add(frontier.get(i));
        }

        return retval;
    }

    /*
    This function performs the merge operation of merge sort.

    Parameters:
    array : vector<int> - array to sort
    s     : int         - start index of merge
    e     : int         - end index (inclusive) of merge
    */
    public static void merge(int[] array, int s, int e) {
        int m = s + (e - s) / 2;
        int[] left = new int[m - s + 1];
        int[] right = new int[e - m];
        int l_ptr = 0, r_ptr = 0;
        for(int i = s; i <= e; i++) {
            if(i <= m) {
                left[l_ptr++] = array[i];
            } else {
                right[r_ptr++] = array[i];
            }
        }
        l_ptr = r_ptr = 0;

        for(int i = s; i <= e; i++) {
            // no more elements on left half
            if(l_ptr == m - s + 1) {
                array[i] = right[r_ptr];
                r_ptr++;

            // no more elements on right half or left element comes first
            } else if(r_ptr == e - m || left[l_ptr] <= right[r_ptr]) {
                array[i] = left[l_ptr];
                l_ptr++;
            } else {
                array[i] = right[r_ptr];
                r_ptr++;
            }
        }
    }
}

class Interval {
    private int start;
    private int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}