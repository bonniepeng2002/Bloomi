package com.bonniepeng.bloomi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bonniepeng.bloomi.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addPlant;
    private Toolbar myToolbar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIREBASE
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        instantiate();
//        createNotificationChannel();
        Log.i("LOGIN", String.valueOf(mAuth.getCurrentUser()));
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

//        // GET TIMES FROM DB
//        CollectionReference userPlants = db.collection("users")
//                .document(currentUser.getUid()).collection("plants");
//
//        Map<String, Timestamp> plantNotifTimes = new HashMap<String, Timestamp>();
//        userPlants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                        String name;
//                        String sciName = document.getData().get("sciName").toString();
//                        String nickname = document.getData().get("nickname").toString();
//                        if (sciName.equals("")) {
//                            name = nickname;
//                        } else {
//                            name = sciName;
//                        }
//                        String hour = document.getData().get("notifHour").toString();
//                        String minute = document.getData().get("notifMinute").toString();
//                        String day = document.getData().get("notifDay").toString();
//                        String month = document.getData().get("notifMonth").toString();
//                        String year = document.getData().get("notifYear").toString();
//                        String frequency = document.getData().get("notifFrequency").toString();
//
//                        if (month.length() == 1) {
//                            month = "0" + month;
//                        }
//                        if (day.length() == 1) {
//                            day = "0" + day;
//                        }
//                        if (hour.length() == 1) {
//                            hour = "0" + hour;
//                        }
//                        if (minute.length() == 1) {
//                            minute = "0" + minute;
//                        }
//                        if (!year.equals("-1")) {
//                            setNotif(year, month, day, hour, minute, frequency);
//                        }
//                    }
//                } else {
//                    Log.e("GETTING NOTIFTIMES", "Error getting documents: ", task.getException());
//                }
//            }
//        });


        //TABS
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        //ADDPLANT
        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPlant.class);
                startActivity(intent);
            }
        });

    }


//    // NOTIFICATIONS
//
//    @SuppressLint("ShortAlarm")
//    private void setNotif(String year, String month, String day, String hour, String minute, String frequency) {
//
//        //create repeating notification
//        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        AlarmManager manager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
//        int daily = 24 * 60 * 60 * 1000;
//        int weekly = daily * 7;
//        int twoWeeks = weekly * 2;
//        int once = 0;
//        Log.i("CAL DATE", cal.toString());
//
//        switch (frequency) {
//            case "Once":
//                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), once, pendingIntent);
//                Log.i("FREQUENCY", "Once");
//                break;
//            case "Daily":
//                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//                Log.i("FREQUENCY", "Daily");
//                break;
//            case "Weekly":
//                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), weekly, pendingIntent);
//                Log.i("FREQUENCY", "Weekly");
//                break;
//            default:
//                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), twoWeeks, pendingIntent);
//                Log.i("FREQUENCY", "Two weeks");
//                break;
//        }
//
//    }
//
//    public class Notify extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Notification builder = new NotificationCompat.Builder(context, "Watering")
//                    .setSmallIcon(R.drawable.ic_leaf)
//                    .setContentTitle("Water Reminder")
//                    .setContentText("Don't forget to water your plants!")
//                    .setContentIntent(PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0))
//                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .build();
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(1, builder);
//        }
//    }


    private void instantiate() {
        addPlant = findViewById(R.id.addPlant);
        myToolbar = findViewById(R.id.toolbar);

        // FIREBASE
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


    }

    // CHECK IF USER NEEDS TO LOG IN
    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                Log.i("LOGIN STATUS", "user needs to log in");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };

//    // NOTIFICATIONS
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "Watering Notifications";
//            String description = "Time to water your plants";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("Watering", name, importance);
//            channel.setDescription(description);
//
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.menu) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}