package com.example.finalyearauthientication.ui.Comments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearauthientication.Adapter.MyCommentAdapter;
import com.example.finalyearauthientication.Callback.ICommentCallbackListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.CommentModel;
import com.example.finalyearauthientication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class CommentFragment extends BottomSheetDialogFragment implements ICommentCallbackListener {

    private CommentViewModel commentViewModel;

    private Unbinder unbinder;

    @BindView(R.id.recyceler_comment)
    RecyclerView recyceler_comment;

    AlertDialog alertDialog;
    ICommentCallbackListener listener;


    public CommentFragment() {

        listener=this;
    }

    public static CommentFragment instance;

    public static CommentFragment getInstance(){
        if (instance==null){
            instance=new CommentFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView=LayoutInflater.from(getContext())
                .inflate(R.layout.bottom_sheet_comment_fragment,container,false);


        unbinder= ButterKnife.bind(this,itemView);

        initViews();
        loadCommentsFirebase();

        commentViewModel.getMutableLiveDataFoodlist().observe(this, commentModels -> {

            MyCommentAdapter adapter=new MyCommentAdapter(getContext(),commentModels);
            recyceler_comment.setAdapter(adapter);

        });

        return itemView;
    }

    private void loadCommentsFirebase() {

        alertDialog.show();
        List<CommentModel> commentModels=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference(Common.COMMENT_REF)
                .child(Common.selectedFood.getId())
                .orderByChild("commentTimeStamp")
                .limitToLast(100)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot commentSnapshot:dataSnapshot.getChildren()){

                            CommentModel commentall=commentSnapshot.getValue(CommentModel.class);

                            commentModels.add(commentall);
                        }
                        listener.onCommentLoadSuccess(commentModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void initViews() {

        commentViewModel= ViewModelProviders.of(this).get(CommentViewModel.class);

        alertDialog=new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        recyceler_comment.setHasFixedSize(true);
        recyceler_comment.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true));
        recyceler_comment.addItemDecoration(new DividerItemDecoration(getContext(),new
                LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false).getOrientation()));
    }

    @Override
    public void onCommentLoadSuccess(List<CommentModel> commentModels) {

        alertDialog.dismiss();
        commentViewModel.setCommentList(commentModels);

    }

    @Override
    public void onCommentLoadFailed(String message) {

        alertDialog.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
}