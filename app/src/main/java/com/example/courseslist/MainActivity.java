package com.example.courseslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;

import com.example.CoursesList.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Check";
    List<String> subjectList;
    HashMap<String, List<String>> subjectMap;


    //Firebase storage references
    private FirebaseDatabase mFirebasedatabase;

    //Categories (Core subject) database references:
    private DatabaseReference mCategoriesDatabaseReference;
    // Init top level data
    List<String> listDataHeader ;

    ParentLevelAdapter parentLevelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        // Init top level data
        listDataHeader = new ArrayList<>();
        //Initialize Firebase Components
        mFirebasedatabase = FirebaseDatabase.getInstance();
        //For Reading Tasks(Not Using For Now)
        mCategoriesDatabaseReference = mFirebasedatabase.getReference().child("Coding Languages");
        //Now Saving courses(from firebase nodes:task1,task2,task3) to ArrayList and then adding data to HashMap
        subjectMap = new HashMap<String, List<String>>();
        subjectList = new ArrayList<>();


        //Getting data from firebase
        getDataFromFirebase();

       // String[] mItemHeaders = getResources().getStringArray(R.array.items_array_expandable_level_one);*/


       // Collections.addAll(listDataHeader, mItemHeaders);
        final ExpandableListView mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView_Parent);
        if (mExpandableListView != null) {
            parentLevelAdapter = new ParentLevelAdapter(this, listDataHeader);
            mExpandableListView.setAdapter(parentLevelAdapter);
            //Refreshing Adapter
            parentLevelAdapter.notifyDataSetChanged();

            // display only one expand item
//            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//                int previousGroup = -1;
//                @Override
//                public void onGroupExpand(int groupPosition) {
//                    if (groupPosition != previousGroup)
//                        mExpandableListView.collapseGroup(previousGroup);
//                    previousGroup = groupPosition;
//                }
//            });
        }
    }
    //Reading Data From firebase
    void getDataFromFirebase(){
        //Firebase Read Listener
        mCategoriesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Running Foreach loop
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    //Getting Category (Core subject) Value //from 1 to 10 in ArrayList(tasks)
                    listDataHeader.add(d.getValue().toString());
                }
                System.out.println(listDataHeader);
                //Putting key & tasks(ArrayList) in HashMap
                subjectMap.put(dataSnapshot.getKey(),listDataHeader);

                subjectList.add(dataSnapshot.getKey());

                listDataHeader=new ArrayList<>();

                Log.d(TAG, "onChildAdded: dataSnapshot.getChildren: "+dataSnapshot.getChildren());
                Log.d(TAG, "onChildAdded: KEY"+dataSnapshot.getKey()+" value "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //task.remove("" + dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // mTasksDatabaseReference.addChildEventListener(mChildEventListener);
    }


}
