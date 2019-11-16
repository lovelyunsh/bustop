package org.soonytown.bustop_user;//package org.soonytown.bustop_user;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//
//public class FirebaseDatabaseHelper {
//    private FirebaseDatabase mDatabase;
//    private DatabaseReference mReferenceReserves;
//    private List<Reserve> reserves = new ArrayList<>();
//
//    public interface DataStatus{
//        void DataIsLoaded(List<Reserve> reserves, List<String> keys);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
//    }
//
//    public FirebaseDatabaseHelper(){
//        mDatabase = FirebaseDatabase.getInstance();
//        mReferenceReserves = mDatabase.getReference("reserves");
//    }
//
//    public void readReserves(final DataStatus dataStatus){
//        mReferenceReserves.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                reserves.clear();
//                List<String> keys = new ArrayList<>();
//                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
//                    keys.add(keyNode.getKey());
//                    Reserve reserve = keyNode.getValue(Reserve.class);
//                    reserves.add(reserve);
//                }
//                dataStatus.DataIsLoaded(reserves,keys);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//}
