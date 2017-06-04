package com.team29.cse110.team29dejaphoto.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Manages the photos to be shown to the user that is not in history
 */
public class Priorities {

    /* Priority Queue of DejaPhotos based on scores */
    private PriorityQueue<LocalPhoto> pq;

    /** Default Constructor */
    public Priorities() {
        pq = new PriorityQueue<>(10, Collections.<LocalPhoto>reverseOrder());
    }

    /**
     * Adds a photo to the Priorities object
     *
     * @param photo - The photo to be added into the sorted structure
     * @return True - If the photo is added
     *         False - Otherwise
     */
    public boolean add(LocalPhoto photo) {
        if(photo == null) return false;

        return pq.add(photo);
    }

    /**
     * Takes off the highest priority photo from the structure and returns it
     *
     * @return LocalPhoto - The LocalPhoto with the highest score
     */
    public LocalPhoto getNewPhoto(){
        return pq.poll();
    }

    /**
     * Updates the priorities of each LocalPhoto object in the structure
     *
     * @param location - The new current location to update score with respects to
     */
    public void updatePriorities(Location location, Preferences prefs) {
        // Naive implementation
        // create new temporary arraylist to hold dejaphoto objects for reshuffling
        ArrayList<LocalPhoto> temp = new ArrayList<>();

        // update each photo's score
        for (LocalPhoto photo : pq) {
            photo.updateScore(location, prefs);
            temp.add(photo);
        }

        // reset the original priorityqueue and re-add each photo from the temporary
        // arraylist to it
        pq = new PriorityQueue<>(10, Collections.<LocalPhoto>reverseOrder());
        for (LocalPhoto photo : temp) {
            pq.add(photo);
        }
    }

}
