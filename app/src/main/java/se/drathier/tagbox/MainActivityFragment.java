package se.drathier.tagbox;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import se.drathier.tagbox.adapters.TagAdapter;
import se.drathier.tagbox.common.SnomedDB;
import se.drathier.tagbox.tagbox.Model;
import se.drathier.tagbox.tagbox.mifare.deserializer;
import se.drathier.tagbox.tagbox.mifare.mifare_ultralight;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public RecyclerView recyclerView;
    public static TagAdapter tagAdapter;

    private GoogleApiClient mGoogleApiClient;
    private Message mActiveMessage;
    private MessageListener mMessageListener;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity().getApplicationContext())
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tag_list);

        final Model model = new Model();
        model.CountryCode = "se";
        model.SSN = "910612-1099";
        model.given_name = "Anders";
        model.bt_ab = Model.BloodTypeAB.A;
        model.bt_plus = Model.BloodTypePlusMinus.Plus;
        model.is_organ_donor = false;
        model.is_male = true;

        Model.Snomed_id snomed_id = new Model.Snomed_id();
        snomed_id.id = 91934008;
        snomed_id.from = Calendar.getInstance();
        snomed_id.from.set(1991, 6, 12);
        snomed_id.to = Calendar.getInstance();
        snomed_id.severity = Model.Severity.High;

        Model.Snomed_id snomed_id2 = new Model.Snomed_id();
        snomed_id2.id = 34563004;
        snomed_id2.from = Calendar.getInstance();
        snomed_id2.from.set(1991, 6, 12);
        snomed_id2.to = Calendar.getInstance();
        snomed_id2.severity = Model.Severity.High;

        model.snomed_ids = new ArrayList<>();
        model.snomed_ids.add(snomed_id);
        model.snomed_ids.add(snomed_id2);


        if(tagAdapter == null) {
            tagAdapter = new TagAdapter();
            tagAdapter.list = new ArrayList<>();
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int spanCount = (int) (metrics.widthPixels / (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, metrics));

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                int space = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics());

                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }
        });


        setHasOptionsMenu(true);


        recyclerView.setAdapter(tagAdapter);

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d("Found message: ", messageAsString);
                Toast.makeText(getActivity().getApplicationContext(), "found: " + message.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d("Lost sight of message: ", messageAsString);
                Toast.makeText(getActivity().getApplicationContext(), "lost: " + message.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.action_scan) {
            Toast.makeText(MainActivityFragment.this.getContext(), "SCAN!", Toast.LENGTH_SHORT).show();

            /*
            final Model des = (new deserializer()).deserialize(all);

                SnomedDB.fetch("en", des, new SnomedDB.SnomedModelResponse() {
                    @Override
                    public void dataAdded() {

                        if(tagAdapter.list == null)
                            tagAdapter.list = new ArrayList<>();

                        tagAdapter.list.add(des);
                        tagAdapter.notifyDataSetChanged();
                    }
                });
             */


            unsubscribe();
            Log.d("Delayed", "unsub");

            publish("sudo scp * me");
            Log.d("Delayed", "pub");

            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    Log.d("Delayed", "unpub");
                    unpublish();
                    subscribe();
                    Log.d("Delayed", "sub");
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            unsubscribe();
                            Log.d("Delayed", "unsub");
                        }
                    }, 5000);
                }
            }, 5000);

            // magic sub!!
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void publish(String message) {
        Log.i("Publishing message", message);
        mActiveMessage = new Message(message.getBytes());
        Nearby.Messages.publish(mGoogleApiClient, mActiveMessage);
    }

    private void unpublish() {
        if(mActiveMessage != null)
            Log.i("Unpublishing", this.mActiveMessage.toString());

        if (mActiveMessage != null) {
            Nearby.Messages.unpublish(mGoogleApiClient, mActiveMessage);
            mActiveMessage = null;
        }
    }

    // Subscribe to receive messages.
    private void subscribe() {
        Log.i("Subscribing.", "");
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener);
    }

    private void unsubscribe() {
        Log.i("Unsubscribing.", "");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        publish("Hello World");
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO
    }

    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();


        Model m;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        NdefMessage[] msgs;

        Intent intent = getActivity().getIntent();
        Log.d("MainAct", intent.toString());

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getActivity().getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    Log.d("rawMsg", rawMsgs[i].toString());
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getActivity().getIntent().getAction())) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


            MifareUltralight a = MifareUltralight.get(tagFromIntent);


            mifare_ultralight mul = new mifare_ultralight(a);

            /*
            Log.d("tag_tech", tagFromIntent.toString());

            try {
                mul.mul.connect();
                mul.writeSerialized(this.m);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mul.mul.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            */

            try {
                a.connect();
                ArrayList<Byte> all = mul.read_all();
                //Log.d("raw_json", gson.toJson(all));
                final Model des = (new deserializer()).deserialize(all);

                SnomedDB.fetch("en", des, new SnomedDB.SnomedModelResponse() {
                    @Override
                    public void dataAdded() {

                        if(tagAdapter.list == null)
                            tagAdapter.list = new ArrayList<>();

                        tagAdapter.list.add(des);
                        tagAdapter.notifyDataSetChanged();
                    }
                });

                //Log.d("pre_serializ_model", gson.toJson(m));
                Log.d("deserialized_model", gson.toJson(des));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    a.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //process the msgs array

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO
    }
}
