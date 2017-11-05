package com.dabinu.apps.skeda;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class ContactDisplayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{


    ListView mContactsList;
    long mContactId;
    String mContactKey;
    Uri mContactUri;
    private SimpleCursorAdapter mCursorAdapter;
    private static final long CONTACT_ID_INDEX = 0;
    private static final int LOOKUP_KEY_INDEX = 1;


    public ContactDisplayFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.entire_contact_list_guy, container, false);

}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mSelectionArgs[0] = "%" + mSearchString + "%";
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, mSelectionArgs, null
        );
    }

    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};



    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};


    private final static int[] TO_IDS = {android.R.id.text1};


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mCursorAdapter.swapCursor(null);
    }



    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mContactsList = (ListView) getActivity().findViewById(R.id.listOfContacts);
        mCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.entire_contact_list_guy, null, FROM_COLUMNS, TO_IDS, 0);
        mContactsList.setAdapter(mCursorAdapter);
        mContactsList.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }


    @SuppressLint("InlinedApi")
    private static final String SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
    private String mSearchString;
    private String[] mSelectionArgs = { mSearchString };


    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position, long rowID){
                    parent.getAdapter().getItem(position);
                    parent.getAdapter().getItem(position);
                    mContactId = (CONTACT_ID_INDEX);
                    mContactKey = getString(LOOKUP_KEY_INDEX);
                    mContactUri = ContactsContract.Contacts.getLookupUri(mContactId, mContactKey);

    }

}
