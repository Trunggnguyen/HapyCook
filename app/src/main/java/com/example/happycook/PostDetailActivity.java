package com.example.happycook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.happycook.Fragments.BlankFragment_Cmt;
import com.example.happycook.Model.Post;
import com.example.happycook.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {
    String postid;
    TextView name,nameuser, nguenlieu, b1, b2, b3;
    CircleImageView circleImageView;
    ImageView like, share, save, edit,spam, avata, postimage;
    String publisherid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent intent = getIntent();
        name = findViewById(R.id.nmanefood);
        nameuser = findViewById(R.id.username);
        nguenlieu = findViewById(R.id.nlieu);
        postimage = findViewById(R.id.post_image);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        avata = findViewById(R.id.image_profile);
        like = findViewById(R.id.ic_like);
        share = findViewById(R.id.ic_share);
        save = findViewById(R.id.ic_delete);
        edit = findViewById(R.id.ic_edit);
        spam = findViewById(R.id.ic_baocao);
        publisherid = intent.getStringExtra("publisherid");
        postid = intent.getStringExtra("postid");
        readPosts();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.farme, new BlankFragment_Cmt(postid, publisherid))
                .commit();

    }



    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPostid().equals(postid)){
                        publisherInfo(avata,nameuser,post.getPublisher());
                        name.setText(post.getDescription());
                        nguenlieu.setText(post.getNlieu());
                        b1.setText(post.getBuoc1());
                        b2.setText(post.getBuoc2());
                        b3.setText(post.getBuoc3());
                        Glide.with(PostDetailActivity.this).load(post.getPostimage()).into(postimage);
                        isLiked(post.getPostid(), like);
                        if (post.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            spam.setVisibility(View.GONE);

                        }else {
                            isSaved(post.getPostid(), save);
                            edit.setVisibility(View.GONE);

                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (save.getTag().equals("save")){
                                        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(post.getPostid()).setValue(true);
                                    } else {
                                        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(post.getPostid()).removeValue();
                                    }
                                }
                            });
                        }

                        like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (like.getTag().equals("like")) {
                                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                    addNotification(post.getPublisher(),postid);
                                } else {
                                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }
                            }
                        });
                        spam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PopupMenu popupMenu = new PopupMenu(PostDetailActivity.this, view);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()){
                                            case R.id.report:
                                                Toast.makeText(PostDetailActivity.this, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }
                                });
                                popupMenu.inflate(R.menu.post_menu);
                                popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                                popupMenu.getMenu().findItem(R.id.delete).setVisible(false);

                                popupMenu.show();
                            }
                        });

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addNotification(String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);
    }
    private void isLiked(final String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.hearted);
                    imageView.setTag("liked");
                } else{
                    imageView.setImageResource(R.drawable.heart);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isSaved(final String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.saved);
                    imageView.setTag("saved");
                } else{
                    imageView.setImageResource(R.drawable.save);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void publisherInfo(final ImageView image_profile, final TextView username,  final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(PostDetailActivity.this).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}