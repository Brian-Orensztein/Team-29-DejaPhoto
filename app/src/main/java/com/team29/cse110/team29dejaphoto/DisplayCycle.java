package com.team29.cse110.team29dejaphoto;

public class DisplayCycle {

    /* Initialize member variables for DisplayCycle */
    private History history;
    private Priorities priorities;

    /** Default Constructor */
    public DisplayCycle() {
        history = new History();
        priorities = new Priorities();
    }

    /** Overloaded Constructor */
    public DisplayCycle(DejaPhoto[] gallery) {
        history = new History();
        priorities = new Priorities();

        for(DejaPhoto photo : gallery) {
            priorities.add(photo);
        }
    }

    /**
     * Add a single photo to album.
     */
    public void addToCycle(DejaPhoto photo) {
        priorities.add(photo);
    }

    /**
     * Used to get the next photo in the sequence, calling other helper methods to
     * determine where to get the next photo.
     * @param none
     * @return DejaPhoto - photo to be displayed
     */
    public DejaPhoto getNextPhoto() {
        DejaPhoto next = history.getNext();
        if(next == null) {
            DejaPhoto newPhoto = priorities.getNewPhoto();
            if(newPhoto != null) {
                DejaPhoto removed = history.addPhoto(newPhoto);
                if(removed != null) {
                    priorities.add(removed);
                }
            } else {
                return history.cycle();
            }
            return newPhoto;
        }
        return next;
    }

    /**
     * Used to get the previous photo in the sequence, calling other helper methods to
     * determine where to get the previous photo.
     * @param none
     * @return DejaPhoto - photo to be displayed
     *         null - there are no previous photos available
     */
    public DejaPhoto getPrevPhoto() {
        return history.getPrev();
    }

}
