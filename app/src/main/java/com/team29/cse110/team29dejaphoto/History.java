package com.team29.cse110.team29dejaphoto;

import android.location.Location;


import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Manages history of previous photos
 */
public class History {

    private static final String TAG = "History";

    private LinkedList<DejaPhoto> historyList; // Underlying List structure
    private ListIterator<DejaPhoto> iterator;  // Iterator to move through the history
    private boolean forward;                   // Whether the iterator is currently moving forward
    private int nelems;                        // Number of elements

    /** Default Constructor */
    public History() {
        historyList = new LinkedList<>(); //new LinkedList<DejaPhoto>();
        iterator = historyList.listIterator();
        forward = true;
    }

    public boolean isHistoryEmpty() {
        return nelems == 0;
    }

    /**
     * Used to check if we are currently at the latest DejaPhoto in the history list.
     *
     * @return boolean - False if the number of photos in the list equal the counter.
     *                   Otherwise, returns true.
     */
    private boolean checkValidNext() {
        return iterator.hasPrevious();
    }

    /**
     * Used to check if we are currently at the oldest DejaPhoto in the history list.
     *
     * @return boolean - True if there are photos available when traversing backwards through
     *                   the history; false otherwise.
     */
    private boolean checkValidPrev() {
        return iterator.hasNext();
    }

    /**
     * Used to increment the counter (which photo we are looking at in the history
     * list) and to get the next photo.
     *
     * @return DejaPhoto - the photo to be displayed
     */
    public DejaPhoto getNext() {
        if(!forward && checkValidNext()) {
            iterator.previous();
            forward = true;
        }
        return checkValidNext() ? iterator.previous() : null;
    }

    /**
     * Used to increment the iterator and to get the previous photo.
     *
     * @return DejaPhoto - the photo to be displayed
     *         null - there are no previous photos available
     */
    public DejaPhoto getPrev() {
        if(forward && checkValidPrev()) {
            iterator.next();
            forward = false;
        }
        return checkValidPrev() ? iterator.next() : null;
    }

    /**
     * Used to remove DejaPhoto's from the history list when it reaches 10
     * photos, and to add DejaPhoto's to the history when we swipe left.
     *
     * @param photo - Photo to add from priority queue to history list
     * @return DejaPhoto - Photo to be added back into the PQ if removed from
     *                     the list
     */
    public DejaPhoto addPhoto(DejaPhoto photo) {
        DejaPhoto removed = null;

        // if history is full, take the earliest photo out to make room for the new one
        if(nelems == 10) {
            removed = historyList.removeLast();
        } else {
            nelems ++;
        }

        // add photo to the history
        historyList.addFirst(photo);
        iterator = historyList.listIterator();

        //this photo will be added back to the priority queue
        return removed;
    }

    /**
     * Cycles the last photo in the history towards the front
     * @return DejaPhoto cycled back to the front of the list
     * used in the event there are less than 10 photos total
     */
    public DejaPhoto cycle() {
        // make sure we have photos. If we do, proceed
        if(nelems != 0) {
            DejaPhoto toCycle = historyList.removeLast();
            historyList.addFirst(toCycle);
            iterator = historyList.listIterator();

            return toCycle;
        }
        return null;
    }

    /**
     * Update the priorites of all DejaPhoto Objects held by this History structure
     *
     * @param location The new current location to update score with respects to
     */
    public void updatePriorities(Location location, Preferences prefs) {
        for(DejaPhoto photo : historyList) {
            photo.updateScore(location, prefs);
        }
    }

    public void removeFromHistory() {

        if(nelems == 0) {
            return;
        }

        if(forward) {
            if(iterator.hasNext()) iterator.next();

        } else {
            if(iterator.hasPrevious()) iterator.previous();
            forward = true;
        }

        iterator.remove();
        nelems--;
    }

    /* Returns photo currently displayed on the home screen */
    public DejaPhoto getCurrentPhoto() {

        if(nelems == 0) {
            return null;
        }

        if(forward) {
            if(iterator.hasNext()) return iterator.next();

        } else {
            if(iterator.hasPrevious()) return iterator.previous();
            forward = true;
        }

        return null;
    }

}