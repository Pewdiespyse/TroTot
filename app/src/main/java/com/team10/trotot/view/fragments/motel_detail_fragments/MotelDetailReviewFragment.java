package com.team10.trotot.view.fragments.motel_detail_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Comment;
import com.team10.trotot.model.basic_classes.Rating;
import com.team10.trotot.model.basic_classes.Reply;
import com.team10.trotot.view.activities.MotelDetailActivity;
import com.team10.trotot.view.supports.GlideApp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS_CONTENT;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS_LIKE;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS_REPLY;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS_REPLY_CONTENT;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS_REPLY_USER_ID;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_COMMENTS_USER_ID;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS_EVALUATE;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_RATINGS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_AVATAR;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_NAME;

/**
 * Created by vinhkhang on 16/10/2017.
 */

public class MotelDetailReviewFragment extends Fragment {

    private final int MINIMUM_COMMENT_CHARACTER = MotelDetailActivity.MINIMUM_COMMENT_CHARACTER;
    private final int MINIMUM_REPLY_CHARACTER = MotelDetailActivity.MINIMUM_REPLY_CHARACTER;

    private String motelID = MotelDetailActivity.motelID;
    private String userID = MotelDetailActivity.userID;

    private SwipeRefreshLayout srlReview;
    private TextView tvRatingStartAvg;
    private RatingBar rbRatingStartAvg;
    private TextView tvRatingStartCount; // \u2603
    private ArrayList<ProgressBar> pbRating;
    private MaterialRatingBar rbRatingStartMe;

    private ArrayList<Comment> commentArrayList;
    private CommentAdapter commentAdapter;
    private ArrayList<UserInfo> userInfoArrayList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motel_detail_review, container, false);

        srlReview = view.findViewById(R.id.srl_motel_detail_review);
        tvRatingStartAvg = view.findViewById(R.id.tv_motel_detail_review_rating_star_avg); // \u2603 1.200
        rbRatingStartAvg = view.findViewById(R.id.rb_motel_detail_review_rating_star_avg);
        tvRatingStartCount = view.findViewById(R.id.tv_motel_detail_review_rating_count);
        pbRating = new ArrayList<>();
        pbRating.add((ProgressBar) view.findViewById(R.id.pb_motel_detail_review_rating_5));
        pbRating.add((ProgressBar) view.findViewById(R.id.pb_motel_detail_review_rating_4));
        pbRating.add((ProgressBar) view.findViewById(R.id.pb_motel_detail_review_rating_3));
        pbRating.add((ProgressBar) view.findViewById(R.id.pb_motel_detail_review_rating_2));
        pbRating.add((ProgressBar) view.findViewById(R.id.pb_motel_detail_review_rating_1));
        rbRatingStartMe = view.findViewById(R.id.rb_motel_detail_review_rating_star_me);
        FrameLayout flTouchMe = view.findViewById(R.id.fl_motel_detail_review_rating_me);
        RecyclerView rvComment = view.findViewById(R.id.rv_motel_detail_review_comment);

        TextView tvAddComment1 = view.findViewById(R.id.tv_item_motel_detail_review_add_comment_1);
        Button tvAddComment2 = view.findViewById(R.id.btn_item_motel_detail_review_add_comment_2);

        tvAddComment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        tvAddComment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        //
        userInfoArrayList = new ArrayList<>();

        //
        commentArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter();
        rvComment.setNestedScrollingEnabled(false);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvComment.setItemAnimator(new DefaultItemAnimator());
        rvComment.setAdapter(commentAdapter);

        LayerDrawable stars = (LayerDrawable) rbRatingStartAvg.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        flTouchMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                // set min
                final MaterialRatingBar ratingBar = new MaterialRatingBar(getContext());
                ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                        if (rating < 1.0f) {
                            ratingBar.setRating(1.0f);
                        }
                    }
                });
                ratingBar.setStepSize(1);
                ratingBar.setMax(5);
                ratingBar.setRating(rbRatingStartMe.getRating());
                ratingBar.setPadding(20, 20, 20, 20);
                alertDialog.setView(ratingBar);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_RATINGS).child(motelID).child(userID).setValue((int) ratingBar.getRating());
                        Toast.makeText(getContext(), "" + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();
            }
        });

        srlReview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlReview.setRefreshing(false);
            }
        });


        // not signed in
        if (userID.equals("")) {
            tvAddComment2.setVisibility(View.GONE);
            view.findViewById(R.id.rl_motel_detail_review_rating_me_parent).setVisibility(View.GONE);
        }

        updateUI();

        return view;
    }


    private void logcat(String data) {
//        Log.v("Ahihi", data);
    }


    // ---------------------------------------------------------------------------------------------
    private void addComment() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_motel_detail_review_comment, null);
        alertDialog.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.et_dialog_motel_detail_review_comment);
        final TextView textView = dialogView.findViewById(R.id.tv_dialog_motel_detail_review_comment);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String data = s.length() + "/" + MINIMUM_COMMENT_CHARACTER;
                textView.setText(data);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alertDialog.setTitle(getString(R.string.home_model_detail_review_comment_add_comment));
        alertDialog.setPositiveButton(getString(R.string.home_model_detail_ok), null);
        alertDialog.setNegativeButton(getString(R.string.home_model_detail_cancel), null);

        Dialog dialog = alertDialog.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (editText.getText().length() >= MINIMUM_COMMENT_CHARACTER) {
                            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000 * 1000);
                            FirebaseDatabase.getInstance().getReference().child("comments").child(motelID).child(timeStamp).child(FIRE_BASE_STRING_COMMENTS_CONTENT).setValue(editText.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("comments").child(motelID).child(timeStamp).child(FIRE_BASE_STRING_COMMENTS_USER_ID).setValue(userID);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void addReply(final int commentIndex) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_motel_detail_review_comment, null);
        alertDialog.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.et_dialog_motel_detail_review_comment);
        final TextView textView = dialogView.findViewById(R.id.tv_dialog_motel_detail_review_comment);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String data = s.length() + "/" + MINIMUM_REPLY_CHARACTER;
                textView.setText(data);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alertDialog.setTitle(getString(R.string.home_model_detail_review_comment_add_reply));

        String content = commentArrayList.get(commentIndex).getContent();
        if (content.length() > 20) {
            content = content.substring(0, 20);
        }

        alertDialog.setMessage(content);
        alertDialog.setPositiveButton(getString(R.string.home_model_detail_ok), null);
        alertDialog.setNegativeButton(getString(R.string.home_model_detail_cancel), null);

        Dialog dialog = alertDialog.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (editText.getText().length() >= MINIMUM_REPLY_CHARACTER) {
                            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000 * 1000);
                            FirebaseDatabase.getInstance().getReference().child("comments").child(motelID).child(String.valueOf(commentArrayList.get(commentIndex).getTime())).child(FIRE_BASE_STRING_COMMENTS_REPLY).child(timeStamp).child(FIRE_BASE_STRING_COMMENTS_REPLY_CONTENT).setValue(editText.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("comments").child(motelID).child(String.valueOf(commentArrayList.get(commentIndex).getTime())).child(FIRE_BASE_STRING_COMMENTS_REPLY).child(timeStamp).child(FIRE_BASE_STRING_COMMENTS_REPLY_USER_ID).setValue(userID);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    // ---------------------------------------------------------------------------------------------
    private void updateUI() {
        updateUserProfile();
        updateRatingUI();
        updateCommentUI();
    }

    private void updateUserProfile() {

        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfoArrayList.clear();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator();

                    UserInfo userInfo;
                    logcat(dataSnapshot.getChildrenCount() + " a ");
                    while (result.hasNext()) {
                        DataSnapshot item = result.next();

                        userInfo = new UserInfo();
                        userInfo.setId(item.getKey());
                        userInfo.setName(item.child(FIRE_BASE_STRING_USERS_NAME).getValue(String.class));
                        userInfo.setAvatar(item.child(FIRE_BASE_STRING_USERS_AVATAR).getValue(String.class));

                        userInfoArrayList.add(userInfo);
                    }

                    for (UserInfo item : userInfoArrayList) {
                        logcat(item.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateRatingUI() {
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_RATINGS).orderByKey().equalTo(motelID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int MAX_RATING_TYPE = 5;

                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator().next().getChildren().iterator();
                    ArrayList<Rating> ratingArrayList = new ArrayList<>();
                    ArrayList<Integer> scoreArray = new ArrayList<>();

                    // cast to array list
                    while (result.hasNext()) {
                        DataSnapshot item = result.next();
                        ratingArrayList.add(new Rating(item.getKey(), item.getValue(Integer.class)));
                    }

                    // init
                    for (int i = 0; i < MAX_RATING_TYPE; i++) {
                        scoreArray.add(0);
                    }

                    // count
                    for (Rating item : ratingArrayList) {
                        int score = item.getScore();
                        if (score < 1 || score > 5) {
                            Toast.makeText(getContext(), "Database ratings/" + motelID + " error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        scoreArray.set(score - 1, scoreArray.get(score - 1) + 1);
                    }

                    // max
                    int maxScoreCount = 0;
                    for (int item : scoreArray) {
                        if (item > maxScoreCount) {
                            maxScoreCount = item;
                        }
                    }

                    // avg score
                    float avgScore = 0;
                    for (int i = 0; i < MAX_RATING_TYPE; i++) {
                        avgScore = avgScore + (i + 1) * scoreArray.get(i);
                    }
                    avgScore = avgScore / ratingArrayList.size();

                    // percent
                    for (int i = 0; i < MAX_RATING_TYPE; i++) {
                        scoreArray.set(i, scoreArray.get(i) * 100 / maxScoreCount);
                    }

                    // update UI all progress bar
                    for (int i = 0; i < MAX_RATING_TYPE; i++) {
                        pbRating.get(i).setProgress(scoreArray.get(i));
                    }

                    // update UI my rating bar
                    rbRatingStartMe.setRating(0);
                    for (Rating item : ratingArrayList) {
                        if (item.getUserId().equals(userID)) {
                            rbRatingStartMe.setRating(item.getScore());
                            break;
                        }
                    }

                    // update rating bar avg and tv rating agv
                    tvRatingStartAvg.setText(String.valueOf(avgScore));
                    rbRatingStartAvg.setRating(avgScore);

                    // update motel evaluate
                    FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).child(motelID).child(FIRE_BASE_STRING_MOTELS_EVALUATE).setValue(avgScore);

                    // update rating count
                    tvRatingStartCount.setText(String.valueOf("\u2603 " + ratingArrayList.size()));

                } else {
                    for (int i = 0; i < MAX_RATING_TYPE; i++) {
                        pbRating.get(i).setProgress(0);
                    }
                    rbRatingStartMe.setRating(0);
                    tvRatingStartAvg.setText("");
                    rbRatingStartAvg.setRating(0);
                    tvRatingStartCount.setText(String.valueOf("\u2603 " + 0));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateCommentUI() {

        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_COMMENTS).orderByKey().equalTo(motelID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                commentArrayList.clear();

                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator().next().getChildren().iterator();

                    while (result.hasNext()) {
                        DataSnapshot item = result.next();

                        Comment comment = new Comment();
                        comment.setTime(Long.valueOf(item.getKey()));
                        comment.setContent(String.valueOf(item.child("content").getValue()));
                        comment.setUserId(String.valueOf(item.child(FIRE_BASE_STRING_COMMENTS_USER_ID).getValue()));

                        // likes
                        Iterator<DataSnapshot> likesDataSnapshot = item.child(FIRE_BASE_STRING_COMMENTS_LIKE).getChildren().iterator();
                        List<String> likesList = new ArrayList<>();
                        if (item.child(FIRE_BASE_STRING_COMMENTS_LIKE).hasChildren()) {
                            while (likesDataSnapshot.hasNext()) {
                                likesList.add(likesDataSnapshot.next().getValue(String.class));
                            }
                        } else {
                            if (item.child(FIRE_BASE_STRING_COMMENTS_LIKE).getValue(String.class) != null) {
                                likesList.add(item.child(FIRE_BASE_STRING_COMMENTS_LIKE).getValue(String.class));
                            }
                        }


                        comment.setLikes(likesList);

                        // replies
                        Iterator<DataSnapshot> replyDataSnapshot = item.child(FIRE_BASE_STRING_COMMENTS_REPLY).getChildren().iterator();
                        List<Reply> replyList = new ArrayList<>();
                        if (item.child(FIRE_BASE_STRING_COMMENTS_REPLY).hasChildren()) {
                            while (replyDataSnapshot.hasNext()) {
                                Reply reply = new Reply();

                                DataSnapshot ds = replyDataSnapshot.next();
                                reply.setTime(Long.valueOf(ds.getKey()));
                                reply.setContent(ds.child(FIRE_BASE_STRING_COMMENTS_REPLY_CONTENT).getValue(String.class));
                                reply.setUserId(ds.child(FIRE_BASE_STRING_COMMENTS_REPLY_USER_ID).getValue(String.class));


                                replyList.add(reply);
                            }
                        }
                        comment.setReplies(replyList);

                        // add
                        commentArrayList.add(comment);
                    }
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    class UserInfo {
        private String id;
        private String name;
        private String avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }
    }

    // ---------------------------------------------------------------------------------------------
    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        CommentAdapter() {

        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View itemView = li.inflate(R.layout.item_fragment_motel_detail_review_comment, parent, false);

            return new CommentViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CommentViewHolder holder, int position) {
            final Comment comment = commentArrayList.get(position);

            // id
            holder.commentID = String.valueOf(comment.getTime());

            // content
            holder.tvContent.setText(comment.getContent());

            // time
            Timestamp timestamp = new Timestamp(comment.getTime());
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM hh:mm", Locale.getDefault());
            holder.tvTime.setText(simpleDateFormat.format(date));

            // profile name
            holder.tvProfile.setText(comment.getUserId());

            // like text
            final Boolean isLiked;
            final String likeString;
            if (comment.getLikes().contains(userID)) {
                likeString = getString(R.string.home_model_detail_review_comment_liked) + "(" + comment.getLikes().size() + ")";
                isLiked = true;
                holder.tvLike.setText(likeString);
            } else {
                likeString = getString(R.string.home_model_detail_review_comment_like) + "(" + comment.getLikes().size() + ")";
                isLiked = false;
                holder.tvLike.setText(likeString);
            }

            // reply text
            String replyString;
            if (comment.getLikes().contains(userID)) {
                replyString = getString(R.string.home_model_detail_review_comment_reply) + "(" + comment.getReplies().size() + ")";
                holder.tvReply.setText(replyString);
            } else {
                replyString = getString(R.string.home_model_detail_review_comment_reply) + "(" + comment.getReplies().size() + ")";
                holder.tvReply.setText(replyString);
            }

            // reply event
            holder.tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isShowReply || comment.getReplies().size() == 0) {
                        if (!userID.equals("")) {
                            addReply(holder.getAdapterPosition());
                        }
                    } else {
                        holder.isShowReply = true;
                        holder.rvReply.setVisibility(View.VISIBLE);
                    }

                }
            });

            // like event
            holder.tvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLiked) {
                        // delete like
                        comment.getLikes().remove(userID);
                        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_COMMENTS).child(motelID).child(holder.commentID).child(FIRE_BASE_STRING_COMMENTS_LIKE).setValue(comment.getLikes());
                    } else {
                        // add like
                        comment.getLikes().add(userID);
                        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_COMMENTS).child(motelID).child(holder.commentID).child(FIRE_BASE_STRING_COMMENTS_LIKE).setValue(comment.getLikes());
                    }
                }
            });


            // reply view
            holder.replyAdapter = new ReplyAdapter(position);
            holder.rvReply.setAdapter(holder.replyAdapter);


            for (UserInfo item : userInfoArrayList) {
                if (item.getId().equals(comment.getUserId())) {
                    holder.tvProfile.setText(item.getName());
                    if (item.getAvatar() != null) {
//                        holder.imgProfile.setImageURI(Uri.parse(item.getAvatar()));
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(item.getAvatar());
                        GlideApp.with(getContext())
                                .load(storageReference)
                                .error(R.drawable.load_user)
                                .into(holder.imgProfile);
                    }
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return commentArrayList.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {

            ImageView imgProfile;
            TextView tvProfile;
            TextView tvContent;
            TextView tvTime;

            TextView tvLike;
            TextView tvReply;
            RecyclerView rvReply;
            ReplyAdapter replyAdapter;

            String commentID = "";
            Boolean isShowReply = false;

            CommentViewHolder(View itemView) {
                super(itemView);

                imgProfile = itemView.findViewById(R.id.img_item_motel_detail_review_comment_profile);
                tvProfile = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_profile);
                tvTime = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_time);
                tvContent = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_content);
                tvLike = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_like);
                tvReply = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_reply);
                rvReply = itemView.findViewById(R.id.rv_item_motel_detail_review_comment_reply);

                rvReply.setVisibility(View.GONE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShowReply = !isShowReply;
                        if (isShowReply) {
                            rvReply.setVisibility(View.VISIBLE);
                        } else {
                            rvReply.setVisibility(View.GONE);
                        }
                    }
                });


                rvReply.setNestedScrollingEnabled(false);
                rvReply.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                rvReply.setItemAnimator(new DefaultItemAnimator());

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (userID.equals(commentArrayList.get(getAdapterPosition()).getUserId())) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle(getString(R.string.home_model_detail_review_comment_delete));
                            alertDialog.setMessage(getString(R.string.home_model_detail_review_comment_delete_comment));
                            alertDialog.setPositiveButton(getString(R.string.home_model_detail_review_comment_delete_delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_COMMENTS).child(motelID).child(String.valueOf(commentArrayList.get(getAdapterPosition()).getTime())).removeValue();
                                }
                            });
                            alertDialog.setNegativeButton(getString(R.string.home_model_detail_review_comment_delete_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
                        return true;
                    }
                });
            }

        }
    }

    // ---------------------------------------------------------------------------------------------
    class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

        private int commentIndex;

        ReplyAdapter(int commentIndex) {
            this.commentIndex = commentIndex;
        }

        @Override
        public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View itemView = li.inflate(R.layout.item_fragment_motel_detail_review_comment_reply, parent, false);

            return new ReplyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ReplyViewHolder holder, int position) {
            Reply reply = commentArrayList.get(commentIndex).getReplies().get(position);

            // id
            holder.replyID = String.valueOf(reply.getTime());

            // content
            holder.tvContent.setText(reply.getContent());

            // time
            Timestamp timestamp = new Timestamp(reply.getTime());
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM hh:mm", Locale.getDefault());
            holder.tvTime.setText(simpleDateFormat.format(date));

            // profile name + image
            for (UserInfo item : userInfoArrayList) {
                if (item.getId().equals(reply.getUserId())) {
                    holder.tvProfile.setText(item.getName());
                    if (item.getAvatar() != null) {

//                        holder.imgProfile.setImageURI(Uri.parse(item.getAvatar()));
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(item.getAvatar());
                        GlideApp.with(getContext())
                                .load(storageReference)
                                .error(R.drawable.load_user)
                                .into(holder.imgProfile);
                    }
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return commentArrayList.get(commentIndex).getReplies().size();
        }

        class ReplyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgProfile;
            TextView tvProfile;
            TextView tvContent;
            TextView tvTime;

            String replyID = "";

            ReplyViewHolder(View itemView) {
                super(itemView);

                imgProfile = itemView.findViewById(R.id.img_item_motel_detail_review_comment_reply_profile);
                tvProfile = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_reply_profile);
                tvTime = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_reply_time);
                tvContent = itemView.findViewById(R.id.tv_item_motel_detail_review_comment_reply_content);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        // check delete
                        if (userID.equals(commentArrayList.get(commentIndex).getReplies().get(getAdapterPosition()).getUserId())) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle(getString(R.string.home_model_detail_review_comment_delete));
                            alertDialog.setMessage(getString(R.string.home_model_detail_review_comment_delete_reply));
                            alertDialog.setPositiveButton(getString(R.string.home_model_detail_review_comment_delete_delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_COMMENTS).child(motelID).child(String.valueOf(commentArrayList.get(commentIndex).getTime())).child(FIRE_BASE_STRING_COMMENTS_REPLY).child(String.valueOf(commentArrayList.get(commentIndex).getReplies().get(getAdapterPosition()).getTime())).removeValue();
                                }
                            });
                            alertDialog.setNegativeButton(getString(R.string.home_model_detail_review_comment_delete_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
                        return true;
                    }
                });
            }
        }
    }
}
