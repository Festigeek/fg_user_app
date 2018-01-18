package com.example.flavia.fg_user_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnNotificationsPreferencesSet} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Unbinder unbinder;

    private OnNotificationsPreferencesSet mListener;

    @BindView(R.id.all_tournaments)
    protected Switch allTournamentSwitch;

    @BindView(R.id.hearthstone_switch)
    protected Switch hsSwitch;

    @BindView(R.id.lol_switch)
    protected Switch lolSwitch;

    @BindView(R.id.csgo_switch)
    protected Switch csgoSwitch;

    @BindView(R.id.ow_switch)
    protected Switch owSwitch;

    @BindView(R.id.animation)
    protected Switch animSwitch;

    @BindView(R.id.food)
    protected Switch foodSwitch;

    @BindView(R.id.ceremony)
    protected Switch ceremonySwitch;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);


        // Set all listener


        allTournamentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                NotificationPreference preferences = NotificationPreference.getInstance();
                // Check status of :
                // HS
                // LOL
                // CSGO
                // OW
                if (isChecked) {

                    preferences.add("HS");
                    hsSwitch.setChecked(true);

                    preferences.add("LOL");
                    lolSwitch.setChecked(true);

                    preferences.add("CSGO");
                    csgoSwitch.setChecked(true);

                    preferences.add("OW");
                    owSwitch.setChecked(true);

                } else {

                    preferences.remove("HS");
                    hsSwitch.setChecked(false);

                    preferences.remove("LOL");
                    lolSwitch.setChecked(false);

                    preferences.remove("CSGO");
                    csgoSwitch.setChecked(false);

                    preferences.remove("OW");
                    owSwitch.setChecked(false);
                }
            }
        });


        hsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                NotificationPreference preferences = NotificationPreference.getInstance();
                // Check status of :
                // HS
                if (isChecked) {

                    preferences.add("HS");
                    allTournamentSwitch.setChecked(true);

                } else {

                    preferences.remove("HS");

                    if (! preferences.haveAtLeastOneGame()){
                        allTournamentSwitch.setChecked(false);
                    }


                }
            }
        });

        lolSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                NotificationPreference preferences = NotificationPreference.getInstance();
                // Check status of :
                // LOL
                if (isChecked) {

                    preferences.add("LOL");
                    allTournamentSwitch.setChecked(true);

                } else {

                    preferences.remove("LOL");

                    if (! preferences.haveAtLeastOneGame()){
                        allTournamentSwitch.setChecked(false);
                    }


                }
            }
        });




        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(List<String> listNotifications) {
        if (mListener != null) {
            mListener.upadateNotificationRequest(listNotifications);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationsPreferencesSet) {
            mListener = (OnNotificationsPreferencesSet) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Notification List should contain only :
    // HS
    // LOL
    // CSGO
    // OW
    // Anim
    // Food
    // Ceremony
    public interface OnNotificationsPreferencesSet {
        // TODO: Update argument type and name
        void upadateNotificationRequest(List<String> listNotifications);
    }
}
