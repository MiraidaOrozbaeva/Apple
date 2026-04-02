package org.example.gorest.controller;

import io.restassured.response.Response;
import org.example.gorest.HttpRequest;
import org.example.gorest.endPoint.EndPoint;
import org.example.gorest.models.Post;

public class PostController extends org.example.gorest.HttpRequest {

    public PostController(String url) {
        super(url);
    }

    public Post[] getAllUsersPosts(){
        return super.get(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.POSTS)).as(Post[].class);
    }

    public Post[] getUserPostsById(Integer id){ //    /public/v2/users/8409614/posts
        return super.get(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id),
                        EndPoint.POSTS)).as(Post[].class);
    }

    public Post createUserPost(Post post, Integer id){ //    /public/v2/users/8409614/posts
        Response response = super.post(
                getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS,
                        String.valueOf(id), EndPoint.POSTS),
                post.toJson()
        );
        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Failed to create post: " + response.asPrettyString());
        }
        return response.as(Post.class);
    }

    public void deletePost(Integer id){ //     /public/v2/posts/8400274
        super.delete(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.POSTS, String.valueOf(id)));
    }
}
