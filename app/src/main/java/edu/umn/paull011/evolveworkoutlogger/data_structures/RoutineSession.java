package edu.umn.paull011.evolveworkoutlogger.data_structures;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import edu.umn.paull011.evolveworkoutlogger.BuildConfig;

/**
 * Created by Mitchell on 12/16/2015.
 *
 * Class holds data about a routine that was performed on a specific day
 *
 */
public class RoutineSession {

    // Private
    private Routine mRoutine;
    private long mId;
    private Date mDate;
    private boolean mCompleted;
    private ArrayList<ExerciseSession> mExerciseSessions;
    private String mNotes;
    private static final String TAG = RoutineSession.class.getSimpleName();

    // Public
    public RoutineSession(Routine routine, boolean createExerciseSessions){
        mRoutine = routine;
        mId = -1;
        mDate = new Date(Calendar.getInstance().getTimeInMillis());
        mCompleted = false;
        mNotes = "";
        int numExercises = routine.getNumExercises();
        mExerciseSessions = new ArrayList<>(numExercises);
        boolean increment;

        if (createExerciseSessions) {
            Exercise exercise;
            ExerciseSession exerciseSession;
            for(int i = 0; i < numExercises; i++){
                exercise = routine.getExercise(i);
                exerciseSession = exercise.createNewExerciseSession();
                mExerciseSessions.add(exerciseSession);
            }
        }
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public Routine getRoutine() {
        return mRoutine;
    }

    public void addNewExerciseSessionFromExercise(Exercise exercise, boolean addToRoutine){
        ExerciseSession exerciseSession = exercise.createNewExerciseSession();
        mExerciseSessions.add(exerciseSession);
        if(addToRoutine)
            mRoutine.addExercise(exercise);
    }

    public void addExerciseSession(ExerciseSession session) {
        mExerciseSessions.add(session);
    }

    public void removeExerciseSession(int index){
        mExerciseSessions.remove(index);
    }


    public ExerciseSession getExerciseSession(int index){
        return mExerciseSessions.get(index);
    }


    public int getExerciseSessionCount(){
        return mExerciseSessions.size();
    }

    public Date getDate(){
        return mDate;
    }


    public void setDate(Date date){
        mDate = date;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void finish() {
        mCompleted = true;
    }

    public void swapExerciseSessions(int fromPosition, int toPosition) {
        if (BuildConfig.DEBUG) {
            if (fromPosition < mExerciseSessions.size()) {
                throw new AssertionError();
            }
            if (fromPosition < mExerciseSessions.size()) {
                throw new AssertionError();
            }
            if (toPosition < mExerciseSessions.size()) {
                throw new AssertionError();
            }
            if (toPosition < mExerciseSessions.size()) {
                throw new AssertionError();
            }
            if (fromPosition != toPosition) {
                throw new AssertionError();
            }
        }
        ExerciseSession from = mExerciseSessions.get(fromPosition);
        ExerciseSession to = mExerciseSessions.get(toPosition);
        mExerciseSessions.set(fromPosition, to);
        mExerciseSessions.set(toPosition, from);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // Objects of the same class?
        if (!(obj instanceof RoutineSession)) {return false;}
        RoutineSession other = (RoutineSession) obj;
        // Do the routine sessions have the same exercise sessions?
        if (mExerciseSessions.size() != other.mExerciseSessions.size()){
            return false;
        }
        if (this.isCompleted() != other.isCompleted()) {
            return false;
        }
        for(int i = 0; i < mExerciseSessions.size(); i++){
            if (!mExerciseSessions.get(i).equals(other.mExerciseSessions.get(i)))
                return false;
        }
        return true;
    }
}
