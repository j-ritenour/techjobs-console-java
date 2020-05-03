package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    // create a public static method called findByColumnAndValue that takes string column and string value and returns
    // and ArrayList of hashmaps --> going to be the parameter that gets passed thru printJobs
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();
// created new empty arrayList of hashmaps called jobs
// where we will put our jobs that match the search criteria
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        // looping thru every row of an arraylist called allJobs
// each row is a single job, data type is hashmap
        for (HashMap<String, String> row : allJobs) {
// creating a variable 'aValue
// stores the value of the cell in the column you're searching for in findByColumnAndValue
            String aValue = row.get(column);
            aValue = aValue.toUpperCase();


// If the value of a cell matches the value we entered as a parameter for findByColumnAndValue
 //  add it to our arrayList 'jobs'

            if (aValue.contains(value.toUpperCase())) {
                jobs.add(row);
            }
        }
// returns the arrayList of matching jobs
// going to be the input parameter for printJobs()
        return jobs;
    }

    //****************************************************************************************************


    // findByValue is a function that takes whatever we want to search for
    // returns a list of jobs that meet that criteria
    public static ArrayList<HashMap<String, String>> findByValue(String searchTerm) {

        // loads the dataset of all possible jobs
        loadData();
// creates new ArrayList called matchingJobs where we put all the jobs that match what we're searching for
        ArrayList<HashMap<String, String>> matchingJobs = new ArrayList<>();

        // for loop that loops thru every job in the data set that contains all of the possible jobs
        for (HashMap<String, String> job : allJobs) {
// creates an ArrayList 'cellContents'
// where we store all the contents of each job so we can search just this arrayList
            ArrayList<String>  cellContents = new ArrayList<>();
// Loops thru every cell of each job (key/value pairs; key = column value = contents of cell)
            for (Map.Entry<String, String> cell : job.entrySet()) {
// adds values of every cell to the arraylist 'cellContents' we created on line 114
                cellContents.add(cell.getValue());
            }

// loops thru all of the values of cellContents, if it matches our search term we add the job to our matchingJobs array
            for (String values: cellContents) {
                values = values.toUpperCase();
                if (values.contains(searchTerm.toUpperCase())) {
                    matchingJobs.add(job);
                    break;
            }
            }
        }
// returns the arrayList of matching jobs
// going to be the input parameter for printJobs()
        return matchingJobs;
    }

// TODO: a paragraph about how the function works
    //case insensitive
    // findByValue is a function that takes a search term (searchTerm) as its parameter and then searches a data set
    // for matching criteria. It creates a new ArrayList called matchingJobs that takes HashMaps of all the job
    // listings that fit the criteria. Every HashMap has a Name, Employer, Location, Position Type, and Core
    // Competency. It loops thru every entry in the data set that contains all possible jobs and then creates an Array
    //List called 'cellContents' where it stores all of the contents of every job HashMap. It then loops thru every
    // cell (Name, Employer, Location, Position Type, Core Competency) looking for the matching value (contents of
    // the individual cells), adding these values to cellContents ArrayList. Then it loops thru all of these values
    // looking for the search term the user entered. If the searchTerm is present, the entire HashMap where
    // the searchTerm is present gets added to an arraylist called
    // matchingJobs and returned.

    //1. Which methods are called when searching?
    // A: findByValue and findByColumnAndValue
    // 2. How is the user’s search string compared against the values of fields of the job HashMap objects?
    // A: [findByValue] for (String values: cellContents) {
    //                if (values.contains(searchTerm)) {
    //                    matchingJobs.add(job);
    //                    break;
    //            }
    // [findByColumnAndValue]  if (aValue.contains(value)) {
    //                jobs.add(row);
    //            }
    // 3. How can you make this comparison in a way that effectively ignores the case of the strings?
    // A: Make findByValue's searchTerm AND value toUpperCase()
    // A: Make findByColumnAndValue's aValue AND value toUpperCase()?
















    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
