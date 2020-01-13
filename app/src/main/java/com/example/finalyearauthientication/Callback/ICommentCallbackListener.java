package com.example.finalyearauthientication.Callback;



import com.example.finalyearauthientication.Model.CommentModel;

import java.util.List;

public interface ICommentCallbackListener {

    void onCommentLoadSuccess(List<CommentModel> commentModels);
    void onCommentLoadFailed(String message);
}
