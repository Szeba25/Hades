package hu.szeba.hades.controller;

import java.io.IOException;

public interface Work {

    void execute(Publisher publisher) throws IOException, InterruptedException;

}
