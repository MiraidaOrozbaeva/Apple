package org.example.gorest.controller;


import org.example.gorest.HttpRequest;
import org.example.gorest.endPoint.EndPoint;
import org.example.gorest.models.Comment;

public class CommentController extends HttpRequest {

    public CommentController(String url) {
        super(url);
    }

    public Comment[] getUserCommentsById(Integer id){ //    /public/v2/posts/7911361/comments
        return super.get(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.POSTS, String.valueOf(id),
                EndPoint.COMMENTS)).as(Comment[].class);
    }

    public Comment createUserComments(Comment comment, Integer post_id){
        return super.post(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.POSTS, String.valueOf(post_id),
                EndPoint.COMMENTS), comment.toJson()).as(Comment.class);
    }
}
