package com.example.acs;

//this forces all RequestHandlers to have a processResponse method
//we define the processResponse when we create a RequestHandler in the activity
//demonstrated in the lecture videos
public interface RequestHandler {
    public abstract void processResponse(String response);
}
