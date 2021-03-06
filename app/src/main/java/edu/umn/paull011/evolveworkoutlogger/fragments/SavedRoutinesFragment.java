package edu.umn.paull011.evolveworkoutlogger.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ItemTouchHelperCallback;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RecyclerViewItemClickListener;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineCursorAdapter;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.TestItemTouchHelper;

/**
 * A {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavedRoutinesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavedRoutinesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedRoutinesFragment extends BaseFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private RoutineCursorAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Boolean mAllowSwipeToDelete;

    private OnFragmentInteractionListener mFragmentInteractionListener;
    private Cursor mCursor;
    private static final String TAG = SavedRoutinesFragment.class.getSimpleName();

    public SavedRoutinesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedRoutinesFragment.
     */
    public static SavedRoutinesFragment newInstance() {
        return new SavedRoutinesFragment();
    }

    // Get saved routines from the database
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        mCursor = db.getRoutinesCursor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_routines, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_saved_routines);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new RoutineCursorAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        if (mAllowSwipeToDelete) {
            ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
            ItemTouchHelper touchHelper = new TestItemTouchHelper(callback); //Prints log messages
            touchHelper.attachToRecyclerView(mRecyclerView);
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(getActivity(),
                        new RecyclerViewItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                onRoutineSelected(position);
                            }
                        })
        );

        // Display the empty view if there is nothing in the database
        mEmptyView = (TextView) view.findViewById(R.id.empty_view_saved_routines);
        hideOrShowRecyclerView();
    }

    // Ensure that the activity is listening to the fragment according to the
    // OnFragmentInteractionListener interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
            mAllowSwipeToDelete = mFragmentInteractionListener.routinesDeletable();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    // Runs when a Routine is selected from the list view
    public void onRoutineSelected(int position) {
        if (mFragmentInteractionListener != null) {
            // Get the Routine Id based on the position selected
            String routineName = mAdapter.getRoutineNameFromPosition(position);
            mFragmentInteractionListener.routineSelected(routineName);
        }
    }

    public void refresh() {
        mAdapter.refreshCursor();
        mAdapter.notifyDataSetChanged();
        hideOrShowRecyclerView();
    }

    public void deleteSwipedRoutineFromAdapter() {
        mAdapter.deleteSwipedRoutine();
    }

    public void unDismissSwipedRoutineFromAdapter() {
        mAdapter.unDismissSwipedRoutine();
    }

    private void hideOrShowRecyclerView() {
        if (mAdapter.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public interface OnFragmentInteractionListener {
        void routineSelected(String routineName);
        boolean routinesDeletable();
    }
}
